package com.zachthelen.spelling;

import java.util.Random;

public class Equation {
    private int number1;
    private int number2;
    private String operator;
    private int expectedAnswer;

    // Initialize arrays
    private int[] singleDigitArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int[] twoDigitArray;
    private int[] threeDigitArray;
    private void fillTwoDigitArray() {
        int arraySize = 90; // Numbers from 10 to 99, inclusive
        twoDigitArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            twoDigitArray[i] = i + 10;
        }
    }

    private void fillThreeDigitArray() {
        int arraySize = 900; // Numbers from 100 to 999, inclusive
        threeDigitArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            threeDigitArray[i] = i + 100;
        }
    }

    public static Equation generateEquation(String selectedDifficulty, String selectedOperation) {
        Equation equation = new Equation(selectedDifficulty, selectedOperation);
        return equation;
    }


    public Equation(String selectedDifficulty, String selectedOperation) {
        Random random = new Random();
        fillTwoDigitArray();
        fillThreeDigitArray();

        int randomIndex1, randomIndex2;

        if ("0".equals(selectedDifficulty)) {
            randomIndex1 = random.nextInt(singleDigitArray.length);
            randomIndex2 = random.nextInt(singleDigitArray.length);
        } else if ("1".equals(selectedDifficulty) || "2".equals(selectedDifficulty)) {
            randomIndex1 = random.nextInt(twoDigitArray.length);
            randomIndex2 = random.nextInt(twoDigitArray.length);
        } else {
            randomIndex1 = random.nextInt(threeDigitArray.length);
            randomIndex2 = random.nextInt(threeDigitArray.length);
        }

        number1 = getNumberFromIndex(selectedDifficulty, randomIndex1, singleDigitArray, twoDigitArray, threeDigitArray);
        number2 = getNumberFromIndex(selectedDifficulty, randomIndex2, singleDigitArray, twoDigitArray, threeDigitArray);

        if ("0".equals(selectedOperation)) {
            // Always addition
            operator = "+";
        } else if ("1".equals(selectedOperation)) {
            // Always subtraction
            operator = "-";
        } else if ("2".equals(selectedOperation)) {
            // Randomly choose between addition and subtraction
            operator = (random.nextBoolean()) ? "+" : "-";
        }

        // Handle conditions based on selectedOperation and selectedDifficulty
        if ("0".equals(selectedOperation) && "0".equals(selectedDifficulty)) {
            // Check if the sum of two single-digit numbers is 10 or less
            while (number1 + number2 > 10) {
                randomIndex1 = random.nextInt(singleDigitArray.length);
                randomIndex2 = random.nextInt(singleDigitArray.length);
                number1 = getNumberFromIndex(selectedDifficulty, randomIndex1, singleDigitArray, twoDigitArray, threeDigitArray);
                number2 = getNumberFromIndex(selectedDifficulty, randomIndex2, singleDigitArray, twoDigitArray, threeDigitArray);
            }
        } else if ("0".equals(selectedOperation) && "1".equals(selectedDifficulty)) {
            // Check if the ones digit of both numbers sums to less than 10
            while ((number1 % 10 + number2 % 10) >= 10) {
                randomIndex1 = random.nextInt(twoDigitArray.length);
                randomIndex2 = random.nextInt(twoDigitArray.length);
                number1 = getNumberFromIndex(selectedDifficulty, randomIndex1, singleDigitArray, twoDigitArray, threeDigitArray);
                number2 = getNumberFromIndex(selectedDifficulty, randomIndex2, singleDigitArray, twoDigitArray, threeDigitArray);
            }
        } else if ("1".equals(selectedOperation) && "0".equals(selectedDifficulty)) {
            // Check if the subtraction result is positive
            while (number1 - number2 <= 0) {
                randomIndex1 = random.nextInt(singleDigitArray.length);
                randomIndex2 = random.nextInt(singleDigitArray.length);
                number1 = getNumberFromIndex(selectedDifficulty, randomIndex1, singleDigitArray, twoDigitArray, threeDigitArray);
                number2 = getNumberFromIndex(selectedDifficulty, randomIndex2, singleDigitArray, twoDigitArray, threeDigitArray);
            }
        } else if ("1".equals(selectedOperation) && "1".equals(selectedDifficulty)) {
            // Check if the ones digit of the result is greater than the ones digit of the second number
            while (number1 % 10 <= number2 % 10) {
                randomIndex1 = random.nextInt(twoDigitArray.length);
                randomIndex2 = random.nextInt(twoDigitArray.length);
                number1 = getNumberFromIndex(selectedDifficulty, randomIndex1, singleDigitArray, twoDigitArray, threeDigitArray);
                number2 = getNumberFromIndex(selectedDifficulty, randomIndex2, singleDigitArray, twoDigitArray, threeDigitArray);
            }
        }


        expectedAnswer = calculateAnswer(number1, number2, selectedOperation);
    }

    // Getter methods for each field
    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public String getOperator() {
        return operator;
    }

    public int getExpectedAnswer() {
        return expectedAnswer;
    }

    // ... other methods in the Equation class

    private int getNumberFromIndex(String difficulty, int index, int[] singleDigitArray, int[] twoDigitArray, int[] threeDigitArray) {
        switch (difficulty) {
            case "0":
                return singleDigitArray[index];
            case "1":
                return twoDigitArray[index];
            case "2":
                return twoDigitArray[index];
            case "3":
                return threeDigitArray[index];
            default:
                return 0;
        }
    }

    private int calculateAnswer(int number1, int number2, String selectedOperation) {
        // Implement your logic to calculate the answer based on the selected operation
        // For example:
        if ("+".equals(selectedOperation)) {
            return number1 + number2;
        } else if ("-".equals(selectedOperation)) {
            return number1 - number2;
        } else {
            // Handle other operations if needed
            return 0;
        }
    }
    public Equation(int number1, int number2, String operator) {
        this.number1 = number1;
        this.number2 = number2;
        this.operator = operator;
        // Calculate or set expectedAnswer as needed
    }
//    public Equation(Equation originalEquation) {
//        this.number1 = originalEquation.getNumber1();
//        this.number2 = originalEquation.getNumber2();
//        this.operator = originalEquation.getOperator();
//        this.expectedAnswer = originalEquation.getExpectedAnswer();
//    }
}