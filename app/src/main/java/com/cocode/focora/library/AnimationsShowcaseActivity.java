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
 * AnimationsShowcaseActivity — one button per AnimationStyle.
 * Tap a button → a 1-step Focora tour starts targeting the card with that animation.
 * Best captured as individual GIFs — one per animation button.
 *
 * Demonstrates: EXPAND, FADE, PULSE, SLIDE, NONE
 */
public class AnimationsShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations_showcase);

        CardView cardAnimTarget = findViewById(R.id.cardAnimTarget);

        Button btnExpand = findViewById(R.id.btnExpand);
        Button btnFade = findViewById(R.id.btnFade);
        Button btnPulse = findViewById(R.id.btnPulse);
        Button btnSlide = findViewById(R.id.btnSlide);
        Button btnNone = findViewById(R.id.btnNone);

        FocoraTheme theme = new FocoraTheme.Builder()
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

        btnExpand.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardAnimTarget)
                                .title("EXPAND Animation")
                                .description("The spotlight grows outward from the center of the target with a smooth decelerate curve.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .animationStyle(AnimationStyle.EXPAND)
                                .build())
                        .build().start()
        );

        btnFade.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardAnimTarget)
                                .title("FADE Animation")
                                .description("The spotlight fades in and out using alpha only — no resize. Clean and minimal.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .animationStyle(AnimationStyle.FADE)
                                .build())
                        .build().start()
        );

        btnPulse.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardAnimTarget)
                                .title("PULSE Animation")
                                .description("The spotlight overshoots its target size slightly, then snaps back — a subtle \"ping\" effect.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .animationStyle(AnimationStyle.PULSE)
                                .build())
                        .build().start()
        );

        btnSlide.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardAnimTarget)
                                .title("SLIDE Animation")
                                .description("The spotlight slides in from the direction of the previous step's position on screen.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .animationStyle(AnimationStyle.SLIDE)
                                .build())
                        .build().start()
        );

        btnNone.setOnClickListener(v ->
                new Focora.Builder(this)
                        .theme(theme)
                        .showNewStepsOnly(false)
                        .addStep(new FocoraStep.Builder(cardAnimTarget)
                                .title("NONE Animation")
                                .description("Instant cut with no animation. Recommended when respecting reduced motion accessibility settings.")
                                .shape(FocoraShape.ROUNDED_RECT)
                                .cornerRadius(16f)
                                .tooltipPosition(TooltipPosition.ABOVE)
                                .animationStyle(AnimationStyle.NONE)
                                .build())
                        .build().start()
        );
    }
}
