package com.zachthelen.spelling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        Button btnSpellingGameOptions = findViewById(R.id.btnSpellingGameOptions);
        Button btnMathGameOptions = findViewById(R.id.btnMathGameOptions);
        Button btnGoToChildDashboard = findViewById(R.id.btnGoToChildDashboard);

        btnSpellingGameOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpellingGameOptions();
            }
        });

        btnMathGameOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMathGameOptions();
            }
        });

        btnGoToChildDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChildDashboard();
            }
        });
    }

    public void openSpellingGameOptions() {
        Intent intent = new Intent(this, ParentSpelling.class);
        startActivity(intent);
    }

    public void openMathGameOptions() {
        Intent intent = new Intent(this, ParentMath.class);
        startActivity(intent);
    }

    public void goToChildDashboard() {
        Intent intent = new Intent(this, ChildDashboard.class);
        startActivity(intent);
    }
}