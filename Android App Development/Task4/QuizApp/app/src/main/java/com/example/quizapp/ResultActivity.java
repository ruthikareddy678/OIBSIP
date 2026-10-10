package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra(QuizActivity.EXTRA_SCORE, 0);
        int total = getIntent().getIntExtra(QuizActivity.EXTRA_TOTAL, 0);
        int incorrect = total - score;
        int percent = total == 0 ? 0 : Math.round(score * 100f / total);

        TextView tvFinalScore = findViewById(R.id.tvFinalScore);
        TextView tvCorrect = findViewById(R.id.tvCorrect);
        TextView tvIncorrect = findViewById(R.id.tvIncorrect);
        TextView tvMessage = findViewById(R.id.tvMessage);
        Button btnRestart = findViewById(R.id.btnRestart);

        tvFinalScore.setText(score + " / " + total);
        tvCorrect.setText("Correct answers: " + score);
        tvIncorrect.setText("Incorrect answers: " + incorrect);

        if (percent >= 80) {
            tvMessage.setText("Excellent work! (" + percent + "%)");
        } else if (percent >= 50) {
            tvMessage.setText("Good job! (" + percent + "%)");
        } else {
            tvMessage.setText("Keep practising! (" + percent + "%)");
        }

        btnRestart.setOnClickListener(v -> {
            // QuizActivity builds a fresh shuffled question set on start
            startActivity(new Intent(this, QuizActivity.class));
            finish();
        });
    }
}
