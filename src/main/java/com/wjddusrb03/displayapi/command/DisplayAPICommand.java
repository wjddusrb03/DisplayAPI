package com.wjddusrb03.displayapi.command;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.animation.AnimationBuilder;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class DisplayAPICommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("displayapi.admin")) {
            sender.sendMessage(Component.text("[DisplayAPI] 권한이 없습니다.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "test" -> handleTest(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "removeall" -> handleRemoveAll(sender);
            case "list" -> handleList(sender);
            case "reload" -> handleReload(sender);
            case "save" -> handleSave(sender);
            case "info" -> handleInfo(sender);
            default -> sendHelp(sender);
        }

        return true;
    }

    private void handleTest(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[DisplayAPI] 플레이어만 사용 가능합니다.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("[DisplayAPI] /dapi test <text|block|item|popup|group>").color(NamedTextColor.YELLOW));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "text" -> {
                DisplayAPI.text(player.getLocation().add(0, 2, 0))
                        .text(Component.text("Hello DisplayAPI!").color(NamedTextColor.GOLD))
                        .noBackground()
                        .shadowed(true)
                        .id("test-text")
                        .spawn();
                msg(sender, "텍스트 디스플레이 생성 완료");
            }
            case "block" -> {
                DisplayAPI.block(player.getLocation().add(0, 2, 0))
                        .block(Material.DIAMOND_BLOCK)
                        .scale(0.5f)
                        .id("test-block")
                        .spawn();
                msg(sender, "블록 디스플레이 생성 완료");
            }
            case "item" -> {
                DisplayAPI.item(player.getLocation().add(0, 2, 0))
                        .item(new ItemStack(Material.DIAMOND_SWORD))
                        .id("test-item")
                        .spawn();
                msg(sender, "아이템 디스플레이 생성 완료");
            }
            case "popup" -> {
                DisplayAPI.popup(player.getLocation().add(0, 2, 0))
                        .text(Component.text("-25").color(NamedTextColor.RED))
                        .duration(30)
                        .startScale(1.5f)
                        .endScale(0.3f)
                        .spawn();
                msg(sender, "팝업 생성 완료");
            }
            case "group" -> {
                var anchor = player.getLocation().add(0, 2.5, 0);
                var d1 = DisplayAPI.text(anchor)
                        .text(Component.text("Line 1").color(NamedTextColor.AQUA))
                        .noBackground()
                        .spawn();
                var d2 = DisplayAPI.text(anchor.clone().add(0, -0.3, 0))
                        .text(Component.text("Line 2").color(NamedTextColor.GREEN))
                        .noBackground()
                        .spawn();
                var d3 = DisplayAPI.text(anchor.clone().add(0, -0.6, 0))
                        .text(Component.text("Line 3").color(NamedTextColor.YELLOW))
                        .noBackground()
                        .spawn();

                var group = DisplayAPI.group("test-group", anchor);
                group.add(d1);
                group.add(d2);
                group.add(d3);
                DisplayAPI.getManager().registerGroup(group);

                msg(sender, "디스플레이 그룹 생성 완료 (3개 라인)");
            }
            case "pulse" -> {
                var d = DisplayAPI.text(player.getLocation().add(0, 2, 0))
                        .text(Component.text("Pulse!").color(NamedTextColor.LIGHT_PURPLE))
                        .noBackground()
                        .shadowed(true)
                        .id("test-pulse")
                        .spawn();
                DisplayAPI.animate(d).pulse(0.8f, 1.5f, 30).loop(true).play();
                msg(sender, "펄스 애니메이션 생성 완료");
            }
            case "spin" -> {
                var d = DisplayAPI.block(player.getLocation().add(0, 2, 0))
                        .block(Material.DIAMOND_BLOCK)
                        .scale(0.6f)
                        .id("test-spin")
                        .spawn();
                DisplayAPI.animate(d).spin(AnimationBuilder.Axis.Y, 40).loop(true).play();
                msg(sender, "회전 애니메이션 생성 완료");
            }
            case "bounce" -> {
                var d = DisplayAPI.item(player.getLocation().add(0, 2, 0))
                        .item(new ItemStack(Material.NETHER_STAR))
                        .id("test-bounce")
                        .spawn();
                DisplayAPI.animate(d).floating(0.3f, 40).loop(true).play();
                msg(sender, "바운스 애니메이션 생성 완료");
            }
            case "shake" -> {
                var d = DisplayAPI.text(player.getLocation().add(0, 2, 0))
                        .text(Component.text("DANGER!").color(NamedTextColor.RED))
                        .noBackground()
                        .id("test-shake")
                        .spawn();
                DisplayAPI.animate(d).shake(0.15f, 20).loop(true).play();
                msg(sender, "흔들림 애니메이션 생성 완료");
            }
            case "fadein" -> {
                var d = DisplayAPI.text(player.getLocation().add(0, 2, 0))
                        .text(Component.text("Fade In!").color(NamedTextColor.GREEN))
                        .noBackground()
                        .id("test-fadein")
                        .spawn();
                DisplayAPI.animate(d).fadeIn(40).play();
                msg(sender, "페이드인 애니메이션 생성 완료");
            }
            case "leaderboard" -> {
                Map<String, Integer> data = new LinkedHashMap<>();
                data.put("Player_A", 150);
                data.put("Player_B", 120);
                data.put("Player_C", 95);
                data.put("Player_D", 80);
                data.put("Player_E", 65);

                DisplayAPI.leaderboard(player.getLocation().add(0, 3, 0))
                        .title("Kill Rankings")
                        .data(data)
                        .maxRows(5)
                        .spawn();
                msg(sender, "리더보드 생성 완료");
            }
            case "interactive" -> {
                DisplayAPI.interactive(player.getLocation().add(0, 2, 0))
                        .text(Component.text("Click me!").color(NamedTextColor.GREEN))
                        .hitbox(1.5f, 1.0f)
                        .scale(1.2f)
                        .onClick(p -> p.sendMessage(Component.text("[DisplayAPI] Left click!").color(NamedTextColor.YELLOW)))
                        .onRightClick(p -> p.sendMessage(Component.text("[DisplayAPI] Right click!").color(NamedTextColor.AQUA)))
                        .spawn();
                msg(sender, "인터랙티브 디스플레이 생성 완료 (좌/우클릭 테스트)");
            }
            case "follow" -> {
                var d = DisplayAPI.text(player.getLocation().add(0, 2.5, 0))
                        .text(Component.text(player.getName()).color(NamedTextColor.AQUA))
                        .noBackground()
                        .shadowed(true)
                        .id("test-follow")
                        .spawn();
                DisplayAPI.follow(d, player)
                        .offset(0, 2.5, 0)
                        .smoothTeleport(3)
                        .start();
                msg(sender, "팔로우 디스플레이 생성 완료 (머리 위를 따라다님)");
            }
            default -> sender.sendMessage(Component.text("[DisplayAPI] test <text|block|item|popup|group|pulse|spin|bounce|shake|fadein|leaderboard|interactive|follow>").color(NamedTextColor.YELLOW));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            msg(sender, "/dapi remove <id>");
            return;
        }
        String id = args[1];
        SpawnedDisplay sd = DisplayAPI.getManager().getById(id);
        if (sd != null) {
            DisplayAPI.getManager().remove(id);
            msg(sender, "'" + id + "' 제거 완료");
        } else {
            var group = DisplayAPI.getManager().getGroupById(id);
            if (group != null) {
                DisplayAPI.getManager().removeGroup(id);
                msg(sender, "그룹 '" + id + "' 제거 완료");
            } else {
                msg(sender, "'" + id + "' 를 찾을 수 없습니다.");
            }
        }
    }

    private void handleRemoveAll(CommandSender sender) {
        int count = DisplayAPI.getManager().getDisplayCount() + DisplayAPI.getManager().getGroupCount();
        DisplayAPI.getManager().removeAll();
        msg(sender, "모든 디스플레이 제거 완료 (" + count + "개)");
    }

    private void handleList(CommandSender sender) {
        var displays = DisplayAPI.getManager().getAllDisplays();
        var groups = DisplayAPI.getManager().getAllGroups();

        sender.sendMessage(Component.text("=== DisplayAPI 목록 ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("디스플레이: " + displays.size() + "개, 그룹: " + groups.size() + "개").color(NamedTextColor.GRAY));

        for (SpawnedDisplay sd : displays) {
            String type = sd.getEntity().getClass().getSimpleName().replace("Craft", "");
            String alive = sd.isAlive() ? "alive" : "dead";
            String persist = sd.isPersistent() ? " [P]" : "";
            sender.sendMessage(Component.text("  " + sd.getId() + " - " + type + " (" + alive + ")" + persist).color(NamedTextColor.WHITE));
        }

        for (var group : groups) {
            sender.sendMessage(Component.text("  [G] " + group.getId() + " - " + group.size() + "개").color(NamedTextColor.AQUA));
        }
    }

    private void handleReload(CommandSender sender) {
        DisplayAPI.getPlugin().reloadConfig();
        msg(sender, "설정 재로드 완료");
    }

    private void handleSave(CommandSender sender) {
        DisplayAPI.getPersistenceManager().save();
        int count = DisplayAPI.getManager().getPersistentDisplays().size();
        msg(sender, "영속 디스플레이 " + count + "개 저장 완료");
    }

    private void handleInfo(CommandSender sender) {
        sender.sendMessage(Component.text("=== DisplayAPI Info ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  Version: " + DisplayAPI.getPlugin().getDescription().getVersion()).color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  Displays: " + DisplayAPI.getManager().getDisplayCount()).color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  Groups: " + DisplayAPI.getManager().getGroupCount()).color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  Persistent: " + DisplayAPI.getManager().getPersistentDisplays().size()).color(NamedTextColor.WHITE));
    }

    private void msg(CommandSender sender, String message) {
        sender.sendMessage(Component.text("[DisplayAPI] " + message).color(NamedTextColor.GREEN));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== DisplayAPI 도움말 ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  /dapi test <type> - 테스트 생성").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi remove <id> - ID로 제거").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi removeall - 모든 디스플레이 제거").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi list - 활성 디스플레이 목록").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi save - 영속 디스플레이 저장").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi reload - 설정 재로드").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi info - 플러그인 정보").color(NamedTextColor.WHITE));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(List.of("test", "remove", "removeall", "list", "save", "reload", "info"), args[0]);
        }
        if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "test" -> filter(List.of("text", "block", "item", "popup", "group", "pulse", "spin", "bounce", "shake", "fadein", "leaderboard", "interactive", "follow"), args[1]);
                case "remove" -> {
                    List<String> ids = new ArrayList<>();
                    DisplayAPI.getManager().getAllDisplays().forEach(d -> ids.add(d.getId()));
                    DisplayAPI.getManager().getAllGroups().forEach(g -> ids.add(g.getId()));
                    yield filter(ids, args[1]);
                }
                default -> List.of();
            };
        }
        return List.of();
    }

    private List<String> filter(List<String> options, String input) {
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
