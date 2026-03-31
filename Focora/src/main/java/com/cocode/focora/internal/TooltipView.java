package com.cocode.focora.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocode.focora.FocoraTheme;
import com.cocode.focora.utils.FocoraUtils;

class TooltipView extends LinearLayout {
    private final TextView titleView;
    private final TextView descView;
    private final TextView btnNext;
    private final TextView btnSkip;
    private final LinearLayout dotContainer;
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
        titleView.setTextColor(theme.getTitleTextColor());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, theme.getTitleTextSizeSp());
        titleView.setTypeface(theme.getTitleTypeface() != null ? theme.getTitleTypeface() : Typeface.DEFAULT_BOLD);
        content.addView(titleView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        descView = new TextView(context);
        descView.setTextColor(theme.getDescTextColor());
        descView.setTextSize(TypedValue.COMPLEX_UNIT_SP, theme.getDescTextSizeSp());
        descView.setTypeface(theme.getDescTypeface() != null ? theme.getDescTypeface() : Typeface.DEFAULT);
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
        dotContainer.setVisibility(theme.isShowStepIndicator() ? VISIBLE : GONE);
        bottomRow.addView(dotContainer, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        buildDots(context, totalSteps, 0);

        btnSkip = new TextView(context);
        btnSkip.setText(theme.getSkipButtonLabel());
        btnSkip.setTextColor(theme.getDescTextColor());
        btnSkip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        btnSkip.setVisibility(theme.isShowSkipButton() ? VISIBLE : GONE);
        btnSkip.setPadding(pad / 2, pad / 2, pad / 2, pad / 2);
        btnSkip.setOnClickListener(v -> onSkip.run());
        bottomRow.addView(btnSkip);

        btnNext = new TextView(context);
        btnNext.setText(theme.getNextButtonLabel());
        btnNext.setTextColor(theme.getButtonTextColor());
        btnNext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
        btnNext.setTypeface(Typeface.DEFAULT_BOLD);
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
            }
        }
    }

    ImageView getArrowView() { return arrowView; }
}