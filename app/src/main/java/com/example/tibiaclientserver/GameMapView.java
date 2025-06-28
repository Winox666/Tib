package com.example.tibiaclientserver;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.*;

public class GameMapView extends View {
    private Bitmap mapBitmap;
    private Bitmap playerSprite, npcSprite, monsterSprite;
    private Paint backgroundPaint;
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
        backgroundPaint = new Paint();
        playerSprite = BitmapFactory.decodeResource(getResources(), R.drawable.player_sprite);
        npcSprite = BitmapFactory.decodeResource(getResources(), R.drawable.npc_sprite);
        monsterSprite = BitmapFactory.decodeResource(getResources(), R.drawable.monster_sprite);

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

        // Dibujar mapa
        canvas.drawBitmap(mapBitmap, 0, 0, backgroundPaint);

        // Dibujar NPCs
        for (Point npc : npcPositions) {
            canvas.drawBitmap(npcSprite, npc.x, npc.y, null);
        }

        // Dibujar monstruos
        for (Point monster : monsterPositions) {
            canvas.drawBitmap(monsterSprite, monster.x, monster.y, null);
        }

        // Dibujar jugador
        canvas.drawBitmap(playerSprite, playerPosition.x, playerPosition.y, null);

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
        }
        return true;
    }
}