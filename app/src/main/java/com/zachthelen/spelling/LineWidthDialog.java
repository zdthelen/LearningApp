package com.zachthelen.spelling;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class LineWidthDialog extends Dialog {

    private SeekBar seekBarLineWidth;
    private TextView textViewPreview;
    private Button btnSave;

    private OnLineWidthSelectedListener listener;

    public LineWidthDialog(@NonNull Context context, OnLineWidthSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_line_width);

        seekBarLineWidth = findViewById(R.id.seekBarLineWidth);
        textViewPreview = findViewById(R.id.textViewPreview);
        btnSave = findViewById(R.id.btnSave);

        seekBarLineWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreview(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lineWidth = seekBarLineWidth.getProgress() + 1; // Add 1 to avoid 0 line width
                if (listener != null) {
                    listener.onLineWidthSelected(lineWidth);
                }
                dismiss();
            }
        });
    }

    private void updatePreview(int lineWidth) {
        textViewPreview.setText(".");
        textViewPreview.setTextSize(lineWidth * 4); // Adjust size based on line width
    }

    public interface OnLineWidthSelectedListener {
        void onLineWidthSelected(int lineWidth);
    }
}
