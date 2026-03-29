package com.wjddusrb03.displayapi.builder;

import com.wjddusrb03.displayapi.display.SpawnedDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;

public class TextDisplayBuilder extends AbstractDisplayBuilder<TextDisplayBuilder> {

    private Component text = Component.empty();
    private Color backgroundColor = null;
    private byte opacity = (byte) -1; // fully opaque
    private boolean shadowed = false;
    private boolean seeThrough = false;
    private TextAlignment alignment = TextAlignment.CENTER;
    private int lineWidth = 200;

    public TextDisplayBuilder(org.bukkit.Location location) {
        super(location);
    }

    public TextDisplayBuilder text(Component text) {
        this.text = text;
        return this;
    }

    public TextDisplayBuilder text(String text) {
        this.text = Component.text(text);
        return this;
    }

    public TextDisplayBuilder background(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public TextDisplayBuilder background(int r, int g, int b, int a) {
        this.backgroundColor = Color.fromARGB(a, r, g, b);
        return this;
    }

    public TextDisplayBuilder noBackground() {
        this.backgroundColor = Color.fromARGB(0, 0, 0, 0);
        return this;
    }

    public TextDisplayBuilder opacity(int opacity) {
        this.opacity = (byte) Math.clamp(opacity, 0, 255);
        return this;
    }

    public TextDisplayBuilder shadowed(boolean shadowed) {
        this.shadowed = shadowed;
        return this;
    }

    public TextDisplayBuilder seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    public TextDisplayBuilder alignment(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextDisplayBuilder lineWidth(int width) {
        this.lineWidth = width;
        return this;
    }

    @Override
    public SpawnedDisplay spawn() {
        org.bukkit.World world = location.getWorld();
        if (world == null) throw new IllegalStateException("Location has no world");

        TextDisplay display = world.spawn(location, TextDisplay.class, td -> {
            td.text(text);
            td.setTextOpacity(opacity);
            td.setShadowed(shadowed);
            td.setSeeThrough(seeThrough);
            td.setAlignment(alignment);
            td.setLineWidth(lineWidth);
            if (backgroundColor != null) {
                td.setBackgroundColor(backgroundColor);
            }
            applyCommon(td);
        });

        return register(display);
    }
}
