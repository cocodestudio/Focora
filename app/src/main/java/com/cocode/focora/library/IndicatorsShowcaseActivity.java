package com.cocode.focora.library;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.Focora;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.StepIndicatorStyle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * IndicatorsShowcaseActivity — two buttons each launching a 4-step tour.
 * One uses DOTS style, one uses TEXT style ("1 of 4").
 * Screenshot mid-tour (e.g., at step 2) to show each indicator style clearly.
 * Demonstrates: StepIndicatorStyle.DOTS vs StepIndicatorStyle.TEXT
 */
public class IndicatorsShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicators_showcase);

        ImageView ivTarget1 = findViewById(R.id.ivTarget1);
        CardView cardTarget2 = findViewById(R.id.cardTarget2);
        Button btnTarget3 = findViewById(R.id.btnTarget3);
        FloatingActionButton fabTarget4 = findViewById(R.id.fabTarget4);

        Button btnDots = findViewById(R.id.btnDots);
        Button btnText = findViewById(R.id.btnText);

        btnDots.setOnClickListener(v -> {
            FocoraTheme dotsTheme = new FocoraTheme.Builder()
                    .overlayColor(Color.argb(215, 0, 0, 0))
                    .tooltipBackgroundColor(Color.WHITE)
                    .titleTextColor(Color.parseColor("#1A1A1A"))
                    .descTextColor(Color.parseColor("#555555"))
                    .buttonBackgroundColor(Color.parseColor("#6200EE"))
                    .buttonTextColor(Color.WHITE)
                    .buttonCornerRadius(999f)
                    .tooltipCornerRadius(20f)
                    .showStepIndicator(true)
                    .stepIndicatorStyle(StepIndicatorStyle.DOTS)
                    .stepIndicatorColors(Color.parseColor("#6200EE"), Color.parseColor("#CCCCCC"))
                    .stepIndicatorSize(7f)
                    .showSkipButton(true)
                    .skipButtonLabel("Skip")
                    .finishButtonLabel("Done!")
                    .spotlightPadding(10f)
                    .build();

            startTour(ivTarget1, cardTarget2, btnTarget3, fabTarget4, dotsTheme);
        });

        btnText.setOnClickListener(v -> {
            FocoraTheme textTheme = new FocoraTheme.Builder()
                    .overlayColor(Color.argb(215, 0, 0, 0))
                    .tooltipBackgroundColor(Color.parseColor("#1E1E1E"))
                    .titleTextColor(Color.WHITE)
                    .descTextColor(Color.parseColor("#AAAAAA"))
                    .buttonBackgroundColor(Color.parseColor("#03DAC5"))
                    .buttonTextColor(Color.parseColor("#000000"))
                    .buttonCornerRadius(999f)
                    .tooltipCornerRadius(20f)
                    .showStepIndicator(true)
                    .stepIndicatorStyle(StepIndicatorStyle.TEXT)
                    .stepIndicatorTextFormat("Step %d of %d")
                    .showSkipButton(true)
                    .skipButtonLabel("Skip")
                    .finishButtonLabel("Done!")
                    .spotlightPadding(10f)
                    .build();

            startTour(ivTarget1, cardTarget2, btnTarget3, fabTarget4, textTheme);
        });
    }

    private void startTour(ImageView target1, CardView target2, Button target3,
                           FloatingActionButton target4, FocoraTheme theme) {
        new Focora.Builder(this)
                .theme(theme)
                .showNewStepsOnly(false)
                .animationStyle(AnimationStyle.EXPAND)

                .addStep(new FocoraStep.Builder(target1)
                        .title("Step One")
                        .description("This is the first step. Notice the progress indicator at the bottom left.")
                        .shape(FocoraShape.CIRCLE)
                        .build())

                .addStep(new FocoraStep.Builder(target2)
                        .title("Step Two")
                        .description("The indicator updates as you advance. It always shows where you are in the tour.")
                        .shape(FocoraShape.ROUNDED_RECT)
                        .cornerRadius(12f)
                        .build())

                .addStep(new FocoraStep.Builder(target3)
                        .title("Step Three")
                        .description("Almost there! The indicator is at its third position now.")
                        .shape(FocoraShape.PILL)
                        .build())

                .addStep(new FocoraStep.Builder(target4)
                        .title("Final Step")
                        .description("This is the last step. The Next button has become the Finish/Done button.")
                        .shape(FocoraShape.CIRCLE)
                        .build())

                .build()
                .start();
    }
}
