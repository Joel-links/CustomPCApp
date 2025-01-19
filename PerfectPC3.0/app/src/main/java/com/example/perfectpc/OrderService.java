package com.example.perfectpc;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    // Une liste simulant un stockage des commandes
    private List<Order> orders;

    // Constructeur
    public OrderService() {
        orders = new ArrayList<>();
    }

    // Méthode pour créer une commande
    public Order createOrder(String requesterId, List<String> componentIds) {
        Order newOrder = new Order(requesterId, componentIds);
        orders.add(newOrder);
        return newOrder;
    }

    // Méthode pour consulter l'état d'une commande par ID
    public String viewOrderState(String orderId) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order.getState();
            }
        }
        return "Commande introuvable";
    }

    // Méthode pour supprimer une commande par ID
    public boolean deleteOrder(String orderId) {
        return orders.removeIf(order -> order.getId().equals(orderId));
    }

    // Méthode pour mettre à jour l'état d'une commande
    public boolean updateOrderState(String orderId, String newState) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                order.setState(newState);
                return true;
            }
        }
        return false;
    }

    // Méthode pour obtenir les détails d'une commande
    public Order getOrderDetails(String orderId) {
        for (Order order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    // Méthode pour vérifier la disponibilité d'un composant
    public boolean checkComponentAvailability(String componentId) {
        // Simuler la logique de vérification de la disponibilité
        return true; // Retourne true pour l'exemple
    }
}
