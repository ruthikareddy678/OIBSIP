package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_TOTAL = "total";

    private static final int QUESTIONS_PER_QUIZ = 10;

    private static final int COLOR_DEFAULT = Color.parseColor("#37474F");
    private static final int COLOR_CORRECT = Color.parseColor("#2E7D32"); // green
    private static final int COLOR_WRONG = Color.parseColor("#C62828");   // red

    private List<Question> quiz;
    private int index = 0;
    private int score = 0;
    private boolean answered = false;

    private TextView tvCounter, tvScore, tvQuestion;
    private ProgressBar progressBar;
    private Button[] optionButtons;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvCounter = findViewById(R.id.tvCounter);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        progressBar = findViewById(R.id.progressBar);
        btnNext = findViewById(R.id.btnNext);
        optionButtons = new Button[]{
                findViewById(R.id.btnOption1),
                findViewById(R.id.btnOption2),
                findViewById(R.id.btnOption3),
                findViewById(R.id.btnOption4)
        };

        // New random set of questions every time the quiz starts
        quiz = QuestionBank.getRandomQuiz(QUESTIONS_PER_QUIZ);
        progressBar.setMax(quiz.size());

        for (Button b : optionButtons) {
            b.setOnClickListener(v -> onOptionSelected((Button) v));
        }
        btnNext.setOnClickListener(v -> goToNext());

        showQuestion();
    }

    private void showQuestion() {
        answered = false;
        Question q = quiz.get(index);

        tvCounter.setText("Question " + (index + 1) + " of " + quiz.size());
        tvScore.setText("Score: " + score);
        progressBar.setProgress(index + 1);
        tvQuestion.setText(q.getText());

        // Shuffle the answer positions for every question
        List<String> options = new ArrayList<>(q.getOptions());
        Collections.shuffle(options);

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            setButtonColor(optionButtons[i], COLOR_DEFAULT);
        }

        btnNext.setText(index == quiz.size() - 1 ? "See Results" : "Next");
        btnNext.setVisibility(android.view.View.INVISIBLE);
    }

    private void onOptionSelected(Button chosen) {
        if (answered) return; // ignore further taps once answered
        answered = true;

        String correctAnswer = quiz.get(index).getCorrectAnswer();
        String chosenText = chosen.getText().toString();

        if (chosenText.equals(correctAnswer)) {
            score++;
            setButtonColor(chosen, COLOR_CORRECT);
        } else {
            setButtonColor(chosen, COLOR_WRONG);
            // Also reveal the right answer in green
            for (Button b : optionButtons) {
                if (b.getText().toString().equals(correctAnswer)) {
                    setButtonColor(b, COLOR_CORRECT);
                }
            }
        }

        tvScore.setText("Score: " + score);
        btnNext.setVisibility(android.view.View.VISIBLE);
    }

    private void goToNext() {
        index++;
        if (index < quiz.size()) {
            showQuestion();
        } else {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(EXTRA_SCORE, score);
            intent.putExtra(EXTRA_TOTAL, quiz.size());
            startActivity(intent);
            finish();
        }
    }

    private void setButtonColor(Button button, int color) {
        button.setBackgroundTintList(ColorStateList.valueOf(color));
        button.setTextColor(Color.WHITE);
    }
}
