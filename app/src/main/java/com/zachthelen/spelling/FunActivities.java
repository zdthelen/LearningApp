package com.zachthelen.spelling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FunActivities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_activities);

        //    tictactoeScoreTextView = findViewById(R.id.chi);
//    int spellingScore = getSpellingScore();
        ImageView ticTacToeGameImage = findViewById(R.id.ticTacToeGameImage);
//        spellingScoreTextView.setText(String.valueOf(spellingScore));

        ticTacToeGameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action for Spelling Game
                openTicTacToeActivity();
            }
        });

        ImageView drawingActivityImage = findViewById(R.id.drawingGameImage);

        drawingActivityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawingActivity();
            }
        });

    }

    private void openTicTacToeActivity() {
        Intent intent = new Intent(this, TicTacToe.class);
        startActivity(intent);
    }

    private void openDrawingActivity() {
        Intent intent = new Intent(this, Drawing.class);
        startActivity(intent);
    }


}