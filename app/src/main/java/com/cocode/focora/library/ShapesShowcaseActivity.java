package com.cocode.focora.library;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cocode.focora.Focora;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.TooltipPosition;

/**
 * ShapesShowcaseActivity — one button per shape.
 * Tap a shape button → a 1-step Focora tour appears highlighting the target
 * with that specific spotlight shape. Take a screenshot for each.
 *
 * Demonstrates: CIRCLE, ROUNDED_RECT, PILL, RECT
 */
public class ShapesShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapes_showcase);

        ImageView ivCircleTarget = findViewById(R.id.ivCircleTarget);
        CardView cardRectTarget = findViewById(R.id.cardRectTarget);
        Button btnPillTarget = findViewById(R.id.btnPillTarget);
        ImageView ivRectTarget = findViewById(R.id.ivRectTarget);

        Button btnCircle = findViewById(R.id.btnCircle);
        Button btnRoundedRect = findViewById(R.id.btnRoundedRect);
        Button btnPill = findViewById(R.id.btnPill);
        Button btnRect = findViewById(R.id.btnRect);

        FocoraTheme theme = buildTheme();

        btnCircle.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(ivCircleTarget)
                                .title("CIRCLE Spotlight")
                                .description("A perfect circle that encloses the target. Ideal for avatar images, icon buttons, and FABs.")
                                .shape(FocoraShape.CIRCLE)
                                .tooltipPosition(TooltipPosition.BELOW)
                                .build())
                        .build()
                        .start()
        );

        btnRoundedRect.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardRectTarget)
                                .title("ROUNDED_RECT Spotlight")
                                .description("A rounded rectangle that matches your card or view corners. Corner radius is fully configurable.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.BELOW)
                                .build())
                        .build()
                        .start()
        );

        btnPill.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(btnPillTarget)
                                .title("PILL Spotlight")
                                .description("Fully rounded on the short sides — perfect for wide pill-shaped buttons and tab bar items.")
                                .shape(FocoraShape.PILL)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .build())
                        .build()
                        .start()
        );

        btnRect.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(ivRectTarget)
                                .title("RECT Spotlight")
                                .description("A sharp rectangle with zero corner radius. Best for image previews, grids, or full-bleed banners.")
                                .shape(FocoraShape.RECT)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .build())
                        .build()
                        .start()
        );
    }

    private FocoraTheme buildTheme() {
        return new FocoraTheme.Builder()
                .overlayColor(Color.argb(210, 0, 0, 0))
                .tooltipBackgroundColor(Color.WHITE)
                .titleTextColor(Color.parseColor("#1A1A1A"))
                .descTextColor(Color.parseColor("#555555"))
                .buttonBackgroundColor(Color.parseColor("#6200EE"))
                .buttonTextColor(Color.WHITE)
                .buttonCornerRadius(999f)
                .tooltipCornerRadius(20f)
                .showStepIndicator(false)
                .showSkipButton(false)
                .finishButtonLabel("Got it!")
                .spotlightPadding(8f)
                .build();
    }
}
