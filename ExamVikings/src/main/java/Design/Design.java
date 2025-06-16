package Design;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Класс, содержащий статические ресурсы и утилитарные методы для работы с графическим интерфейсом.
 * Включает загрузку изображений, шрифтов и создание кастомных компонентов.
 */
public class Design {

    /**
     * Изображение кнопки "Право".
     */
    private static Image rightImage;

    /**
     * Изображение кнопки "Лево".
     */
    private static Image leftImage;

    /**
     * Первое фоновое изображение .
     */
    private static Image firstImage;

    /**
     * Изображение Астры (дочери Звезд).
     */
    private static Image astraImage;

    /**
     * Изображение карты.
     */
    private static Image mapImage;

    /**
     * Изображение правой панели выбора.
     */
    private static Image rightPanelImage;

    /**
     * Инициализирует статические изображения при загрузке класса.
     */
    static {
        try {
            rightImage = ImageIO.read(Design.class.getResourceAsStream("/право.png"));
            leftImage = ImageIO.read(Design.class.getResourceAsStream("/лево.png"));
            firstImage = ImageIO.read(Design.class.getResourceAsStream("/море.jpg"));
            astraImage = ImageIO.read(Design.class.getResourceAsStream("/Astra.png"));
            mapImage = ImageIO.read(Design.class.getResourceAsStream("/карта.png"));
            rightPanelImage = ImageIO.read(Design.class.getResourceAsStream("/праваяПанелька.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Статический шрифт большого размера для заголовков.
     */
    private static Font bigFont;

    /**
     * Инициализирует шрифт большого размера.
     */
    static {
        try (InputStream fontStream = Design.class.getResourceAsStream("/bigFont.otf")) {
            bigFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            bigFont = bigFont.deriveFont(Font.BOLD, 46);
        } catch (IOException | FontFormatException e) {
            bigFont = new Font("Serif", Font.PLAIN, 28);
        }
    }

    /**
     * Статический базовый шрифт для текста.
     */
    private static Font baseFont;

    /**
     * Инициализирует базовый шрифт.
     */
    static {
        try (InputStream fontStream = Design.class.getResourceAsStream("/baseFont.otf")) {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            baseFont = baseFont.deriveFont(Font.BOLD, 18);
        } catch (IOException | FontFormatException e) {
            baseFont = new Font("Serif", Font.PLAIN, 18);
        }
    }

    /**
     * Возвращает изображение правой панели.
     *
     * @return изображение правой панели
     */
    public static Image getRightPanelImage() {
        return rightPanelImage;
    }

    /**
     * Возвращает изображение карты.
     *
     * @return изображение карты
     */
    public static Image getMapImage() {
        return mapImage;
    }

    /**
     * Возвращает большой шрифт для заголовков.
     *
     * @return объект {@link Font}
     */
    public static Font getBigFont() {
        return bigFont;
    }

    /**
     * Возвращает базовый шрифт для текста.
     *
     * @return объект {@link Font}
     */
    public static Font getBaseFont() {
        return baseFont;
    }

    /**
     * Возвращает изображение кнопки "Лево".
     *
     * @return изображение кнопки "Лево"
     */
    public static Image getLeftImage() {
        return leftImage;
    }

    /**
     * Возвращает первое фоновое изображение.
     *
     * @return первое фоновое изображение
     */
    public static Image getFirstImage() {
        return firstImage;
    }

    /**
     * Возвращает изображение Астры (дочери Звезд).
     *
     * @return изображение Астры
     */
    public static Image getAstraImage() {
        return astraImage;
    }

    /**
     * Возвращает изображение кнопки "Право".
     *
     * @return изображение кнопки "Право"
     */
    public static Image getRightImage() {
        return rightImage;
    }

    /**
     * Создаёт панель с заданным фоновым изображением.
     *
     * @param image фоновое изображение
     * @return панель с наложенным изображением
     */
    public static JPanel createPanelWithPhoto(Image image) {
        if (image == null) {
            System.out.println("Изображение не загружено.");
            return new JPanel(new BorderLayout());
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

    /**
     * Устанавливает базовый шрифт для всех компонентов контейнера и его дочерних элементов.
     *
     * @param container контейнер, для которого нужно установить шрифт
     */
    public static void setFontForAllComponents(Container container) {
        for (Component component : container.getComponents()) {
            component.setFont(baseFont);

            if (component instanceof Container) {
                Container container1 = (Container) component;
                setFontForAllComponents(container1);
            }
        }
    }

    /**
     * Устанавливает базовый шрифт и цвет текста для всех компонентов контейнера и его дочерних элементов.
     *
     * @param container контейнер, для которого нужно установить шрифт и цвет
     * @param color цвет текста
     */
    public static void setFontForAllComponents(Container container, Color color) {
        for (Component component : container.getComponents()) {
            component.setFont(baseFont);
            component.setForeground(color);

            if (component instanceof Container) {
                Container container1 = (Container) component;
                setFontForAllComponents(container1);
            }
        }
    }

    /**
     * Внутренний класс для создания кастомной кнопки с эффектами наведения и нажатия.
     */
    public static class CustomButton extends JButton {
        private boolean isHovering = false;
        private boolean isPressed = false;

        /**
         * Конструктор кастомной кнопки.
         *
         * @param text текст кнопки
         */
        public CustomButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            // Добавляем слушатели мыши для эффектов наведения/нажатия
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

        /**
         * Перерисовывает кнопку с учетом состояния наведения и нажатия.
         *
         * @param g графический контекст
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            // Определение цвета фона в зависимости от состояния
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

            // Рисуем текст поверх кастомного фона
            super.paintComponent(g);
        }
    }

    /**
     * Устанавливает все компоненты контейнера и его дочерние элементы как непрозрачные.
     *
     * @param container контейнер, для которого нужно установить прозрачность
     */
    public static void makeAllNonOpaque(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComponent jComp) {
                jComp.setOpaque(false);
            }
            if (comp instanceof Container cont) {
                makeAllNonOpaque(cont);
            }
        }
    }
}