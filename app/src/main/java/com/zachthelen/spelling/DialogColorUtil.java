package com.zachthelen.spelling;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class DialogColorUtil {

    private ConstraintLayout targetLayout;
    private Handler handler = new Handler();
    private Random random = new Random();

    // Define your color arrays
    private int[] backgroundColors;
    private int[] textColors;

    public DialogColorUtil(ConstraintLayout layout, Context context) {
        this.targetLayout = layout;

        // Initialize color arrays from resources
        backgroundColors = context.getResources().getIntArray(R.array.backgroundColors);
        textColors = context.getResources().getIntArray(R.array.textColors);
    }

    // Runnable to change the background color
    private Runnable backgroundChangeRunnable = new Runnable() {
        @Override
        public void run() {
            // Set a random background color
            int randomBackgroundColor = backgroundColors[random.nextInt(backgroundColors.length)];
            targetLayout.setBackgroundColor(randomBackgroundColor);

            // Schedule the next background color change
            handler.postDelayed(this, 100);
        }
    };

    // Runnable to change the text color
    private Runnable textChangeRunnable = new Runnable() {
        @Override
        public void run() {
            // Implement your logic to change text color
            // For example, change text color of TextView inside the ConstraintLayout

            // Schedule the next text color change
            handler.postDelayed(this, 3000);
        }
    };

    public void startColorChange() {
        // Schedule the background color change every 1.7 seconds
        handler.postDelayed(backgroundChangeRunnable, 100);

        // Schedule the text color change every 3 seconds
        handler.postDelayed(textChangeRunnable, 3000);
    }

    public void stopColorChange() {
        // Remove any pending callbacks to stop the color change
        handler.removeCallbacksAndMessages(null);
    }
}