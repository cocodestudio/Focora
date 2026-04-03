package com.cocode.focora;

import android.graphics.Color;
import android.graphics.Typeface;

public final class FocoraTheme {
    private final int overlayColor;
    private final int spotlightBorderColor;
    private final float spotlightBorderWidth;
    private final float spotlightPaddingDp;
    private final int tooltipBackgroundColor;
    private final float tooltipCornerRadiusDp;
    private final float tooltipElevationDp;
    private final int tooltipMaxWidthDp;
    private final int titleTextColor;
    private final float titleTextSizeSp;
    private final int descTextColor;
    private final float descTextSizeSp;
    private final Typeface titleTypeface;
    private final Typeface descTypeface;
    private final int buttonBackgroundColor;
    private final int buttonTextColor;
    private final float buttonCornerRadiusDp;
    private final String nextButtonLabel;
    private final String finishButtonLabel;
    private final boolean showSkipButton;
    private final String skipButtonLabel;
    private final boolean showArrow;
    private final int arrowColor;
    private final float arrowSizeDp;
    private final boolean showStepIndicator;
    private final int stepIndicatorActiveColor;
    private final int stepIndicatorInactiveColor;
    private final float stepIndicatorSizeDp;
    private final boolean respectReducedMotion;

    private FocoraTheme(Builder b) {
        this.overlayColor = b.overlayColor;
        this.spotlightBorderColor = b.spotlightBorderColor;
        this.spotlightBorderWidth = b.spotlightBorderWidth;
        this.spotlightPaddingDp = b.spotlightPaddingDp;
        this.tooltipBackgroundColor = b.tooltipBackgroundColor;
        this.tooltipCornerRadiusDp = b.tooltipCornerRadiusDp;
        this.tooltipElevationDp = b.tooltipElevationDp;
        this.tooltipMaxWidthDp = b.tooltipMaxWidthDp;
        this.titleTextColor = b.titleTextColor;
        this.titleTextSizeSp = b.titleTextSizeSp;
        this.descTextColor = b.descTextColor;
        this.descTextSizeSp = b.descTextSizeSp;
        this.titleTypeface = b.titleTypeface;
        this.descTypeface = b.descTypeface;
        this.buttonBackgroundColor = b.buttonBackgroundColor;
        this.buttonTextColor = b.buttonTextColor;
        this.buttonCornerRadiusDp = b.buttonCornerRadiusDp;
        this.nextButtonLabel = b.nextButtonLabel;
        this.finishButtonLabel = b.finishButtonLabel;
        this.showSkipButton = b.showSkipButton;
        this.skipButtonLabel = b.skipButtonLabel;
        this.showArrow = b.showArrow;
        this.arrowColor = b.arrowColor;
        this.arrowSizeDp = b.arrowSizeDp;
        this.showStepIndicator = b.showStepIndicator;
        this.stepIndicatorActiveColor = b.stepIndicatorActiveColor;
        this.stepIndicatorInactiveColor = b.stepIndicatorInactiveColor;
        this.stepIndicatorSizeDp = b.stepIndicatorSizeDp;
        this.respectReducedMotion = b.respectReducedMotion;
    }

    public int getOverlayColor() { return overlayColor; }
    public int getSpotlightBorderColor() { return spotlightBorderColor; }
    public float getSpotlightBorderWidth() { return spotlightBorderWidth; }
    public float getSpotlightPaddingDp() { return spotlightPaddingDp; }
    public int getTooltipBackgroundColor() { return tooltipBackgroundColor; }
    public float getTooltipCornerRadiusDp() { return tooltipCornerRadiusDp; }
    public float getTooltipElevationDp() { return tooltipElevationDp; }
    public int getTooltipMaxWidthDp() { return tooltipMaxWidthDp; }
    public int getTitleTextColor() { return titleTextColor; }
    public float getTitleTextSizeSp() { return titleTextSizeSp; }
    public int getDescTextColor() { return descTextColor; }
    public float getDescTextSizeSp() { return descTextSizeSp; }
    public Typeface getTitleTypeface() { return titleTypeface; }
    public Typeface getDescTypeface() { return descTypeface; }
    public int getButtonBackgroundColor() { return buttonBackgroundColor; }
    public int getButtonTextColor() { return buttonTextColor; }
    public float getButtonCornerRadiusDp() { return buttonCornerRadiusDp; }
    public String getNextButtonLabel() { return nextButtonLabel; }
    public String getFinishButtonLabel() { return finishButtonLabel; }
    public boolean isShowSkipButton() { return showSkipButton; }
    public String getSkipButtonLabel() { return skipButtonLabel; }
    public boolean isShowArrow() { return showArrow; }
    public int getArrowColor() { return arrowColor; }
    public float getArrowSizeDp() { return arrowSizeDp; }
    public boolean isShowStepIndicator() { return showStepIndicator; }
    public int getStepIndicatorActiveColor() { return stepIndicatorActiveColor; }
    public int getStepIndicatorInactiveColor() { return stepIndicatorInactiveColor; }
    public float getStepIndicatorSizeDp() { return stepIndicatorSizeDp; }
    public boolean isRespectReducedMotion() { return respectReducedMotion; }

