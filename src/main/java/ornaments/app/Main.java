package ornaments.app;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ornaments.classes.OrnamentColor;
import ornaments.classes.OrnamentDrawer;
import ornaments.config.ApplicationConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static ApplicationConfig config;
    private static boolean saveEnabled;

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);
        config = context.getBean(ApplicationConfig.class);
        saveEnabled = true;

        try {
            Files.createDirectory(Path.of(config.getOrnamentSavePath()));
        } catch (FileAlreadyExistsException ignored) {

        } catch (IOException e) {
            saveEnabled = false;
        }


        JFrame ornamentFrame = createMainFrame();
        OrnamentDrawer ornamentDrawer = createOrnamentDrawer();
        JPanel buttonPanel = createButtonPanel(ornamentDrawer);

        ornamentFrame.add(ornamentDrawer, BorderLayout.WEST);
        ornamentFrame.add(buttonPanel, BorderLayout.EAST);

        ornamentFrame.pack();
        ornamentFrame.setVisible(true);
    }

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Ornament Drawer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static OrnamentDrawer createOrnamentDrawer() {
        List<OrnamentColor> colors = getOrnamentColors();
        int rectWidth = config.getRectWidth();
        int repeats = config.getRepeats();
        return new OrnamentDrawer(colors, rectWidth, repeats);
    }

    private static List<OrnamentColor> getOrnamentColors() {
        return config.getOrnamentColors();
    }

    private static JPanel createButtonPanel(OrnamentDrawer ornamentDrawer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (saveEnabled) {
            JButton buttonSave = createButton("Save",
                    e -> ornamentDrawer.saveOrnament(
                            config.getOrnamentSavePath()));
            panel.add(buttonSave);
        }
        JButton buttonRegenerate = createButton("Regenerate",
                e -> ornamentDrawer.drawNewOrnament());

        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonRegenerate);

        return panel;
    }

    private static JButton createButton(String title,
                                        ActionListener actionListener) {
        JButton button = new JButton(title);
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actionListener);
        return button;
    }
}
