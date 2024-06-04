import java.awt.*;
import java.applet.*;


public class SecondTask extends Applet implements Runnable {
    Rectangle[] rectangles = new Rectangle[30];
    int dx = 11, dy = 7;
    volatile boolean pleaseStop;
    Thread animator;

    public void init() {
        for (int i = 0; i < 10; i++) {
            rectangles[i] = new ColorableRect(50 * i, 50 * i + 100, 30, 30, Color.PINK);
            rectangles[i + 10] = new ColorableRect(50 * i, 50 * i + 300, 30, 30, Color.GREEN);
            rectangles[i + 20] = new ColorableRect(50 * i + 200, 50 * i + 200, 30, 30, Color.CYAN);
        }
    }

    public void paint(Graphics g) {
        for (Rectangle rect : rectangles) {
            if (rect instanceof DrawableRect) {
                ((DrawableRect) rect).draw(g);
            } else if (rect instanceof ColorableRect) {
                ((ColorableRect) rect).draw(g);
            } else {
                g.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }

    public void animate() {
        Rectangle bounds = getBounds();
        for (Rectangle rect : rectangles) {
            if ((rect.x + dx < 0) || (rect.x + rect.width + dx > bounds.width)) dx = -dx;
            if ((rect.y + dy < 0) || (rect.y + rect.height + dy > bounds.height)) dy = -dy;
            rect.x += dx;
            rect.y += dy;
        }
        repaint();
    }

    public void run() {
        while (!pleaseStop) {
            animate();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }

    public void start() {
        animator = new Thread(this);
        pleaseStop = false;
        animator.start();
    }

    public void stop() {
        pleaseStop = true;
    }
}



class DrawableRect extends Rectangle {
    public DrawableRect(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
    }
}



class ColorableRect extends DrawableRect {
    private Color color;

    public ColorableRect(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}