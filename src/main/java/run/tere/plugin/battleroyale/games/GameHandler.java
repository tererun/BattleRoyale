package run.tere.plugin.battleroyale.games;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import run.tere.plugin.battleroyale.BattleRoyale;
import run.tere.plugin.battleroyale.apis.PlayerAPI;
import run.tere.plugin.battleroyale.areas.AreaHandler;
import run.tere.plugin.battleroyale.areas.AreaScheduler;
import run.tere.plugin.battleroyale.guns.GunHandler;
import run.tere.plugin.battleroyale.guns.ammos.AmmoHandler;
import run.tere.plugin.battleroyale.itemspawns.ItemSpawnHandler;
import run.tere.plugin.battleroyale.minimaps.MinimapHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class GameHandler {
    private AreaHandler areaHandler;
    private AreaScheduler areaScheduler;
    private AmmoHandler ammoHandler;
    private GunHandler gunHandler;
    private ItemSpawnHandler itemSpawnHandler;
    private MinimapHandler minimapHandler;

    public GameHandler() {
        this.gunHandler = new GunHandler();
        this.areaHandler = new AreaHandler(Bukkit.getWorld("world"));
        this.ammoHandler = new AmmoHandler();
        this.minimapHandler = new MinimapHandler();
        Bukkit.getServer().getPluginManager().registerEvents(new GameCheckListenerClass(), BattleRoyale.getPlugin());
    }

    public void resetWorldBorder() {
        WorldBorder worldBorder = areaHandler.getWorldBorder();
        BukkitRunnable moveRunnable = areaHandler.getMoveRunnable();
        if (!moveRunnable.isCancelled()) moveRunnable.cancel();
        if (!areaScheduler.isCancelled()) areaScheduler.cancel();
        worldBorder.setCenter(65, -236);
        worldBorder.setSize(1500);
    }

    public void setItemSpawnHandler(ItemSpawnHandler itemSpawnHandler) {
        this.itemSpawnHandler = itemSpawnHandler;
    }

    public void startGame() {
        prepareGame();
    }

    private void prepareGame() {
        Team team = prepareTeam();
        PlayerAPI.clearInventory();
        PlayerAPI.setGameMode(GameMode.ADVENTURE);
        PlayerAPI.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 255, false, false, false));
        PlayerAPI.teamAddEntry(team);
        preparePigTrain();
        spawnItems();
        areaScheduler = new AreaScheduler();
        new BukkitRunnable() {
            @Override
            public void run() {
                areaScheduler.runTaskTimer(BattleRoyale.getPlugin(), 0L, 20L);
            }
        }.runTaskLater(BattleRoyale.getPlugin(), 1200L);
    }

    private Team prepareTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("noName");
        if (team != null) {
            team.unregister();
        }
        team = scoreboard.registerNewTeam("noName");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        return team;
    }

    private List<Pig> pigTrains;
    private Location pigTrainStartLocation;
    private Location pigTrainEndLocation;
    private double divideX;
    private double divideZ;

    public void spawnItems() {
        World world = areaHandler.getWorldBorder().getCenter().getWorld();
        for (Entity entity : world.getEntities()) {
            if (entity.getScoreboardTags().contains("selectitems")) {
                entity.remove();
            }
        }
        Random random = new Random();
        for (Location location : itemSpawnHandler.getItemSpawnLocationHandler().getItemSpawnBukkitLocations()) {
            if (random.nextInt(10) == 9) continue;
            itemSpawnHandler.spawnItems(location.clone(), itemSpawnHandler.getRandomItemSpawn());
        }
    }

    public void preparePigTrain() {
        pigTrains = new ArrayList<>();
        long timeLength = 1000;
        randomRoute(timeLength);
        spawnPigTrains();
        movePigTrans();
    }

    private void randomRoute(long tick) {
        Random random = new Random();
        int direction = random.nextInt(4);
        WorldBorder worldBorder = this.getAreaHandler().getWorldBorder();
        double areaSize = worldBorder.getSize();
        double halfSize = areaSize / 2;
        Location areaCenter = worldBorder.getCenter().clone();
        if (direction == 0) {
            //NORTH
            pigTrainStartLocation = areaCenter.clone().add(-halfSize, 0, -halfSize).add(random.nextInt((int) areaSize), 0, 0);
            pigTrainEndLocation = areaCenter.clone().add(-halfSize, 0, halfSize).add(random.nextInt((int) areaSize), 0, 0);
        } else if (direction == 1) {
            //SOUTH
            pigTrainStartLocation = areaCenter.clone().add(-halfSize, 0, halfSize).add(random.nextInt((int) areaSize), 0, 0);
            pigTrainEndLocation = areaCenter.clone().add(-halfSize, 0, -halfSize).add(random.nextInt((int) areaSize), 0, 0);
        } else if (direction == 2) {
            //WEST
            pigTrainStartLocation = areaCenter.clone().add(-halfSize, 0, -halfSize).add(0, 0, random.nextInt((int) areaSize));
            pigTrainEndLocation = areaCenter.clone().add(halfSize, 0, -halfSize).add(0, 0, random.nextInt((int) areaSize));
        } else {
            //EAST
            pigTrainStartLocation = areaCenter.clone().add(halfSize, 0, -halfSize).add(0, 0, random.nextInt((int) areaSize));
            pigTrainEndLocation = areaCenter.clone().add(-halfSize, 0, -halfSize).add(0, 0, random.nextInt((int) areaSize));
        }
        pigTrainStartLocation.setY(200);
        pigTrainEndLocation.setY(200);
        pigTrainStartLocation.setYaw((float) getDirectionBetweenPoints(pigTrainStartLocation, pigTrainEndLocation));
        divideX = (pigTrainEndLocation.getX() - pigTrainStartLocation.getX()) / tick;
        divideZ = (pigTrainEndLocation.getZ() - pigTrainStartLocation.getZ()) / tick;
        worldBorder.getCenter().getWorld().loadChunk(pigTrainEndLocation.getChunk());
    }

    private void spawnPigTrains() {
        World world = this.pigTrainStartLocation.getWorld();
        for (Entity entity : world.getEntities()) {
            if (entity.getScoreboardTags().contains("pigtrain")) {
                entity.remove();
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(pigTrainStartLocation);
            Pig pig = world.spawn(pigTrainStartLocation, Pig.class, pigTrain -> {
                pigTrain.setGravity(false);
                pigTrain.setInvulnerable(true);
                pigTrain.setSaddle(true);
                pigTrain.setAI(false);
                pigTrain.addScoreboardTag("pigtrain");
            });
            pigTrains.add(pig);
            pig.addPassenger(player);
        }
    }

    public void movePigTrans() {
        System.out.println("pig detect!");
        for (Pig pig : pigTrains) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = pig.getLocation().clone().add(divideX, 0, divideZ);
                    try {
                        methods[1].invoke(methods[0].invoke(pig), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    } catch (Exception ex) {
                    }
                    if (pig.getLocation().distance(pigTrainEndLocation) <= 1) {
                        for (Entity entity : pig.getPassengers()) {
                            entity.eject();
                        }
                        pig.remove();
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(BattleRoyale.getPlugin(), 2L * pigTrains.indexOf(pig), 1L);
        }
    }

    private final Method[] methods = ((Supplier<Method[]>) () -> {
        try {
            Method getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity").getDeclaredMethod("getHandle");
            return new Method[] {
                    getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation", double.class, double.class, double.class, float.class, float.class)
            };
        } catch (Exception ex) {
            return null;
        }
    }).get();

    public static double getDirectionBetweenPoints(final Location from, final Location to) {
        double deltaX = from.getX() - to.getX();
        double deltaZ = from.getZ() - to.getZ();
        //????????????
        double div = deltaZ / deltaX;
        double dir = Math.toDegrees(Math.atan(div));
        {
            //x-z?????????????????????????????????????????????tan???2????????????????????????????????????
            if(deltaX > 0){
                //+x ?????????
                dir += 90;
            }
            if(deltaX < 0){
                //-x ?????????
                dir -= 90;
            }
        }
        return dir;
    }

    public AreaHandler getAreaHandler() {
        return areaHandler;
    }

    public GunHandler getGunHandler() {
        return gunHandler;
    }

    public ItemSpawnHandler getItemSpawnHandler() {
        return itemSpawnHandler;
    }

    public AmmoHandler getAmmoHandler() {
        return ammoHandler;
    }

    public MinimapHandler getMinimapHandler() {
        return minimapHandler;
    }
}
