package com.mycompany.examvikings.GUI;

import Design.Design;
import javax.swing.*;
import java.awt.*;

public class LoreFrame extends JFrame {
    public LoreFrame() {
        setTitle("Пророчество Викингов");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Панель с картинкой
        JPanel panel = Design.createPanelWithPhoto(Design.getAstraImage());
        panel.setLayout(null);

        // Первый заголовок
        JLabel titleLabel = new JLabel("Астра, Дочь Звезд", SwingConstants.CENTER);
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(230, 102, 24));
        titleLabel.setBounds(100, 65, 600, 50);
        panel.add(titleLabel);

        // Второй заголовок
        JLabel title2Label = new JLabel("Пророчица, чьи видения направляют викингов", SwingConstants.CENTER);
        title2Label.setFont(Design.getBigFont().deriveFont(22f));
        title2Label.setForeground(new Color(230, 180, 80));
        title2Label.setBounds(50, 120, 700, 50);
        
        panel.add(title2Label);

        // Многострочное описание через JTextArea (чтобы переносило строки и выглядело аккуратно)
        JTextArea loreArea = new JTextArea(
                "В маленькой скандинавской деревне родилась девочка Астра, " +
                "наделенная даром предвидеть будущее.\n\n" +
                "Её пророчества стали ключом к успеху викингских набегов. " +
                "Теперь вы можете использовать её дар, " +
                "чтобы направить свои корабли к победе или выбрать другой путь..."
        );
        loreArea.setFont(Design.getBaseFont());
        loreArea.setOpaque(false);
        loreArea.setEditable(false);
        loreArea.setLineWrap(true);
        loreArea.setWrapStyleWord(true);
        loreArea.setForeground(Color.WHITE);
        loreArea.setBounds(120, 180, 560, 2200);
        loreArea.setHighlighter(null);
        panel.add(loreArea);

        // Кнопка 
        Design.CustomButton startButton = new Design.CustomButton("Далее");
        startButton.setFont(Design.getBaseFont());
        startButton.setForeground(Color.WHITE);
        startButton.setBounds(300, 470, 200, 50);
        panel.add(startButton);

        setContentPane(panel);
        setVisible(true);
    }
}