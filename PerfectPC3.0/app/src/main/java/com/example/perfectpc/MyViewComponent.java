package com.example.perfectpc;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewComponent extends RecyclerView.ViewHolder {

    private ManageStock manageStock;

    private final TextView viewtitle,viewtype,viewsubtype,quantity;
    private final Button decrement,increment;
    private  final DatabaseHelper dbHelper;


    // Initialiser l'interface de la liste des éléments
    public MyViewComponent(@NonNull View itemView, ManageStock manageStock, DatabaseHelper dbHelper, String role, AdapterComponent.OnComponentClickListener listener) {
        super(itemView);
        viewtitle = itemView.findViewById(R.id.viewtitle);
        viewtype = itemView.findViewById(R.id.viewtype);
        viewsubtype = itemView.findViewById(R.id.viewsubtype);
        quantity = itemView.findViewById(R.id.quantity);
        decrement = itemView.findViewById(R.id.decrement);
        increment = itemView.findViewById(R.id.increment);

        this.manageStock = manageStock;
        this.dbHelper = dbHelper;

        if("Requester".equals(role)){
            quantity.setVisibility(View.GONE);
            decrement.setVisibility(View.GONE);
            increment.setVisibility(View.GONE);
        } else if ("StoreKeeper".equals(role)) {
            quantity.setVisibility(View.VISIBLE);
            decrement.setVisibility(View.VISIBLE);
            increment.setVisibility(View.VISIBLE);
        }

        itemView.setOnClickListener(v -> {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onComponentClick((Component) itemView.getTag(), position);
            }
        });
    }

    public void bind(Component component) {
        if (component != null) {
            viewtitle.setText(component.getTitle());
            viewtype.setText(component.getType());
            viewsubtype.setText(component.getSubType());
            quantity.setText(String.valueOf(component.getQuantity()));

            increment.setOnClickListener(view -> {
                int newQuantity = component.getQuantity() + 1;
                updateQuantity(component, newQuantity);
            });

            decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (component.getQuantity()>0){
                        int newQuantity = component.getQuantity() - 1;
                        updateQuantity(component, newQuantity);
                    }

                }
            });
        }
    }

    private void updateQuantity(Component component, int newQuantity) {

        component.setQuantity(newQuantity);

        // mis a jour de la base de donnée
        boolean success = dbHelper.updateComponentQuantity(component.getTitle(), newQuantity);
        if (success) {

            manageStock.modifyComponent(component.getTitle(), newQuantity, null);


            quantity.setText(String.valueOf(newQuantity));
        } else {

            Toast.makeText(itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
        }
    }
}
