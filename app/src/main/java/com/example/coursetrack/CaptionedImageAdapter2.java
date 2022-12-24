package com.example.coursetrack;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CaptionedImageAdapter2 extends RecyclerView.Adapter<CaptionedImageAdapter2.ViewHolder> {
    private ArrayList<String> nameArray;
    interface Listener{
        void onClick(int position);
    }
    private CaptionedImageAdapter.Listener listener;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ViewHolder(@NonNull CardView cv) {
            super(cv);
            cardView = cv;
        }
    }
    public CaptionedImageAdapter2(ArrayList<String> nameArray){
        this.nameArray = nameArray;
    }
    @NonNull
    @Override
    public CaptionedImageAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view2, parent, false);
        return new CaptionedImageAdapter2.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionedImageAdapter2.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.name);
        name.setText(nameArray.get(position));
        cardView.setOnClickListener(view -> {
            if(listener!=null){
                listener.onClick(position);
            }
        });
    }


    public void setListener(CaptionedImageAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return nameArray.size();
    }
}
