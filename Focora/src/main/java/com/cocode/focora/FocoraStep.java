package com.cocode.focora;

import android.view.View;

public final class FocoraStep {
    private final View target;
    private final String title;
    private final String description;
    private final FocoraShape shape;
    private final TooltipPosition tooltipPosition;
    private final AnimationStyle animationStyle;
    private final float customCornerRadiusDp;
    private final boolean dismissOnTapOutside;
    private final Runnable onStepShownAction;
    private final View customTooltipView;

    private FocoraStep(Builder b) {
        this.target = b.target;
        this.title = b.title;
        this.description = b.description;
        this.shape = b.shape;
        this.tooltipPosition = b.tooltipPosition;
        this.animationStyle = b.animationStyle;
        this.customCornerRadiusDp = b.customCornerRadiusDp;
        this.dismissOnTapOutside = b.dismissOnTapOutside;
        this.onStepShownAction = b.onStepShownAction;
        this.customTooltipView = b.customTooltipView;
    }

    public View getTarget() { return target; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public FocoraShape getShape() { return shape; }
    public TooltipPosition getTooltipPosition() { return tooltipPosition; }
    public AnimationStyle getAnimationStyle() { return animationStyle; }
    public float getCustomCornerRadiusDp() { return customCornerRadiusDp; }
    public boolean isDismissOnTapOutside() { return dismissOnTapOutside; }
    public Runnable getOnStepShownAction() { return onStepShownAction; }
    public View getCustomTooltipView() { return customTooltipView; }

    public static final class Builder {
        private final View target;
        private String title = "";
        private String description = "";
        private FocoraShape shape = FocoraShape.ROUNDED_RECT;
        private TooltipPosition tooltipPosition = TooltipPosition.AUTO;
        private AnimationStyle animationStyle = AnimationStyle.EXPAND;
        private float customCornerRadiusDp = 12f;
        private boolean dismissOnTapOutside = false;
        private Runnable onStepShownAction = null;
        private View customTooltipView = null;

        public Builder(View target) { this.target = target; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder shape(FocoraShape shape) { this.shape = shape; return this; }
        public Builder tooltipPosition(TooltipPosition position) { this.tooltipPosition = position; return this; }
        public Builder animationStyle(AnimationStyle style) { this.animationStyle = style; return this; }
        public Builder cornerRadius(float dp) { this.customCornerRadiusDp = dp; return this; }
        public Builder dismissOnTapOutside(boolean dismiss) { this.dismissOnTapOutside = dismiss; return this; }
        public Builder onShown(Runnable action) { this.onStepShownAction = action; return this; }
        public Builder customTooltipView(View view) { this.customTooltipView = view; return this; }
        public FocoraStep build() { return new FocoraStep(this); }
    }
}