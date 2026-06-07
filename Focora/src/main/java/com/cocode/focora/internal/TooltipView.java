package com.cocode.focora.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocode.focora.FocoraTheme;
import com.cocode.focora.StepIndicatorStyle;
import com.cocode.focora.utils.FocoraUtils;

class TooltipView extends LinearLayout {
    private final TextView titleView;
    private final TextView descView;
    private final TextView btnNext;
    private final TextView btnSkip;
    private final LinearLayout dotContainer;
    private final TextView stepTextView;
    private final ImageView arrowView;
    private final FocoraTheme theme;

    TooltipView(Context context, FocoraTheme theme, int totalSteps, Runnable onNext, Runnable onSkip) {
        super(context);
        this.theme = theme;
        setOrientation(VERTICAL);
        setClipToPadding(false);
        setClipChildren(false);

        int pad = (int) FocoraUtils.dpToPx(context, 20);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(theme.getTooltipBackgroundColor());
        bg.setCornerRadius(FocoraUtils.dpToPx(context, theme.getTooltipCornerRadiusDp()));
        setBackground(bg);

        setElevation(FocoraUtils.dpToPx(context, theme.getTooltipElevationDp()));

        arrowView = new ImageView(context);
        int arrowSizePx = (int) FocoraUtils.dpToPx(context, theme.getArrowSizeDp());
        LayoutParams arrowParams = new LayoutParams(arrowSizePx * 2, arrowSizePx);
        arrowParams.gravity = Gravity.CENTER_HORIZONTAL;
        int arrowColor = theme.getArrowColor() != 0 ? theme.getArrowColor() : theme.getTooltipBackgroundColor();
        arrowView.setImageDrawable(new ArrowDrawable(arrowColor, arrowSizePx));
        arrowView.setVisibility(theme.isShowArrow() ? VISIBLE : GONE);
        addView(arrowView, arrowParams);

        LinearLayout content = new LinearLayout(context);
        content.setOrientation(VERTICAL);
        content.setPadding(pad, pad, pad, pad);
        addView(content, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        titleView = new TextView(context);
        if (theme.getTitleTextAppearanceRes() != 0) {
            titleView.setTextAppearance(context, theme.getTitleTextAppearanceRes());
        } else {
            titleView.setTextColor(theme.getTitleTextColor());
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, theme.getTitleTextSizeSp());
        }
        if (theme.getTitleTypeface() != null) {
            titleView.setTypeface(theme.getTitleTypeface());
        } else if (theme.getTitleTextAppearanceRes() == 0) {
            titleView.setTypeface(Typeface.DEFAULT_BOLD);
        }
        content.addView(titleView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        descView = new TextView(context);
        if (theme.getDescTextAppearanceRes() != 0) {
            descView.setTextAppearance(context, theme.getDescTextAppearanceRes());
        } else {
            descView.setTextColor(theme.getDescTextColor());
            descView.setTextSize(TypedValue.COMPLEX_UNIT_SP, theme.getDescTextSizeSp());
        }
        if (theme.getDescTypeface() != null) {
            descView.setTypeface(theme.getDescTypeface());
        } else if (theme.getDescTextAppearanceRes() == 0) {
            descView.setTypeface(Typeface.DEFAULT);
        }
        int maxWidthPx = theme.getTooltipMaxWidthDp() > 0 ? (int) FocoraUtils.dpToPx(context, theme.getTooltipMaxWidthDp()) : Integer.MAX_VALUE;
        descView.setMaxWidth(maxWidthPx);
        LayoutParams descParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        descParams.topMargin = (int) FocoraUtils.dpToPx(context, 8);
        content.addView(descView, descParams);

        LinearLayout bottomRow = new LinearLayout(context);
        bottomRow.setOrientation(HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        bottomParams.topMargin = (int) FocoraUtils.dpToPx(context, 16);
        content.addView(bottomRow, bottomParams);

        dotContainer = new LinearLayout(context);
        dotContainer.setOrientation(HORIZONTAL);
        dotContainer.setGravity(Gravity.CENTER_VERTICAL);
        buildDots(context, totalSteps, 0);

        android.widget.HorizontalScrollView dotScroll = new android.widget.HorizontalScrollView(context);
        dotScroll.setHorizontalScrollBarEnabled(false);
        dotScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dotScroll.addView(dotContainer, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        stepTextView = new TextView(context);
        if (theme.getStepIndicatorTextAppearanceRes() != 0) {
            stepTextView.setTextAppearance(context, theme.getStepIndicatorTextAppearanceRes());
        } else {
            stepTextView.setTextColor(theme.getDescTextColor());
            stepTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        }
        stepTextView.setTypeface(Typeface.DEFAULT_BOLD);
        
        boolean showDots = theme.isShowStepIndicator() && theme.getStepIndicatorStyle() == StepIndicatorStyle.DOTS;
        boolean showText = theme.isShowStepIndicator() && theme.getStepIndicatorStyle() == StepIndicatorStyle.TEXT;

        dotScroll.setVisibility(showDots ? VISIBLE : GONE);
        stepTextView.setVisibility(showText ? VISIBLE : GONE);

        bottomRow.addView(dotScroll, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
        bottomRow.addView(stepTextView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));

        View spacer = new View(context);
        spacer.setVisibility(theme.isShowStepIndicator() ? GONE : VISIBLE);
        bottomRow.addView(spacer, new LayoutParams(0, 0, 1f));

        btnSkip = new TextView(context);
        btnSkip.setText(theme.getSkipButtonLabel());
        if (theme.getSkipButtonTextAppearanceRes() != 0) {
            btnSkip.setTextAppearance(context, theme.getSkipButtonTextAppearanceRes());
        } else {
            btnSkip.setTextColor(theme.getDescTextColor());
            btnSkip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        }
        if (theme.getSkipButtonTypeface() != null) {
            btnSkip.setTypeface(theme.getSkipButtonTypeface());
        }
        btnSkip.setVisibility(theme.isShowSkipButton() ? VISIBLE : GONE);
        btnSkip.setPadding(pad / 2, pad / 2, pad / 2, pad / 2);
        btnSkip.setOnClickListener(v -> onSkip.run());
        bottomRow.addView(btnSkip);

        btnNext = new TextView(context);
        btnNext.setText(theme.getNextButtonLabel());
        if (theme.getNextButtonTextAppearanceRes() != 0) {
            btnNext.setTextAppearance(context, theme.getNextButtonTextAppearanceRes());
        } else {
            btnNext.setTextColor(theme.getButtonTextColor());
            btnNext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        }
        if (theme.getNextButtonTypeface() != null) {
            btnNext.setTypeface(theme.getNextButtonTypeface());
        } else if (theme.getNextButtonTextAppearanceRes() == 0) {
            btnNext.setTypeface(Typeface.DEFAULT_BOLD);
        }
        GradientDrawable btnBg = new GradientDrawable();
        btnBg.setColor(theme.getButtonBackgroundColor());
        btnBg.setCornerRadius(FocoraUtils.dpToPx(context, theme.getButtonCornerRadiusDp()));
        btnNext.setBackground(btnBg);
        int btnPadH = (int) FocoraUtils.dpToPx(context, 16);
        int btnPadV = (int) FocoraUtils.dpToPx(context, 8);
        btnNext.setPadding(btnPadH, btnPadV, btnPadH, btnPadV);
        btnNext.setOnClickListener(v -> onNext.run());
        LayoutParams btnNextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btnNextParams.leftMargin = (int) FocoraUtils.dpToPx(context, 8);
        bottomRow.addView(btnNext, btnNextParams);
    }

    void updateStep(String title, String description, int stepIndex, int totalSteps, boolean isLast) {
        titleView.setText(title);
        descView.setText(description);
        btnNext.setText(isLast ? theme.getFinishButtonLabel() : theme.getNextButtonLabel());
        updateDots(stepIndex, totalSteps);
        
        if (theme.getStepIndicatorStyle() == StepIndicatorStyle.TEXT) {
            String text = String.format(java.util.Locale.getDefault(), theme.getStepIndicatorTextFormat(), stepIndex + 1, totalSteps);
            stepTextView.setText(text);
        }
    }

    void setNextEnabled(boolean enabled) {
        btnNext.setEnabled(enabled);
        btnNext.setAlpha(enabled ? 1f : 0.5f);
    }

    private void buildDots(Context context, int totalSteps, int activeIndex) {
        dotContainer.removeAllViews();
        float sizePx = FocoraUtils.dpToPx(context, theme.getStepIndicatorSizeDp());
        float marginPx = FocoraUtils.dpToPx(context, 3);
        for (int i = 0; i < totalSteps; i++) {
            View dot = new View(context);
            GradientDrawable dotBg = new GradientDrawable();
            dotBg.setShape(GradientDrawable.OVAL);
            dotBg.setColor(i == activeIndex ? theme.getStepIndicatorActiveColor() : theme.getStepIndicatorInactiveColor());
            dot.setBackground(dotBg);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams((int) sizePx, (int) sizePx);
            p.setMargins((int) marginPx, 0, (int) marginPx, 0);
            dotContainer.addView(dot, p);
        }
    }

    private void updateDots(int activeIndex, int totalSteps) {
        for (int i = 0; i < dotContainer.getChildCount(); i++) {
            View dot = dotContainer.getChildAt(i);
            boolean isActive = i == activeIndex;
            if (dot.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) dot.getBackground()).setColor(
                        isActive ? theme.getStepIndicatorActiveColor() : theme.getStepIndicatorInactiveColor()
                );
            }
            if (isActive) {
                dot.animate().scaleX(1.4f).scaleY(1.4f).setDuration(100).withEndAction(() ->
                        dot.animate().scaleX(1f).scaleY(1f).setDuration(100).start()).start();

                if (dotContainer.getParent() instanceof android.widget.HorizontalScrollView) {
                    android.widget.HorizontalScrollView hsv = (android.widget.HorizontalScrollView) dotContainer.getParent();
                    hsv.post(() -> {
                        int dotLeft = dot.getLeft();
                        int dotRight = dot.getRight();
                        int scrollX = hsv.getScrollX();
                        int hsvWidth = hsv.getWidth();
                        if (dotLeft < scrollX || dotRight > scrollX + hsvWidth) {
                            hsv.smoothScrollTo(dotLeft - hsvWidth / 2 + dot.getWidth() / 2, 0);
                        }
                    });
                }
            }
        }
    }

    ImageView getArrowView() { return arrowView; }
}