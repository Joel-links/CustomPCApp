package com.example.perfectpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageStock {
    private List<Component> stockList = new ArrayList<>();

    public ManageStock() {
        this.stockList = new ArrayList<>();
    }

    public ManageStock(List<Component> components) {
        this.stockList = components;
    }


    //Method pour ajouter un élément
    public void addComponent(Component component){
        if (component != null) {
            stockList.add(component);
        }
    }

    //Method pour modifier un élément
    public void modifyComponent(String title, Integer newQuantity, String newComment){
        Optional<Component> componentOpt = stockList.stream().filter(component -> component.getTitle().equals(title)).findFirst();

        componentOpt.ifPresent(component -> {
            if (newQuantity != null) component.setQuantity(newQuantity);
            if (newComment != null) component.setComment(newComment);
        });
    }

    public void updateStockList(List<Component> components) {
        this.stockList = components;
    }


    //Liste des éléments
    public List<Component> getStockList() {
        return stockList;
    }
}
