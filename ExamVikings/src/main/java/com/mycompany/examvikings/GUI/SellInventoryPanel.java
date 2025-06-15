package com.mycompany.examvikings.GUI;

import Entity.Inventory;
import Entity.Loot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class SellInventoryPanel {
    private JLabel silverLabel;
    private JSpinner slavesSpinner;
    private JLabel slavesCountLabel;
    private JPanel mainPanel;
    
    // Для добычи
    private final List<JSpinner> lootSpinners = new ArrayList<>();
    private final List<String> lootNames = new ArrayList<>();
    private final List<JLabel> lootQtyLabels = new ArrayList<>();
    private final List<JPanel> lootItemPanels = new ArrayList<>();
    private JPanel lootGridPanel;

    public JPanel create() {
        initializeComponents();
        return mainPanel;
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        createSilverPanel();
        addSlavesSection();
        addLootGridSection();
        addSellButton();
    }

    private void createSilverPanel() {
        silverLabel = new JLabel("Серебро: " + Inventory.getSilver());
        silverLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(silverLabel);
        topPanel.setOpaque(false);
        mainPanel.add(topPanel);
    }

    private void addSlavesSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel title = new JLabel("РАБЫ");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);

        // Добавляем лейбл "У вас рабов"
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.setOpaque(false);
        
        JLabel slavesTextLabel = new JLabel("У вас рабов: ");
        slavesTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        
        slavesCountLabel = new JLabel(String.valueOf(Inventory.getSlaves()));
        slavesCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        slavesCountLabel.setForeground(Color.BLUE);
        
        countPanel.add(slavesTextLabel);
        countPanel.add(slavesCountLabel);
        
        panel.add(countPanel);

        JLabel sellLabel = new JLabel("Сколько продать:");
        sellLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sellLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sellLabel);

        // Инициализируем спиннер с текущим количеством рабов
        slavesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Inventory.getSlaves(), 1));
        slavesSpinner.setMaximumSize(new Dimension(80, 30));
        slavesSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Добавляем слушатель для обновления максимального значения
        slavesSpinner.addChangeListener(e -> {
            SpinnerNumberModel model = (SpinnerNumberModel) slavesSpinner.getModel();
            model.setMaximum(Inventory.getSlaves());
        });
        
        panel.add(slavesSpinner);
        panel.add(Box.createVerticalStrut(20));

        mainPanel.add(panel);
    }

    private void updateSlavesCount() {
        slavesCountLabel.setText(String.valueOf(Inventory.getSlaves()));
        
        // Обновляем спиннер
        SpinnerNumberModel model = (SpinnerNumberModel) slavesSpinner.getModel();
        model.setMaximum(Inventory.getSlaves());
        if ((Integer)slavesSpinner.getValue() > Inventory.getSlaves()) {
            slavesSpinner.setValue(0);
        }
    }

    private void addLootGridSection() {
        clearLootData();

        Map<String, Integer> lootCounts = countLootItems();

        JLabel title = new JLabel("ДОБЫЧА");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(10));

        createLootGrid(lootCounts);
        mainPanel.add(Box.createVerticalStrut(30));
    }

    private void clearLootData() {
        lootSpinners.clear();
        lootNames.clear();
        lootQtyLabels.clear();
        lootItemPanels.clear();

        if (lootGridPanel != null) {
            mainPanel.remove(lootGridPanel);
        }
    }

    private Map<String, Integer> countLootItems() {
        Map<String, Integer> lootCounts = new LinkedHashMap<>();
        for (Loot loot : Inventory.getProducts()) {
            lootCounts.merge(loot.getName(), 1, Integer::sum);
        }
        return lootCounts;
    }

    private void createLootGrid(Map<String, Integer> lootCounts) {
        lootGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        lootGridPanel.setOpaque(false);

        for (Map.Entry<String, Integer> entry : lootCounts.entrySet()) {
            createLootItem(entry.getKey(), entry.getValue());
        }

        mainPanel.add(lootGridPanel);
    }

    private void createLootItem(String name, int qty) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel qtyLabel = new JLabel("Количество: " + qty);
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        qtyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sellLabel = new JLabel("Продать:");
        sellLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sellLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, qty, 1));
        spinner.setMaximumSize(new Dimension(70, 26));
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemPanel.add(Box.createVerticalStrut(10));
        itemPanel.add(nameLabel);
        itemPanel.add(qtyLabel);
        itemPanel.add(sellLabel);
        itemPanel.add(spinner);
        itemPanel.add(Box.createVerticalStrut(10));

        lootGridPanel.add(itemPanel);

        lootNames.add(name);
        lootSpinners.add(spinner);
        lootQtyLabels.add(qtyLabel);
        lootItemPanels.add(itemPanel);
    }

    private void addSellButton() {
        JButton sellButton = new JButton("Продать выбранное");
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.addActionListener(this::confirmAndSell);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(sellButton);
        mainPanel.add(Box.createVerticalStrut(20));
    }

    private void confirmAndSell(ActionEvent e) {
        StringBuilder confirmationText = new StringBuilder("<html><b>Вы собираетесь продать:</b><ul>");
        boolean hasSelection = false;

        int slavesToSell = (Integer) slavesSpinner.getValue();
        if (slavesToSell > 0) {
            confirmationText.append("<li>").append(slavesToSell).append(" рабов</li>");
            hasSelection = true;
        }

        for (int i = 0; i < lootSpinners.size(); i++) {
            int toSell = (Integer) lootSpinners.get(i).getValue();
            if (toSell > 0) {
                confirmationText.append("<li>").append(toSell).append(" ").append(lootNames.get(i)).append("</li>");
                hasSelection = true;
            }
        }

        confirmationText.append("</ul></html>");

        if (!hasSelection) {
            JOptionPane.showMessageDialog(null, "Ничего не выбрано для продажи.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, confirmationText.toString(), "Подтвердите продажу",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        performSale();
    }

    private void performSale() {
        SaleResult result = calculateSale();

        updateInventory(result);
        updateUI(result);
        showResultMessage(result);
    }

    private SaleResult calculateSale() {
        SaleResult result = new SaleResult();

        // Продаем рабов
        int slavesToSell = (Integer) slavesSpinner.getValue();
        if (slavesToSell > 0) {
            int currentSlaves = Inventory.getSlaves();
            slavesToSell = Math.min(slavesToSell, currentSlaves);

            result.totalPrice += slavesToSell * 20;
            result.totalSold += slavesToSell;
            result.soldItems.append(slavesToSell).append(" рабов<br>");
            result.slavesSold = slavesToSell;
        }

        // Продаем добычу
        for (int i = 0; i < lootSpinners.size(); i++) {
            int toSell = (Integer) lootSpinners.get(i).getValue();
            if (toSell <= 0) continue;

            String name = lootNames.get(i);
            int removed = removeLootItems(name, toSell);

            if (removed > 0) {
                result.totalPrice += removed * 10;
                result.totalSold += removed;
                result.soldItems.append(removed).append(" ").append(name).append("<br>");

                if (getRemainingLootCount(name) <= 0) {
                    result.itemsToRemove.add(i);
                }
            }
        }

        return result;
    }

    private int removeLootItems(String name, int maxToRemove) {
        int removed = 0;
        Iterator<Loot> it = Inventory.getProducts().iterator();
        while (it.hasNext() && removed < maxToRemove) {
            Loot l = it.next();
            if (l.getName().equals(name)) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    private int getRemainingLootCount(String name) {
        int count = 0;
        for (Loot loot : Inventory.getProducts()) {
            if (loot.getName().equals(name)) {
                count++;
            }
        }
        return count;
    }

    private void updateInventory(SaleResult result) {
        // Обновляем количество рабов
        if (result.slavesSold > 0) {
            Inventory.setSlaves(Inventory.getSlaves() - result.slavesSold);
        }

        // Добавляем серебро
        Inventory.setSilver(Inventory.getSilver() + result.totalPrice);
    }

    private void updateUI(SaleResult result) {
        // Обновляем метку серебра
        silverLabel.setText("Серебро: " + Inventory.getSilver());

        // Обновляем рабов
        if (result.slavesSold > 0) {
            updateSlavesCount();
        }

        // Обновляем добычу
        for (int i = 0; i < lootSpinners.size(); i++) {
            if (result.itemsToRemove.contains(i)) continue;

            String name = lootNames.get(i);
            int remaining = getRemainingLootCount(name);
            
            if (remaining > 0) {
                lootQtyLabels.get(i).setText("Количество: " + remaining);
                JSpinner spinner = lootSpinners.get(i);
                spinner.setModel(new SpinnerNumberModel(0, 0, remaining, 1));
                spinner.setValue(0);
            } else {
                result.itemsToRemove.add(i);
            }
        }

        // Удаляем элементы с нулевым количеством (в обратном порядке)
        Collections.sort(result.itemsToRemove, Collections.reverseOrder());
        for (int index : result.itemsToRemove) {
            lootGridPanel.remove(lootItemPanels.get(index));
            lootNames.remove(index);
            lootSpinners.remove(index);
            lootQtyLabels.remove(index);
            lootItemPanels.remove(index);
        }

        // Обновляем интерфейс
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showResultMessage(SaleResult result) {
        JOptionPane.showMessageDialog(null,
                "<html>Вы продали:<br>" + result.soldItems +
                        "<br>Всего предметов: " + result.totalSold +
                        "<br>Серебро получено: " + result.totalPrice + "</html>",
                "Результат продажи", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class SaleResult {
        int totalSold = 0;
        double totalPrice = 0;
        int slavesSold = 0;
        StringBuilder soldItems = new StringBuilder();
        List<Integer> itemsToRemove = new ArrayList<>();
    }
}