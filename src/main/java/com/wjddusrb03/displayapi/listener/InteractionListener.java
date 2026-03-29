package com.wjddusrb03.displayapi.listener;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.InteractiveDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Routes Interaction entity events to InteractiveDisplay handlers.
 */
public class InteractionListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Interaction)) return;

        InteractiveDisplay interactive = DisplayAPI.getManager()
                .getInteractiveByEntityUUID(e.getRightClicked().getUniqueId());
        if (interactive == null) return;

        e.setCancelled(true);
        interactive.handleRightClick(e.getPlayer());
    }

    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Interaction)) return;
        if (!(e.getDamager() instanceof Player player)) return;

        InteractiveDisplay interactive = DisplayAPI.getManager()
                .getInteractiveByEntityUUID(e.getEntity().getUniqueId());
        if (interactive == null) return;

        e.setCancelled(true);
        interactive.handleLeftClick(player);
    }
}
