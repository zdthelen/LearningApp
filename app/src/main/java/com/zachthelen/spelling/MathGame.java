package com.zachthelen.spelling;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathGame extends AppCompatActivity {


    public static final String MATH_PREFS = "mathPrefs";
    public static final String MATH_SCORE_KEY = "mathScore";
    private static final String DIFFICULTY_KEY = "difficulty";
    private static final String OPERATION_KEY = "operation";
    private static final String QUESTION_COUNT_KEY = "questionCount";
    SharedPreferences preferences;
    private TextView textViewTop;
    private TextView textViewBottom;
    private TextView textViewOperator;
    private EditText editTextAnswer;
    private Button btnSubmit;
    private String selectedOperation;
    private String selectedDifficulty;
    private int questionCount;
    private int currentQuestion = 0;
    private List<Equation> incorrectEquations = new ArrayList<>();
    private int correctAnswers = 0;
    private List<Equation> mathRedemption = new ArrayList<>();
    private int playthroughNumber = 1;
    private int currentSessionScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);

        preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);

        textViewTop = findViewById(R.id.textViewTop);
        textViewBottom = findViewById(R.id.textViewBottom);
        textViewOperator = findViewById(R.id.textViewOperator);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);
        currentSessionScore = 0;

//        // Retrieve from SharedPreferences
        loadMathDataFromSharedPreferences();
        questionCount = preferences.getInt(QUESTION_COUNT_KEY, 5);
