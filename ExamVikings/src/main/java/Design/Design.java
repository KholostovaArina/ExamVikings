package Design;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Design {
    private static Image rightImage;
    private static Image leftImage;
    private static Image firstImage;
    private static Image astraImage;        
    
    static {
        try {
            rightImage = ImageIO.read(Design.class.getResourceAsStream("/право.png"));
            leftImage = ImageIO.read(Design.class.getResourceAsStream("/лево.png"));
            firstImage = ImageIO.read(Design.class.getResourceAsStream("/море.jpg"));
            astraImage = ImageIO.read(Design.class.getResourceAsStream("/Astra.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static Font bigFont;
    
    
    static {
        try (InputStream fontStream = Design.class.getResourceAsStream("/bigFont.otf")) {
            bigFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            bigFont = bigFont.deriveFont(Font.BOLD, 46);
        } catch (IOException | FontFormatException e) {
            bigFont = new Font("Serif", Font.PLAIN, 28);
        }
    }
    
    private static Font baseFont;
    static {
        try (InputStream fontStream = Design.class.getResourceAsStream("/baseFont.otf")) {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            baseFont = baseFont.deriveFont(Font.BOLD, 22);
        } catch (IOException | FontFormatException e) {
            baseFont = new Font("Serif", Font.PLAIN, 18);
        }
    }

    public static Font getBigFont() {
        return bigFont;
    }

    public static Font getBaseFont() {
        return baseFont;
    }
    
    public static Image getLeftImage() {
        return leftImage;
    }

    public static Image getFirstImage() {
        return firstImage;
    }

    public static Image getAstraImage() {
        return astraImage;
    }
    
    public static Image getRightImage() {
        return rightImage;
    }
    
        public static JPanel createPanelWithPhoto(Image image) {
        if (image == null) {
            System.out.println("Изображение не загружено.");
            return (new JPanel(new BorderLayout())); 
        }

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setOpaque(false);
        return backgroundPanel;
    }
        
        
        
    // Внутренний класс кастомной кнопки с эффектами hover/press
    public static class CustomButton extends JButton {
        private boolean isHovering = false;
        private boolean isPressed = false;

        public CustomButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            // Слушатели мыши для эффекта наведения/нажатия
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovering = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovering = false;
                    isPressed = false;
                    repaint();
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        isPressed = true;
                        repaint();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Визуальный стиль: обычная/hover/pressed
            Color base = new Color(0, 0, 0, 100);
            Color hover = new Color(30, 30, 30, 160);
            Color pressed = new Color(60, 60, 60, 200);
            if (isPressed) {
                g2.setColor(pressed);
            } else if (isHovering) {
                g2.setColor(hover);
            } else {
                g2.setColor(base);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();

            // Текст рисуем поверх кастомного фона
            super.paintComponent(g);
        }
    }    
}
