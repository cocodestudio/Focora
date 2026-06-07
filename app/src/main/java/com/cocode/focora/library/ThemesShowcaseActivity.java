package com.cocode.focora.library;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.Focora;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.TooltipPosition;

/**
 * ThemesShowcaseActivity — three buttons each launching the same tour with a different theme.
 * Screenshot after tapping each to show the theme difference.
 *
 * Demonstrates: defaultLight(), defaultDark(), custom theme
 */
public class ThemesShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes_showcase);

        CardView cardTarget = findViewById(R.id.cardTarget);
        Button btnLightTheme = findViewById(R.id.btnLightTheme);
        Button btnDarkTheme = findViewById(R.id.btnDarkTheme);
        Button btnCustomTheme = findViewById(R.id.btnCustomTheme);

        btnLightTheme.setOnClickListener(v -> startTour(cardTarget, FocoraTheme.defaultLight()));

        btnDarkTheme.setOnClickListener(v -> startTour(cardTarget, FocoraTheme.defaultDark()));

        btnCustomTheme.setOnClickListener(v -> {
            FocoraTheme customTheme = new FocoraTheme.Builder()
                    .overlayColor(Color.argb(200, 0, 0, 20))
                    .tooltipBackgroundColor(Color.parseColor("#1A0033"))
                    .titleTextColor(Color.parseColor("#BB86FC"))
                    .descTextColor(Color.parseColor("#CCCCCC"))
                    .buttonBackgroundColor(Color.parseColor("#03DAC5"))
                    .buttonTextColor(Color.parseColor("#000000"))
                    .buttonCornerRadius(999f)
                    .tooltipCornerRadius(24f)
                    .spotlightBorder(Color.parseColor("#BB86FC"), 3f)
                    .spotlightPadding(12f)
                    .showStepIndicator(true)
                    .stepIndicatorColors(Color.parseColor("#BB86FC"), Color.parseColor("#444444"))
                    .arrowColor(Color.parseColor("#1A0033"))
                    .showSkipButton(true)
                    .skipButtonLabel("Maybe later")
                    .finishButtonLabel("Awesome!")
                    .build();
            startTour(cardTarget, customTheme);
        });
    }

    private void startTour(CardView cardTarget, FocoraTheme theme) {
        new Focora.Builder(this)
                .theme(theme)
                .showNewStepsOnly(false)
                .animationStyle(AnimationStyle.FADE)
                .addStep(new FocoraStep.Builder(cardTarget)
                        .title("Feature Highlight")
                        .description("This is how your tooltip looks with this theme. Every color, font, and style is fully configurable.")
                        .shape(FocoraShape.ROUNDED_RECT)
                        .cornerRadius(16f)
                        .tooltipPosition(TooltipPosition.BELOW)
                        .build())
                .build()
                .start();
    }
}
