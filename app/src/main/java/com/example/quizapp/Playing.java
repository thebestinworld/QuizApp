package com.example.quizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.constants.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static Long INTERVAL = 1000L; // One second
    final static Long TIMEOUT = 7000L; // Seven seconds
    Integer progressValue = 0;

    CountDownTimer countDownTimer;

    Integer index = 0;
    Integer score = 0;
    Integer thisQuestion = 0;
    Integer totalQuestion = 0;
    Integer correctAnswer = 0;


    ProgressBar progressBar;
    ImageView question_image;
    TextView question_text;
    TextView txtScore;
    TextView txtQuestionNum;
    Button btnA;
    Button btnB;
    Button btnC;
    Button btnD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        txtScore = findViewById(R.id.txtScore);
        txtQuestionNum = findViewById(R.id.txtTotalQuestions);
        question_text = findViewById(R.id.questionText);

        progressBar = findViewById(R.id.progressBar);

        question_image = findViewById(R.id.questionImage);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        countDownTimer.cancel();

        if (index < totalQuestion) {
            Button clickedButton = (Button) v;

            if (clickedButton.getText().equals(Common.questions.get(index).getCorrectAnswer())) {
                score += 10;
                correctAnswer++;
                showNextQuestion(++index);
            } else {
                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE", score);
                dataSend.putInt("TOTAL", totalQuestion);
                dataSend.putInt("CORRECT", correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();

            }
            txtScore.setText(String.format("%d", score));
        }
    }

    private void showNextQuestion(Integer index) {

        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questions.get(index).getIsImageQuestion().equals("true")) {
                Picasso.with(getBaseContext())
                        .load(Common.questions.get(index).getImage())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.VISIBLE);
                question_text.setText(Common.questions.get(index).getQuestion());

            } else {
                question_image.setVisibility(View.INVISIBLE);
                question_text.setText(Common.questions.get(index).getQuestion());
            }

            btnA.setText(Common.questions.get(index).getAnswerA());
            btnB.setText(Common.questions.get(index).getAnswerB());
            btnC.setText(Common.questions.get(index).getAnswerC());
            btnD.setText(Common.questions.get(index).getAnswerD());

            countDownTimer.start();
        } else {
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion = Common.questions.size();
        countDownTimer = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                showNextQuestion(++index);
            }
        };
        showNextQuestion(index);
    }
}
