package com.mycompany.examvikings.GUI;

import Design.Design;
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

    public JPanel create(JLabel sharedSilverLabel) {
        this.silverLabel = sharedSilverLabel;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        createSilverPanel();
        addSlavesSection();
        addLootGridSection();
        addSellButton();
        Design.setFontForAllComponents(mainPanel, new Color(39, 44, 59));
                

        return mainPanel;
    }

    private void createSilverPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(silverLabel);
        topPanel.setOpaque(false);
        mainPanel.add(topPanel);
    }

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

        slavesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Inventory.getSlaves(), 1));
        slavesSpinner.setMaximumSize(new Dimension(80, 30));
        slavesSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        slavesSpinner.addChangeListener(e -> {
            SpinnerNumberModel model = (SpinnerNumberModel) slavesSpinner.getModel();
            model.setMaximum(Inventory.getSlaves());
        });

        panel.add(slavesSpinner);
        panel.add(Box.createVerticalStrut(20));
        mainPanel.add(panel);
    }

    private void addLootGridSection() {
        Map<String, Integer> lootCounts = countLootItems();
        createLootGrid(lootCounts);
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
        itemPanel.setOpaque(true);
        itemPanel.setBackground(new Color(250, 250, 250));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel qtyLabel = new JLabel("В наличии: " + qty);
        qtyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sellLabel = new JLabel("Продать:");
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
        Design.CustomButton sellButton = new Design.CustomButton("Продать выбранное");
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
        if (confirm != JOptionPane.YES_OPTION) return;

        performSale(hasSelection, slavesToSell);
    }

    private void performSale(boolean hasSelection, int slavesToSell) {
        SaleResult result = new SaleResult();
        result.slavesSold = slavesToSell;
        result.totalPrice += slavesToSell * 10;

        // Продаем рабов
        if (result.slavesSold > 0) {
            Inventory.setSlaves(Inventory.getSlaves() - result.slavesSold);
        }

        // Продаем лут
        for (int i = 0; i < lootSpinners.size(); i++) {
            int toSell = (Integer) lootSpinners.get(i).getValue();
            if (toSell <= 0) continue;
            String name = lootNames.get(i);
            int removed = removeLootItems(name, toSell);
            if (removed > 0) {
                result.totalPrice += removed * 10;
                result.totalSold += removed;
                result.soldItems.append(removed).append(" ").append(name).append("<br>");
            }
        }

        // Добавляем серебро
        Inventory.setSilver(Inventory.getSilver() + result.totalPrice);

        // Обновляем UI
        refreshSilver();
        updateSlavesCount();

        showResultMessage(result);
        clearSelections();
    }

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

    private void updateSlavesCount() {
        slavesCountLabel.setText(String.valueOf(Inventory.getSlaves()));
        ((SpinnerNumberModel) slavesSpinner.getModel()).setMaximum(Inventory.getSlaves());
    }

    private void clearSelections() {
        slavesSpinner.setValue(0);
        for (JSpinner spinner : lootSpinners) {
            spinner.setValue(0);
        }
    }

    private void showResultMessage(SaleResult result) {
        JOptionPane.showMessageDialog(null,
                "<html>Вы продали:<br>" +
                        result.soldItems +
                        "<br>Всего предметов: " + result.totalSold +
                        "<br>Серебро получено: " + result.totalPrice + "</html>",
                "Результат продажи", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshSilver() {
        if (silverLabel != null) {
            silverLabel.setText("Серебро: " + Inventory.getSilver());
        }
    }

    private static class SaleResult {
        int totalSold = 0;
        double totalPrice = 0;
        int slavesSold = 0;
        StringBuilder soldItems = new StringBuilder();
    }
}