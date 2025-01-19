package com.example.perfectpc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterComponent extends RecyclerView.Adapter<MyViewComponent>{

    private final List<Component> componentList;
    private final ManageStock manageStock;
    private final OnComponentClickListener onComponentClickListener;
    private final DatabaseHelper dbHelper;
    private final String role;

    public interface OnComponentClickListener {
        void onComponentClick(Component component, int position);
    }

    public AdapterComponent(List<Component> componentList, ManageStock manageStock, DatabaseHelper dbHelper, String role, OnComponentClickListener listener) {
        this.componentList = componentList;
        this.manageStock = manageStock;
        this.onComponentClickListener = listener;
        this.dbHelper = dbHelper;
        this.role = role;
    }

    @NonNull
    @Override
    public MyViewComponent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewComponent(LayoutInflater.from(parent.getContext()).inflate(R.layout.component_list,parent,false), manageStock, dbHelper, role, onComponentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewComponent holder, int position) {

        Component component = componentList.get(position);
        if (component != null) {
            holder.bind(component);
        }
    }

    @Override
    public int getItemCount() {
        return componentList.size() ;
    }

}
