package data;

import javax.swing.*;
import java.util.Objects;

public class IconData {

    private final ImageIcon icon;
    private final int xOffset, yOffset;

    public IconData(String path, int xOffset, int yOffset) {
        this.icon = imageIconFromResource(path);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    private static ImageIcon imageIconFromResource(String path) {
        return new ImageIcon(Objects.requireNonNull(IconData.class.getResource(path)));
    }
}
