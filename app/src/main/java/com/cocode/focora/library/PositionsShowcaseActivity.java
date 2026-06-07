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
 * PositionsShowcaseActivity — walks through all 4 explicit tooltip positions
 * plus AUTO in a single 5-step tour.
 *
 * Targets are spread across the screen so the tooltip is forced to each position.
 * Best captured as a full screen recording walking through each step.
 *
 * Demonstrates: ABOVE, BELOW, LEFT, RIGHT, AUTO
 */
public class PositionsShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions_showcase);

        Button btnTopLeft = findViewById(R.id.btnTopLeft);
        Button btnTopRight = findViewById(R.id.btnTopRight);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);
        Button btnBottomLeft = findViewById(R.id.btnBottomLeft);
        Button btnBottomRight = findViewById(R.id.btnBottomRight);
        CardView cardCenter = findViewById(R.id.cardCenter);
        Button btnStartTour = findViewById(R.id.btnStartTour);

        FocoraTheme theme = new FocoraTheme.Builder()
                .overlayColor(Color.argb(200, 0, 0, 0))
                .tooltipBackgroundColor(Color.WHITE)
                .titleTextColor(Color.parseColor("#1A1A1A"))
                .descTextColor(Color.parseColor("#555555"))
                .buttonBackgroundColor(Color.parseColor("#6200EE"))
                .buttonTextColor(Color.WHITE)
                .buttonCornerRadius(999f)
                .tooltipCornerRadius(20f)
                .tooltipMaxWidth(240)
                .showStepIndicator(true)
                .stepIndicatorColors(Color.parseColor("#6200EE"), Color.parseColor("#CCCCCC"))
                .showSkipButton(false)
                .finishButtonLabel("Done!")
                .spotlightPadding(8f)
                .build();

        btnStartTour.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .animationStyle(AnimationStyle.EXPAND)

                        .addStep(new FocoraStep.Builder(btnTopLeft)
                                .title("BELOW position")
                                .description("Target is at the top — tooltip is placed below the spotlight.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.BELOW)
                                .build())

                        .addStep(new FocoraStep.Builder(btnTopRight)
                                .title("BELOW position")
                                .description("Same logic for a top-right target. The tooltip still appears below.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.BELOW)
                                .build())

                        .addStep(new FocoraStep.Builder(btnLeft)
                                .title("RIGHT position")
                                .description("Target is on the left edge — tooltip is placed to its right.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.RIGHT)
                                .build())

                        .addStep(new FocoraStep.Builder(btnRight)
                                .title("LEFT position")
                                .description("Target is on the right edge — tooltip is placed to its left.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.LEFT)
                                .build())

                        .addStep(new FocoraStep.Builder(btnBottomLeft)
                                .title("ABOVE position")
                                .description("Target is near the bottom — tooltip is placed above the spotlight.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .build())

                        .addStep(new FocoraStep.Builder(btnBottomRight)
                                .title("AUTO position")
                                .description("Focora automatically picks the best position based on available screen space.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.AUTO)
                                .build())

                        .build()
                        .start()
        );
    }
}
