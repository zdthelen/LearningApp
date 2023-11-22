package com.zachthelen.spelling;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ParentMath extends AppCompatActivity {

    private static final String MATH_PREFS = "mathPrefs";
    private static final String DIFFICULTY_KEY = "difficulty";
    private static final String OPERATION_KEY = "operation";
    private static final String QUESTION_COUNT_KEY = "questionCount";
    private Spinner difficultySpinner;
    private Spinner operationSpinner;
    private Spinner questionsSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_math);

        difficultySpinner = findViewById(R.id.difficultySpinner);
        operationSpinner = findViewById(R.id.operationSpinner);
        questionsSpinner = findViewById(R.id.questionsSpinner);


        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);
        int savedQuestionCount = preferences.getInt(QUESTION_COUNT_KEY, 5);
        questionsSpinner.setSelection(getIndex(questionsSpinner, String.valueOf(savedQuestionCount)));
        // Add options to the spinners and set up listeners
        // (You can refer to Android documentation or other sources for Spinner setup)

        Button saveSelectionsButton = findViewById(R.id.saveSelectionsButton);
        saveSelectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelections();
            }
        });

        Button dashboardFromMathButton = findViewById(R.id.btn_dashboard_from_math);
        dashboardFromMathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardFromMath();
            }
        });
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;  // Default to the first item if not found
    }

    private void dashboardFromMath() {
        Intent intent = new Intent(this, ParentDashboard.class);
        startActivity(intent);
    }

    private void saveSelections() {
        int selectedDifficulty = difficultySpinner.getSelectedItemPosition();
        int selectedOperation = operationSpinner.getSelectedItemPosition();

        // Save selected values to SharedPreferences (implement this)
        saveToSharedPreferences(selectedDifficulty, selectedOperation);
    }

    private void saveToSharedPreferences(int selectedDifficulty, int selectedOperation) {
        // Implement this method to save selections to SharedPreferences as strings
        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DIFFICULTY_KEY, String.valueOf(selectedDifficulty));
        editor.putString(OPERATION_KEY, String.valueOf(selectedOperation));
        editor.apply();
    }
}
