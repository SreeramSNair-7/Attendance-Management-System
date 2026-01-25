package com.attendance.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * Enhanced with mouse interaction and dynamic responsiveness
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
    private int mouseX = -1;
    private int mouseY = -1;
    private float mouseInfluence = 0f;
    private List<Ripple> ripples;
    private float glowIntensity = 0f;

    public CustomBackgroundPanel() {
        this(null);
    }

    public CustomBackgroundPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        random = new Random();
        particles = new ArrayList<>();
        ripples = new ArrayList<>();
        initializeParticles();
        startAnimation();
        setupMouseInteraction();
    }

    private void initializeParticles() {
        // Create floating particles for visual interest
        int particleCount = 50;
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle());
        }
    }

    private void setupMouseInteraction() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                mouseInfluence = 1.0f;
                // Attract nearby particles
                for (Particle p : particles) {
                    float dx = mouseX - p.x;
                    float dy = mouseY - p.y;
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist < 150) {
                        float force = (150 - dist) / 150f * 0.1f;
                        p.vx += dx / dist * force;
                        p.vy += dy / dist * force;
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Create ripple effect on click
                ripples.add(new Ripple(e.getX(), e.getY()));
                glowIntensity = 1.0f;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                mouseX = -1;
                mouseY = -1;
                mouseInfluence = 0f;
            }
        };
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void startAnimation() {
        animationTimer = new Timer(16, e -> { // 60 FPS for smoother animation
            animationOffset += 0.005f;
            waveOffset += 0.08f;
            if (animationOffset > 1.0f) {
                animationOffset = 0f;
            }
            // Update particle positions
            for (Particle p : particles) {
                p.update(getWidth(), getHeight());
            }
            // Update ripples
            ripples.removeIf(r -> r.alpha <= 0);
            for (Ripple r : ripples) {
                r.update();
            }
            // Fade mouse influence
            if (mouseInfluence > 0) {
                mouseInfluence -= 0.02f;
            }
            // Fade glow intensity
            if (glowIntensity > 0) {
                glowIntensity -= 0.05f;
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
        
        // Draw mouse interaction effects
        if (mouseX >= 0 && mouseY >= 0 && mouseInfluence > 0) {
            drawMouseGlow(g2d);
        }
        
        // Draw ripple effects
        drawRipples(g2d);
        
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
        for (Particle p : particles) {
            float alpha = Math.min(p.alpha / 255f * 0.5f, 0.5f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            // Add glow effect around particles
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {
                new Color(255, 255, 255, p.alpha),
                new Color(255, 255, 255, 0)
            };
            
            Point2D center = new Point2D.Float(p.x + p.size / 2, p.y + p.size / 2);
            float radius = p.size * 2;
            RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
            g2d.setPaint(gradient);
            g2d.fill(new Ellipse2D.Double(p.x - p.size, p.y - p.size, p.size * 3, p.size * 3));
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawWavePatterns(Graphics2D g2d, int width, int height) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Draw multiple wave layers with enhanced motion
        for (int layer = 0; layer < 4; layer++) {
            float layerAlpha = 0.2f + layer * 0.1f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, layerAlpha));
            g2d.setColor(new Color(255, 255, 255, 40 + layer * 15));
            
            int amplitude = 35 + layer * 12;
            int frequency = 60 + layer * 25;
            int yOffset = height / 5 + layer * 80;
            
            for (int x = 0; x < width; x += 3) {
                double waveSpeed = waveOffset * (layer + 1) * 1.2;
                int y = yOffset + (int) (Math.sin((x + waveSpeed) / frequency) * amplitude);
                int nextX = x + 3;
                int nextY = yOffset + (int) (Math.sin((nextX + waveSpeed) / frequency) * amplitude);
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

    private void drawMouseGlow(Graphics2D g2d) {
        float alpha = mouseInfluence * 0.3f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        float[] dist = {0.0f, 0.5f, 1.0f};
        Color[] colors = {
            new Color(255, 255, 255, 180),
            new Color(255, 255, 255, 90),
            new Color(255, 255, 255, 0)
        };
        
        Point2D center = new Point2D.Float(mouseX, mouseY);
        float radius = 200f;
        RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
        g2d.setPaint(gradient);
        g2d.fill(new Ellipse2D.Double(mouseX - radius, mouseY - radius, radius * 2, radius * 2));
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawRipples(Graphics2D g2d) {
        for (Ripple ripple : ripples) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ripple.alpha));
            g2d.setStroke(new BasicStroke(3f));
            g2d.setColor(new Color(255, 255, 255, (int) (ripple.alpha * 255)));
            g2d.draw(new Ellipse2D.Double(
                ripple.x - ripple.radius, 
                ripple.y - ripple.radius,
                ripple.radius * 2, 
                ripple.radius * 2
            ));
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
            
            // Apply damping to velocity for smoother motion
            vx *= 0.98f;
            vy *= 0.98f;
            
            // Keep minimum velocity
            if (Math.abs(vx) < 0.1f) vx = (random.nextFloat() - 0.5f) * 0.5f;
            if (Math.abs(vy) < 0.1f) vy = (random.nextFloat() - 0.5f) * 0.5f;

            // Wrap around edges for continuous effect
            if (x < 0) x = maxWidth;
            if (x > maxWidth) x = 0;
            if (y < 0) y = maxHeight;
            if (y > maxHeight) y = 0;
        }
    }

    // Inner class for ripple effects
    private class Ripple {
        float x, y;
        float radius;
        float alpha;
        float growthRate;

        Ripple(int x, int y) {
            this.x = x;
            this.y = y;
            this.radius = 10;
            this.alpha = 1.0f;
            this.growthRate = 5f;
        }

        void update() {
            radius += growthRate;
            alpha -= 0.02f;
            if (alpha < 0) alpha = 0;
        }
    }
}
