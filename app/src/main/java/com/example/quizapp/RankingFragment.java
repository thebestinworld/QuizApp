package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapp.ViewHolder.RankingViewHolder;
import com.example.quizapp.constants.Common;
import com.example.quizapp.interfaces.ItemClickLIstener;
import com.example.quizapp.interfaces.RankingCallback;
import com.example.quizapp.model.QuestionScore;
import com.example.quizapp.model.Ranking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {

    View myFragment;

    FirebaseDatabase database;

    DatabaseReference questionScore;

    DatabaseReference rankingTable;

    RecyclerView  rankingList;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    Long sum = 0L;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTable = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        rankingList = myFragment.findViewById(R.id.ranking_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(linearLayoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallback<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTable.child(ranking.getUserName()).setValue(ranking);
                //showRanking();
            }
        });

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class,R.layout.layout_ranking,RankingViewHolder.class,
                rankingTable.orderByChild("score")) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {
                viewHolder.txt_name.setText(model.getUserName());
                viewHolder.txt_score.setText(model.getScore().toString());

                viewHolder.setItemClickLIstener(new ItemClickLIstener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myFragment;
    }

    private void showRanking() {
        rankingTable.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Ranking local = child.getValue(Ranking.class);
                        Log.d("DEBUG",local.getUserName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(final String userName, final RankingCallback<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            QuestionScore questionScore = child.getValue(QuestionScore.class);
                            sum += Long.parseLong(questionScore.getScore());
                        }
                        Ranking ranking = new Ranking(userName, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
