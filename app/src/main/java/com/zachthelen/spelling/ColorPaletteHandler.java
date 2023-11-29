package com.zachthelen.spelling;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class ColorPaletteHandler {

    private Context context;
    private PopupWindow popupWindow; // Declare PopupWindow as a member variable
    private ColorSelectedListener colorSelectedListener;
    private ColorSelectedListener backgroundSelectedListener;

    public ColorPaletteHandler(Context context) {
        this.context = context;
    }

    public void setupColorPaletteButton(ImageView colorPaletteButton, Paint drawingPaint) {
        colorPaletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorSelectionPopup(drawingPaint);
            }
        });
    }

    public void setupBackgroundColorButton(ImageView backgroundColorButton, Paint drawingPaint) {//OnColorSelectedListener backgroundColorListener
        backgroundColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackgroundColorPopup(drawingPaint);
            }
        });
    }

    private void showColorSelectionPopup(Paint drawingPaint) {
        // Inflate the color palette popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_color_palette, null);

        // Create a PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set up the color palette grid
        GridLayout colorPaletteGrid = popupView.findViewById(R.id.colorPaletteGrid);
        setupColorPaletteGrid(colorPaletteGrid, drawingPaint);

        // Provide a background for the PopupWindow (choose either drawable or color)
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private void setupColorPaletteGrid(GridLayout colorPaletteGrid, Paint drawingPaint) {
        int[] colors = context.getResources().getIntArray(R.array.drawing_palette_colors);

        for (int color : colors) {
            ImageView colorSquare = (ImageView) LayoutInflater.from(context).inflate(R.layout.color_palette_square_sample, colorPaletteGrid, false);
            colorSquare.setBackgroundColor(color);
            colorSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedColor = ((ColorDrawable) colorSquare.getBackground()).getColor();
                    if (colorSelectedListener != null) {
                        colorSelectedListener.onColorSelected(selectedColor);
                    }
                    popupWindow.dismiss(); // Dismiss the PopupWindow after color selection
                }
            });

            colorPaletteGrid.addView(colorSquare);
        }
    }

    private void setupBackgroundColorPaletteGrid(GridLayout colorPaletteGrid, Paint drawingPaint) {
        int[] colors = context.getResources().getIntArray(R.array.drawing_palette_colors);

        for (int color : colors) {
            ImageView colorSquare = (ImageView) LayoutInflater.from(context).inflate(R.layout.color_palette_square_sample, colorPaletteGrid, false);
            colorSquare.setBackgroundColor(color);
            colorSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedColor = ((ColorDrawable) colorSquare.getBackground()).getColor();
                    if (backgroundSelectedListener != null) {
                        backgroundSelectedListener.onBackgroundColorSelected(selectedColor);
                    }
                    popupWindow.dismiss(); // Dismiss the PopupWindow after color selection
                }
            });

            colorPaletteGrid.addView(colorSquare);
        }
    }

    private void showBackgroundColorPopup(Paint drawingPaint) {//OnColorSelectedListener backgroundColorListener
        // Inflate the color palette popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_color_palette, null);

        // Create a PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set up the color palette grid
        GridLayout colorPaletteGrid = popupView.findViewById(R.id.colorPaletteGrid);
        setupBackgroundColorPaletteGrid(colorPaletteGrid, drawingPaint);
//        setupColorPaletteGrid(colorPaletteGrid, backgroundColorListener, popupWindow);
        // Provide a background for the PopupWindow (choose either drawable or color)
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }





    public void setOnColorSelectedListener(ColorSelectedListener listener) {
        this.colorSelectedListener = listener;
    }


    public interface ColorSelectedListener {
        void onColorSelected(int selectedColor);

        void onBackgroundColorSelected(int selectedColor);
    }

    public void setOnBackgroundColorSelectedListener(ColorSelectedListener listener) {
        this.backgroundSelectedListener = listener;
    }

    public interface onBackgroundColorSelectedListener {
        void onBackgroundColorSelected(int selectedColor);
    }

}

