package run.tere.plugin.battleroyale.apis;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GlowAPI {
    public static void setGlowing(Entity glowing, Player sendPacketPlayer, boolean glow) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1);
        sendPacketPlayer.spawnParticle(Particle.REDSTONE, glowing.getLocation(), 50, 0, 0.5, 0, 0, dustOptions);
    }
}
