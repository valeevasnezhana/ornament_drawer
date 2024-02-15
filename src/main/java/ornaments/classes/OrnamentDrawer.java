package ornaments.classes;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrnamentDrawer extends JPanel {
    private final List<OrnamentColor> colors;
    private final int rectHeight;
    private final int rectWidth;
    private final int repeats;
    private ColorChoiceMatrix colorChoiceMatrix;
    private List<ColorStatistics> colorStats;

    public OrnamentDrawer(List<OrnamentColor> colors, int rectWidth,
                          int repeats) {
        if (repeats < 3) {
            throw new IllegalStateException("repeats must be more or equal " +
                    "than 3");
        }
        if (rectWidth < 1) {
            throw new IllegalStateException("rectWidth must be positive " +
                    "integer (size in pixels)");
        }
        if (colors.isEmpty()) {
            throw new IllegalStateException("colors must be no-empty list of " +
                    "colors");
        }
        this.colors = colors;
        this.rectHeight = (int) Math.round(rectWidth * Math.sqrt(2));
        this.rectWidth = rectWidth;
        this.repeats = repeats;
        generateStart();
        setPreferredSize(
                new Dimension(repeats * rectWidth,
                        (repeats - 3) * rectHeight + rectWidth));
    }

    private void generateStart() {
        colorStats = new ArrayList<>();
        for (OrnamentColor color : colors) {
            colorStats.add(new ColorStatistics(color));
        }
        colorChoiceMatrix = new ColorChoiceMatrix(repeats, colors);
    }

    public void drawNewOrnament() {
        if (colorChoiceMatrix == null) {
            generateStart();
        } else {
            colorChoiceMatrix.refill();
        }
        repaint();
    }

    public void saveOrnament(String saveDir) {
        String imageIndex = colorChoiceMatrix.calculateIdentifier();
        String imagePath = saveDir + "orn_" + imageIndex + ".png";
        BufferedImage image = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        try {
            paintComponent(g2d);
        } finally {
            g2d.dispose();
        }

        try {
            ImageIO.write(image, "png", new File(imagePath));
            System.out.println("Image " + imagePath + " saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String statsPath = saveDir + "orn_" + imageIndex + "_stats.txt";

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(statsPath))) {
            analyzePiecesAmount();
            for (ColorStatistics stat : colorStats) {
                writer.write(stat.toString() + "\n");
            }
            System.out.println("File " + statsPath + " saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawEvenPolygons(Graphics2D g2d) {
        int y = -rectWidth / 2 + 6;
        int x = rectWidth;

        for (int j = 0; j < repeats; j += 2) {
            for (int i = 0; i < repeats - 1; i++) {
                OrnamentColor color =
                        colors.get(colorChoiceMatrix.getEntry()[i][j]);
                int[] yPoints = {y, y + rectHeight, y + rectHeight - rectWidth,
                        y - rectWidth};
                int[] xPoints = {x, x, x - rectWidth, x - rectWidth};
                Polygon polygon = new Polygon(xPoints, yPoints, 4);
                g2d.setColor(color.getColor());
                g2d.fill(polygon);

                y += rectHeight;
            }
            y = -rectWidth / 2 + 6;
            x += 2 * rectWidth;
        }
    }

    private void drawOddPolygons(Graphics2D g2d) {
        int y = 0;
        int x = rectWidth * 2;

        for (int j = 1; j < repeats; j += 2) {
            for (int i = 0; i < repeats - 1; i++) {
                OrnamentColor color =
                        colors.get(colorChoiceMatrix.getEntry()[i][j]);
                int[] yPoints = {y, y - rectHeight, y - rectHeight + rectWidth,
                        y + rectWidth};
                int[] xPoints = {x, x, x - rectWidth, x - rectWidth};
                Polygon polygon = new Polygon(xPoints, yPoints, 4);
                g2d.setColor(color.getColor());
                g2d.fill(polygon);

                y += rectHeight;
            }
            y = 0;
            x += 2 * rectWidth;
        }
    }

    private void analyzePiecesAmount() {
        boolean left = true;

        for (ColorStatistics stat : colorStats) {
            stat.clearStats();
        }

        for (int index = 0; index < colorChoiceMatrix.getEntry().length;
             index++) {
            int[] mat = colorChoiceMatrix.getEntry()[index];
            boolean half = false;
            boolean bottom = true;

            if (index == 0) {
                half = true;
            }
            if (index == colorChoiceMatrix.getEntry().length - 1) {
                half = true;
                bottom = false;
            }

            for (int x : mat) {
                if (half) {
                    colorStats.get(x).addHalf(bottom, left);
                } else {
                    colorStats.get(x).addFull(left);
                }
                left = !left;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawEvenPolygons(g2d);
        drawOddPolygons(g2d);
    }
}
