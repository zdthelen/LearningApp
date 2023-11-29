package com.zachthelen.spelling;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DrawingFragment extends Fragment {

    private SurfaceView surfaceView;
    private static SurfaceHolder surfaceHolder;
    private static Paint paint;
    private static Path path;
//    private static List<Path> pathList = new ArrayList<>(); // List to store multiple paths
    private static List<Stroke> strokes = new ArrayList<>();  // Store existing strokes
    private static int currentLineWidth = 5; // Default line width
    private static int backgroundColor = Color.BLACK; // Default background color

    public DrawingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);

        surfaceView = view.findViewById(R.id.surfaceViewDrawing);
        surfaceHolder = surfaceView.getHolder();

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        // Set up SurfaceHolder.Callback
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                super.surfaceCreated(holder);
                // Draw the background color only when the surface is created
//                redrawSurfaceView();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Handle surface changes, if needed
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // Release resources, if needed
            }
        });

        // Handle touch events for drawing
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        drawStart(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        drawMove(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        drawStop();
                        break;
                }
                return true;
            }
        });
        return view;
    }


    private void drawStart(float x, float y) {
        // Start a new stroke
        path = new Path();
        path.moveTo(x, y);
    }

    private void drawMove(float x, float y) {
        // Continue drawing as the user moves their finger
        if (path != null) {
            path.lineTo(x, y);
            draw();
        }
    }

    private void drawStop() {
        // Stop drawing, add the completed stroke to the list
        if (path != null) {
            strokes.add(new Stroke(new Path(path), new Paint(paint)));
            path = null;
        }
    }

    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(backgroundColor); // Set the background color

        // Draw completed strokes
        for (Stroke stroke : strokes) {
            canvas.drawPath(stroke.path, stroke.paint);
        }

        // Draw the current stroke
        if (path != null) {
            canvas.drawPath(path, paint);
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    // Helper class to represent a stroke
    private static class Stroke {
        Path path;
        Paint paint;

        Stroke(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }

    //this did weird stuff...
//    private void drawStart(float x, float y) {
//        // Start drawing at the specified coordinates
//        currentLine = new Line();
//        currentLine.path.moveTo(x, y);
//        currentLine.paint.setStrokeWidth(currentLineWidth);
//        currentLine.paint.setColor(paint.getColor());
//    }
//
//    private void drawMove(float x, float y) {
//        // Continue drawing as the user moves their finger
//        if (currentLine != null) {
//            currentLine.path.lineTo(x, y);
//            draw();
//        }
//    }
//
//    private void drawStop() {
//        // Stop drawing, if needed
//        // Add the completed line to the list of lines
//        if (currentLine != null) {
//            lines.add(currentLine);
//        }
//        currentLine = null;
//    }
//
//    private void draw() {
//        Canvas canvas = surfaceHolder.lockCanvas();
//        canvas.drawColor(backgroundColor); // Set the background color
//
//        // Draw existing lines
//        for (Line line : lines) {
//            canvas.drawPath(line.path, line.paint);
//        }
//
//        // Draw the currently drawn line
//        if (currentLine != null) {
//            canvas.drawPath(currentLine.path, currentLine.paint);
//        }
//
//        surfaceHolder.unlockCanvasAndPost(canvas);
//    }

    private class DrawingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Perform your drawing operations in the background
            // This method runs on a background thread
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the UI on the main thread
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(backgroundColor); // Set the background color
                for (Stroke stroke : strokes) {
                    canvas.drawPath(stroke.path, stroke.paint);
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    public static void setNewColor(int selectedColor) {
        // Use the selectedColor in DrawingFragment
        paint.setColor(selectedColor);
    }

    public static void setCurrentLineWidth(int lineWidth) {
        currentLineWidth = lineWidth;
    }

    public static void setBackgroundColor(int selectedColor) {
        backgroundColor = selectedColor;
        redrawSurfaceView();

    }

    private static void redrawSurfaceView() {
        // Redraw the entire SurfaceView with the new background color
        if (surfaceHolder.getSurface().isValid()) {
            Log.d("Debug", "Happening");
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(backgroundColor); // Set the background color
            // Redraw any existing paths or elements
            for (Stroke stroke : strokes) {
                canvas.drawPath(stroke.path, stroke.paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public static int getBackgroundColor() {
        return backgroundColor;
    }


}