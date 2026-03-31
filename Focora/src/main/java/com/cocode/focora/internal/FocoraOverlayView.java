package com.cocode.focora.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.utils.FocoraUtils;

public class FocoraOverlayView extends FrameLayout {
    private final RectF currentSpotlightRect = new RectF();
    private float currentCornerRadius = 0f;
    private int currentBgAlpha = 0;
    private FocoraShape currentShape = FocoraShape.ROUNDED_RECT;
    private boolean isAnimating = false;

    private final FocoraRenderer renderer = new FocoraRenderer();
    private final TooltipView builtInTooltipView;
    private View activeTooltipView;
    private final FocoraTheme theme;
    private final int totalSteps;
    private final boolean dismissOnBackPress;
    private final Runnable onNext;
    private final Runnable onSkip;
    private ValueAnimator currentAnimator;
    private boolean dismissOnTapOutside = false;

    public FocoraOverlayView(@NonNull Context context, FocoraTheme theme, int totalSteps, boolean dismissOnBackPress, Runnable onNext, Runnable onSkip) {
        super(context);
        this.theme = theme;
        this.totalSteps = totalSteps;
        this.dismissOnBackPress = dismissOnBackPress;
        this.onNext = onNext;
        this.onSkip = onSkip;

        setWillNotDraw(false);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        builtInTooltipView = new TooltipView(context, theme, totalSteps, this::handleNextPressed, onSkip);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(builtInTooltipView, lp);

        activeTooltipView = builtInTooltipView;
        activeTooltipView.setAlpha(0f);
        activeTooltipView.setScaleX(0f);
        activeTooltipView.setScaleY(0f);
    }

    public void animateEntrance(FocoraStep step, int stepIndex, AnimationStyle style, Runnable onComplete) {
        isAnimating = true;
        setupTooltipViewForStep(step);
        builtInTooltipView.setNextEnabled(false);
        builtInTooltipView.updateStep(step.getTitle(), step.getDescription(), stepIndex, totalSteps, stepIndex == totalSteps - 1);

        post(() -> {
            if (getWidth() <= 0) return;
            RectF targetRect = calculateTargetRect(step);
            float targetRadius = calculateTargetRadius(step, targetRect);

            currentSpotlightRect.set(targetRect.centerX(), targetRect.centerY(), targetRect.centerX(), targetRect.centerY());
            currentShape = step.getShape();
            currentCornerRadius = 0f;
            currentBgAlpha = 0;

            positionTooltip(targetRect, targetRadius, true);
            runSpotlightAnimation(targetRect, targetRadius, getTargetBgAlpha(), style, () -> {
                isAnimating = false;
                builtInTooltipView.setNextEnabled(true);
                announceForAccessibility(step.getTitle() + ". " + step.getDescription());
                if (onComplete != null) onComplete.run();
            });
        });
    }

    public void animateTransition(FocoraStep step, int stepIndex, AnimationStyle style, Runnable onComplete) {
        isAnimating = true;
        setupTooltipViewForStep(step);
        builtInTooltipView.setNextEnabled(false);
        builtInTooltipView.updateStep(step.getTitle(), step.getDescription(), stepIndex, totalSteps, stepIndex == totalSteps - 1);

        post(() -> {
            if (getWidth() <= 0) return;
            RectF targetRect = calculateTargetRect(step);
            float targetRadius = calculateTargetRadius(step, targetRect);
            currentShape = step.getShape();

            positionTooltip(targetRect, targetRadius, false);
            runSpotlightAnimation(targetRect, targetRadius, getTargetBgAlpha(), style, () -> {
                isAnimating = false;
                builtInTooltipView.setNextEnabled(true);
                announceForAccessibility(step.getTitle() + ". " + step.getDescription());
                if (onComplete != null) onComplete.run();
            });
        });
    }

    public void animateExit(Runnable onComplete) {
        isAnimating = true;
        RectF collapseTarget = new RectF(currentSpotlightRect.centerX(), currentSpotlightRect.centerY(), currentSpotlightRect.centerX(), currentSpotlightRect.centerY());

        activeTooltipView.animate().alpha(0f).scaleX(0.5f).scaleY(0.5f)
                .setDuration(resolveMs(300)).setInterpolator(new AnticipateInterpolator(1.0f)).start();

        runSpotlightAnimation(collapseTarget, 0f, 0, AnimationStyle.EXPAND, () -> {
            isAnimating = false;
            renderer.recycleBitmap();
            if (onComplete != null) onComplete.run();
        });
    }

