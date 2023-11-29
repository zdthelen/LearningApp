package com.zachthelen.spelling;
// Add necessary imports
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TicTacToe extends AppCompatActivity {

    private static int currentTicTacToeSessionScore = 0;
    private int totalTicTacToeScore = 0;

    public static int ticTacToeDifficultySelection = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        Button btnPlayAgainstComputer = findViewById(R.id.btnPlayAgainstComputer);
        Button btnPlayWithFriend = findViewById(R.id.btnPlayWithFriend);



        startGame(true);

        btnPlayAgainstComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDifficultySelectionDialog();
                startGame(true); // Play against computer
            }
        });

        btnPlayWithFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(false); // Play with friend
            }
        });
    }

    private void loadFragment(TicTacToeFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    public void startGame(boolean againstComputer) {
        // Reset the current session score
        currentTicTacToeSessionScore = 0;
        TicTacToeFragment fragment = new TicTacToeFragment();
        fragment.setGameMode(againstComputer);

        loadFragment(fragment);
    }

    public void showDifficultySelectionDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_tictactoe_difficulty_selection, null);

        // Initialize UI components
        TextView textViewTitle = dialogView.findViewById(R.id.textViewTitle);
        Spinner spinnerDifficulty = dialogView.findViewById(R.id.spinnerDifficulty);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        // Set the title and background color of the spinner
        textViewTitle.setBackgroundColor(getRandomColorForDifficultySelection());
        spinnerDifficulty.setBackgroundColor(getRandomColorForDifficultySelection());

        // Set up the spinner with an array adapter and values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create the dialog
        final AlertDialog dialog = builder.create();

        // Set click listener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the selected difficulty level
                int selectedPosition = spinnerDifficulty.getSelectedItemPosition();
                ticTacToeDifficultySelection = selectedPosition;

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private int getRandomColorForDifficultySelection() {
        int[] colors = getResources().getIntArray(R.array.backgroundColors); // Assuming you have defined the array in resources
        List<Integer> colorList = Arrays.asList(Arrays.stream(colors).boxed().toArray(Integer[]::new));
        Collections.shuffle(colorList);
        return colorList.get(0);
    }

}
