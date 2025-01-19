package com.example.perfectpc;

import java.time.LocalDateTime;

public class Commande {

    private final String id;
    private final String stockItemId;
    private final long createdDate;
    private  final long updatedDate;

    public Commande(String id, String stockItemId) {
        this.id = id;
        this.stockItemId = stockItemId;
        this.createdDate = System.currentTimeMillis();
        this.updatedDate = this.createdDate;
    }

    public Commande(String id, String stockItemId, long createdDate, long updatedDate) {
        this.id = id;
        this.stockItemId = stockItemId;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    }

    public String getStockItemId() {
        return stockItemId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

}
