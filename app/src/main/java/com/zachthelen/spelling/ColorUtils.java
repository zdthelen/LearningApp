package com.zachthelen.spelling;

import android.graphics.Color;

public class ColorUtils {

    public static int getRandomTextColor() {
        int[] colors = {
                Color.BLACK,
                Color.WHITE,
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                // Add more contrasting colors as needed
        };
        return getRandomColor(colors);
    }

    public static int getRandomButtonColor() {
        int[] colors = {
                Color.parseColor("#FF5722"), // Deep Orange
                Color.parseColor("#2196F3"), // Blue
                Color.parseColor("#4CAF50"), // Green
                Color.parseColor("#FFC107"), // Amber
                // Add more contrasting colors as needed
        };
        return getRandomColor(colors);
    }

    private static int getRandomColor(int[] colors) {
        int randomIndex = (int) (Math.random() * colors.length);
        return colors[randomIndex];
    }
}