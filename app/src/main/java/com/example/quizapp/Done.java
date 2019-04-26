package com.example.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.constants.Common;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore;
    TextView txtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference questionScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");

        txtResultScore = findViewById(R.id.txtTotalScore);
        txtResultQuestion = findViewById(R.id.txtTotalQuestions);
        progressBar = findViewById(R.id.doneProgressBar);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            Integer score = extra.getInt("SCORE");
            Integer totalQuestion = extra.getInt("TOTAL");
            Integer correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("Total Score : %d",score));
            txtResultQuestion.setText(String.format("Passed : %d / %d",correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            questionScore.child(String.format("%s_%s", Common.currentUser.getUserName(),Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));

        }
    }
}
