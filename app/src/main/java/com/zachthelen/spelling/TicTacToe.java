package com.zachthelen.spelling;
// Add necessary imports
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class TicTacToe extends AppCompatActivity {

    private static int currentTicTacToeSessionScore = 0;
    private int totalTicTacToeScore = 0;


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


}
