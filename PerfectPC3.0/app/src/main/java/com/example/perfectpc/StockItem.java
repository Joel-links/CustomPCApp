package com.example.perfectpc;

public class StockItem {
    private String itemName;
    private int quantity;

    public StockItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    // Getter pour le nom de l'élément
    public String getItemName() {
        return itemName;
    }

    // Setter pour le nom de l'élément
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getter pour la quantité
    public int getQuantity() {
        return quantity;
    }

    // Setter pour la quantité
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}