package com.example.quizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.interfaces.ItemClickLIstener;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name;

    public TextView txt_score;

    private ItemClickLIstener itemClickLIstener;


    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_score = itemView.findViewById(R.id.txt_score);

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
