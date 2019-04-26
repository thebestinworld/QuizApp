package com.example.quizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.interfaces.ItemClickLIstener;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;

    public ImageView category_image;

    private ItemClickLIstener itemClickLIstener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_image = itemView.findViewById(R.id.category_image);
        category_name = itemView.findViewById(R.id.category_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickLIstener(ItemClickLIstener itemClickLIstener) {
        this.itemClickLIstener = itemClickLIstener;
    }

    @Override
    public void onClick(View v) {
        itemClickLIstener.onCLick(v,getAdapterPosition(),false);
    }
}
