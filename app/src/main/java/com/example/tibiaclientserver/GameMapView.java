package com.example.tibiaclientserver;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.*;

public class GameMapView extends View {
    private Bitmap mapBitmap;
    private Paint playerPaint, npcPaint, monsterPaint;
    private Point playerPosition;
    private List<Point> npcPositions;
    private List<Point> monsterPositions;
    private float scaleFactor = 1.0f;
    private float translateX = 0f, translateY = 0f;
    private float lastTouchX, lastTouchY;

    public GameMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        playerPaint = new Paint();
        playerPaint.setColor(Color.BLUE);
        playerPaint.setStyle(Paint.Style.FILL);

        npcPaint = new Paint();
        npcPaint.setColor(Color.GREEN);
        npcPaint.setStyle(Paint.Style.FILL);

        monsterPaint = new Paint();
        monsterPaint.setColor(Color.RED);
        monsterPaint.setStyle(Paint.Style.FILL);

        playerPosition = new Point(0, 0);
        npcPositions = new ArrayList<>();
        monsterPositions = new ArrayList<>();
    }

    public void setMap(Bitmap bitmap) {
        this.mapBitmap = bitmap;
        invalidate();
    }

    public void setPlayerPosition(int x, int y) {
        playerPosition.set(x, y);
        invalidate();
    }

    public void setNpcPositions(List<Point> positions) {
        npcPositions = positions;
        invalidate();
    }

    public void setMonsterPositions(List<Point> positions) {
        monsterPositions = positions;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mapBitmap == null) return;

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(translateX, translateY);
        canvas.drawBitmap(mapBitmap, 0, 0, null);

        // Dibujar jugador
        canvas.drawCircle(playerPosition.x, playerPosition.y, 10, playerPaint);

        // Dibujar NPCs
        for (Point npc : npcPositions) {
            canvas.drawCircle(npc.x, npc.y, 8, npcPaint);
        }

        // Dibujar monstruos
        for (Point monster : monsterPositions) {
            canvas.drawCircle(monster.x, monster.y, 8, monsterPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastTouchX;
                float dy = event.getY() - lastTouchY;
                translateX += dx / scaleFactor;
                translateY += dy / scaleFactor;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // Manejar zoom si es necesario
                break;
        }
        return true;
    }
}