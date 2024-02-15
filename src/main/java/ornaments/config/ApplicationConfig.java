package ornaments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ornaments.classes.OrnamentColor;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ComponentScan(basePackages = "ornaments")
@Configuration
@PropertySource("classpath:app.properties")
public class ApplicationConfig {

    @Value("${ornament.savePath}")
    private String ornamentSavePath;

    @Value("${ornament.colors}")
    private String[] colorDefinitions;

    @Value("${ornament.rectWidth}")
    private int rectWidth;

    @Value("${ornament.repeats}")
    private int repeats;

    public String getOrnamentSavePath() {
        if (ornamentSavePath.equals("default")) {
            FileSystemView view = FileSystemView.getFileSystemView();
            File file = view.getHomeDirectory();
            ornamentSavePath = file.getPath() + "/Desktop/ornaments/";
        } else if (!ornamentSavePath.endsWith("/")) {
            ornamentSavePath += "/";
        }
        return ornamentSavePath;
    }

    public List<OrnamentColor> getOrnamentColors() {
        return Stream.of(colorDefinitions)
                .map(colorDef -> {
                    String[] parts = getStrings(colorDef);
                    Color color = Color.decode("#" + parts[0]);
                    if (parts.length == 2) {
                        return new OrnamentColor(color, parts[1]);
                    } else {
                        return new OrnamentColor(color, "#" + parts[0]);
                    }
                })
                .collect(Collectors.toList());
    }

    private static String[] getStrings(String colorDef) {
        String[] parts = colorDef.split(":");
        if (parts.length < 1 || parts.length > 2) {
            throw new IllegalArgumentException(
                    "Invalid color definition in properties: " +
                            colorDef + "\nExample: ornament" +
                            ".colors=938477:Light Beige," +
                            "6C583E:Dark Beige\n or ornament.colors=938477,6C583E:Dark Beige");
        }
        return parts;
    }


    public int getRectWidth() {
        return rectWidth;
    }

    public int getRepeats() {
        return repeats;
    }
}
