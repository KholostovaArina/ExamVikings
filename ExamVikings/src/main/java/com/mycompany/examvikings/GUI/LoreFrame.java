package com.mycompany.examvikings.GUI;

import Design.Design;
import javax.swing.*;
import java.awt.*;

/**
 * Экран с легендой о пророчице Астре — дочери звёзд.
 * Отображает фоновое изображение, заголовки и описание,
 * а также кнопку для перехода на главный экран.
 */
public class LoreFrame extends JFrame {

    /**
     * Создаёт и настраивает окно с легендой.
     * Устанавливает фоновое изображение, текстовые элементы и кнопку "Далее".
     */
    public LoreFrame() {
        setTitle("Пророчество Викингов");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = Design.createPanelWithPhoto(Design.getAstraImage());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        panel.add(Box.createVerticalStrut(40));

        JLabel titleLabel = new JLabel("Астра, Дочь Звезд");
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(230, 102, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(15));

        JLabel title2Label = new JLabel("Пророчица, чьи видения направляют викингов");
        title2Label.setFont(Design.getBigFont().deriveFont(22f));
        title2Label.setForeground(new Color(230, 180, 80));
        title2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title2Label);

        panel.add(Box.createVerticalStrut(18));

        JTextArea loreArea = new JTextArea(
            "В маленькой скандинавской деревне родилась девочка Астра, " +
            "наделенная даром предвидеть будущее.\n\n" +
            "Её пророчества стали ключом к успеху викингских набегов. " +
            "Теперь вы можете использовать её дар, " +
            "чтобы направить свои корабли к победе или выбрать другой путь..."
        );
        loreArea.setFont(Design.getBaseFont().deriveFont(21f));
        loreArea.setOpaque(false);
        loreArea.setEditable(false);
        loreArea.setLineWrap(true);
        loreArea.setWrapStyleWord(true);
        loreArea.setForeground(Color.WHITE);
        loreArea.setHighlighter(null);
        loreArea.setMaximumSize(new Dimension(700, 150));
        loreArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loreArea);

        panel.add(Box.createVerticalStrut(40));

        Design.CustomButton startButton = new Design.CustomButton("Далее");
        startButton.setFont(Design.getBaseFont().deriveFont(24f));
        startButton.setForeground(Color.WHITE);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> {
            dispose();
            new HomeScreen();
        });
        panel.add(startButton);

        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        setVisible(true);
    }
}