package run.tere.plugin.battleroyale.areas;

import org.bukkit.scheduler.BukkitRunnable;
import run.tere.plugin.battleroyale.BattleRoyale;

public class AreaScheduler extends BukkitRunnable {
    private int time;

    public AreaScheduler() {
        time = 0;
    }

    @Override
    public void run() {
        time++;
        if (time == 30) {
            BattleRoyale.getGameHandler().getAreaHandler().move(800, 120L);
            return;
        }

        if (time == 210) {
            BattleRoyale.getGameHandler().getAreaHandler().move(400, 60L);
            return;
        }

        if (time == 300) {
            BattleRoyale.getGameHandler().getAreaHandler().move(200, 60L);
            return;
        }

        if (time == 390) {
            BattleRoyale.getGameHandler().getAreaHandler().move(80, 60L);
            return;
        }

        if (time == 480) {
            BattleRoyale.getGameHandler().getAreaHandler().move(20, 60L);
            return;
        }

        if (time == 570) {
            BattleRoyale.getGameHandler().getAreaHandler().move(1, 60L);
            return;
        }
        if (time >= 640) {
            this.cancel();
            return;
        }
    }
}
