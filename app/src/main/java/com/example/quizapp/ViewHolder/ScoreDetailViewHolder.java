package com.example.quizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.quizapp.R;

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_name;
    public TextView txt_score;

    public ScoreDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_score = itemView.findViewById(R.id.txt_score);
    }
}
