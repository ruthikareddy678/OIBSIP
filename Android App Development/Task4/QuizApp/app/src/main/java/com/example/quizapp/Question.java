package com.example.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** One multiple-choice question with exactly four options. */
public class Question {
    private final String text;
    private final String correctAnswer;
    private final List<String> options;

    public Question(String text, String correctAnswer, String wrong1, String wrong2, String wrong3) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.options = new ArrayList<>(Arrays.asList(correctAnswer, wrong1, wrong2, wrong3));
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /** The four options (correct one included). Shuffle a copy before showing. */
    public List<String> getOptions() {
        return options;
    }
}
