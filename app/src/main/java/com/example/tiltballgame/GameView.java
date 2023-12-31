package com.example.tiltballgame;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {
    private Paint mazePaint;
    private Paint ballPaint;
    private float ballRadius;
    private float ballX;
    private float ballY;
    private float tiltX;
    private float tiltY;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize paints for maze and ball
        mazePaint = new Paint();
        mazePaint.setColor(Color.BLACK);
        mazePaint.setStyle(Paint.Style.FILL);

        ballPaint = new Paint();
        ballPaint.setColor(Color.RED);
        ballPaint.setStyle(Paint.Style.FILL);

        // Set initial ball position
        ballRadius = 30; // Adjust this as needed
        ballX = 350; // Initial X position
        ballY = 150; // Initial Y position
    }

    public void setTiltX(float tiltX) {
        this.tiltX = tiltX;
    }

    public void setTiltY(float tiltY) {
        this.tiltY = tiltY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Update ball position based on tilt along both X and Y axes
        ballX += tiltX * 5; // Adjust multiplier for sensitivity
        ballY += tiltY * 5; // Adjust multiplier for sensitivity

        // Ensure the ball stays within the screen bounds
        if (ballX < ballRadius) ballX = ballRadius;
        if (ballX > getWidth() - ballRadius) ballX = getWidth() - ballRadius;
        if (ballY < ballRadius) ballY = ballRadius;
        if (ballY > getHeight() - ballRadius) ballY = getHeight() - ballRadius;

        // Define maze boundaries to fill the screen
        int mazeLeft = 0;
        int mazeTop = 0;
        int mazeRight = getWidth();
        int mazeBottom = getHeight();

        // Define wall thickness
        int wallThickness = 20;
        float left = 0, right = 0, top = 0, bottom = 0;
        int DEFAULT_PADDING = 100;

        // Define walls
        RectF[] walls = new RectF[13];
        left = mazeLeft + DEFAULT_PADDING;
        top = mazeTop + DEFAULT_PADDING;
        right = mazeLeft + DEFAULT_PADDING + wallThickness;
        bottom = mazeBottom - DEFAULT_PADDING;

        walls[0] = new RectF(left, top, right, bottom);

        left = mazeLeft + DEFAULT_PADDING;
        top = mazeTop + DEFAULT_PADDING;
        right = mazeRight - DEFAULT_PADDING;
        bottom = mazeTop + DEFAULT_PADDING + wallThickness;

        walls[1] = new RectF(left, top, right, bottom);

        left = mazeLeft + DEFAULT_PADDING;
        top = mazeBottom - DEFAULT_PADDING - wallThickness;
        right = mazeRight - DEFAULT_PADDING;
        bottom = mazeBottom - DEFAULT_PADDING;

        walls[2] = new RectF(left, top, right, bottom);

        left = mazeRight - DEFAULT_PADDING - wallThickness;
        top = mazeTop + DEFAULT_PADDING;
        right = mazeRight - DEFAULT_PADDING;
        bottom = mazeBottom - DEFAULT_PADDING;

        walls[3] = new RectF(left, top, right, bottom);


        double DEFAULT_RATE = 0.15;
        double DEFAULT_WALL_HEIGHT = mazeBottom - DEFAULT_PADDING * 2 * 0.15;
        // Obstacles
        left = (float) (mazeLeft + DEFAULT_PADDING * 2.5);
        top = (float) (mazeTop + DEFAULT_PADDING + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - top);

        walls[4] = new RectF(left, top, right, bottom);

        left = (float) (left * 1.7);
        top = (float) (mazeTop + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - mazeBottom * 0.6);

        walls[5] = new RectF(left, top, right, bottom);

        left = (float) (left);
        top = (float) (mazeBottom - mazeBottom * 0.4);
        right = left + wallThickness ;
        bottom = (float) (mazeBottom - DEFAULT_PADDING);

        walls[6] = new RectF(left, top, right, bottom);

        left = (float) (left * 1.4);
        top = (float) (mazeTop + DEFAULT_PADDING + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - top);

        walls[7] = new RectF(left, top, right, bottom);


        left = (float) (left * 1.3);
        top = (float) (mazeTop + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - mazeBottom * 0.6);

        walls[8] = new RectF(left, top, right, bottom);

        left = (float) (left);
        top = (float) (mazeBottom - mazeBottom * 0.4);
        right = left + wallThickness ;
        bottom = (float) (mazeBottom - DEFAULT_PADDING);

        walls[9] = new RectF(left, top, right, bottom);


        left = (float) (left * 1 + DEFAULT_PADDING * 1.7);
        top = (float) (mazeTop + DEFAULT_PADDING + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - top);

        walls[10] = new RectF(left, top, right, bottom);

        left = (float) (left + DEFAULT_PADDING * 1.7);
        top = (float) (mazeTop + DEFAULT_PADDING);
        right = left + wallThickness;
        bottom = (float) (mazeBottom - mazeBottom * 0.28);

        walls[11] = new RectF(left, top, right, bottom);

        left = (float) (left + DEFAULT_PADDING * 1.7);
        top = (float) (mazeBottom * 0.28);
        right = left + wallThickness ;
        bottom = (float) (mazeBottom - DEFAULT_PADDING);

        walls[12] = new RectF(left, top, right, bottom);


        int index = 0;

        // Check collision with maze walls
        for (RectF wall : walls) {
            boolean condition = ballX + ballRadius > wall.left && ballX - ballRadius < wall.right
                    && ballY + ballRadius > wall.top && ballY - ballRadius < wall.bottom;
            if (ballX + ballRadius > wall.left && ballX - ballRadius < wall.right
                    && ballY + ballRadius > wall.top && ballY - ballRadius < wall.bottom) {

                // Calculate the overlap between the ball and the wall
                float overlapX = Math.min(Math.abs(ballX + ballRadius - wall.left), Math.abs(wall.right - ballX + ballRadius));
                float overlapY = Math.min(Math.abs(ballY + ballRadius - wall.top), Math.abs(wall.bottom - ballY + ballRadius));

                // Adjust the ball's position based on the overlap
                if (overlapX < overlapY) {
                    ballX += ballX < wall.left ? -overlapX : overlapX;
                    tiltX = -tiltX; // Change direction
                } else {
                    ballY += ballY < wall.top ? -overlapY : overlapY;
                    tiltY = -tiltY; // Change direction
                }
            }
        }

        // Draw the maze walls
        mazePaint.setColor(Color.BLACK);
        for (RectF wall : walls) {
            canvas.drawRoundRect(wall, 30, 30, mazePaint);
        }

        // Draw the exit
        mazePaint.setColor(Color.GREEN);
        int exitSize = 40;
        int exitX = mazeRight - exitSize - 200;
        int exitY = mazeBottom - exitSize - 200;
        canvas.drawRect(exitX, exitY, exitX + exitSize, exitY + exitSize, mazePaint);

        // Check if ball reached the exit
        if (ballX >= exitX && ballX <= exitX + exitSize && ballY >= exitY && ballY <= exitY + exitSize) {
            // Handle game won condition
            ballX = exitX + exitSize / 2; // Center the ball on the exit
            ballY = exitY + exitSize / 2;
            Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT).show();
        }

        // Draw the ball
        canvas.drawCircle(ballX, ballY, ballRadius, ballPaint);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle configuration changes here (if needed)
    }




}
