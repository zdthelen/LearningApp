package com.zachthelen.spelling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Drawing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        // Button for New Drawing
        Button btnNewDrawing = findViewById(R.id.btnNewDrawing);
        btnNewDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to start a new drawing
                // Load DrawingFragment
                loadFragment(new DrawingFragment(), R.id.fragmentContainerDrawing);

                // Load ToolbarFragment
                Log.d("Debug", "Loading Toolbar Fragment");
                loadFragment(new ToolbarFragment(), R.id.fragmentContainerToolbar);
            }
        });

        // Button for Continue Drawing
        Button btnContinueDrawing = findViewById(R.id.btnContinueDrawing);
        btnContinueDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to continue the last drawing
            }
        });

        // Button for Gallery
        Button btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to open the gallery
            }
        });

        // Button for Back to Activities
        Button btnBackToActivities = findViewById(R.id.btnBackToActivities);
        btnBackToActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to go back to the main activities screen
                Intent intent = new Intent(Drawing.this, FunActivities.class);
                startActivity(intent);
                finish(); // Close the drawing activity
            }
        });

        // Add other logic for drawing, color palette, etc.
    }


    private void loadFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }

}