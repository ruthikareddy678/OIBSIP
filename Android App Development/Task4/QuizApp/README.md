# Quiz App (Android)

A multiple-choice general-knowledge quiz. Answer 10 random questions one at a time, get instant feedback, and see your final score at the end. Built with Java and XML in Android Studio.

## Features

- Welcome screen with a **Start** button
- Question screen: question text, 4 answer buttons, a counter ("Question 3 of 10"), a live score and a progress bar
- 15 hardcoded questions; each round uses 10 of them in a **new random order**, and the answer positions are shuffled for every question
- Instant feedback: the chosen answer turns **green** if correct, **red** if wrong (the correct answer is then also shown in green)
- **Next** button (becomes "See Results" on the last question)
- Score tracked throughout the quiz
- Results screen: total score, number correct, number incorrect, percentage message and a **Restart Quiz** button (starts a freshly shuffled quiz)

## How It Works

- `QuestionBank` holds the questions and returns a random selection.
- `QuizActivity` shows one question at a time, colours the buttons after a tap and tracks the score.
- The score and total are passed to `ResultActivity` through an `Intent` extras Bundle.

## Project Structure

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/quizapp/
│   ├── WelcomeActivity.java
│   ├── QuizActivity.java
│   ├── ResultActivity.java
│   ├── Question.java
│   └── QuestionBank.java
└── res/layout/
    ├── activity_welcome.xml
    ├── activity_quiz.xml
    └── activity_result.xml
```

## Setup

1. In Android Studio: New Project > Empty Views Activity, Language **Java**, Name `QuizApp`, Package `com.example.quizapp`.
2. Copy the files above into the matching folders (delete the generated `MainActivity.java` and `activity_main.xml`, and replace `AndroidManifest.xml`).
3. Sync Gradle and run on an emulator or a phone.

## Tech Stack

- Java, XML layouts
- Android Studio

## Screenshots
![alt text](<Screenshot 2026-10-10 151010.png>)
![alt text](<Screenshot 2026-10-10 162104.png>)

## Author

Kakulavaram Ruthika
