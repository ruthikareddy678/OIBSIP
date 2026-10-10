package com.example.quizapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Hardcoded general-knowledge questions. */
public final class QuestionBank {

    private QuestionBank() { }

    private static List<Question> all() {
        List<Question> list = new ArrayList<>();
        list.add(new Question("What is the capital city of Australia?",
                "Canberra", "Sydney", "Melbourne", "Perth"));
        list.add(new Question("Which planet is known as the Red Planet?",
                "Mars", "Venus", "Jupiter", "Mercury"));
        list.add(new Question("What is the chemical symbol for gold?",
                "Au", "Ag", "Gd", "Go"));
        list.add(new Question("Who wrote the play \"Romeo and Juliet\"?",
                "William Shakespeare", "Charles Dickens", "Mark Twain", "Jane Austen"));
        list.add(new Question("Which is the largest ocean on Earth?",
                "Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean"));
        list.add(new Question("How many continents are there on Earth?",
                "7", "5", "6", "8"));
        list.add(new Question("What is the hardest natural substance?",
                "Diamond", "Gold", "Iron", "Quartz"));
        list.add(new Question("In which year did India gain independence?",
                "1947", "1935", "1950", "1942"));
        list.add(new Question("What is the largest mammal in the world?",
                "Blue whale", "African elephant", "Giraffe", "Great white shark"));
        list.add(new Question("Which gas do plants absorb from the air for photosynthesis?",
                "Carbon dioxide", "Oxygen", "Nitrogen", "Hydrogen"));
        list.add(new Question("What is the fastest land animal?",
                "Cheetah", "Lion", "Horse", "Leopard"));
        list.add(new Question("What is the currency of Japan?",
                "Yen", "Won", "Yuan", "Ringgit"));
        list.add(new Question("What is the smallest prime number?",
                "2", "0", "1", "3"));
        list.add(new Question("What is the national animal of India?",
                "Tiger", "Lion", "Elephant", "Leopard"));
        list.add(new Question("Who painted the Mona Lisa?",
                "Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Michelangelo"));
        return list;
    }

    /** Returns {@code count} questions in a fresh random order. */
    public static List<Question> getRandomQuiz(int count) {
        List<Question> list = all();
        Collections.shuffle(list);
        return new ArrayList<>(list.subList(0, Math.min(count, list.size())));
    }
}
