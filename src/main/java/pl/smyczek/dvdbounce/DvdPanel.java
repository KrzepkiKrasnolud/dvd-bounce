package pl.smyczek.dvdbounce;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DvdPanel extends JPanel implements ActionListener {

    private static final int LOGO_W = 120;
    private static final int LOGO_H = 60;
    private static final int SPEED = 2;
    private static final int FPS = 60;

    private int x = 100, y = 80;
    private int dx = SPEED, dy = SPEED;
    private Color color = JBColor.RED;
    private final Timer timer;

    private static final Color[] COLORS = {
            JBColor.RED,
            JBColor.GREEN,
            JBColor.BLUE,
            JBColor.CYAN,
            JBColor.MAGENTA,
            JBColor.YELLOW,
            JBColor.ORANGE
    };
    private int colorIndex = 0;

    public DvdPanel() {
        setOpaque(false);
        timer = new Timer(1000 / FPS, this);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        x += dx;
        y += dy;

        boolean bounced = false;

        if (x + LOGO_W >= w) {
            x = w - LOGO_W;
            dx = -SPEED;
            bounced = true;
        }
        if (x <= 0) {
            x = 0;
            dx =  SPEED;
            bounced = true;
        }
        if (y + LOGO_H >= h) {
            y = h - LOGO_H;
            dy = -SPEED;
            bounced = true;
        }
        if (y <= 0) {
            y = 0;
            dy =  SPEED;
            bounced = true;
        }

        if (bounced) {
            color = COLORS[++colorIndex % COLORS.length];
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);

        g2.setFont(new Font("SansSerif", Font.BOLD, 38));
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth("DVD");
        int textX = x + (LOGO_W - textW) / 2;
        int textY = y + (LOGO_H / 2) + fm.getAscent() / 3;
        g2.drawString("DVD", textX, textY);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        fm = g2.getFontMetrics();
        int vw = fm.stringWidth("VIDEO");
        g2.drawString("VIDEO", x + (LOGO_W - vw) / 2, textY + 14);

        g2.dispose();
    }
}
