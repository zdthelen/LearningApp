package com.zachthelen.spelling;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ToolbarFragment extends Fragment implements ColorPaletteHandler.ColorSelectedListener {

    private ColorPaletteHandler colorPaletteHandler;
    private Paint drawingPaint = new Paint(); // Initialize the Paint object


    // Add the IDs of the elements you want to change colors dynamically
    private int[] elementIds = {
            R.id.imgColorPalette, R.id.tvColorPalette,
            R.id.imgLineWidth, R.id.tvLineWidth,
            R.id.imgBackgroundColor, R.id.tvBackgroundColor,
            R.id.imgErase, R.id.tvErase,
            R.id.imgUndo, R.id.tvUndo,
            R.id.imgExit, R.id.tvExit,
            R.id.divider, R.id.divider2,
            R.id.divider3, R.id.divider4,
            R.id.divider5, R.id.divider6,
            R.id.divider7, R.id.divider9
    };

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toolbar, container, false);

        // Find buttons by their IDs
        ImageView btnLineWidth = view.findViewById(R.id.imgLineWidth);
        ImageView btnErase = view.findViewById(R.id.imgErase);
        ImageView btnUndo = view.findViewById(R.id.imgUndo);
        ImageView btnExit = view.findViewById(R.id.imgExit);



        btnLineWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Line Width button click
                // Open a dialog or activity for adjusting line width
                showLineWidthDialog();
            }
        });

        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Erase button click
                // Change the drawing color to the background color
//                drawingPaint.setColor(DrawingFragment.getBackgroundColor()); // Assuming yourDrawingView has a method to get its background color
//                // Force a redraw if you are using a custom drawing view
//                DrawingFragment.invalidate();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Undo button click
                // Remove the last stroke
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Exit button click
                // Open a dialog asking whether to save or exit
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        colorPaletteHandler = new ColorPaletteHandler(requireContext());

        ImageView colorPaletteButton = view.findViewById(R.id.imgColorPalette);
        ImageView backgroundColorButton = view.findViewById(R.id.imgBackgroundColor);

        colorPaletteHandler.setupColorPaletteButton(colorPaletteButton, drawingPaint);

        // Set the listener after setupColorPaletteButton
        colorPaletteHandler.setOnColorSelectedListener(this);
        colorPaletteHandler.setOnBackgroundColorSelectedListener(this);

        colorPaletteHandler.setupBackgroundColorButton(backgroundColorButton, drawingPaint);

        // Update icon and text colors based on the initial background color
        updateIconColors(DrawingFragment.getBackgroundColor());


    }

    @Override
    public void onColorSelected(int selectedColor) {
        // Handle the selected color in ToolbarFragment
        // You can store it as a member variable or perform any required actions
        drawingPaint.setColor(selectedColor);
        DrawingFragment.setNewColor(selectedColor);

    }

    @Override
    public void onBackgroundColorSelected(int selectedColor) {
        // Handle the selected color in ToolbarFragment
        drawingPaint.setColor(selectedColor);
        DrawingFragment.setBackgroundColor(selectedColor);
        // Update icon colors based on background color
        updateIconColors(selectedColor);
    }

    private void showLineWidthDialog() {
        LineWidthDialog dialog = new LineWidthDialog(requireContext(), new LineWidthDialog.OnLineWidthSelectedListener() {
            @Override
            public void onLineWidthSelected(int lineWidth) {
                // Handle the selected line width
                drawingPaint.setStrokeWidth(lineWidth);
                DrawingFragment.setCurrentLineWidth(lineWidth);
            }
        });

        dialog.show();
    }


    private void updateIconColors(int backgroundColor) {

        // Check whether the background color is light or dark
        boolean isLight = ColorUtils.calculateLuminance(backgroundColor) > 0.5;

        for (int elementId : elementIds) {
            View element = view.findViewById(elementId);

            // Set text and icon colors based on the background

            if (element instanceof TextView) {
                ((TextView) element).setTextColor(isLight ? Color.BLACK : Color.WHITE);
            }

            if (element instanceof ImageView) {
                ((ImageView) element).setColorFilter(isLight ? Color.BLACK : Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

        }
    }
}

    // Rest of your ToolbarFragment code...
