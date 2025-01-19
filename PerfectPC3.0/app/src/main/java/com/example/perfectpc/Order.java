package com.example.perfectpc;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;                   // Identifiant unique de la commande
    private String requesterId;          // Identifiant de l'utilisateur ayant passé la commande
    private List<String> componentIds;   // Liste des composants dans la commande
    private String state;                // État de la commande
    private String rejectionReason;      // Raison du rejet (facultatif)
    private Date creationDate;           // Date de création de la commande
    private Date modificationDate;       // Date de dernière modification

    // Constructeur
    public Order(String requesterId, List<String> componentIds) {
        this.requesterId = requesterId;
        this.componentIds = componentIds;
        this.state = "En attente d'acceptation"; // État initial par défaut
        this.id = generateOrderId();            // Méthode pour générer un ID unique
        this.creationDate = new Date();         // Date de création
        this.modificationDate = new Date();     // Initialisation de la date de modification
        this.rejectionReason = "";              // Initialisation de la raison de rejet
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public List<String> getComponentIds() {
        return componentIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        updateModificationDate();
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
        updateModificationDate();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    // Méthodes utilitaires
    private String generateOrderId() {
        // Logique simple pour générer un ID unique
        return "ORD-" + System.currentTimeMillis();
    }

    private void updateModificationDate() {
        this.modificationDate = new Date();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", requesterId='" + requesterId + '\'' +
                ", componentIds=" + componentIds +
                ", state='" + state + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