    public static FocoraTheme defaultLight() { return new Builder().build(); }
    public static FocoraTheme defaultDark() {
        return new Builder()
                .overlayColor(Color.argb(180, 0, 0, 0))
                .tooltipBackgroundColor(Color.parseColor("#1E1E1E"))
                .titleTextColor(Color.WHITE)
                .descTextColor(Color.parseColor("#AAAAAA"))
                .buttonBackgroundColor(Color.parseColor("#BB86FC"))
                .buttonTextColor(Color.BLACK)
                .build();
    }

    public static final class Builder {
        private int overlayColor = Color.argb(204, 0, 0, 0);
        private int spotlightBorderColor = 0;
        private float spotlightBorderWidth = 0f;
        private float spotlightPaddingDp = 8f;
        private int tooltipBackgroundColor = Color.WHITE;
        private float tooltipCornerRadiusDp = 20f;
        private float tooltipElevationDp = 8f;
        private int tooltipMaxWidthDp = 280;
        private int titleTextColor = Color.parseColor("#1A1A1A");
        private float titleTextSizeSp = 16f;
        private int descTextColor = Color.parseColor("#666666");
        private float descTextSizeSp = 13f;
        private Typeface titleTypeface = null;
        private Typeface descTypeface = null;
        private int buttonBackgroundColor = Color.parseColor("#6200EE");
        private int buttonTextColor = Color.WHITE;
        private float buttonCornerRadiusDp = 999f;
        private String nextButtonLabel = "Next";
        private String finishButtonLabel = "Got it";
        private boolean showSkipButton = true;
        private String skipButtonLabel = "Skip";
        private boolean showArrow = true;
        private int arrowColor = 0;
        private float arrowSizeDp = 10f;
        private boolean showStepIndicator = true;
        private int stepIndicatorActiveColor = Color.parseColor("#6200EE");
        private int stepIndicatorInactiveColor = Color.parseColor("#CCCCCC");
        private float stepIndicatorSizeDp = 6f;
        private boolean respectReducedMotion = true;

        public Builder overlayColor(int color) { this.overlayColor = color; return this; }
        public Builder spotlightBorder(int color, float widthPx) { this.spotlightBorderColor = color; this.spotlightBorderWidth = widthPx; return this; }
        public Builder spotlightPadding(float dp) { this.spotlightPaddingDp = dp; return this; }
        public Builder tooltipBackgroundColor(int color) { this.tooltipBackgroundColor = color; return this; }
        public Builder tooltipCornerRadius(float dp) { this.tooltipCornerRadiusDp = dp; return this; }
        public Builder tooltipElevation(float dp) { this.tooltipElevationDp = dp; return this; }
        public Builder tooltipMaxWidth(int dp) { this.tooltipMaxWidthDp = dp; return this; }
        public Builder titleTextColor(int color) { this.titleTextColor = color; return this; }
        public Builder titleTextSize(float sp) { this.titleTextSizeSp = sp; return this; }
        public Builder descTextColor(int color) { this.descTextColor = color; return this; }
        public Builder descTextSize(float sp) { this.descTextSizeSp = sp; return this; }
        public Builder titleTypeface(Typeface typeface) { this.titleTypeface = typeface; return this; }
        public Builder descTypeface(Typeface typeface) { this.descTypeface = typeface; return this; }
        public Builder buttonBackgroundColor(int color) { this.buttonBackgroundColor = color; return this; }
        public Builder buttonTextColor(int color) { this.buttonTextColor = color; return this; }
        public Builder buttonCornerRadius(float dp) { this.buttonCornerRadiusDp = dp; return this; }
        public Builder nextButtonLabel(String label) { this.nextButtonLabel = label; return this; }
        public Builder finishButtonLabel(String label) { this.finishButtonLabel = label; return this; }
        public Builder showSkipButton(boolean show) { this.showSkipButton = show; return this; }
        public Builder skipButtonLabel(String label) { this.skipButtonLabel = label; return this; }
        public Builder showArrow(boolean show) { this.showArrow = show; return this; }
        public Builder arrowColor(int color) { this.arrowColor = color; return this; }
        public Builder arrowSize(float dp) { this.arrowSizeDp = dp; return this; }
        public Builder showStepIndicator(boolean show) { this.showStepIndicator = show; return this; }
        public Builder stepIndicatorColors(int active, int inactive) { this.stepIndicatorActiveColor = active; this.stepIndicatorInactiveColor = inactive; return this; }
        public Builder stepIndicatorSize(float dp) { this.stepIndicatorSizeDp = dp; return this; }
        public Builder respectReducedMotion(boolean respect) { this.respectReducedMotion = respect; return this; }
        public FocoraTheme build() { return new FocoraTheme(this); }
    }
}