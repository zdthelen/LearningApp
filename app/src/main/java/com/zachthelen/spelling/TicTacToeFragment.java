package com.zachthelen.spelling;

// Add necessary imports
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TicTacToeFragment extends Fragment implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean playerXTurn = true; // True if it's X's turn, false if it's O's turn
    private boolean againstComputer;
    private ColorChangeUtil colorChangeUtil;
    private TextView winnerAnnounceTextView;
    private boolean checkForWin;
    private int xColor;
    private int oColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);


        // Initialize buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "btn" + i + j;
                int resId = getResources().getIdentifier(buttonId, "id", getActivity().getPackageName());
                buttons[i][j] = view.findViewById(resId);
                buttons[i][j].setOnClickListener(this);
            }
        }

        xColor = getRandomXColor();
        oColor = getRandomOColor();
        return view;
    }

    @Override
    public void onClick(View v) {
        // Handle button click
        Button button = (Button) v;

        if (button.getText().toString().equals("")) {
            // Button is not occupied
            if (playerXTurn) {
                setButtonText(button, "X");
            } else {
                setButtonText(button, "O");
            }
            if (!checkForWin()) {
                if (!isBoardFull()) {
                    playerXTurn = !playerXTurn; // Switch turns
                } else {
                    announceDraw();
                }
            }

            // If playing against the computer, simulate the computer's move
            if (againstComputer && !playerXTurn) {
                makeComputerMove();
            }

        }
    }

    private void setButtonText(Button button, String symbol) {
        int textColor = (symbol.equals("X")) ? xColor : oColor;
        button.setText(symbol);
        button.setTextColor(textColor);
    }


    private void makeComputerMove() {
        // Collect a list of available (empty) buttons
        List<Button> emptyButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    emptyButtons.add(buttons[i][j]);
                }
            }
        }

        // Check if there are any empty buttons
        if (!emptyButtons.isEmpty()) {
            // Choose a random empty button
            Random random = new Random();
            Button selectedButton = emptyButtons.get(random.nextInt(emptyButtons.size()));

            // Set "O" on the selected button
            selectedButton.setText("O");
            playerXTurn = !playerXTurn; // Switch turns
//            checkForWin(); // Check for a win after each move
        }
    }

    public void setGameMode(boolean againstComputer) {
        this.againstComputer = againstComputer;
    }

    private boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (checkRow(i)) {
                // Win detected
                announceWinner(buttons[i][0].getText().toString());
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (checkColumn(i)) {
                // Win detected
                announceWinner(buttons[0][i].getText().toString());
                return true;
            }
        }

        // Check diagonals
        if (checkDiagonal(0, 0, 1, 1) || checkDiagonal(0, 2, 1, -1)) {
            // Win detected
            announceWinner(buttons[1][1].getText().toString());
            return true;
        }
        return false;
    }

    private boolean checkRow(int row) {
        String symbol = buttons[row][0].getText().toString();
        return !symbol.equals("") && symbol.equals(buttons[row][1].getText().toString()) && symbol.equals(buttons[row][2].getText().toString());
    }

    private boolean checkColumn(int col) {
        String symbol = buttons[0][col].getText().toString();
        return !symbol.equals("") && symbol.equals(buttons[1][col].getText().toString()) && symbol.equals(buttons[2][col].getText().toString());
    }

    private boolean checkDiagonal(int startRow, int startCol, int rowIncrement, int colIncrement) {
        String symbol = buttons[startRow][startCol].getText().toString();
        return !symbol.equals("") &&
                symbol.equals(buttons[startRow + rowIncrement][startCol + colIncrement].getText().toString()) &&
                symbol.equals(buttons[startRow + 2 * rowIncrement][startCol + 2 * colIncrement].getText().toString());
    }

    private void announceWinner(String winnerSymbol) {
        // Create a custom dialog
        final Dialog dialog = new Dialog(requireContext(), R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_announce_winner_tictactoe); // Set your custom layout


        // Initialize UI components
        TextView tvWinnerMessage = dialog.findViewById(R.id.tvWinnerMessage);

        Button btnBackToActivities = dialog.findViewById(R.id.btnBackToActivities);
        Button btnNewGame = dialog.findViewById(R.id.btnNewGame);

        // Set the winner message dynamically
        tvWinnerMessage.setText(winnerSymbol + " wins!");
        // Set the background color of the entire dialog view
        ConstraintLayout dialogLayout = dialog.findViewById(R.id.winner_message_constraint);
        DialogColorUtil dialogColorUtil = new DialogColorUtil(dialogLayout, getContext());
        dialogColorUtil.startColorChange();




        // Set click listeners for buttons
        btnBackToActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to FunActivities activity
                Intent intent = new Intent(requireContext(), FunActivities.class);
                startActivity(intent);
                dialog.dismiss();
                // Add your code to navigate to FunActivities activity
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new game
                clearBoard(); // Implement a method to clear the board
                // Add your code to start a new game based on the previous game mode
                dialog.dismiss();
                ((TicTacToe) requireActivity()).startGame(againstComputer);

            }
        });

        // Show the dialog
        dialog.show();
    }

    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private int getRandomXColor() {
        int[] colors = getResources().getIntArray(R.array.backgroundColors); // Assuming you have defined the array in resources
        List<Integer> colorList = Arrays.asList(Arrays.stream(colors).boxed().toArray(Integer[]::new));
        Collections.shuffle(colorList);
        return colorList.get(0);
    }

    private int getRandomOColor() {
        int[] colors = getResources().getIntArray(R.array.backgroundColors); // Assuming you have defined the array in resources
        List<Integer> colorList = Arrays.asList(Arrays.stream(colors).boxed().toArray(Integer[]::new));
        Collections.shuffle(colorList);

        // Ensure that the O color is different from X color
        int xColor = getRandomXColor();
        while (colorList.get(0) == xColor) {
            Collections.shuffle(colorList);
        }

        return colorList.get(0);
    }


    private boolean isBoardFull() {
        // Check if all buttons are filled
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    return false; // Found an empty button, board is not full
                }
            }
        }
        return true; // All buttons are filled, board is full
    }

    private void announceDraw() {
        // Create a custom dialog for a draw
        final Dialog dialog = new Dialog(requireContext(), R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_announce_winner_tictactoe); // Set your custom layout

        // Initialize UI components
        TextView tvWinnerMessage = dialog.findViewById(R.id.tvWinnerMessage);
        Button btnBackToActivities = dialog.findViewById(R.id.btnBackToActivities);
        Button btnNewGame = dialog.findViewById(R.id.btnNewGame);

        // Set the draw message dynamically
        tvWinnerMessage.setText("It's a draw!");

        // Set the background color of the entire dialog view
        ConstraintLayout dialogLayout = dialog.findViewById(R.id.winner_message_constraint);
        DialogColorUtil dialogColorUtil = new DialogColorUtil(dialogLayout, getContext());
        dialogColorUtil.startColorChange();

        // Set click listeners for buttons
        btnBackToActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to FunActivities activity
                Intent intent = new Intent(requireContext(), FunActivities.class);
                startActivity(intent);
                dialog.dismiss();
                // Add your code to navigate to FunActivities activity
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new game
                clearBoard(); // Implement a method to clear the board
                // Add your code to start a new game based on the previous game mode
                dialog.dismiss();
                ((TicTacToe) requireActivity()).startGame(againstComputer);
            }
        });

        // Show the dialog
        dialog.show();
    }

}
