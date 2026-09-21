package com.cocode.focora.library;

import com.cocode.focora.AnimationStyle;
import com.cocode.focora.Focora;
import com.cocode.focora.FocoraShape;
import com.cocode.focora.FocoraStep;
import com.cocode.focora.FocoraTheme;
import com.cocode.focora.StepIndicatorStyle;
import com.cocode.focora.TooltipPosition;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class FocoraApiTest {

    @Test(expected = IllegalStateException.class)
    public void testFocoraBuilderThrowsOnEmptySteps() {
        new Focora.Builder(null).build();
    }

    @Test
    public void testFocoraStepVirtualRectBuilder() {
        AtomicBoolean targetClicked = new AtomicBoolean(false);

        FocoraStep step = new FocoraStep.Builder(100f, 200f, 300f, 400f)
                .title("Virtual Target")
                .description("Targeting without a physical View")
                .shape(FocoraShape.CIRCLE)
                .tooltipPosition(TooltipPosition.BELOW)
                .animationStyle(AnimationStyle.FADE)
                .advanceOnTargetTap(true)
                .onTargetClicked(() -> targetClicked.set(true))
                .build();

        assertNull(step.getTarget());
        assertTrue(step.hasVirtualTarget());
        assertEquals("Virtual Target", step.getTitle());
        assertEquals("Targeting without a physical View", step.getDescription());
        assertEquals(FocoraShape.CIRCLE, step.getShape());
        assertEquals(TooltipPosition.BELOW, step.getTooltipPosition());
        assertEquals(AnimationStyle.FADE, step.getAnimationStyle());
        assertTrue(step.isAdvanceOnTargetTap());

        assertNotNull(step.getOnTargetClickedAction());
        step.getOnTargetClickedAction().run();
        assertTrue(targetClicked.get());

        assertEquals(100f, step.getVirtualLeft(), 0.001f);
        assertEquals(200f, step.getVirtualTop(), 0.001f);
        assertEquals(300f, step.getVirtualRight(), 0.001f);
        assertEquals(400f, step.getVirtualBottom(), 0.001f);
    }

    @Test
    public void testFocoraStepCircularCoordinatesBuilder() {
        FocoraStep step = new FocoraStep.Builder(150f, 250f, 50f)
                .title("Circle Coordinate")
                .build();

        assertTrue(step.hasVirtualTarget());
        assertEquals(FocoraShape.CIRCLE, step.getShape());
        assertEquals(100f, step.getVirtualLeft(), 0.001f);
        assertEquals(200f, step.getVirtualTop(), 0.001f);
        assertEquals(200f, step.getVirtualRight(), 0.001f);
        assertEquals(300f, step.getVirtualBottom(), 0.001f);
    }

    @Test
    public void testFocoraThemePulseRings() {
        FocoraTheme theme = new FocoraTheme.Builder()
                .pulseRings(true)
                .pulseRingColor(0xFF0000FF)
                .pulseRingMaxRadius(24f)
                .stepIndicatorStyle(StepIndicatorStyle.TEXT)
                .build();

        assertTrue(theme.isPulseRingsEnabled());
        assertEquals(0xFF0000FF, theme.getPulseRingColor());
        assertEquals(24f, theme.getPulseRingMaxRadiusDp(), 0.001f);
        assertEquals(StepIndicatorStyle.TEXT, theme.getStepIndicatorStyle());
    }

    @Test
    public void testFocoraThemeDefaultValues() {
        FocoraTheme lightTheme = FocoraTheme.defaultLight();
        assertNotNull(lightTheme);
        assertTrue(lightTheme.isShowArrow());
        assertTrue(lightTheme.isShowSkipButton());
        assertTrue(lightTheme.isShowStepIndicator());
        assertEquals(StepIndicatorStyle.DOTS, lightTheme.getStepIndicatorStyle());

        FocoraTheme darkTheme = FocoraTheme.defaultDark();
        assertNotNull(darkTheme);
        assertEquals(0xFFFFFFFF, darkTheme.getTitleTextColor());
    }

    @Test
    public void testEnumsCompleteness() {
        assertEquals(4, FocoraShape.values().length);
        assertEquals(5, TooltipPosition.values().length);
        assertEquals(5, AnimationStyle.values().length);
        assertEquals(2, StepIndicatorStyle.values().length);
    }
}
