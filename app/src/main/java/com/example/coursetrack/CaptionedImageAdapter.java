package com.example.coursetrack;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CaptionedImageAdapter extends RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder> {
    private ArrayList<String> nameArray;

    interface Listener{
        void onClick(int position);
    }
    private Listener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(@NonNull CardView cv) {
            super(cv);
            cardView = cv;
        }
    }
    public CaptionedImageAdapter(ArrayList<String> nameArray){
        this.nameArray = nameArray;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionedImageAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.name);
        name.setText(nameArray.get(position));
        ImageView bookImage = cardView.findViewById(R.id.info_image);
        bookImage.setOnClickListener(view -> {
            if(listener!=null){
                listener.onClick(position);
            }
        });
    }


    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return nameArray.size();
    }
}
