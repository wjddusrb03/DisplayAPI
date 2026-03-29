package com.wjddusrb03.displayapi.command;

import com.wjddusrb03.displayapi.DisplayAPI;
import com.wjddusrb03.displayapi.animation.AnimationBuilder;
import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class DisplayAPICommand implements CommandExecutor, TabCompleter {

    private static final List<String> COLORS = List.of(
            "red", "green", "blue", "yellow", "aqua", "purple", "white",
            "gold", "gray", "dark_red", "dark_green", "dark_blue",
            "dark_aqua", "dark_purple", "dark_gray", "black"
    );

    private static final List<String> BILLBOARD_MODES = List.of(
            "center", "fixed", "horizontal", "vertical"
    );

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
            case "create" -> handleCreate(sender, args);
            case "edit" -> handleEdit(sender, args);
            case "move" -> handleMove(sender, args);
            case "animate" -> handleAnimate(sender, args);
            case "popup" -> handlePopup(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "removeall" -> handleRemoveAll(sender);
            case "list" -> handleList(sender);
            case "near" -> handleNear(sender, args);
            case "reload" -> handleReload(sender);
            case "save" -> handleSave(sender);
            case "info" -> handleInfo(sender);
            default -> sendHelp(sender);
        }

        return true;
    }

    // ========================
    // CREATE
    // ========================

    private void handleCreate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[DisplayAPI] 플레이어만 사용 가능합니다.");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(Component.text("[DisplayAPI] 사용법:").color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("  /dapi create text <텍스트> [id]").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi create block <블록> [id]").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi create item <아이템> [id]").color(NamedTextColor.WHITE));
            return;
        }

        String type = args[1].toLowerCase();
        switch (type) {
            case "text" -> {
                String text = joinArgs(args, 2);
                String id = null;

                // 마지막 인자가 id:xxx 형식이면 ID로 사용
                if (args[args.length - 1].startsWith("id:")) {
                    id = args[args.length - 1].substring(3);
                    text = joinArgs(args, 2, args.length - 1);
                }

                // &색코드 지원
                Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(text);

                var builder = DisplayAPI.text(player.getLocation().add(0, 2, 0))
                        .text(comp)
                        .noBackground()
                        .shadowed(true)
                        .billboard(Billboard.CENTER);

                if (id != null) builder.id(id);

                SpawnedDisplay sd = builder.spawn();
                msg(sender, "텍스트 디스플레이 생성 (ID: " + sd.getId() + ")");
            }
            case "block" -> {
                Material mat = parseMaterial(args[2]);
                if (mat == null || !mat.isBlock()) {
                    err(sender, "올바른 블록 이름이 아닙니다: " + args[2]);
                    return;
                }
                String id = args.length >= 4 ? args[3] : null;

                var builder = DisplayAPI.block(player.getLocation().add(0, 2, 0))
                        .block(mat)
                        .billboard(Billboard.CENTER);

                if (id != null) builder.id(id);

                SpawnedDisplay sd = builder.spawn();
                msg(sender, "블록 디스플레이 생성: " + mat.name() + " (ID: " + sd.getId() + ")");
            }
            case "item" -> {
                Material mat = parseMaterial(args[2]);
                if (mat == null || !mat.isItem()) {
                    err(sender, "올바른 아이템 이름이 아닙니다: " + args[2]);
                    return;
                }
                String id = args.length >= 4 ? args[3] : null;

                var builder = DisplayAPI.item(player.getLocation().add(0, 2, 0))
                        .item(new ItemStack(mat))
                        .billboard(Billboard.CENTER);

                if (id != null) builder.id(id);

                SpawnedDisplay sd = builder.spawn();
                msg(sender, "아이템 디스플레이 생성: " + mat.name() + " (ID: " + sd.getId() + ")");
            }
            default -> err(sender, "유형은 text, block, item 중 하나입니다.");
        }
    }

    // ========================
    // EDIT
    // ========================

    private void handleEdit(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Component.text("[DisplayAPI] 사용법:").color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("  /dapi edit <id> text <새텍스트>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> block <블록>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> item <아이템>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> scale <크기>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> billboard <모드>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> glow <색상|off>").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi edit <id> persist <true|false>").color(NamedTextColor.WHITE));
            return;
        }

        String id = args[1];
        SpawnedDisplay sd = DisplayAPI.getManager().getById(id);
        if (sd == null || !sd.isAlive()) {
            err(sender, "디스플레이를 찾을 수 없습니다: " + id);
            return;
        }

        String prop = args[2].toLowerCase();
        String value = joinArgs(args, 3);

        switch (prop) {
            case "text" -> {
                Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(value);
                sd.updateText(comp);
                msg(sender, "텍스트 변경 완료");
            }
            case "block" -> {
                Material mat = parseMaterial(value);
                if (mat == null || !mat.isBlock()) {
                    err(sender, "올바른 블록이 아닙니다: " + value);
                    return;
                }
                sd.updateBlock(mat);
                msg(sender, "블록 변경 완료: " + mat.name());
            }
            case "item" -> {
                Material mat = parseMaterial(value);
                if (mat == null || !mat.isItem()) {
                    err(sender, "올바른 아이템이 아닙니다: " + value);
                    return;
                }
                sd.updateItem(mat);
                msg(sender, "아이템 변경 완료: " + mat.name());
            }
            case "scale" -> {
                try {
                    float scale = Float.parseFloat(value);
                    if (scale <= 0 || scale > 20) {
                        err(sender, "크기는 0~20 사이여야 합니다.");
                        return;
                    }
                    sd.getEntity().setTransformation(
                            new org.bukkit.util.Transformation(
                                    new org.joml.Vector3f(0f, 0f, 0f),
                                    new org.joml.AxisAngle4f(0f, 0f, 1f, 0f),
                                    new org.joml.Vector3f(scale, scale, scale),
                                    new org.joml.AxisAngle4f(0f, 0f, 1f, 0f)
                            )
                    );
                    msg(sender, "크기 변경 완료: " + scale);
                } catch (NumberFormatException e) {
                    err(sender, "숫자를 입력하세요: " + value);
                }
            }
            case "billboard" -> {
                Billboard bb = parseBillboard(value);
                if (bb == null) {
                    err(sender, "모드: center, fixed, horizontal, vertical");
                    return;
                }
                sd.setBillboard(bb);
                msg(sender, "빌보드 변경 완료: " + bb.name());
            }
            case "glow" -> {
                if (value.equalsIgnoreCase("off") || value.equalsIgnoreCase("false")) {
                    sd.setGlowing(false);
                    msg(sender, "발광 해제");
                } else {
                    Color color = parseColor(value);
                    if (color == null) {
                        err(sender, "색상: " + String.join(", ", COLORS) + " 또는 #RRGGBB");
                        return;
                    }
                    sd.setGlowing(true);
                    sd.setGlowColor(color);
                    msg(sender, "발광 설정: " + value);
                }
            }
            case "persist" -> {
                // Persistence requires re-registration, just inform the user
                boolean persist = value.equalsIgnoreCase("true");
                // Note: Changing persistence at runtime requires re-creating the SpawnedDisplay
                // For simplicity, we save current persistent displays
                if (persist) {
                    DisplayAPI.getPersistenceManager().save();
                    msg(sender, "현재 영속 디스플레이 저장 완료. 새 디스플레이 생성 시 id:로 ID를 지정하세요.");
                } else {
                    msg(sender, "비영속으로 변경하려면 제거 후 다시 생성하세요.");
                }
            }
            default -> err(sender, "속성: text, block, item, scale, billboard, glow");
        }
    }

    // ========================
    // MOVE
    // ========================

    private void handleMove(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[DisplayAPI] 플레이어만 사용 가능합니다.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("[DisplayAPI] /dapi move <id> — 현재 위치로 이동").color(NamedTextColor.YELLOW));
            return;
        }

        String id = args[1];
        SpawnedDisplay sd = DisplayAPI.getManager().getById(id);
        if (sd == null || !sd.isAlive()) {
            // Try group
            var group = DisplayAPI.getManager().getGroupById(id);
            if (group != null) {
                group.teleport(player.getLocation().add(0, 2, 0));
                msg(sender, "그룹 '" + id + "' 이동 완료");
            } else {
                err(sender, "디스플레이를 찾을 수 없습니다: " + id);
            }
            return;
        }

        sd.teleport(player.getLocation().add(0, 2, 0));
        msg(sender, "'" + id + "' 현재 위치로 이동 완료");
    }

    // ========================
    // ANIMATE
    // ========================

    private void handleAnimate(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Component.text("[DisplayAPI] 사용법:").color(NamedTextColor.YELLOW));
            sender.sendMessage(Component.text("  /dapi animate <id> pulse").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> spin [x|y|z]").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> bounce").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> float").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> shake").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> fadein").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> fadeout").color(NamedTextColor.WHITE));
            sender.sendMessage(Component.text("  /dapi animate <id> growin").color(NamedTextColor.WHITE));
            return;
        }

        String id = args[1];
        SpawnedDisplay sd = DisplayAPI.getManager().getById(id);
        if (sd == null || !sd.isAlive()) {
            err(sender, "디스플레이를 찾을 수 없습니다: " + id);
            return;
        }

        String anim = args[2].toLowerCase();
        switch (anim) {
            case "pulse" -> {
                DisplayAPI.animate(sd).pulse(0.8f, 1.5f, 30).loop(true).play();
                msg(sender, "펄스 애니메이션 적용");
            }
            case "spin" -> {
                AnimationBuilder.Axis axis = AnimationBuilder.Axis.Y;
                if (args.length >= 4) {
                    axis = switch (args[3].toLowerCase()) {
                        case "x" -> AnimationBuilder.Axis.X;
                        case "z" -> AnimationBuilder.Axis.Z;
                        default -> AnimationBuilder.Axis.Y;
                    };
                }
                DisplayAPI.animate(sd).spin(axis, 40).loop(true).play();
                msg(sender, "회전 애니메이션 적용 (축: " + axis + ")");
            }
            case "bounce" -> {
                DisplayAPI.animate(sd).bounce(0.3f, 20).loop(true).play();
                msg(sender, "바운스 애니메이션 적용");
            }
            case "float" -> {
                DisplayAPI.animate(sd).floating(0.3f, 40).loop(true).play();
                msg(sender, "떠다니기 애니메이션 적용");
            }
            case "shake" -> {
                DisplayAPI.animate(sd).shake(0.15f, 20).loop(true).play();
                msg(sender, "흔들림 애니메이션 적용");
            }
            case "fadein" -> {
                DisplayAPI.animate(sd).fadeIn(40).play();
                msg(sender, "페이드인 애니메이션 적용");
            }
            case "fadeout" -> {
                DisplayAPI.animate(sd).fadeOut(40).play();
                msg(sender, "페이드아웃 애니메이션 적용");
            }
            case "growin" -> {
                DisplayAPI.animate(sd).growIn(1.0f, 20).play();
                msg(sender, "등장 애니메이션 적용");
            }
            default -> err(sender, "애니메이션: pulse, spin, bounce, float, shake, fadein, fadeout, growin");
        }
    }

    // ========================
    // POPUP
    // ========================

    private void handlePopup(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[DisplayAPI] 플레이어만 사용 가능합니다.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("[DisplayAPI] /dapi popup <텍스트> — 팝업 생성").color(NamedTextColor.YELLOW));
            return;
        }

        String text = joinArgs(args, 1);
        Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(text);

        DisplayAPI.popup(player.getLocation().add(0, 2, 0))
                .text(comp)
                .duration(30)
                .startScale(1.5f)
                .endScale(0.3f)
                .spawn();
        msg(sender, "팝업 생성 완료");
    }

    // ========================
    // NEAR
    // ========================

    private void handleNear(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("[DisplayAPI] 플레이어만 사용 가능합니다.");
            return;
        }

        double radius = 10.0;
        if (args.length >= 2) {
            try {
                radius = Double.parseDouble(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        var displays = DisplayAPI.getManager().getAllDisplays();
        List<SpawnedDisplay> nearby = new ArrayList<>();
        for (SpawnedDisplay sd : displays) {
            if (!sd.isAlive()) continue;
            var loc = sd.getLocation();
            if (loc == null || loc.getWorld() == null) continue;
            if (loc.getWorld().equals(player.getWorld()) && loc.distance(player.getLocation()) <= radius) {
                nearby.add(sd);
            }
        }

        if (nearby.isEmpty()) {
            msg(sender, "반경 " + radius + "블록 내 디스플레이 없음");
            return;
        }

        sender.sendMessage(Component.text("=== 근처 디스플레이 (반경 " + radius + ") ===").color(NamedTextColor.GOLD));
        for (SpawnedDisplay sd : nearby) {
            double dist = sd.getLocation().distance(player.getLocation());
            String type = sd.getEntity().getClass().getSimpleName().replace("Craft", "");
            sender.sendMessage(Component.text("  " + sd.getId() + " - " + type + " (" + String.format("%.1f", dist) + "m)")
                    .color(NamedTextColor.WHITE));
        }
    }

    // ========================
    // EXISTING COMMANDS
    // ========================

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            err(sender, "/dapi remove <id>");
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
                err(sender, "'" + id + "' 를 찾을 수 없습니다.");
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
            String type = sd.isAlive() ? sd.getEntity().getClass().getSimpleName().replace("Craft", "") : "Unknown";
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

    // ========================
    // HELPERS
    // ========================

    private void msg(CommandSender sender, String message) {
        sender.sendMessage(Component.text("[DisplayAPI] " + message).color(NamedTextColor.GREEN));
    }

    private void err(CommandSender sender, String message) {
        sender.sendMessage(Component.text("[DisplayAPI] " + message).color(NamedTextColor.RED));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("=== DisplayAPI 도움말 ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  /dapi create text <텍스트> [id:아이디]").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi create block <블록> [아이디]").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi create item <아이템> [아이디]").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi edit <id> <속성> <값>").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi move <id> — 현재 위치로 이동").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi animate <id> <애니메이션>").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi popup <텍스트> — 팝업 생성").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi remove <id> — 제거").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi removeall — 전체 제거").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi list — 목록").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi near [반경] — 근처 디스플레이").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi save — 저장").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi reload — 설정 재로드").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  /dapi info — 정보").color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  색코드: &a초록 &c빨강 &6금색 등 사용 가능").color(NamedTextColor.GRAY));
    }

    // ========================
    // TAB COMPLETE
    // ========================

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(List.of("create", "edit", "move", "animate", "popup",
                    "remove", "removeall", "list", "near", "save", "reload", "info"), args[0]);
        }

        if (args.length == 2) {
            return switch (args[0].toLowerCase()) {
                case "create" -> filter(List.of("text", "block", "item"), args[1]);
                case "edit", "move", "animate", "remove" -> {
                    List<String> ids = new ArrayList<>();
                    DisplayAPI.getManager().getAllDisplays().forEach(d -> ids.add(d.getId()));
                    DisplayAPI.getManager().getAllGroups().forEach(g -> ids.add(g.getId()));
                    yield filter(ids, args[1]);
                }
                default -> List.of();
            };
        }

        if (args.length == 3) {
            return switch (args[0].toLowerCase()) {
                case "create" -> {
                    if (args[1].equalsIgnoreCase("block") || args[1].equalsIgnoreCase("item")) {
                        yield filter(getMaterialNames(args[1].equalsIgnoreCase("block")), args[2]);
                    }
                    yield List.of();
                }
                case "edit" -> filter(List.of("text", "block", "item", "scale", "billboard", "glow"), args[2]);
                case "animate" -> filter(List.of("pulse", "spin", "bounce", "float", "shake", "fadein", "fadeout", "growin"), args[2]);
                default -> List.of();
            };
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("edit")) {
                return switch (args[2].toLowerCase()) {
                    case "block" -> filter(getMaterialNames(true), args[3]);
                    case "item" -> filter(getMaterialNames(false), args[3]);
                    case "billboard" -> filter(BILLBOARD_MODES, args[3]);
                    case "glow" -> {
                        List<String> opts = new ArrayList<>(COLORS);
                        opts.add("off");
                        yield filter(opts, args[3]);
                    }
                    case "scale" -> filter(List.of("0.5", "1.0", "1.5", "2.0", "3.0", "5.0"), args[3]);
                    default -> List.of();
                };
            }
            if (args[0].equalsIgnoreCase("animate") && args[2].equalsIgnoreCase("spin")) {
                return filter(List.of("x", "y", "z"), args[3]);
            }
        }

        return List.of();
    }

    // ========================
    // PARSE UTILITIES
    // ========================

    private String joinArgs(String[] args, int from) {
        return joinArgs(args, from, args.length);
    }

    private String joinArgs(String[] args, int from, int to) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < to; i++) {
            if (i > from) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private Material parseMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Billboard parseBillboard(String name) {
        return switch (name.toLowerCase()) {
            case "center" -> Billboard.CENTER;
            case "fixed" -> Billboard.FIXED;
            case "horizontal" -> Billboard.HORIZONTAL;
            case "vertical" -> Billboard.VERTICAL;
            default -> null;
        };
    }

    private Color parseColor(String name) {
        if (name.startsWith("#") && name.length() == 7) {
            try {
                int r = Integer.parseInt(name.substring(1, 3), 16);
                int g = Integer.parseInt(name.substring(3, 5), 16);
                int b = Integer.parseInt(name.substring(5, 7), 16);
                return Color.fromRGB(r, g, b);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return switch (name.toLowerCase()) {
            case "red" -> Color.RED;
            case "green" -> Color.GREEN;
            case "blue" -> Color.BLUE;
            case "yellow" -> Color.YELLOW;
            case "aqua" -> Color.AQUA;
            case "purple" -> Color.PURPLE;
            case "white" -> Color.WHITE;
            case "gold" -> Color.ORANGE;
            case "gray" -> Color.GRAY;
            case "dark_red" -> Color.MAROON;
            case "dark_green" -> Color.fromRGB(0, 128, 0);
            case "dark_blue" -> Color.NAVY;
            case "dark_aqua" -> Color.TEAL;
            case "dark_purple" -> Color.fromRGB(128, 0, 128);
            case "dark_gray" -> Color.fromRGB(64, 64, 64);
            case "black" -> Color.BLACK;
            default -> null;
        };
    }

    private List<String> getMaterialNames(boolean blocksOnly) {
        List<String> names = new ArrayList<>();
        for (Material mat : Material.values()) {
            if (mat.isLegacy()) continue;
            if (blocksOnly && !mat.isBlock()) continue;
            if (!blocksOnly && !mat.isItem()) continue;
            names.add(mat.name().toLowerCase());
        }
        return names;
    }

    private List<String> filter(List<String> options, String input) {
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
