package com.zachthelen.spelling;

import static com.zachthelen.spelling.SpellingGame.SPELLING_PREFS;
import static com.zachthelen.spelling.SpellingGame.SPELLING_SCORE_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildDashboard extends AppCompatActivity {

    SharedPreferences preferences;
    private TextView mathScoreTextView;
    private TextView spellingScoreTextView;
    private TextView otherActivitiesTextView;
    private TextView gamesTitle;
    private TextView totalGameScoreTextView;
    private int totalGameScore;
    private ColorChangeUtil colorChangeUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);

        gamesTitle = findViewById(R.id.childDashboard_games_title);
        totalGameScoreTextView = findViewById(R.id.childDashboard_totalScore_value);
        totalGameScore = 0;


        spellingScoreTextView = findViewById(R.id.childDashboard_spellingScore_value);
        int spellingScore = getSpellingScore();
        ImageView spellingGameImage = findViewById(R.id.spellingGameImage);
        spellingScoreTextView.setText(String.valueOf(spellingScore));

        mathScoreTextView = findViewById(R.id.childDashboard_mathScore_value);
        int mathScore = getMathScore();
        ImageView mathGameImage = findViewById(R.id.mathGameImage);
        mathScoreTextView.setText(String.valueOf(mathScore));

        otherActivitiesTextView = findViewById(R.id.childDashboard_otherActivitiesScore_value);

        ImageView otherActivitiesImage = findViewById(R.id.otherActivitiesImage);


        // Set OnClickListener for Spelling Game
        spellingGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action for Spelling Game
                openSpellingGameActivity();
            }
        });

        mathGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle click action for Math Game
                openMathGameActivity();
            }
        });

        otherActivitiesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtherActivitiesDashboard();
            }
        });
        // Add similar listeners for other games as needed

        colorChangeUtil = new ColorChangeUtil(gamesTitle);

        colorChangeUtil.startColorChange();
        calculateTotalGameScore();
    }

    private void openOtherActivitiesDashboard() {
        Intent intent = new Intent(this, FunActivities.class);
        startActivity(intent);
    }

    private void calculateTotalGameScore() {
        totalGameScore = getMathScore() + getSpellingScore();
        totalGameScoreTextView.setText(String.valueOf(totalGameScore));
    }

    private int getMathScore() {
        preferences = getSharedPreferences(MathGame.MATH_PREFS, MODE_PRIVATE);
        return preferences.getInt(MathGame.MATH_SCORE_KEY, 0);
    }

    private int getSpellingScore() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        return preferences.getInt(SPELLING_SCORE_KEY, 0);
    }

    private void openSpellingGameActivity() {
        // Define the intent to start the SpellingGameActivity
        Intent intent = new Intent(this, SpellingGame.class);

        // You can add extra information to the intent if needed
        // For example, passing user data or settings

        // Start the activity
        startActivity(intent);
    }

    private void openMathGameActivity() {
        Intent intent = new Intent(this, MathGame.class);
        startActivity(intent);
    }

    // Add similar methods for other game activities if needed

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (colorChangeUtil != null) {
            colorChangeUtil.stopColorChange();
        }
    }
}