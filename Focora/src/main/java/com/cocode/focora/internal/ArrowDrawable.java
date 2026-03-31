package com.cocode.focora.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class ArrowDrawable extends Drawable {
    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private Direction direction = Direction.DOWN;

    ArrowDrawable(int color, float sizePx) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        setBounds(0, 0, (int) sizePx * 2, (int) sizePx);
    }

    void setDirection(Direction direction) {
        this.direction = direction;
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int w = getBounds().width();
        int h = getBounds().height();
        path.reset();

        switch (direction) {
            case DOWN:
                path.moveTo(0, 0); path.lineTo(w, 0); path.lineTo(w / 2f, h); break;
            case UP:
                path.moveTo(w / 2f, 0); path.lineTo(w, h); path.lineTo(0, h); break;
            case LEFT:
                path.moveTo(w, 0); path.lineTo(w, h); path.lineTo(0, h / 2f); break;
            case RIGHT:
                path.moveTo(0, 0); path.lineTo(w, h / 2f); path.lineTo(0, h); break;
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override public void setAlpha(int alpha) { paint.setAlpha(alpha); }
    @Override public void setColorFilter(@Nullable ColorFilter colorFilter) { paint.setColorFilter(colorFilter); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}