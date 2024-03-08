package ornaments.classes;


public class ColorStatistics {
    private final OrnamentColor color;
    private int fullLeft;
    private int fullRight;
    private int halfLeftUp;
    private int halfRightUp;
    private int halfLeftBottom;
    private int halfRightBottom;


    public ColorStatistics(OrnamentColor color) {
        this.color = color;
        clearStats();
    }

    public OrnamentColor getColor() {
        return color;
    }

    public void addHalf(boolean bottom, boolean left) {
        if (bottom) {
            if (left) {
                halfLeftBottom++;
            } else {
                halfRightBottom++;
            }
        } else {
            if (left) {
                halfLeftUp++;
            } else {
                halfRightUp++;
            }
        }
    }

    public void addFull(boolean left) {
        if (left) {
            fullLeft++;
        } else {
            fullRight++;
        }
    }

    public void clearStats() {
        fullLeft = 0;
        fullRight = 0;
        halfLeftUp = 0;
        halfRightUp = 0;
        halfLeftBottom = 0;
        halfRightBottom = 0;
    }

    public String toString() {
        return color.name() + ": " +
                "\n\tfullLeft=" + fullLeft +
                ", fullRight=" + fullRight +
                ", halfLeftUp=" + halfLeftUp +
                ", halfRightUp=" + halfRightUp +
                ", halfLeftBottom=" + halfLeftBottom +
                ", halfRightBottom=" + halfRightBottom + "\n";
    }
}