    public void requestNextStep() {
        if (!isAnimating) handleNextPressed();
    }

    public void setDismissOnTapOutside(boolean dismiss) {
        this.dismissOnTapOutside = dismiss;
    }

    private void setupTooltipViewForStep(FocoraStep step) {
        if (step.getCustomTooltipView() != null) {
            if (activeTooltipView != step.getCustomTooltipView()) {
                removeView(activeTooltipView);
                activeTooltipView = step.getCustomTooltipView();
                addView(activeTooltipView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        } else {
            if (activeTooltipView != builtInTooltipView) {
                removeView(activeTooltipView);
                activeTooltipView = builtInTooltipView;
                addView(activeTooltipView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    private void handleNextPressed() {
        if (isAnimating) return;
        onNext.run();
    }

    private RectF calculateTargetRect(FocoraStep step) {
        int[] loc = new int[2];
        step.getTarget().getLocationInWindow(loc);
        float pad = FocoraUtils.dpToPx(getContext(), theme.getSpotlightPaddingDp());

        RectF rect = new RectF(loc[0] - pad, loc[1] - pad, loc[0] + step.getTarget().getWidth() + pad, loc[1] + step.getTarget().getHeight() + pad);
        if (step.getShape() == FocoraShape.CIRCLE) {
            float cx = rect.centerX(), cy = rect.centerY();
            float r = Math.max(rect.width(), rect.height()) / 2f;
            rect.set(cx - r, cy - r, cx + r, cy + r);
        }
        return rect;
    }

    private float calculateTargetRadius(FocoraStep step, RectF rect) {
        switch (step.getShape()) {
            case CIRCLE: return Math.max(rect.width(), rect.height()) / 2f;
            case PILL: return rect.height() / 2f;
            case RECT: return 0f;
            case ROUNDED_RECT:
            default: return FocoraUtils.dpToPx(getContext(), step.getCustomCornerRadiusDp() > 0 ? step.getCustomCornerRadiusDp() : 12f);
        }
    }

    private int getTargetBgAlpha() {
        return android.graphics.Color.alpha(theme.getOverlayColor());
    }

    private void positionTooltip(RectF spotlight, float radius, boolean isEntrance) {
        activeTooltipView.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
        float tipH = activeTooltipView.getMeasuredHeight(), tipW = activeTooltipView.getMeasuredWidth();
        float margin = FocoraUtils.dpToPx(getContext(), 24);
        float arrowH = theme.isShowArrow() ? FocoraUtils.dpToPx(getContext(), theme.getArrowSizeDp()) : 0;
        float screenH = getHeight(), screenW = getWidth();

        float targetY;
        ArrowDrawable.Direction arrowDir;
        if (spotlight.bottom + tipH + arrowH + margin < screenH) {
            targetY = spotlight.bottom + arrowH + margin;
            arrowDir = ArrowDrawable.Direction.UP;
        } else if (spotlight.top - tipH - arrowH - margin > 0) {
            targetY = spotlight.top - tipH - arrowH - margin;
            arrowDir = ArrowDrawable.Direction.DOWN;
        } else {
            targetY = margin;
            arrowDir = ArrowDrawable.Direction.DOWN;
        }

        if (activeTooltipView == builtInTooltipView && builtInTooltipView.getArrowView() != null && builtInTooltipView.getArrowView().getDrawable() instanceof ArrowDrawable) {
            ((ArrowDrawable) builtInTooltipView.getArrowView().getDrawable()).setDirection(arrowDir);
        }

        float targetX = spotlight.centerX() - tipW / 2f;
        targetX = Math.max(margin, Math.min(targetX, screenW - tipW - margin));

        boolean isRtl = getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        if (isRtl && activeTooltipView == builtInTooltipView && builtInTooltipView.getArrowView() != null) {
            builtInTooltipView.getArrowView().setScaleX(-1f);
        }

        if (isEntrance) {
            activeTooltipView.setTranslationX(targetX);
            activeTooltipView.setTranslationY(targetY);
            activeTooltipView.setAlpha(0f);
            activeTooltipView.setScaleX(0.7f);
            activeTooltipView.setScaleY(0.7f);
            activeTooltipView.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .setDuration(resolveMs(450)).setInterpolator(new OvershootInterpolator(1.2f)).start();
        } else {
            activeTooltipView.animate().translationX(targetX).translationY(targetY)
                    .setDuration(resolveMs(400)).setInterpolator(new DecelerateInterpolator(1.5f)).start();
        }
    }

    private void runSpotlightAnimation(RectF targetRect, float targetRadius, int targetAlpha, AnimationStyle style, Runnable onComplete) {
        if (currentAnimator != null) currentAnimator.cancel();
        long duration = resolveMs(450);

        if (style == AnimationStyle.NONE || duration == 0L) {
            currentSpotlightRect.set(targetRect);
            currentCornerRadius = targetRadius;
            currentBgAlpha = targetAlpha;
            invalidate();
            if (onComplete != null) onComplete.run();
            return;
        }

        RectF startRect = new RectF(currentSpotlightRect);
        float startRadius = currentCornerRadius;
        int startAlpha = currentBgAlpha;

        currentAnimator = ValueAnimator.ofFloat(0f, 1f);
        currentAnimator.setDuration(duration);
        currentAnimator.setInterpolator(style == AnimationStyle.PULSE ? new OvershootInterpolator(1.5f) : new DecelerateInterpolator(1.5f));

        currentAnimator.addUpdateListener(anim -> {
            float f = anim.getAnimatedFraction();
            if (style != AnimationStyle.FADE) {
                currentSpotlightRect.left   = lerp(startRect.left, targetRect.left, f);
                currentSpotlightRect.top    = lerp(startRect.top, targetRect.top, f);
                currentSpotlightRect.right  = lerp(startRect.right, targetRect.right, f);
                currentSpotlightRect.bottom = lerp(startRect.bottom, targetRect.bottom, f);
                currentCornerRadius = lerp(startRadius, targetRadius, f);
            } else {
                currentSpotlightRect.set(targetRect);
                currentCornerRadius = targetRadius;
            }
            currentBgAlpha = (int) lerp(startAlpha, targetAlpha, f);
            invalidate();
        });

        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                if (style == AnimationStyle.PULSE) {
                    ValueAnimator pulse = ValueAnimator.ofFloat(1f, 1.08f, 1f);
                    pulse.setDuration(resolveMs(250)).setInterpolator(new OvershootInterpolator(1.2f));
                    pulse.addUpdateListener(a -> {
                        float scale = (float) a.getAnimatedValue();
                        currentSpotlightRect.set(targetRect.left * scale, targetRect.top * scale, targetRect.right * scale, targetRect.bottom * scale);
                        invalidate();
                    });
                    pulse.addListener(new AnimatorListenerAdapter() {
                        @Override public void onAnimationEnd(Animator a) { if (onComplete != null) onComplete.run(); }
                    });
                    pulse.start();
                } else {
                    if (onComplete != null) onComplete.run();
                }
            }
        });
        currentAnimator.start();
    }

    private float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private long resolveMs(long def) { return FocoraUtils.resolveAnimDuration(getContext(), theme.isRespectReducedMotion(), def); }

    @Override protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        renderer.render(canvas, getWidth(), getHeight(), currentSpotlightRect, currentCornerRadius, currentBgAlpha, currentShape, theme);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        renderer.recycleBitmap();
        if (currentAnimator != null) currentAnimator.cancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 1. If user taps inside the spotlight hole, let the touch pass through to the app beneath
            if (currentSpotlightRect.contains(event.getX(), event.getY())) {
                return false;
            }

            // 2. If user taps anywhere outside the spotlight and dismiss is enabled, cancel the tutorial
            if (dismissOnTapOutside) {
                onSkip.run();
                return true; // Consume the touch so they don't accidentally click background buttons
            }

            // 3. Otherwise, block the touch (forces user to use "Next" or "Skip" buttons)
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (dismissOnBackPress && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            onSkip.run();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}