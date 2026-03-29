# Quick Start

## Your First Display

```java
import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Display.Billboard;

// Create a simple text hologram
SpawnedDisplay display = DisplayAPI.text(location)
    .text(Component.text("Hello World!").color(NamedTextColor.GOLD))
    .billboard(Billboard.CENTER)
    .noBackground()
    .spawn();
```

## Common Patterns

### NPC Name Tag
```java
DisplayAPI.text(npc.getLocation().add(0, 2.2, 0))
    .text(Component.text("Shop Keeper").color(NamedTextColor.GREEN))
    .billboard(Billboard.CENTER)
    .noBackground()
    .shadowed(true)
    .spawn();
```

### Damage Indicator
```java
@EventHandler
public void onDamage(EntityDamageByEntityEvent e) {
    if (!(e.getDamager() instanceof Player attacker)) return;

    double damage = e.getFinalDamage();
    DisplayAPI.popup(e.getEntity().getLocation().add(0, 1.5, 0))
        .text(Component.text(String.format("-%.1f", damage)).color(NamedTextColor.RED))
        .duration(25)
        .startScale(1.2f)
        .endScale(0.3f)
        .visibleTo(attacker)
        .spawn();
}
```

### Floating Item Showcase
```java
SpawnedDisplay item = DisplayAPI.item(location)
    .item(new ItemStack(Material.NETHER_STAR))
    .billboard(Billboard.CENTER)
    .spawn();

DisplayAPI.animate(item).floating(0.3f, 40).loop(true).play();
DisplayAPI.animate(item).spin(AnimationBuilder.Axis.Y, 60).loop(true).play();
```

### Interactive Button
```java
DisplayAPI.interactive(location)
    .text(Component.text("[TELEPORT]").color(NamedTextColor.AQUA))
    .hitbox(2.0f, 0.8f)
    .scale(1.5f)
    .onClick(player -> {
        player.teleport(targetLocation);
        player.sendMessage("Teleported!");
    })
    .spawn();
```

### Player Health Display (Follow)
```java
SpawnedDisplay hp = DisplayAPI.text(player.getLocation().add(0, 2.5, 0))
    .text(Component.text("HP: 20").color(NamedTextColor.RED))
    .noBackground()
    .billboard(Billboard.CENTER)
    .spawn();

DisplayAPI.follow(hp, player)
    .offset(0, 2.5, 0)
    .smoothTeleport(3)
    .start();
```

### Multi-line Hologram
```java
var anchor = location.clone().add(0, 2, 0);
var group = DisplayAPI.group("info-board", anchor);

group.add(DisplayAPI.text(anchor)
    .text(Component.text("Server Info").color(NamedTextColor.GOLD))
    .noBackground().spawn());

group.add(DisplayAPI.text(anchor.clone().add(0, -0.3, 0))
    .text(Component.text("Players: 10/100").color(NamedTextColor.GRAY))
    .noBackground().spawn());

group.add(DisplayAPI.text(anchor.clone().add(0, -0.6, 0))
    .text(Component.text("TPS: 20.0").color(NamedTextColor.GREEN))
    .noBackground().spawn());

DisplayAPI.getManager().registerGroup(group);
```

## Next Steps

- [[TextDisplay]] - Full text display options
- [[Animation]] - Animation system details
- [[Interactive]] - Click detection guide
- [[Persistence]] - Survive server restarts
