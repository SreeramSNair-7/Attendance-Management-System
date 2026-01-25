package com.attendance.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Custom panel with animated gradient background, floating particles, and wave effects
 * This only changes visual appearance - no content modifications
 * Features: Responsive sizing, animated gradients, floating particles, wave patterns
 */
public class CustomBackgroundPanel extends JPanel {
    private Color color1 = new Color(52, 152, 219); // Blue
    private Color color2 = new Color(155, 89, 182); // Purple
    private Color color3 = new Color(26, 188, 156); // Teal
    private float animationOffset = 0f;
    private Timer animationTimer;
    private List<Particle> particles;
    private Random random;
    private float waveOffset = 0f;

    public CustomBackgroundPanel() {
        this(null);
    }

    public CustomBackgroundPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        random = new Random();
        particles = new ArrayList<>();
        initializeParticles();
        startAnimation();
    }

    private void initializeParticles() {
        // Create floating particles for visual interest
        int particleCount = 30;
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle());
        }
    }

    private void startAnimation() {
        animationTimer = new Timer(30, e -> {
            animationOffset += 0.003f;
            waveOffset += 0.05f;
            if (animationOffset > 1.0f) {
                animationOffset = 0f;
            }
            // Update particle positions
            for (Particle p : particles) {
                p.update(getWidth(), getHeight());
            }
            repaint();
        });
        animationTimer.start();
    }

    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Multi-layered animated gradient
        drawAnimatedGradient(g2d, width, height);
        
        // Draw floating particles
        drawParticles(g2d);
        
        // Draw animated wave patterns
        drawWavePatterns(g2d, width, height);
        
        // Add geometric pattern overlay
        drawGeometricPattern(g2d, width, height);
        
        g2d.dispose();
    }

    private void drawAnimatedGradient(Graphics2D g2d, int width, int height) {
        float offset = animationOffset;
        
        // Create diagonal gradient with rotation effect
        double angle = offset * Math.PI * 2;
        int x1 = (int) (width / 2 + Math.cos(angle) * width / 2);
        int y1 = (int) (height / 2 + Math.sin(angle) * height / 2);
        int x2 = (int) (width / 2 - Math.cos(angle) * width / 2);
        int y2 = (int) (height / 2 - Math.sin(angle) * height / 2);
        
        Point2D start = new Point2D.Float(x1, y1);
        Point2D end = new Point2D.Float(x2, y2);
        
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
            interpolateColor(color1, color2, offset),
            interpolateColor(color2, color3, offset),
            interpolateColor(color3, color1, offset)
        };
        
        LinearGradientPaint gradient = new LinearGradientPaint(start, end, dist, colors);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawParticles(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (Particle p : particles) {
            g2d.setColor(new Color(255, 255, 255, p.alpha));
            Ellipse2D.Double circle = new Ellipse2D.Double(p.x, p.y, p.size, p.size);
            g2d.fill(circle);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawWavePatterns(Graphics2D g2d, int width, int height) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        g2d.setStroke(new BasicStroke(2f));
        
        // Draw multiple wave layers
        for (int layer = 0; layer < 3; layer++) {
            g2d.setColor(new Color(255, 255, 255, 30 + layer * 10));
            int amplitude = 30 + layer * 10;
            int frequency = 50 + layer * 20;
            int yOffset = height / 4 + layer * 100;
            
            for (int x = 0; x < width; x += 5) {
                int y = yOffset + (int) (Math.sin((x + waveOffset * (layer + 1)) / frequency) * amplitude);
                int nextX = x + 5;
                int nextY = yOffset + (int) (Math.sin((nextX + waveOffset * (layer + 1)) / frequency) * amplitude);
                g2d.drawLine(x, y, nextX, nextY);
            }
        }
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawGeometricPattern(Graphics2D g2d, int width, int height) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
        g2d.setColor(new Color(255, 255, 255, 20));
        
        // Draw diagonal grid pattern
        int spacing = 40;
        for (int i = -height; i < width + height; i += spacing) {
            g2d.drawLine(i, 0, i + height, height);
            g2d.drawLine(i, 0, i - height, height);
        }
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private Color interpolateColor(Color c1, Color c2, float factor) {
        float f = Math.abs((float) Math.sin(factor * Math.PI));
        int r = (int) (c1.getRed() + f * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + f * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + f * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }

    // Setters for custom colors (optional customization)
    public void setGradientColors(Color c1, Color c2, Color c3) {
        this.color1 = c1;
        this.color2 = c2;
        this.color3 = c3;
        repaint();
    }

    // Inner class for floating particles
    private class Particle {
        float x, y;
        float vx, vy;
        int size;
        int alpha;

        Particle() {
            reset(random.nextInt(2000), random.nextInt(2000));
        }

        void reset(int maxWidth, int maxHeight) {
            x = random.nextInt(maxWidth > 0 ? maxWidth : 1200);
            y = random.nextInt(maxHeight > 0 ? maxHeight : 700);
            vx = (random.nextFloat() - 0.5f) * 0.5f;
            vy = (random.nextFloat() - 0.5f) * 0.5f;
            size = random.nextInt(3) + 2;
            alpha = random.nextInt(100) + 50;
        }

        void update(int maxWidth, int maxHeight) {
            if (maxWidth <= 0 || maxHeight <= 0) return;
            
            x += vx;
            y += vy;

            // Wrap around edges for continuous effect
            if (x < 0) x = maxWidth;
            if (x > maxWidth) x = 0;
            if (y < 0) y = maxHeight;
            if (y > maxHeight) y = 0;
        }
    }
}
