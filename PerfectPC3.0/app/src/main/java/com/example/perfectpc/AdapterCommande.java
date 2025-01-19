package com.example.perfectpc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterCommande extends RecyclerView.Adapter<AdapterCommande.ViewHolder> {

    private final List<Commande> commandes;
    private final OnCommandeClickListener listener;

    public interface OnCommandeClickListener {
        void onCommandeClick(Commande commande, int position);
    }

    public AdapterCommande(List<Commande> commandes, OnCommandeClickListener listener) {
        this.commandes = commandes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commande, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commande commande = commandes.get(position);

        // ID de la commande
        holder.commandeId.setText("ID: " + commande.getId());

        // Article lié à la commande
        holder.commandeItem.setText("Article: " + commande.getStockItemId());

        // Dates formatées
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String createdDate = dateFormat.format(new Date(commande.getCreatedDate()));
        String updatedDate = dateFormat.format(new Date(commande.getUpdatedDate()));

        holder.commandeCreatedDate.setText("Créé le: " + createdDate);
        holder.commandeUpdatedDate.setText("Mis à jour le: " + updatedDate);

        // Gérer le clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommandeClick(commande, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commandes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView commandeId, commandeItem, commandeCreatedDate, commandeUpdatedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commandeId = itemView.findViewById(R.id.commande_id);
            commandeItem = itemView.findViewById(R.id.commande_item);
            commandeCreatedDate = itemView.findViewById(R.id.commande_created_date);
            commandeUpdatedDate = itemView.findViewById(R.id.commande_updated_date);
        }
    }
}
