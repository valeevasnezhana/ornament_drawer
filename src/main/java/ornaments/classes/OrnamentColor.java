package ornaments.classes;


import java.awt.*;

public class OrnamentColor {
    private final Color color;
    private final String name;

    public OrnamentColor( Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
