package com.cocode.focora.internal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.cocode.focora.FocoraTheme;
import com.cocode.focora.FocoraShape;

public class FocoraRenderer {
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint eraserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private Canvas offscreenCanvas;

    public FocoraRenderer() {
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void render(Canvas canvas, int viewWidth, int viewHeight, RectF spotlightRect, float cornerRadius, int bgAlpha, FocoraShape shape, FocoraTheme theme) {
        if (viewWidth <= 0 || viewHeight <= 0) return;

        if (bitmap == null || bitmap.getWidth() != viewWidth || bitmap.getHeight() != viewHeight) {
            recycleBitmap();
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            offscreenCanvas = new Canvas(bitmap);
        }

        bitmap.eraseColor(Color.TRANSPARENT);
        int overlayBase = theme.getOverlayColor();
        int overlayWithAlpha = Color.argb(bgAlpha, Color.red(overlayBase), Color.green(overlayBase), Color.blue(overlayBase));
        backgroundPaint.setColor(overlayWithAlpha);
        offscreenCanvas.drawRect(0, 0, viewWidth, viewHeight, backgroundPaint);

        if (bgAlpha > 0 && !spotlightRect.isEmpty()) {
            switch (shape) {
                case CIRCLE:
                    float circleRadius = Math.min(spotlightRect.width(), spotlightRect.height()) / 2f;
                    offscreenCanvas.drawCircle(spotlightRect.centerX(), spotlightRect.centerY(), circleRadius, eraserPaint);
                    break;
                case PILL:
                    // FIX: Dynamically calculate radius based on current height during animation
                    float pillRadius = spotlightRect.height() / 2f;
                    offscreenCanvas.drawRoundRect(spotlightRect, pillRadius, pillRadius, eraserPaint);
                    break;
                case RECT:
                    offscreenCanvas.drawRect(spotlightRect, eraserPaint);
                    break;
                case ROUNDED_RECT:
                default:
                    offscreenCanvas.drawRoundRect(spotlightRect, cornerRadius, cornerRadius, eraserPaint);
                    break;
            }
        }

        if (theme.getSpotlightBorderColor() != 0 && theme.getSpotlightBorderWidth() > 0) {
            borderPaint.setColor(theme.getSpotlightBorderColor());
            borderPaint.setStrokeWidth(theme.getSpotlightBorderWidth());
            borderPaint.setAlpha(bgAlpha);

            // Also apply the dynamic calculation to the border stroke
            switch (shape) {
                case CIRCLE:
                    float r = Math.min(spotlightRect.width(), spotlightRect.height()) / 2f;
                    offscreenCanvas.drawCircle(spotlightRect.centerX(), spotlightRect.centerY(), r, borderPaint);
                    break;
                case PILL:
                    float pRad = spotlightRect.height() / 2f;
                    offscreenCanvas.drawRoundRect(spotlightRect, pRad, pRad, borderPaint);
                    break;
                case RECT:
                    offscreenCanvas.drawRect(spotlightRect, borderPaint);
                    break;
                case ROUNDED_RECT:
                default:
                    offscreenCanvas.drawRoundRect(spotlightRect, cornerRadius, cornerRadius, borderPaint);
                    break;
            }
        }

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public void recycleBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}