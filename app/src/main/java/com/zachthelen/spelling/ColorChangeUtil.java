package com.zachthelen.spelling;

import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

import java.util.Random;

public class ColorChangeUtil {

    private TextView targetTextView;
    private Handler handler = new Handler();
    private Random random = new Random();

    // Define your color arrays
    private int[] backgroundColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private int[] textColors = {Color.WHITE, Color.BLACK, Color.CYAN, Color.MAGENTA};

    public ColorChangeUtil(TextView textView) {
        this.targetTextView = textView;
    }

    // Runnable to change the background color
    private Runnable backgroundChangeRunnable = new Runnable() {
        @Override
        public void run() {
            // Set a random background color
            int randomBackgroundColor = backgroundColors[random.nextInt(backgroundColors.length)];
            targetTextView.setBackgroundColor(randomBackgroundColor);

            // Schedule the next background color change
            handler.postDelayed(this, 1700);
        }
    };

    // Runnable to change the text color
    private Runnable textChangeRunnable = new Runnable() {
        @Override
        public void run() {
            // Set a random text color
            int randomTextColor = textColors[random.nextInt(textColors.length)];
            targetTextView.setTextColor(randomTextColor);

            // Schedule the next text color change
            handler.postDelayed(this, 1300);
        }
    };

    public void startColorChange() {
        // Schedule the background color change every 5 seconds
        handler.postDelayed(backgroundChangeRunnable, 5000);

        // Schedule the text color change every 3 seconds
        handler.postDelayed(textChangeRunnable, 3000);
    }

    public void stopColorChange() {
        // Remove any pending callbacks to stop the color change
        handler.removeCallbacksAndMessages(null);
    }
}