//        clearMathPreferences();
        displayNextEquation();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userAnswerString = editTextAnswer.getText().toString().trim();

                if (!userAnswerString.isEmpty()) {
                    int userAnswer = Integer.parseInt(userAnswerString);
                    int operand1 = Integer.parseInt(textViewTop.getText().toString());
                    int operand2 = Integer.parseInt(textViewBottom.getText().toString());

                    int expectedAnswer;
                    if ("0".equals(selectedOperation)) {
                        expectedAnswer = operand1 + operand2;
                        } else if ("1".equals(selectedOperation)) {
                        expectedAnswer = operand1 - operand2;
                    } else {
                        // Handle other operations if needed
                        return;
                    }

                    // Call the processUserAnswer method to handle the user's answer
                    processUserAnswer(userAnswer, expectedAnswer);
                } else {
                    }
            }
        });
    }

    private void displayNextEquation() {
        if (currentQuestion < questionCount) {
            // Display the next equation

            Equation equation = Equation.generateEquation(selectedDifficulty, selectedOperation);
            int number1 = equation.getNumber1();
            int number2 = equation.getNumber2();
            String operator = equation.getOperator();
            int expectedAnswer = equation.getExpectedAnswer();
            // Populate the TextViews and store expectedAnswer as needed
            textViewTop.setText(String.valueOf(number1));
            textViewBottom.setText(String.valueOf(number2));
            textViewOperator.setText(operator);

                // ... your code to store expectedAnswer or perform any other operations
        } else {

            // Check if there are incorrect equations
            if (!mathRedemption.isEmpty()) {
                // Show the retry dialog for redemption equations
                showRetryDialog("second_chance_math_array", "You got %d out of %d correct", mathRedemption);
            } else {
                // Show the final score dialog
                showFinalScoreDialog(correctAnswers, questionCount);
            }
        }

    }

    private void processUserAnswer(int userAnswer, int expectedAnswer) {
        if (userAnswer == expectedAnswer) {
            // User answered correctly
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            correctAnswers++;
            if (playthroughNumber == 1) {
                currentSessionScore += 5;
                Log.d("DEBUG", "Current Score: " + currentSessionScore);
            } else if (playthroughNumber == 2) {
                currentSessionScore += 3;
                Log.d("DEBUG", "Current Score: " + currentSessionScore);
            } else currentSessionScore+=1;
        } else {
            // User answered incorrectly, add to the redemption list
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();

            // Create an incorrectEquation using the current equation as a template
            Equation incorrectEquation = new Equation(
                    Integer.parseInt(textViewTop.getText().toString()),
                    Integer.parseInt(textViewBottom.getText().toString()),
                    textViewOperator.getText().toString()
            );

            // Move the incorrect equation to the redemption list
            mathRedemption.add(incorrectEquation);
            Log.d("DEBUG", "Equation properties: " +
                    "Number1: " + incorrectEquation.getNumber1() +
                    " Number2: " + incorrectEquation.getNumber2() +
                    " Operator: " + incorrectEquation.getOperator());
            Log.d("MyApp", "Incorrect Equations: " + mathRedemption.toString());
        }

        // Increment the current question count
        currentQuestion++;

        // Continue displaying equations
        displayNextEquation();
    }

    private void showRetryDialog(final String arrayName, final String messageFormat, final List<Equation> equations) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Retry Incorrect Answers");
        builder.setMessage("Do you want to retry the incorrect answers?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Move equations from redemption back to incorrectEquations
                incorrectEquations.clear();
                incorrectEquations.addAll(mathRedemption);
                // Clear the redemption list
                mathRedemption.clear();
                // Reset the current question count
                currentQuestion = 0;
                questionCount = incorrectEquations.size();
                Log.d("MyApp", "After moving equations: " + incorrectEquations.toString());
                // Continue displaying equations
                playthroughNumber ++;
                Log.d("DEBUG", "playthroughNumber is " + playthroughNumber);
                if (playthroughNumber == 2 || playthroughNumber == 3) {
                    secondPlaythroughEquation();

                } else {
                    showFinalScoreDialog(correctAnswers, questionCount);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Implement the logic for not retrying
                // ... your code to handle not retrying

                Intent intent = new Intent(MathGame.this, ChildDashboard.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    private void secondPlaythroughEquation() {
        if (currentQuestion < questionCount) {
            Log.d("DEBUG", "currentQuestion is " + currentQuestion);
            // Display the next equation
            // Use the redemption list during retry
            Equation equation = incorrectEquations.get(currentQuestion);
            Log.d("DEBUG", "equation is " + equation);
            // Log the properties of the equation for additional debugging
            Log.d("DEBUG", "Equation properties: " +
                    "Number1: " + equation.getNumber1() +
                    " Number2: " + equation.getNumber2() +
                    " Operator: " + equation.getOperator());
                // Populate the TextViews and store expectedAnswer as needed
                textViewTop.setText(String.valueOf(equation.getNumber1()));
                textViewBottom.setText(String.valueOf(equation.getNumber2()));
                textViewOperator.setText(equation.getOperator());
                // ... your code to store expectedAnswer or perform any other operations
        } else {
            // Check if there are incorrect equations
            if (!mathRedemption.isEmpty()) {
                // Show the retry dialog for redemption equations
                showRetryDialog("second_chance_math_array", "You got %d out of %d correct", mathRedemption);
            } else {
                // Show the final score dialog
                showFinalScoreDialog(correctAnswers, questionCount);
            }
        }
    }

    private void showFinalScoreDialog(int correctAnswers, int questionCount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Math Score");

        // Calculate the current session score
        int sessionScore = currentSessionScore;

        // Add the session score to the total score
        int totalScore = getMathScore() + sessionScore;

        // Store the updated total score in SharedPreferences
        saveMathScore(totalScore);

        // Display the score message in the dialog
        builder.setMessage("You earned " + sessionScore + " points!\nYour total math score is " + totalScore);

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset current session score to 0 for the next game
                currentSessionScore = 0;
                Intent intent = new Intent(MathGame.this, ChildDashboard.class);
                startActivity(intent);
            }
        });

        builder.show();
    }

    private int calculateSessionScore(int correctAnswers, int questionCount) {
        // Customize the scoring logic as needed
        // For example, you can simply count correct answers as 1 point each
        return correctAnswers;
    }

    private int getMathScore() {
        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);
        return preferences.getInt(MATH_SCORE_KEY, 0);
    }

    private void saveMathScore(int score) {
        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MATH_SCORE_KEY, score);
        editor.apply();
    }

    private void loadMathDataFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);

        // Retrieve saved difficulty and operation as strings
        selectedDifficulty = preferences.getString(DIFFICULTY_KEY, "0");
        selectedOperation = preferences.getString(OPERATION_KEY, "0");

        Log.d("loadMathData", "Selected difficulty retrieved from SharedPreferences: " + selectedDifficulty);
        Log.d("loadMathData", "Selected operation retrieved from SharedPreferences: " + selectedOperation);
    }

//    private void clearMathPreferences() {
//        SharedPreferences preferences = getSharedPreferences(MATH_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//
//        // You may also want to update your current variables to reflect the cleared state
//        selectedDifficulty = null;
//        selectedOperation = null;
//
//        // Now you can use these values in your game setup or reset logic
//        setupGameState();
//    }

}