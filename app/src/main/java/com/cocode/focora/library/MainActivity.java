package com.cocode.focora.library;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.Focora;
import com.cocode.focora.FocoraListener;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.FocoraShape;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Focora focoraTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.cocode.focora.library.R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Find our views
        ImageView ivSettings = findViewById(R.id.ivSettings);
        ImageView ivAvatar = findViewById(R.id.ivAvatar);
        CardView cardStats = findViewById(R.id.cardStats);
        Button btnUpgrade = findViewById(R.id.btnUpgrade);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        Button btnStartTour = findViewById(R.id.btnStartTour);

        // 2. Build a premium custom theme to show off the customization
        FocoraTheme premiumDarkTheme = new FocoraTheme.Builder()
                .overlayColor(Color.argb(220, 0, 0, 0)) // Black with 86% opacity
                .tooltipBackgroundColor(Color.parseColor("#FFFFFF"))
                .titleTextColor(Color.BLACK)
                .descTextColor(Color.BLACK)
                .buttonBackgroundColor(Color.BLACK)
                .buttonTextColor(Color.WHITE)
                .buttonCornerRadius(50f)
                .tooltipCornerRadius(24f)
                .spotlightPadding(10f)
//                .spotlightBorder(Color.parseColor("#3B82F6"), 4f) // Add a glowing blue border to the cutout
                .showStepIndicator(true)
                .stepIndicatorColors(Color.BLACK, Color.GRAY)
                .arrowColor(Color.BLACK)
                .build();

        // 3. Set up the tutorial sequence
        focoraTutorial = new Focora.Builder(this)
                .theme(premiumDarkTheme)
                .tutorialKey("github_showcase_v1")
                .resetOnStart(false) // Forces it to show every time for easy screen recording
                .startDelay(500) // Wait half a second after screen loads before popping up
                .dismissOnTapOutside(false)
                .dismissOnBackPress(false)

                // STEP 1: Show off the CIRCLE shape and FADE animation
                .addStep(new FocoraStep.Builder(ivAvatar)
                        .title("Your Profile")
                        .description("Manage your account settings, update your picture, and view your developer stats.")
                        .shape(FocoraShape.CIRCLE)
                        .animationStyle(AnimationStyle.FADE)
                        .build())

                // STEP 2: Show off the ROUNDED_RECT shape with a custom corner radius
                .addStep(new FocoraStep.Builder(cardStats)
                        .title("Track Progress")
                        .description("Keep an eye on your weekly goals. This card updates in real-time.")
                        .shape(FocoraShape.ROUNDED_RECT)
                        .cornerRadius(16f) // Matches the exact corner radius of our CardView
                        .animationStyle(AnimationStyle.EXPAND)
                        .build())

                // STEP 3: Show off the PILL shape and PULSE animation
                .addStep(new FocoraStep.Builder(btnUpgrade)
                        .title("Unlock Premium")
                        .description("Get access to advanced features and priority support by upgrading to Pro.")
                        .shape(FocoraShape.PILL)
                        .animationStyle(AnimationStyle.EXPAND) // Draws attention to the CTA
                        .build())

                // STEP 4: Small target, auto-positioning, showing the Finish button
                .addStep(new FocoraStep.Builder(fabAdd)
                        .title("Create New")
                        .description("Tap here whenever you're ready to start a brand new project.")
                        .shape(FocoraShape.CIRCLE)
                        .animationStyle(AnimationStyle.EXPAND)
                        .build())

                // Add a listener to trigger a toast when the tutorial ends
                .listener(new FocoraListener.Adapter() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "Tutorial Completed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSkipped(int stepIndex) {
                        Toast.makeText(MainActivity.this, "Tutorial Skipped at step " + (stepIndex + 1), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        // 4. Start the tutorial automatically when the activity opens
        focoraTutorial.start();

//        btnStartTour.setOnClickListener(v -> {
//            if (!focoraTutorial.isRunning()) {
//                focoraTutorial.start();
//            }
//        });
    }
}