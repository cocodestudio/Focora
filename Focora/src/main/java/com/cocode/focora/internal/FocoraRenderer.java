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
    private final Paint pulsePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF pulseRect = new RectF();
    private Bitmap bitmap;
    private Canvas offscreenCanvas;

    public FocoraRenderer() {
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        borderPaint.setStyle(Paint.Style.STROKE);
        pulsePaint.setStyle(Paint.Style.STROKE);
    }

    public void render(Canvas canvas, int viewWidth, int viewHeight, RectF spotlightRect, float cornerRadius, int bgAlpha, FocoraShape shape, FocoraTheme theme) {
        render(canvas, viewWidth, viewHeight, spotlightRect, cornerRadius, bgAlpha, shape, theme, 0f, 0f);
    }

    public void render(Canvas canvas, int viewWidth, int viewHeight, RectF spotlightRect, float cornerRadius, int bgAlpha, FocoraShape shape, FocoraTheme theme, float pulseFraction, float pulseMaxRadiusPx) {
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

        if (theme.getSpotlightBorderColor() != 0 && theme.getSpotlightBorderWidth() > 0 && bgAlpha > 0 && !spotlightRect.isEmpty()) {
            borderPaint.setColor(theme.getSpotlightBorderColor());
            borderPaint.setStrokeWidth(theme.getSpotlightBorderWidth());
            borderPaint.setAlpha(bgAlpha);

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

        if (theme.isPulseRingsEnabled() && pulseFraction > 0f && !spotlightRect.isEmpty() && bgAlpha > 0) {
            float pulseSpread = pulseFraction * pulseMaxRadiusPx;
            pulseRect.set(
                    spotlightRect.left - pulseSpread,
                    spotlightRect.top - pulseSpread,
                    spotlightRect.right + pulseSpread,
                    spotlightRect.bottom + pulseSpread
            );
            int basePulseColor = theme.getPulseRingColor();
            int baseAlpha = Color.alpha(basePulseColor);
            int currentAlpha = (int) (baseAlpha * (1f - pulseFraction) * (bgAlpha / 255f));
            if (currentAlpha > 0) {
                pulsePaint.setColor(basePulseColor);
                pulsePaint.setAlpha(currentAlpha);
                pulsePaint.setStrokeWidth(2f + (1f - pulseFraction) * 2f);

                switch (shape) {
                    case CIRCLE:
                        float r = (Math.min(spotlightRect.width(), spotlightRect.height()) / 2f) + pulseSpread;
                        offscreenCanvas.drawCircle(pulseRect.centerX(), pulseRect.centerY(), r, pulsePaint);
                        break;
                    case PILL:
                        float pRad = (spotlightRect.height() / 2f) + pulseSpread;
                        offscreenCanvas.drawRoundRect(pulseRect, pRad, pRad, pulsePaint);
                        break;
                    case RECT:
                        offscreenCanvas.drawRect(pulseRect, pulsePaint);
                        break;
                    case ROUNDED_RECT:
                    default:
                        offscreenCanvas.drawRoundRect(pulseRect, cornerRadius + pulseSpread, cornerRadius + pulseSpread, pulsePaint);
                        break;
                }
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