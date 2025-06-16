package com.mycompany.examvikings.GUI;

import Design.Design;
import Entity.Inventory;
import Entity.Loot;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Панель, позволяющая игроку продавать рабов и добычу из инвентаря.
 * Содержит элементы управления для выбора количества и выполнения операции продажи.
 */
public class SellInventoryPanel {

    private JLabel silverLabel;
    private JSpinner slavesSpinner;
    private JLabel slavesCountLabel;
    private JPanel mainPanel;

    // Для добычи
    private final List<JSpinner> lootSpinners = new ArrayList<>();
    private final List<String> lootNames = new ArrayList<>();

    /**
     * Создаёт и возвращает панель с интерфейсом продажи предметов из инвентаря.
     *
     * @param sharedSilverLabel общая метка отображения серебра (обновляется при продаже)
     * @return настроенная {@link JPanel} с интерфейсом продажи
     */
    public JPanel create(JLabel sharedSilverLabel) {
        this.silverLabel = sharedSilverLabel;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Верхняя панель с количеством серебра
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(silverLabel);
        mainPanel.add(topPanel);

        addSlavesSection();
        addLootSection();

        JButton sellButton = new JButton("Продать выделенное");
        sellButton.setFont(Design.getBaseFont().deriveFont(24f));
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.addActionListener(this::confirmAndSell);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(sellButton);
        mainPanel.add(Box.createVerticalStrut(20));

        return mainPanel;
    }

    /**
     * Добавляет секцию рабов: отображение количества и возможность указать,
     * сколько продать.
     */
    private void addSlavesSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("РАБЫ");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.setOpaque(false);

        slavesCountLabel = new JLabel("У вас рабов: " + Inventory.getSlaves());
        slavesCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(slavesCountLabel);

        slavesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Inventory.getSlaves(), 1));
        slavesSpinner.setMaximumSize(new Dimension(70, 26));
        slavesSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(slavesSpinner);

        mainPanel.add(panel);
    }

    /**
     * Добавляет секцию добычи: список всех имеющихся предметов,
     * возможность выбрать количество для продажи.
     */
    private void addLootSection() {
        JPanel lootPanel = new JPanel();
        lootPanel.setLayout(new BoxLayout(lootPanel, BoxLayout.Y_AXIS));
        lootPanel.setOpaque(false);
        lootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("ДОБЫЧА");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        lootPanel.add(title);

        lootPanel.add(Box.createVerticalStrut(10));

        List<Loot> inventoryLoots = Inventory.getProducts();
        Map<String, Integer> lootCounts = new HashMap<>();

        for (Loot loot : inventoryLoots) {
            lootCounts.merge(loot.getName(), 1, Integer::sum);
        }

        createLootGrid(lootPanel, lootCounts);
        mainPanel.add(lootPanel);
    }

    /**
     * Создаёт сетку спиннеров и меток для каждого типа добычи.
     *
     * @param lootPanel контейнер, в который будут добавлены элементы
     * @param lootCounts мапа с количеством каждого предмета добычи
     */
    private void createLootGrid(JPanel lootPanel, Map<String, Integer> lootCounts) {
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);

        lootNames.clear();
        lootSpinners.clear();

        for (Map.Entry<String, Integer> entry : lootCounts.entrySet()) {
            String name = entry.getKey();
            int qty = entry.getValue();

            lootNames.add(name);

            JLabel itemNameLabel = new JLabel(name);
            itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemNameLabel.setForeground(Color.WHITE);
            gridPanel.add(itemNameLabel);

            JLabel qtyLabel = new JLabel("В наличии: " + qty);
            qtyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            qtyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            qtyLabel.setForeground(Color.WHITE);
            gridPanel.add(qtyLabel);

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, qty, 1));
            spinner.setMaximumSize(new Dimension(70, 26));
            spinner.setAlignmentX(Component.CENTER_ALIGNMENT);
            gridPanel.add(spinner);

            lootSpinners.add(spinner);
        }

        lootPanel.add(gridPanel);
    }

    /**
     * Обработчик события продажи. Открывает диалог подтверждения перед продажей.
     *
     * @param e событие действия
     */
    private void confirmAndSell(ActionEvent e) {
        StringBuilder confirmationText = new StringBuilder("<html><b>Вы собираетесь продать:</b><ul>");
        boolean hasSelection = false;

        int slavesToSell = (Integer) slavesSpinner.getValue();
        if (slavesToSell > 0) {
            hasSelection = true;
            confirmationText.append("<li>").append(slavesToSell).append(" рабов</li>");
        }

        for (int i = 0; i < lootSpinners.size(); i++) {
            int qty = (Integer) lootSpinners.get(i).getValue();
            if (qty > 0) {
                hasSelection = true;
                confirmationText.append("<li>").append(qty).append(" ").append(lootNames.get(i)).append("</li>");
            }
        }

        confirmationText.append("</ul></html>");

        if (!hasSelection) {
            JOptionPane.showMessageDialog(null, "Ничего не выбрано для продажи.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, confirmationText.toString(),
                "Подтвердите продажу", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        performSale(hasSelection, slavesToSell);
    }

    /**
     * Выполняет продажу выбранных предметов и обновляет инвентарь.
     *
     * @param hasSelection флаг наличия выбранных предметов
     * @param slavesToSell количество рабов к продаже
     */
    private void performSale(boolean hasSelection, int slavesToSell) {
        double earnedSilver = 0;

        if (slavesToSell > 0) {
            Inventory.setSlaves(Inventory.getSlaves() - slavesToSell);
            updateSlavesCount();
            earnedSilver += slavesToSell * 50;
        }

        for (int i = 0; i < lootSpinners.size(); i++) {
            int qty = (Integer) lootSpinners.get(i).getValue();
            if (qty > 0) {
                String lootName = lootNames.get(i);
                removeLootItems(lootName, qty);
                earnedSilver += qty * 20;
            }
        }

        Inventory.setSilver(Inventory.getSilver() + earnedSilver);
        refreshSilver();

        JOptionPane.showMessageDialog(null, String.format("Вы получили %.2f серебра за продажу.", earnedSilver));
    }

    /**
     * Удаляет указанное количество предметов добычи из инвентаря.
     *
     * @param name имя предмета добычи
     * @param quantity количество предметов к удалению
     * @return количество успешно удалённых предметов
     */
    private int removeLootItems(String name, int quantity) {
        List<Loot> products = Inventory.getProducts();
        int removed = 0;

        Iterator<Loot> it = products.iterator();
        while (it.hasNext() && removed < quantity) {
            Loot loot = it.next();
            if (loot.getName().equals(name)) {
                it.remove();
                removed++;
            }
        }

        return removed;
    }

    /**
     * Обновляет отображаемое количество рабов.
     */
    private void updateSlavesCount() {
        slavesCountLabel.setText("У вас рабов: " + Inventory.getSlaves());
    }

    /**
     * Обновляет значение метки с текущим количеством серебра.
     */
    public void refreshSilver() {
        if (silverLabel != null) {
            silverLabel.setText("Серебро: " + Inventory.getSilver());
        }
    }
}