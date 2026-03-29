package com.wjddusrb03.displayapi.listener;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Handles player join to restore per-player visibility.
 */
public class DisplayListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Re-apply visibility for per-player displays
        for (SpawnedDisplay sd : DisplayAPI.getManager().getAllDisplays()) {
            if (sd.isAlive() && sd.isPerPlayer()) {
                sd.applyToNewPlayer(player);
            }
        }
    }
}
