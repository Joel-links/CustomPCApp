package com.example.perfectpc;

import java.time.LocalDateTime;

public class Component {

    private final String type;
    private final String subType;
    private final String title;
    private  int quantity;
    private  String comment;
    private final LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    //Conctructeurs des éléments
    public Component(String title, String subType, String type, int quantity,String comment){
        this.type = type;
        this.subType = subType;
        this.title = title;
        this.quantity = quantity;
        this.comment = comment;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    // Getters et Setters
    public String getType() {return type;}

    public String getSubType() {return subType;}

    public String getTitle() {return title;}

    public int getQuantity() {return quantity;}

    public LocalDateTime getCreatedDate() {return createdDate;}

    public LocalDateTime getUpdatedDate() {return updatedDate;}

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.updatedDate = LocalDateTime.now();
    }

    public String getComment() {return comment;}
    public void setComment(String comment) {
        this.comment = comment;
        this.updatedDate = LocalDateTime.now();
    }

    // Incrémenter et décrémenter la quantité
    public void incrementQuantity(){
        this.quantity ++;
        this.updatedDate = LocalDateTime.now();
    }

    public void decrementQuantity(){
        if (this.quantity >0){
            this.quantity --;
            this.updatedDate = LocalDateTime.now();
        }
    }
}
