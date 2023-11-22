package com.zachthelen.spelling;

import static com.zachthelen.spelling.ParentSpelling.CURRENT_WORD_LIST_KEY;
import static com.zachthelen.spelling.ParentSpelling.WORD_LIST_SELECTION_KEY;
import static com.zachthelen.spelling.ParentSpelling.ALL_WORD_LIST_KEY;
import static com.zachthelen.spelling.ParentSpelling.CUSTOM_WORD_LIST_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class SpellingGame extends AppCompatActivity {

    public static final String SPELLING_PREFS = "MySpellingPrefs";
    public static final String SPELLING_SCORE_KEY = "spellingScore";
    private static final String OG_WORD_LIST = "originalWordList";

    private ConstraintLayout gameLayout;
    private TextView wordTextView;
    private EditText answerInput;
    private TextView scoreTextView;
    private Button actionButton;
    private Button goHomeButton;
    private ArrayList<String> loadedWordList;

    private ArrayList<String> retryWordList;
    private int currentSessionScore;
    private int playthroughNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameLayout = findViewById(R.id.gameLayout);
        wordTextView = findViewById(R.id.wordTextView);
        answerInput = findViewById(R.id.answerInput);
        scoreTextView = findViewById(R.id.scoreTextView);
        actionButton = findViewById(R.id.actionButton);
        goHomeButton = findViewById(R.id.btn_home_from_spelling);

        retryWordList = new ArrayList<>();

        currentSessionScore = 0;


        loadWordList();
        getSpellingScore();

        updateScoreUI();
        showNextWord();

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoHomeButtonClick();

            }
        });

        actionButton.setText("Ready?");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Ready?".equals(actionButton.getText())) {
                    startGame();
                } else {
                    submitAnswer();
                }
                if (loadedWordList.isEmpty()) {
//                    endGame();
                    showResults();
                }
            }
        });

    }

    private void onGoHomeButtonClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startGame() {
        // Hide the wordTextView, show the answerInput, and change button text
        wordTextView.setVisibility(View.GONE);
        answerInput.setVisibility(View.VISIBLE);
        actionButton.setText("Submit!");
    }

    private void submitAnswer() {
        String userAnswer = answerInput.getText().toString().trim();
        String correctAnswer = wordTextView.getText().toString();

          // Increment the attempts for the current word

        // Check the spelling and assign points
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {

            // User answered correctly
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            if (playthroughNumber == 1) {
                currentSessionScore += 5;
                Log.d("DEBUG", "Current Score: " + currentSessionScore);
            } else if (playthroughNumber == 2) {
                currentSessionScore += 3;
                Log.d("DEBUG", "Current Score: " + currentSessionScore);
            } else {
                currentSessionScore+=1;
            }
        } else {
            // User answered incorrectly, add to the redemption list
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            // If the spelling is incorrect, keep the word in the list
            retryWordList.add(correctAnswer);
        }

        updateScoreUI();
        // Remove the word from the list
        loadedWordList.remove(correctAnswer);


        // Show the next word if there are still words left
        if (!loadedWordList.isEmpty()) {
            showNextWord();
            answerInput.getText().clear();
        } else {
            answerInput.getText().clear();
            loadWordList();
            showResults();
        }
    }

    private void showNextWord() {
        // Reset the UI for the next word
        wordTextView.setVisibility(View.VISIBLE);
        answerInput.setVisibility(View.GONE);
        actionButton.setText("Ready?");

        if (!loadedWordList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(loadedWordList.size());
            String word = loadedWordList.get(randomIndex);
            wordTextView.setText(word);

            // Set random background and text color
            int backgroundColor = getRandomColor();
            int textColor = getRandomColor();
            gameLayout.setBackgroundColor(backgroundColor);
            wordTextView.setTextColor(textColor);
            answerInput.setTextColor(getRandomColor());  // Set random color for answerInput text
        }
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void loadWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        int wordListSelection = preferences.getInt(WORD_LIST_SELECTION_KEY, 0);
        Log.d("DEBUG", "wordListSelection: " + wordListSelection);
        switch (wordListSelection) {

            case 0:
                // Current Word List
                loadCurrentWordList();
                break;
            case 1:
                // Custom Word List
                loadCustomWordList();
                break;
            case 2:
                // All Stored Words
                loadAllStoredWords();
                break;
        }
    }

    private void loadCurrentWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> wordSet = preferences.getStringSet(CURRENT_WORD_LIST_KEY, new HashSet<String>());
        loadedWordList = new ArrayList<>(wordSet);
    }

    private void loadCustomWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> wordSet = preferences.getStringSet(CUSTOM_WORD_LIST_KEY, new HashSet<String>());
        loadedWordList = new ArrayList<>(wordSet);
        Log.d("DEBUG", "loadedWordList is " + loadedWordList);
    }

    private void loadAllStoredWords() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> wordSet = preferences.getStringSet(ALL_WORD_LIST_KEY, new HashSet<String>());
        loadedWordList = new ArrayList<>(wordSet);
    }

    private int getSpellingScore() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        return preferences.getInt(SPELLING_SCORE_KEY, 0);
    }

    private void saveSpellingScore(int score) {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SPELLING_SCORE_KEY, score);
        editor.apply();
    }

    private void updateScoreUI() {
        String scoreText = String.valueOf(currentSessionScore);
        scoreTextView.setText(scoreText);
    }


    private void showResults() {
        if (!isFinishing()) {  // Check if the activity is still valid
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Spelling Score");//AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Calculate the current session score
            int sessionScore = currentSessionScore;

            // Add the session score to the total score
            int totalScore = getSpellingScore() + sessionScore;

            // Store the updated total score in SharedPreferences
            saveSpellingScore(totalScore);
            int correctlySpelledCount = loadedWordList.size() - retryWordList.size();
            int totalWords = loadedWordList.size();

            String message = "You spelled " + correctlySpelledCount + " out of " + totalWords + " words correctly. ";

            if (retryWordList.size() == 0) {
                message += "Great job! You spelled all words correctly. \nYou earned " + sessionScore + " points!\nYour total spelling score is " + totalScore;
            } else {
                message += "Retry the ones you missed, restart the game, or return to Game Selection.";
            }

            builder.setTitle("Game Over")
                    .setMessage(message)
                    .setPositiveButton("Return to Game Selection", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            returnToGameSelection();
                        }
                    })
                    .setNegativeButton("Restart Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restartGame();
                        }
                    })
                    .setNeutralButton("Retry Missed Words", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            retryMissedWords();
                        }
                    })
                    .setCancelable(false)  // Prevent the dialog from being canceled with the back button
                    .show();
        }
    }

    private void returnToGameSelection() {
        // Implement logic to return to the game selection screen
        Intent intent = new Intent(this, ChildDashboard.class);
        startActivity(intent);
        finish();
    }

    private void retryMissedWords() {
        // Implement logic to retry the missed words
        playthroughNumber++;
        loadedWordList.clear();
        loadedWordList = retryWordList;
        showNextWord();
    }


    private void restartGame() {
        // Reset score and other necessary variables


        currentSessionScore = 0;
        playthroughNumber = 1;
        // Reload the word list and show the first word
        loadWordList();
        updateScoreUI();
        showNextWord();
    }

}