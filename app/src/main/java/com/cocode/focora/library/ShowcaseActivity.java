package com.cocode.focora.library;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.Focora;
import com.cocode.focora.FocoraListener;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * ShowcaseActivity — Full featured 4-step demo.
 * Best for the hero GIF / screen recording at the top of the README.
 *
 * Demonstrates:
 *  - Step 1: CIRCLE shape + FADE animation  (avatar)
 *  - Step 2: ROUNDED_RECT shape + EXPAND animation  (stats card)
 *  - Step 3: PILL shape + PULSE animation  (upgrade button)
 *  - Step 4: CIRCLE shape + SLIDE animation  (FAB)
 */
public class ShowcaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);

        ImageView ivAvatar = findViewById(R.id.ivAvatar);
        CardView cardStats = findViewById(R.id.cardStats);
        Button btnUpgrade = findViewById(R.id.btnUpgrade);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        FocoraTheme theme = new FocoraTheme.Builder()
                .overlayColor(Color.argb(220, 0, 0, 0))
                .tooltipBackgroundColor(Color.WHITE)
                .titleTextColor(Color.parseColor("#1A1A1A"))
                .descTextColor(Color.parseColor("#555555"))
                .buttonBackgroundColor(Color.parseColor("#6200EE"))
                .buttonTextColor(Color.WHITE)
                .buttonCornerRadius(999f)
                .tooltipCornerRadius(20f)
                .tooltipMaxWidth(280)
                .showStepIndicator(true)
                .stepIndicatorColors(Color.parseColor("#6200EE"), Color.parseColor("#CCCCCC"))
                .arrowColor(Color.WHITE)
                .showSkipButton(true)
                .skipButtonLabel("Skip tour")
                .nextButtonLabel("Next")
                .finishButtonLabel("Got it!")
                .spotlightPadding(10f)
                .build();

        new Focora.Builder(this)
                .theme(theme)
                .startDelay(400)
                .dismissOnBackPress(false)
                .dismissOnTapOutside(false)
                .showNewStepsOnly(false)

                .addStep(new FocoraStep.Builder(ivAvatar)
                        .title("Your Profile")
                        .description("Manage your account settings, update your picture, and view your developer stats.")
                        .shape(FocoraShape.CIRCLE)
                        .animationStyle(AnimationStyle.FADE)
                        .build())

                .addStep(new FocoraStep.Builder(cardStats)
                        .title("Track Your Progress")
                        .description("Keep an eye on your weekly goals. This card updates in real-time as you complete tasks.")
                        .shape(FocoraShape.ROUNDED_RECT)
                        .cornerRadius(16f)
                        .animationStyle(AnimationStyle.EXPAND)
                        .build())

                .addStep(new FocoraStep.Builder(btnUpgrade)
                        .title("Unlock Premium")
                        .description("Get access to advanced features, themes, and priority support by upgrading to Pro.")
                        .shape(FocoraShape.PILL)
                        .animationStyle(AnimationStyle.PULSE)
                        .build())

                .addStep(new FocoraStep.Builder(fabAdd)
                        .title("Create New")
                        .description("Tap here whenever you're ready to start a brand new project from scratch.")
                        .shape(FocoraShape.CIRCLE)
                        .animationStyle(AnimationStyle.SLIDE)
                        .build())

                .listener(new FocoraListener.Adapter() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(ShowcaseActivity.this, "✅ Tutorial complete!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSkipped(int stepIndex) {
                        Toast.makeText(ShowcaseActivity.this, "Skipped at step " + (stepIndex + 1), Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .start();
    }
}
