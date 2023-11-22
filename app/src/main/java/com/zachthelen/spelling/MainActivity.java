package com.zachthelen.spelling;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PARENT_PREFS = "ParentPrefs";
    private static final String USERNAME_KEY = "username";
    private static final String PASSCODE_KEY = "passcode";
    private TextView parentTextView;
    private TextView childTextView;

    private int[] backgroundColors;
    private int[] textColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentTextView = findViewById(R.id.parentTextView);
        childTextView = findViewById(R.id.childTextView);

        // Initialize color arrays
        backgroundColors = getResources().getIntArray(R.array.backgroundColors);
        textColors = getResources().getIntArray(R.array.textColors);

        // Set random colors to TextViews
        setRandomColors(parentTextView);
        setRandomColors(childTextView);

        parentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParentCredentials();
            }
        });

        childTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChildCredentials();
            }
        });
    }

    private void setRandomColors(TextView textView) {
        Random random = new Random();
        int randomBackgroundColor = backgroundColors[random.nextInt(backgroundColors.length)];
        int randomTextColor = textColors[random.nextInt(textColors.length)];

        textView.setBackgroundColor(randomBackgroundColor);
        textView.setTextColor(randomTextColor);
    }
    private void checkParentCredentials() {
        // Check if username and passcode exist in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PARENT_PREFS, MODE_PRIVATE);
        String savedUsername = preferences.getString(USERNAME_KEY, null);
        String savedPasscode = preferences.getString(PASSCODE_KEY, null);

        if (savedUsername != null && savedPasscode != null) {
            // Dialog to enter credentials
            showParentLoginDialog();
        } else {
            // Dialog to set username and passcode
            showSetParentCredentialsDialog();
        }
    }

    private void showSetParentCredentialsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parent Setup");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_parent_setup, null);
        builder.setView(view);

        final EditText usernameInput = view.findViewById(R.id.usernameInput);
        final EditText passcodeInput = view.findViewById(R.id.passcodeInput);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = usernameInput.getText().toString().trim();
                String passcode = passcodeInput.getText().toString().trim();

                // Save username and passcode to SharedPreferences (implement this)
                saveParentCredentials(username, passcode);

                // You might want to navigate to ParentDashboard activity here
                // For now, just show a toast or leave it empty
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void saveParentCredentials(String username, String passcode) {
        SharedPreferences preferences = getSharedPreferences(PARENT_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSCODE_KEY, passcode);
        editor.apply();
    }

    private boolean checkParentCredentials(String enteredUsername, String enteredPasscode) {
        // Implement this method to check if entered credentials match saved credentials
        SharedPreferences preferences = getSharedPreferences(PARENT_PREFS, MODE_PRIVATE);
        String savedUsername = preferences.getString(USERNAME_KEY, null);
        String savedPasscode = preferences.getString(PASSCODE_KEY, null);

        return savedUsername != null && savedPasscode != null &&
                savedUsername.equals(enteredUsername) && savedPasscode.equals(enteredPasscode);
    }


    private void showParentLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Parent Login");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_parent_login, null);
        builder.setView(view);

        final EditText usernameInput = view.findViewById(R.id.usernameInput);
        final EditText passcodeInput = view.findViewById(R.id.passcodeInput);

        // Get SharedPreferences instance
        SharedPreferences preferences = getSharedPreferences(PARENT_PREFS, MODE_PRIVATE);

        // Get SharedPreferences.Editor instance
        final SharedPreferences.Editor editor = preferences.edit();

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredUsername = usernameInput.getText().toString().trim();
                String enteredPasscode = passcodeInput.getText().toString().trim();

                // Check if entered credentials match saved credentials (implement this)
                if (checkParentCredentials(enteredUsername, enteredPasscode)) {
                    // Navigate to ParentDashboard activity
                    startActivity(new Intent(MainActivity.this, ParentDashboard.class));
                } else {
                    // Show error message or retry
                }
            }
        });

        builder.setNegativeButton("Reset Application", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear SharedPreferences
                editor.clear().apply();
                // You might want to add additional reset logic here if needed
            }
        });

        builder.show();
    }

    private void checkChildCredentials() {
        // Check if username exists in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PARENT_PREFS, MODE_PRIVATE);
        String savedUsername = preferences.getString(USERNAME_KEY, null);

        if (savedUsername != null) {
            // Proceed to ChildDashboard activity
            startActivity(new Intent(this, ChildDashboard.class));
        } else {
            // Show dialog to get an adult
            showGetAdultDialog();
        }
    }

    private void showGetAdultDialog() {
        // Implement the dialog to get an adult
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Go get an adult to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Show the Parent Setup dialog again
                        showSetParentCredentialsDialog();
                    }
                })
                .setCancelable(false) // Prevent the dialog from being canceled with the back button
                .show();
    }
}