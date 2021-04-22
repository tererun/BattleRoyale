package run.tere.plugin.battleroyale.guns.reloads;

import java.util.HashSet;
import java.util.UUID;

public class ReloadHandler {
    private HashSet<ReloadTask> reloadTasks;

    public ReloadHandler() {
        this.reloadTasks = new HashSet<>();
    }

    public HashSet<ReloadTask> getReloadTasks() {
        return reloadTasks;
    }

    public ReloadTask getReloadTaskByUUID(UUID uuid) {
        for (ReloadTask reloadTask : this.reloadTasks) {
            if (reloadTask.getUUID().equals(uuid)) {
                return reloadTask;
            }
        }
        return null;
    }
}
