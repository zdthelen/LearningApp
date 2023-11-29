package com.zachthelen.spelling;

import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayerTicTacToe {

    private final Button[][] buttons;
    private final int ticTacToeDifficultySelection;

    public ComputerPlayerTicTacToe(Button[][] buttons, int ticTacToeDifficultySelection) {
        this.buttons = buttons;
        this.ticTacToeDifficultySelection = ticTacToeDifficultySelection;
    }

    public void makeMove() {
        Log.d("ComputerPlayerTicTacToe", "makeMove called with difficulty: " + ticTacToeDifficultySelection);
        switch (ticTacToeDifficultySelection) {
            case 0:
                makeTooEasyMove();
                break;
            case 1:
                makeEasyMove();
                break;
            case 2:
                makeMediumMove();
                break;
            case 3:
                makeHardMove();
                break;
            case 4:
                makeImpossibleMove();
                break;
        }
    }

    private void makeTooEasyMove() {
        List<Button> emptyButtons = getEmptyButtons();
        makeRandomMove(emptyButtons);
    }

    private void makeEasyMove() {
        Log.d("ComputerPlayer", "Making Easy Move");
        List<Button> emptyButtons = getEmptyButtons();

        // Check if there are two X's in a row and have a 25% chance of blocking
        if (shouldBlock(.25) && checkForTwoX()) {
            Log.d("ComputerPlayer", "Blocking Move");

            // Block by placing an O in the empty spot
            for (int i = 0; i < 3; i++) {
                Log.d("DEBUG", "We made it");
                if (checkRowForTwoX(i)) {
                    Log.d("ComputerPlayer", "Found two X's in row " + i);
                    placeBlockMoveRow(i);
                    return; // Exit the method once the move is made
                } else if (checkColumnForTwoX(i)) {
                    placeBlockMoveColumn(i);
                    return;
                } else placeDiagonalBlockMove();
            }
        } else if (shouldPlaceAdditionalO(0.5)) {
            placeAdditionalO(emptyButtons, 0.5);
            return;
        } else {
            // If not placing additional O, make a random move
            makeRandomMove(emptyButtons);
        }


    }

    private void makeMediumMove() {
        Log.d("ComputerPlayer", "Making Medium Move");
        List<Button> emptyButtons = getEmptyButtons();

        // Check if there are two X's in a row and have a 50% chance of blocking
        if (shouldBlock(.5) && checkForTwoX()) {
            Log.d("ComputerPlayer", "Blocking Move");

            // Block by placing an O in the empty spot
            for (int i = 0; i < 3; i++) {
                Log.d("DEBUG", "We made it");
                if (checkRowForTwoX(i)) {
                    Log.d("ComputerPlayer", "Found two X's in row " + i);
                    placeBlockMoveRow(i);
                    return; // Exit the method once the move is made
                } else if (checkColumnForTwoX(i)) {
                    placeBlockMoveColumn(i);
                    return;
                } else placeDiagonalBlockMove();
            }
        } else if (checkForTwoO()) {
            Log.d("ComputerPlayer", "Found a winning move");
            if (shouldPlaceWinningO(.5)) {
            Log.d("ComputerPlayer", "Checking for winning move...");



                // Find the spot that will make the O player win and place an O in that spot
                makeWinningMove();
            }
        }else if (shouldPlaceAdditionalO(1)) {
            placeAdditionalO(emptyButtons, 1);
            return;
        } else {
            // If not placing additional O, make a random move
            makeRandomMove(emptyButtons);
        }
    }

    private void makeHardMove() {
        Log.d("ComputerPlayer", "Making Hard Move");
        List<Button> emptyButtons = getEmptyButtons();

        // Check if there are two X's in a row and have a 50% chance of blocking
        if (shouldBlock(.5) && checkForTwoX()) {
            Log.d("ComputerPlayer", "Blocking Move");

            // Block by placing an O in the empty spot
            for (int i = 0; i < 3; i++) {
                Log.d("DEBUG", "We made it");
                if (checkRowForTwoX(i)) {
                    Log.d("ComputerPlayer", "Found two X's in row " + i);
                    placeBlockMoveRow(i);
                    return; // Exit the method once the move is made
                } else if (checkColumnForTwoX(i)) {
                    placeBlockMoveColumn(i);
                    return;
                } else placeDiagonalBlockMove();
            }
        } else if (checkForTwoO()) {
            Log.d("ComputerPlayer", "Found a winning move");
            if (shouldPlaceWinningO(1)) {
            Log.d("ComputerPlayer", "Checking for winning move...");



                // Find the spot that will make the O player win and place an O in that spot
                makeWinningMove();
            }
        }else if (shouldPlaceAdditionalO(1)) {
            placeAdditionalO(emptyButtons, 1);
            return;
        } else {
            // If not placing additional O, make a random move
            makeRandomMove(emptyButtons);
        }
    }
//    List<Button> emptyButtons = getEmptyButtons();
//
//        // Check if there are two X's in a row and have a 67% chance of blocking
//        if (shouldBlock(0.67) && checkForTwoX()) {
//            Log.d("ComputerPlayer", "Blocking Move");
//
//            // Block by placing an O in the empty spot
//            for (int i = 0; i < 3; i++) {
//                Log.d("DEBUG", "We made it");
//                if (checkRowForTwoX(i)) {
//                    Log.d("ComputerPlayer", "Found two X's in row " + i);
//                    placeBlockMoveRow(i);
//                    return; // Exit the method once the move is made
//                } else if (checkColumnForTwoX(i)) {
//                    placeBlockMoveColumn(i);
//                    return;
//                } else placeDiagonalBlockMove();
//            }
//        } else if (shouldPlaceWinningO(0.5) && checkForTwoO()) {
//            Log.d("ComputerPlayer", "Checking for winning move...");
//            if (isThereWinningMove()) {
//                Log.d("ComputerPlayer", "Found a winning move");
//                // Calculate the chance to make the winning move
//                double chanceToPlaceO = 0.5; // You can adjust this as needed
//                if (shouldPlaceWinningO(chanceToPlaceO)) {
//                    Log.d("ComputerPlayer", "Placing winning O...");
//                    // Place a winning O in the empty spot
//                    makeWinningMove();
//                }
//            }
//        } else if (shouldPlaceAdditionalO(1)) {
//            Log.d("ComputerPlayer", "Placing additional O...");
//            placeAdditionalO(emptyButtons, 1);
//            return;
//        } else {
//            // If not placing an additional O, make a random move
//            Log.d("ComputerPlayer", "Making random move...");
//            makeRandomMove(emptyButtons);
//        }
//    }



    private void makeImpossibleMove() {
        Log.d("ComputerPlayer", "Making Impossible Move");
        List<Button> emptyButtons = getEmptyButtons();

        // Check if there are two X's in a row and have a 50% chance of blocking
        if (checkForTwoO()) {
            Log.d("ComputerPlayer", "Found a winning move");
            if (shouldPlaceWinningO(1)) {
                Log.d("ComputerPlayer", "Checking for winning move...");


                // Find the spot that will make the O player win and place an O in that spot
                makeWinningMove();
            }
        } else if (shouldBlock(1) && checkForTwoX()) {
            Log.d("ComputerPlayer", "Blocking Move");

            // Block by placing an O in the empty spot
            for (int i = 0; i < 3; i++) {
                Log.d("DEBUG", "We made it");
                if (checkRowForTwoX(i)) {
                    Log.d("ComputerPlayer", "Found two X's in row " + i);
                    placeBlockMoveRow(i);
                    return; // Exit the method once the move is made
                } else if (checkColumnForTwoX(i)) {
                    placeBlockMoveColumn(i);
                    return;
                } else placeDiagonalBlockMove();
            }
        } else if (shouldPlaceAdditionalO(1)) {
            placeAdditionalO(emptyButtons, 1);
            return;
        } else {
            // If not placing additional O, make a random move
            makeRandomMove(emptyButtons);
        }

    }


    private List<Button> getEmptyButtons() {
        List<Button> emptyButtons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    emptyButtons.add(buttons[i][j]);
                }
            }
        }
        return emptyButtons;
    }

    private void makeRandomMove(List<Button> emptyButtons) {
        if (!emptyButtons.isEmpty()) {
            // Choose a random empty button
            Random random = new Random();
            Button selectedButton = emptyButtons.get(random.nextInt(emptyButtons.size()));
            // Set "O" on the selected button
            selectedButton.setText("O");
        }
    }

    private boolean shouldBlock(double chanceToBlock) {
        // Generate a random number between 0 and 1
        double randomValue = Math.random();
        Log.d("ComputerPlayer", "Random Value for Blocking: " + randomValue);
        // Return true if the random value is less than or equal to the chance to block
        boolean shouldBlock = randomValue <= chanceToBlock;
        Log.d("ComputerPlayer", "Should Block: " + shouldBlock);
        return shouldBlock;
    }


    private void placeBlockMoveRow(int blockedRow) {
        // Implement logic to place an O in the spot that blocks the row
        // For example, find an empty spot in the blocked row and place the O
        for (int j = 0; j < 3; j++) {
            if (buttons[blockedRow][j].getText().toString().equals("")) {
                buttons[blockedRow][j].setText("O");
                Log.d("ComputerPlayer", "Placed O to block in row " + blockedRow);
                return;
            }
        }
    }

    private void placeBlockMoveColumn(int blockedColumn) {
        // Implement logic to place an O in the spot that blocks the column
        // For example, find an empty spot in the blocked column and place the O
        for (int i = 0; i < 3; i++) {
            if (buttons[i][blockedColumn].getText().toString().equals("")) {
                buttons[i][blockedColumn].setText("O");
                Log.d("ComputerPlayer", "Placed O to block in column " + blockedColumn);
                return;
            }
        }
    }

    private void placeDiagonalBlockMove() {
        if (buttons[0][0].getText().toString().equals("X") && buttons[1][1].getText().toString().equals("X")) {
            buttons[2][2].setText("O");
        } else if (buttons[1][1].getText().toString().equals("X") && buttons[2][2].getText().toString().equals("X")) {
            buttons[0][0].setText("O");
        } else if (buttons[0][2].getText().toString().equals("X") && buttons[1][1].getText().toString().equals("X")) {
            buttons[2][0].setText("O");
        } else if (buttons[1][1].getText().toString().equals("X") && buttons[2][0].getText().toString().equals("X")) {
            buttons[0][2].setText("O");
        } else buttons[1][1].setText("0");
    }

    private boolean checkRowForTwoX(int row) {
        int countX = 0;
        int countBlank = 0;

        for (int j = 0; j < 3; j++) {
            String cellValue = buttons[row][j].getText().toString();

            if (cellValue.equals("X")) {
                countX++;
            } else if (cellValue.equals("")) {
                countBlank++;
            }
        }

        // Check if there are two X's and one blank spot in the row
        return countX == 2 && countBlank == 1;
    }




    // Helper method to check if there is at least one blank spot in the row
    private boolean hasBlankSpotInRow(int row) {
        for (int j = 0; j < 3; j++) {
            if (buttons[row][j].getText().toString().equals("")) {
                return true;  // Found a blank spot
            }
        }
        return false;  // No blank spots in the row
    }

    private boolean checkColumnForTwoX(int col) {
        int countX = 0;
        int countBlank = 0;

        for (int i = 0; i < 3; i++) {
            String cellValue = buttons[i][col].getText().toString();

            if (cellValue.equals("X")) {
                countX++;
            } else if (cellValue.equals("")) {
                countBlank++;
            }
        }

        // Check if there are two X's and one blank spot in the column
        return countX == 2 && countBlank == 1;
    }

    // Helper method to check if there is at least one blank spot in the column
    private boolean hasBlankSpotInColumn(int col) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][col].getText().toString().equals("")) {
                return true;  // Found a blank spot
            }
        }
        return false;  // No blank spots in the column
    }

    private boolean checkDiagonalForTwoX() {
        Log.d("Debug", "checking for two X in diagonal");
        boolean foundX1 = buttons[0][0].getText().toString().equals("X") && buttons[1][1].getText().toString().equals("X") ||
                buttons[0][0].getText().toString().equals("X") && buttons[2][2].getText().toString().equals("X") ||
                buttons[1][1].getText().toString().equals("X") && buttons[2][2].getText().toString().equals("X");

        boolean foundX2 = buttons[0][2].getText().toString().equals("X") && buttons[1][1].getText().toString().equals("X") ||
                buttons[0][2].getText().toString().equals("X") && buttons[2][0].getText().toString().equals("X") ||
                buttons[1][1].getText().toString().equals("X") && buttons[2][0].getText().toString().equals("X");
        Log.d("Debug", "two X in Diagonal? " + foundX1 + ", " + foundX2);

        // If two X's are found in either diagonal and there is at least one blank spot, return true
        if ((foundX1 || foundX2) && hasBlankSpotInDiagonal()) {
            Log.d("Debug", "check Diagonal for two x is true");
            return true;
        } else {
            return false;
        }
    }

    // Helper method to check if there is at least one blank spot in the diagonal
    private boolean hasBlankSpotInDiagonal() {

        // Check from top-left to bottom-right diagonal
        if (buttons[0][0].getText().toString().equals("") ||
                buttons[1][1].getText().toString().equals("") ||
                buttons[2][2].getText().toString().equals("")) {
            return true;
            // Check from top-right to bottom-left diagonal
        } else if ( buttons[0][2].getText().toString().equals("") ||
                buttons[1][1].getText().toString().equals("") ||
                buttons[2][0].getText().toString().equals("")) {
            return true;
        } else return false;
    }

    private boolean checkForTwoXInAnyRow() {
        Log.d("Debug", "checking for two X in any row");
        for (int i = 0; i < 3; i++) {
            if (checkRowForTwoX(i)) {
                Log.d("Debug", "two X in row " + true);
                return true;
            }
        }
        Log.d("Debug", "two X in row " + false);
        return false;
    }

    private boolean checkForTwoXInAnyColumn() {
        Log.d("Debug", "checking for two X in any column");
        for (int j = 0; j < 3; j++) {
            if (checkColumnForTwoX(j)) {
                Log.d("Debug", "two X in column " + true);
                return true;
            }
        }
        Log.d("Debug", "two X in column " + false);
        return false;
    }

    private boolean checkForTwoX() {
        Log.d("Debug", "checking for two X");
        return checkForTwoXInAnyRow() || checkForTwoXInAnyColumn() || checkDiagonalForTwoX();
    }

    private boolean checkRowForTwoO(int row) {
        boolean foundO = false;

        for (int j = 0; j < 2; j++) {
            if (buttons[row][j].getText().toString().equals("O") &&
                    buttons[row][j + 1].getText().toString().equals("O")) {
                foundO = true;
                break;
            }
        }

        // If two O's are found and there is at least one blank spot, return true
        if (foundO && hasBlankSpotInRow(row)) {
            return true;
        } else {
            return false;
        }
    }


    private boolean checkColumnForTwoO(int col) {
        boolean foundO = false;

        for (int i = 0; i < 2; i++) {
            if (buttons[i][col].getText().toString().equals("O") &&
                    buttons[i + 1][col].getText().toString().equals("O")) {
                foundO = true;
                break;
            }
        }

        // If two X's are found and there is at least one blank spot, return true
        if (foundO && hasBlankSpotInColumn(col)) {
            return true;
        } else {
            return false;
        }
    }


    private boolean checkDiagonalForTwoO() {
        boolean foundO1 = buttons[0][0].getText().toString().equals("O") && buttons[1][1].getText().toString().equals("O") ||
                buttons[0][0].getText().toString().equals("O") && buttons[2][2].getText().toString().equals("O") ||
                buttons[1][1].getText().toString().equals("O") && buttons[2][2].getText().toString().equals("O");

        boolean foundO2 = buttons[0][2].getText().toString().equals("O") && buttons[1][1].getText().toString().equals("O") ||
                buttons[0][2].getText().toString().equals("O") && buttons[2][0].getText().toString().equals("O") ||
                buttons[1][1].getText().toString().equals("O") && buttons[2][0].getText().toString().equals("O");

        // If two X's are found in either diagonal and there is at least one blank spot, return true
        if ((foundO1 || foundO2) && hasBlankSpotInDiagonal()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkForTwoOInAnyRow() {
        for (int i = 0; i < 3; i++) {
            if (checkRowForTwoO(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForTwoOInAnyColumn() {
        for (int j = 0; j < 3; j++) {
            if (checkColumnForTwoO(j)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForTwoO() {
        return checkForTwoOInAnyRow() || checkForTwoOInAnyColumn() || checkDiagonalForTwoO();
    }

    private boolean shouldPlaceAdditionalO(double chanceToPlaceO) {
        Log.d("Debug","Checking if should place additional O");
        // Generate a random number between 0 and 1
        double randomValue = Math.random();
        // Return true if the random value is less than or equal to the chance to place an additional O
        return randomValue <= chanceToPlaceO;
    }

    private void placeAdditionalO(List<Button> emptyButtons, double chanceToPlaceO) {
        // Check if there are existing O's on the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("O")) {
                    // Check if should place an additional O connected to it with the given chance
                    if (shouldPlaceAdditionalO(chanceToPlaceO)) {
                        Log.d("ComputerPlayer", "Attempting to place additional O connected to existing O...");
                        // Find an empty spot connected to the existing O
                        List<Button> connectedEmptyButtons = getConnectedEmptyButtons(i, j, emptyButtons);
                        if (!connectedEmptyButtons.isEmpty()) {
                            // Place an O in a random connected empty spot
                            Log.d("ComputerPlayer", "Placing additional O connected to existing O...");
                            makeRandomMove(connectedEmptyButtons);
                            return;
                        } else {
                            Log.d("ComputerPlayer", "No connected empty spots found.");
                        }
                    } else {
                        Log.d("ComputerPlayer", "Not placing additional O based on chance.");
                    }
                }
            }
        }
        Log.d("ComputerPlayer", "No existing O found on the board.");
        makeRandomMove(emptyButtons);
    }

    private List<Button> getConnectedEmptyButtons(int row, int col, List<Button> emptyButtons) {
        List<Button> connectedEmptyButtons = new ArrayList<>();

        // Check horizontally
        for (int j = 0; j < 3; j++) {
            if (j != col && buttons[row][j].getText().toString().equals("")) {
                connectedEmptyButtons.add(buttons[row][j]);
            }
        }

        // Check vertically
        for (int i = 0; i < 3; i++) {
            if (i != row && buttons[i][col].getText().toString().equals("")) {
                connectedEmptyButtons.add(buttons[i][col]);
            }
        }

        // Check diagonally (top-left to bottom-right)
        if (row != col && buttons[0][0].getText().toString().equals("")) {
            connectedEmptyButtons.add(buttons[0][0]);
        }

        // Check diagonally (top-right to bottom-left)
        if (row + col != 2 && buttons[0][2].getText().toString().equals("")) {
            connectedEmptyButtons.add(buttons[0][2]);
        }

        // Add additional checks based on your game rules

        return connectedEmptyButtons;
    }

    private boolean isThereWinningMove() {
        for (int i = 0; i < 3; i++) {
            if (checkRowForTwoO(i)) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        return true;  // Winning move possible
                    }
                }
            }
        }
        return false;  // No winning move possible
    }

    private boolean shouldPlaceWinningO(double chance) {
        double randomValue = Math.random();
        Log.d("ComputerPlayer", "Random Value for Move: " + randomValue);
        return randomValue <= chance;
    }

    private void makeWinningMove() {
        Log.d("ComputerPlayer", "Making winning move...");

        // Find the winning spot and place the O
        for (int i = 0; i < 3; i++) {
            if (checkRowForTwoO(i)) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText("O");
                        return;  // Winning move placed
                    }
                }
            }
        }

        // No winning move or not placing the winning move
    }



}
