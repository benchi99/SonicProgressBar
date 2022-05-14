package ui;

import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import data.BarCharacter;
import settings.AppSettingsState;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ProgressBarUi extends BasicProgressBarUI {
    BufferedImage bufferedImage = null;

    public ProgressBarUi() {
        try {
            bufferedImage = ImageIO.read(this.getClass().getResource("/bar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent jComponent) {
        jComponent.setBorder(JBUI.Borders.empty().asUIResource());
        return new ProgressBarUi();
    }

    @Override
    public Dimension getPreferredSize(JComponent jComponent) {
        return new Dimension(super.getPreferredSize(jComponent).width, JBUIScale.scale(20));
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        progressBar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
            }
        });
    }

    private volatile int offset = 0;
    private volatile int offset2 = 0;
    private volatile int velocity = 1;

    @Override
    protected void paintIndeterminate(Graphics graphics, JComponent jComponent) {
        if (graphics instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Insets progressBarInsets = progressBar.getInsets();
            int barRectWidth = progressBar.getWidth() - (progressBarInsets.right + progressBarInsets.left);
            int barRectHeight = progressBar.getHeight() - (progressBarInsets.top + progressBarInsets.bottom);

            if (barRectWidth > 0 || barRectHeight > 0) {
                graphics2D.setColor(new JBColor(Gray._240.withAlpha(50), Gray._128.withAlpha(50)));
                int width = jComponent.getWidth();
                int height = jComponent.getPreferredSize().height;

                if (isNotEven(jComponent.getHeight() - height)) {
                    height++;
                }

                if (jComponent.isOpaque()) {
                    graphics2D.fillRect(0, (jComponent.getHeight() - height) / 2, width, height);
                }

                graphics2D.setColor(new JBColor(Gray._165.withAlpha(50), Gray._88.withAlpha(50)));
                final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics2D);
                graphics2D.translate(0, (jComponent.getHeight() - height) / 2);

                final float RADIUS = JBUIScale.scale(8f);
                final float RADIUS_2 = JBUIScale.scale(9f);
                final Area containingRoundRect = new Area(new RoundRectangle2D.Float(1f, 1f, width - 2f, height - 2f, RADIUS, RADIUS));
                graphics2D.fill(containingRoundRect);
                offset = (offset + 1) % getPeriodLength();
                offset2 += velocity;

                if (offset2 <= 2) {
                    offset2 = 2;
                    velocity = 1;
                } else if (offset2 >= width - JBUIScale.scale(15)) {
                    offset2 = width - JBUIScale.scale(15);
                    velocity = -1;
                }

                Area area = new Area(new Rectangle2D.Float(0, 0, width, height));
                area.subtract(new Area(new RoundRectangle2D.Float(1f, 1f, width - 2f, height - 2f, RADIUS, RADIUS)));

                if (jComponent.isOpaque()) {
                    graphics2D.fill(area);
                }

                area.subtract(new Area(new RoundRectangle2D.Float(0, 0, width, height, RADIUS_2, RADIUS_2)));

                if (jComponent.isOpaque()) {
                    graphics2D.fill(area);
                }

                AppSettingsState settings = AppSettingsState.getInstance();

                var currentCharacter = settings.character;
                var iconDataToDraw = velocity >= 0 ? currentCharacter.getSkid() : currentCharacter.getSkidMirror();
                iconDataToDraw.getIcon().paintIcon(progressBar, graphics2D, offset2 - JBUIScale.scale(iconDataToDraw.getxOffset()), -JBUIScale.scale(iconDataToDraw.getyOffset()));

                if (settings.andKnuckles) {
                    var knux = BarCharacter.KNUX;
                    var knuxIconData = velocity >= 0 ? knux.getSkid() : knux.getSkidMirror();
                    knuxIconData.getIcon().paintIcon(progressBar, graphics2D, offset2 - JBUIScale.scale((knuxIconData.getxOffset() * 2) + 9), -JBUIScale.scale(knuxIconData.getyOffset()));
                }

                graphics2D.draw(new RoundRectangle2D.Float(1f, 1f, width - 2f - 1f, height - 2f - 1f, RADIUS, RADIUS));
                graphics2D.translate(0, -(jComponent.getHeight() - height) / 2);

                if (progressBar.isStringPainted()) {
                    if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
                        paintString(graphics2D, progressBarInsets.left, progressBarInsets.top, barRectWidth, barRectHeight, boxRect.x, boxRect.width);
                    } else {
                        paintString(graphics2D, progressBarInsets.left, progressBarInsets.top, barRectWidth, barRectHeight, boxRect.y, boxRect.height);
                    }
                }

                config.restore();
            }
        }
    }

    @Override
    protected void paintDeterminate(Graphics graphics, JComponent jComponent) {
        if (graphics instanceof Graphics2D) {
            if (progressBar.getOrientation() != SwingConstants.HORIZONTAL || !jComponent.getComponentOrientation().isLeftToRight()) {
                super.paintDeterminate(graphics, jComponent);
            } else {
                final GraphicsConfig config = GraphicsUtil.setupAAPainting(graphics);
                Insets progressBarInsets = progressBar.getInsets(); // area for border
                int width = progressBar.getWidth();
                int height = progressBar.getPreferredSize().height;

                if (isNotEven(jComponent.getHeight() - height)) {
                    height++;
                }

                int barRectWidth = width - (progressBarInsets.right + progressBarInsets.left);
                int barRectHeight = height - (progressBarInsets.top + progressBarInsets.bottom);

                if (barRectWidth > 0 || barRectHeight > 0) {
                    int amountFull = getAmountFull(progressBarInsets, barRectWidth, barRectHeight);
                    Container parent = jComponent.getParent();
                    Color background = parent != null ? parent.getBackground() : UIUtil.getPanelBackground();
                    graphics.setColor(background);
                    Graphics2D graphics2D = (Graphics2D) graphics;

                    if (jComponent.isOpaque()) {
                        graphics.fillRect(0, 0, width, height);
                    }

                    final float RADIUS = JBUIScale.scale(8f);
                    final float RADIUS_2 = JBUIScale.scale(9f);
                    final float OFFSET = JBUIScale.scale(1f);
                    graphics2D.translate(0, (jComponent.getHeight() - height) / 2);
                    graphics2D.setColor(progressBar.getForeground());
                    graphics2D.fill(new RoundRectangle2D.Float(0, 0, width - OFFSET, height - OFFSET, RADIUS_2, RADIUS_2));
                    graphics2D.setColor(background);
                    graphics2D.fill(new RoundRectangle2D.Float(OFFSET, OFFSET, width - 2f * OFFSET - OFFSET, height - 2f * OFFSET - OFFSET, RADIUS, RADIUS));

                    if (bufferedImage != null) {
                        TexturePaint texturePaint = new TexturePaint(bufferedImage, new Rectangle2D.Double(0, 1, height - 2f * OFFSET - OFFSET, height - 2f * OFFSET - OFFSET));
                        graphics2D.setPaint(texturePaint);
                    }

                    graphics2D.fill(new RoundRectangle2D.Float(2f * OFFSET, 2f * OFFSET, amountFull - JBUIScale.scale(5f), height - JBUIScale.scale(5f), JBUIScale.scale(7f), JBUIScale.scale(7f)));

                    AppSettingsState settings = AppSettingsState.getInstance();

                    var dashIconData = settings.character.getDash();
                    dashIconData.getIcon().paintIcon(progressBar, graphics2D, amountFull - JBUIScale.scale(dashIconData.getxOffset()), -JBUIScale.scale(dashIconData.getyOffset()));
                    if (settings.andKnuckles) {
                        var knuxDashIconData = BarCharacter.KNUX.getDash();
                        knuxDashIconData.getIcon().paintIcon(progressBar, graphics2D, amountFull - JBUIScale.scale((knuxDashIconData.getxOffset() * 2) + 9), -JBUIScale.scale(knuxDashIconData.getyOffset()));
                    }
                    graphics2D.translate(0, -(jComponent.getHeight() - height) / 2);

                    if (progressBar.isStringPainted()) {
                        paintString(graphics, progressBarInsets.left, progressBarInsets.top,
                                barRectWidth, barRectHeight,
                                amountFull, progressBarInsets);
                    }
                    config.restore();
                }
            }
        }
    }

    private void paintString(Graphics graphics, int x, int y, int w, int h, int fillStart, int amountFull) {
        if (!(graphics instanceof Graphics2D)) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;
        String progressString = progressBar.getString();
        graphics2D.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(graphics2D, progressString,
                x, y, w, h);
        Rectangle oldClip = graphics2D.getClipBounds();

        if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
            graphics2D.setColor(getSelectionBackground());
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);

            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(fillStart, y, amountFull, h);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        } else {
            graphics2D.setColor(getSelectionBackground());
            AffineTransform rotate =
                    AffineTransform.getRotateInstance(Math.PI / 2);
            graphics2D.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(graphics2D, progressString,
                    x, y, w, h);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(x, fillStart, w, amountFull);
            BasicGraphicsUtils.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        }
        graphics2D.setClip(oldClip);
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength;
    }

    private int getPeriodLength() {
        return JBUIScale.scale(16);
    }

    private static boolean isNotEven(int value) {
        return value % 2 != 0;
    }
}