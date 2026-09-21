package com.cocode.focora;

import android.graphics.RectF;
import android.view.View;

public final class FocoraStep {
    private final View target;
    private final RectF targetRect;
    private final float targetLeft;
    private final float targetTop;
    private final float targetRight;
    private final float targetBottom;
    private final boolean hasVirtualTarget;
    private final String title;
    private final String description;
    private final FocoraShape shape;
    private final TooltipPosition tooltipPosition;
    private final AnimationStyle animationStyle;
    private final float customCornerRadiusDp;
    private final boolean dismissOnTapOutside;
    private final boolean advanceOnTargetTap;
    private final Runnable onStepShownAction;
    private final Runnable onTargetClickedAction;
    private final View customTooltipView;

    private FocoraStep(Builder b) {
        this.target = b.target;
        this.targetRect = b.targetRect;
        this.targetLeft = b.targetLeft;
        this.targetTop = b.targetTop;
        this.targetRight = b.targetRight;
        this.targetBottom = b.targetBottom;
        this.hasVirtualTarget = b.hasVirtualTarget;
        this.title = b.title;
        this.description = b.description;
        this.shape = b.shape;
        this.tooltipPosition = b.tooltipPosition;
        this.animationStyle = b.animationStyle;
        this.customCornerRadiusDp = b.customCornerRadiusDp;
        this.dismissOnTapOutside = b.dismissOnTapOutside;
        this.advanceOnTargetTap = b.advanceOnTargetTap;
        this.onStepShownAction = b.onStepShownAction;
        this.onTargetClickedAction = b.onTargetClickedAction;
        this.customTooltipView = b.customTooltipView;
    }

    public View getTarget() { return target; }
    public RectF getTargetRect() {
        if (targetRect != null) return targetRect;
        if (hasVirtualTarget) return new RectF(targetLeft, targetTop, targetRight, targetBottom);
        return null;
    }
    public float getVirtualLeft() { return targetLeft; }
    public float getVirtualTop() { return targetTop; }
    public float getVirtualRight() { return targetRight; }
    public float getVirtualBottom() { return targetBottom; }
    public boolean hasVirtualTarget() { return hasVirtualTarget; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public FocoraShape getShape() { return shape; }
    public TooltipPosition getTooltipPosition() { return tooltipPosition; }
    public AnimationStyle getAnimationStyle() { return animationStyle; }
    public float getCustomCornerRadiusDp() { return customCornerRadiusDp; }
    public boolean isDismissOnTapOutside() { return dismissOnTapOutside; }
    public boolean isAdvanceOnTargetTap() { return advanceOnTargetTap; }
    public Runnable getOnStepShownAction() { return onStepShownAction; }
    public Runnable getOnTargetClickedAction() { return onTargetClickedAction; }
    public View getCustomTooltipView() { return customTooltipView; }

    public static final class Builder {
        private final View target;
        private RectF targetRect = null;
        private float targetLeft = 0f;
        private float targetTop = 0f;
        private float targetRight = 0f;
        private float targetBottom = 0f;
        private boolean hasVirtualTarget = false;
        private String title = "";
        private String description = "";
        private FocoraShape shape = FocoraShape.ROUNDED_RECT;
        private TooltipPosition tooltipPosition = TooltipPosition.AUTO;
        private AnimationStyle animationStyle = AnimationStyle.EXPAND;
        private float customCornerRadiusDp = 12f;
        private boolean dismissOnTapOutside = false;
        private boolean advanceOnTargetTap = false;
        private Runnable onStepShownAction = null;
        private Runnable onTargetClickedAction = null;
        private View customTooltipView = null;

        public Builder(View target) {
            this.target = target;
            this.hasVirtualTarget = false;
        }

        public Builder(RectF targetRect) {
            this.target = null;
            if (targetRect != null) {
                this.targetRect = targetRect;
                this.targetLeft = targetRect.left;
                this.targetTop = targetRect.top;
                this.targetRight = targetRect.right;
                this.targetBottom = targetRect.bottom;
                this.hasVirtualTarget = true;
            }
        }

        public Builder(float left, float top, float right, float bottom) {
            this.target = null;
            this.targetLeft = left;
            this.targetTop = top;
            this.targetRight = right;
            this.targetBottom = bottom;
            this.targetRect = new RectF(left, top, right, bottom);
            this.hasVirtualTarget = true;
        }

        public Builder(float cx, float cy, float radiusPx) {
            this.target = null;
            this.targetLeft = cx - radiusPx;
            this.targetTop = cy - radiusPx;
            this.targetRight = cx + radiusPx;
            this.targetBottom = cy + radiusPx;
            this.targetRect = new RectF(targetLeft, targetTop, targetRight, targetBottom);
            this.hasVirtualTarget = true;
            this.shape = FocoraShape.CIRCLE;
        }

        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder shape(FocoraShape shape) { this.shape = shape; return this; }
        public Builder tooltipPosition(TooltipPosition position) { this.tooltipPosition = position; return this; }
        public Builder animationStyle(AnimationStyle style) { this.animationStyle = style; return this; }
        public Builder cornerRadius(float dp) { this.customCornerRadiusDp = dp; return this; }
        public Builder dismissOnTapOutside(boolean dismiss) { this.dismissOnTapOutside = dismiss; return this; }
        public Builder advanceOnTargetTap(boolean advance) { this.advanceOnTargetTap = advance; return this; }
        public Builder onTargetClicked(Runnable action) { this.onTargetClickedAction = action; return this; }
        public Builder onShown(Runnable action) { this.onStepShownAction = action; return this; }
        public Builder customTooltipView(View view) { this.customTooltipView = view; return this; }

        public FocoraStep build() {
            if (target == null && !hasVirtualTarget) {
                throw new IllegalArgumentException("FocoraStep requires either a target View or a target RectF.");
            }
            return new FocoraStep(this);
        }
    }
}