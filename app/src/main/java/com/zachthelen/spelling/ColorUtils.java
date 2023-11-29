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

    // Calculate luminance of a color
    public static double calculateLuminance(int color) {
        double red = Color.red(color) / 255.0;
        double green = Color.green(color) / 255.0;
        double blue = Color.blue(color) / 255.0;

        red = (red <= 0.03928) ? (red / 12.92) : Math.pow((red + 0.055) / 1.055, 2.4);
        green = (green <= 0.03928) ? (green / 12.92) : Math.pow((green + 0.055) / 1.055, 2.4);
        blue = (blue <= 0.03928) ? (blue / 12.92) : Math.pow((blue + 0.055) / 1.055, 2.4);

        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }
}