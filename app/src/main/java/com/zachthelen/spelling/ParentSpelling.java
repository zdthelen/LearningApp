package com.zachthelen.spelling;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParentSpelling extends AppCompatActivity {

    private static final String SPELLING_PREFS = "MySpellingPrefs";
    public static final String WORD_LIST_SELECTION_KEY = "wordListSelection";
    public static final String CURRENT_WORD_LIST_KEY = "currentWordList";
    public static final String CUSTOM_WORD_LIST_KEY = "customWordList";
    public static final String ALL_WORD_LIST_KEY = "allWordList";
    private LinearLayout wordListLayout;
    private EditText wordInput;
    private Spinner spinnerWordList;
    private Button btnStoreWordsAndClearList;
    private Button btnEditCustomList;
    private Button btnDashboardFromParentSpelling;

    private ArrayList<String> currentWordList;
    private ArrayList<String> customWordList;
    private ArrayList<String> allStoredWords;
    private ArrayList<String> temporaryWordList;
    int selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_spelling);

        wordListLayout = findViewById(R.id.wordListLayout);
        wordInput = findViewById(R.id.wordInput);

        spinnerWordList = findViewById(R.id.spinnerListSelection);
        selectedList = spinnerWordList.getSelectedItemPosition();
        btnStoreWordsAndClearList = findViewById(R.id.btnStoreWordsAndClearList);
        btnEditCustomList = findViewById(R.id.btnEditCustomList);
        btnDashboardFromParentSpelling = findViewById(R.id.btn_dashboard_from_spelling);

        // Initialize arrays
        currentWordList = new ArrayList<>();
        customWordList = new ArrayList<>();
        allStoredWords = new ArrayList<>();
        temporaryWordList = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.word_list_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWordList.setAdapter(adapter);
        spinnerWordList.setSelection(loadWordListSelection());


        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord();
            }
        });


        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        btnStoreWordsAndClearList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeWordsAndClearList();
            }
        });

        btnEditCustomList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditCustomListDialog();
            }
        });

        btnDashboardFromParentSpelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDashboardFromParentSpelling();
            }
        });

        spinnerWordList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Save the selected position to SharedPreferences
                saveWordListSelection(position);

                // Update the UI based on the selected list
                updateWordListUI(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });

        loadWordListSelection();
        loadAllWordList();
        loadCustomWordList();
        loadCurrentWordList();
        updateWordListUI(selectedList);
    }

    private void openDashboardFromParentSpelling() {
        Intent intent = new Intent(this, ParentDashboard.class);
        startActivity(intent);
    }

    private void updateWordListUI(String[] wordList) {
        wordListLayout.removeAllViews();
        for (String word : wordList) {
            TextView textView = new TextView(this);
            textView.setText(word);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            wordListLayout.addView(textView);
        }
    }

    private void handleWordListSelection(int position) {
        // Clear gameWordList before adding new words
        temporaryWordList.clear();

        switch (position) {
            case 0:
                // Current Word List
                temporaryWordList.addAll(currentWordList);
                break;
            case 1:
                // Custom Word List
                temporaryWordList.addAll(customWordList);
                break;
            case 2:
                // All Stored Words
                temporaryWordList.addAll(allStoredWords);
                break;
        }


        // Load the selected word list from SharedPreferences
        loadWordListFromPrefs(position);

        // Save the selected word list option
        saveWordListSelection(position);
    }

    private void loadWordListFromPrefs(int position) {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);

        // Clear the current word list
        temporaryWordList.clear();

        switch (position) {
            case 0:
                // Load Current Word List from SharedPreferences
                temporaryWordList.addAll(preferences.getStringSet(CURRENT_WORD_LIST_KEY, new HashSet<>()));
                break;
            case 1:
                // Load Custom Word List from SharedPreferences
                temporaryWordList.addAll(preferences.getStringSet(CUSTOM_WORD_LIST_KEY, new HashSet<>()));
                break;
            case 2:
                // Load All Stored Words from SharedPreferences
                temporaryWordList.addAll(preferences.getStringSet(ALL_WORD_LIST_KEY, new HashSet<>()));
                break;
        }
    }

    private void saveWordListSelection(int selection) {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(WORD_LIST_SELECTION_KEY, selection);
        editor.apply();
    }

    private int loadWordListSelection() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        return preferences.getInt(WORD_LIST_SELECTION_KEY, 0); // Default to 0 if not found
    }


    private void storeWordsAndClearList() {
        // Add words from Current Word List to All Stored Words
        allStoredWords = concatenateArrays(allStoredWords, currentWordList);

        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(ALL_WORD_LIST_KEY, new HashSet<>(allStoredWords));
        editor.apply();

        // Clear Current Word List
        currentWordList.clear();

        // Update UI based on the current spinner selection

        updateWordListUI(selectedList);
    }

    private ArrayList<String> concatenateArrays(ArrayList<String> array1, ArrayList<String> array2) {
        ArrayList<String> result = new ArrayList<>(array1);
        result.addAll(array2);
        return result;
    }


    private void updateWordListUI(int selectedList) {
        List<String> listToShow = new ArrayList<>();

        switch (selectedList) {
            case 0:
                // Current Word List
                listToShow.addAll(currentWordList);
                break;
            case 1:
                // Custom Word List
                listToShow.addAll(customWordList);
                break;
            case 2:
                // All Stored Words
                listToShow.addAll(allStoredWords);
                break;
            // Add more cases if needed for additional lists

            default:
                break;
        }

        wordListLayout.removeAllViews();
        for (String word : listToShow) {
            TextView textView = new TextView(this);
            textView.setText(word);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            wordListLayout.addView(textView);
        }
    }


    private void addWord() {
        String newWord = wordInput.getText().toString().trim();
        if (!newWord.isEmpty()) {

            currentWordList.add(newWord);
            saveCurrentWordList();
            updateWordListUI(selectedList);
            wordInput.getText().clear();
        }
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Words");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
        builder.setView(view);

        final LinearLayout wordCheckboxLayout = view.findViewById(R.id.wordCheckboxLayout);

        // Clear existing checkboxes
        wordCheckboxLayout.removeAllViews();

        // Populate the dialog with checkboxes for each word
        for (final String word : currentWordList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(word);
            wordCheckboxLayout.addView(checkBox);
        }

        builder.setPositiveButton("Remove Selected", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeSelectedWords(wordCheckboxLayout);
            }
        });

        builder.setNegativeButton("Remove All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAllWords();
            }
        });

        builder.setNeutralButton("Cancel", null);

        builder.show();
    }

    private void removeSelectedWords(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) layout.getChildAt(i);
            if (checkBox.isChecked()) {
                currentWordList.remove(checkBox.getText().toString());
            }
        }

        updateWordListUI(selectedList);
    }

    private void removeAllWords() {
        currentWordList.clear();
        saveCurrentWordList();
        updateWordListUI(selectedList);
    }

    private void saveCurrentWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> currentWordSet = new HashSet<>(currentWordList);
        editor.putStringSet(CURRENT_WORD_LIST_KEY, currentWordSet);
        editor.apply();
    }

    private void loadCurrentWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> currentWordSet = preferences.getStringSet(CURRENT_WORD_LIST_KEY, new HashSet<>());

        // Clear currentWordList before adding new words
        currentWordList.clear();

        // Add words from SharedPreferences to currentWordList
        currentWordList.addAll(currentWordSet);
    }

    private void loadCustomWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> customWordSet = preferences.getStringSet(CUSTOM_WORD_LIST_KEY, new HashSet<>());

        // Clear currentWordList before adding new words
        customWordList.clear();

        // Add words from SharedPreferences to currentWordList
        customWordList.addAll(customWordSet);
    }

    private void loadAllWordList() {
        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        Set<String> allWordSet = preferences.getStringSet(ALL_WORD_LIST_KEY, new HashSet<>());

        // Clear currentWordList before adding new words
        allStoredWords.clear();

        // Add words from SharedPreferences to currentWordList
        allStoredWords.addAll(allWordSet);
    }

    private void showEditCustomListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Custom List");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_custom_list, null);
        builder.setView(view);

        final LinearLayout wordCheckboxLayout = view.findViewById(R.id.wordCheckboxLayout);

        // Clear existing checkboxes
        wordCheckboxLayout.removeAllViews();

        // Populate the dialog with checkboxes for each word in All Stored Words
        for (final String word : allStoredWords) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(word);
            // Check the checkbox if the word is in the customWordList
            checkBox.setChecked(Arrays.asList(customWordList).contains(word));
            wordCheckboxLayout.addView(checkBox);
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveCustomWordList(wordCheckboxLayout);
            }
        });

        builder.setNegativeButton("Clear Selections", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSelections(wordCheckboxLayout);
            }
        });

        builder.setNeutralButton("Cancel", null);

        builder.show();
    }

    private void saveCustomWordList(LinearLayout layout) {
        List<String> selectedWords = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) layout.getChildAt(i);
            if (checkBox.isChecked()) {
                selectedWords.add(checkBox.getText().toString());
            }
        }

        // Update the customWordList with the selected words
        customWordList = new ArrayList<>(selectedWords);

        SharedPreferences preferences = getSharedPreferences(SPELLING_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(CUSTOM_WORD_LIST_KEY, new HashSet<>(customWordList));
        editor.apply();

        // Update UI
        handleWordListSelection(spinnerWordList.getSelectedItemPosition());
    }

    private void clearSelections(LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) layout.getChildAt(i);
            checkBox.setChecked(false);
        }
    }
}