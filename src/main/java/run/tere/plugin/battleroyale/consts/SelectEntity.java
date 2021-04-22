package run.tere.plugin.battleroyale.consts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SelectEntity {
    private UUID uuid;
    private UUID entityUUID;

    public SelectEntity(Player player, Entity entity) {
        this.uuid = player.getUniqueId();
        this.entityUUID = entity.getUniqueId();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public Entity getEntity() {
        return Bukkit.getEntity(this.entityUUID);
    }
}
