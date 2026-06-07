package com.cocode.focora.library;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DemoMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_menu);

        CardView cardShowcase = findViewById(R.id.cardShowcase);
        CardView cardShapes = findViewById(R.id.cardShapes);
        CardView cardAnimations = findViewById(R.id.cardAnimations);
        CardView cardPositions = findViewById(R.id.cardPositions);
        CardView cardThemes = findViewById(R.id.cardThemes);
        CardView cardIndicators = findViewById(R.id.cardIndicators);

        cardShowcase.setOnClickListener(v ->
                startActivity(new Intent(this, ShowcaseActivity.class)));

        cardShapes.setOnClickListener(v ->
                startActivity(new Intent(this, ShapesShowcaseActivity.class)));

        cardAnimations.setOnClickListener(v ->
                startActivity(new Intent(this, AnimationsShowcaseActivity.class)));

        cardPositions.setOnClickListener(v ->
                startActivity(new Intent(this, PositionsShowcaseActivity.class)));

        cardThemes.setOnClickListener(v ->
                startActivity(new Intent(this, ThemesShowcaseActivity.class)));

        cardIndicators.setOnClickListener(v ->
                startActivity(new Intent(this, IndicatorsShowcaseActivity.class)));
    }
}
