package main.gui;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class VisualizationWindow extends JPanel {
    private static class Point {
        float x, y;
        String name;

        Point(float x, float y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }
    }
    private Map<String, Color> creatorColors;
    private int animationStep = 0;
    private static final int ANIMATION_STEPS = 100;

    public VisualizationWindow() {
        this.creatorColors = new HashMap<>();

        Timer timer = new Timer(50, e -> {
            animationStep = (animationStep + 1) % ANIMATION_STEPS;
            repaint();
        });
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Object[] row : TableWindow.data) {
                    Point from = new Point(((Long) row[6]).floatValue(), ((Integer) row[7]).floatValue(), (String) row[8]);
                    Point to = new Point(((Long) row[9]).floatValue(), ((Long) row[10]).floatValue(), "");
                    if (isPointOnLine(from, to, e.getPoint())) {
                        JOptionPane.showMessageDialog(VisualizationWindow.this,
                                MessageFormat.format(TableWindow.bundle.getString("route.info"), row),
                                TableWindow.bundle.getString("window.title.routeinfo"),
                                JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        });
    }

    private Color getColorFromName(String name) {
        int hash = name.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Object[] route : TableWindow.data) {
            g2d.setColor(creatorColors.get(route[1]));
            g2d.setStroke(new BasicStroke(4));
            creatorColors.put((String) route[1], getColorFromName((String) route[1]));
            Point from = new Point(((Long) route[6]).floatValue(), ((Integer) route[7]).floatValue(), (String) route[8]);
            Point to = new Point(((Long) route[9]).floatValue(), ((Long) route[10]).floatValue(), "");
            drawArrowLine(g2d, from, to);
            drawPointName(g2d, from);
            drawMovingObject(g2d, from, to);
        }
    }

    private void drawArrowLine(Graphics2D g2d, Point from, Point to) {
        int arrowSize = 10;
        float dx = to.x - from.x, dy = to.y - from.y;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(from.x, from.y);
        at.concatenate(AffineTransform.getRotateInstance(angle));

        g2d.transform(at);
        g2d.drawLine(0, 0, len, 0);
        g2d.fillPolygon(new int[] {len, len-arrowSize, len-arrowSize, len},
                new int[] {0, -arrowSize, arrowSize, 0}, 4);
        g2d.setTransform(new AffineTransform());
    }

    private void drawPointName(Graphics2D g2d, Point point) {
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(point.name);
        int textHeight = fm.getHeight();

        g2d.setColor(Color.WHITE);
        g2d.fillRect((int) (point.x - textWidth / 2), (int) (point.y - textHeight / 2), textWidth, textHeight);

        g2d.setColor(Color.BLACK);
        g2d.drawString(point.name, point.x - textWidth / 2, point.y + textHeight / 4);
    }

    private void drawMovingObject(Graphics2D g2d, Point from, Point to) {
        double t = (double) animationStep / ANIMATION_STEPS;
        int x = (int) (from.x * (1 - t) + to.x * t);
        int y = (int) (from.y * (1 - t) + to.y * t);
        g2d.setColor(Color.RED);
        g2d.fillOval(x - 5, y - 5, 10, 10);
    }

    private boolean isPointOnLine(Point from, Point to, java.awt.Point clickPoint) {
        Line2D line = new Line2D.Float(from.x, from.y, to.x, to.y);
        return line.ptSegDist(clickPoint) <= 5.0;
    }

    public static void createAndShowGUI(ResourceBundle bundle) {
        JFrame frame = new JFrame(bundle.getString("window.title.visualization"));
        VisualizationWindow panel = new VisualizationWindow();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
