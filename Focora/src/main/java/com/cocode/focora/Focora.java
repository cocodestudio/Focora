package com.cocode.focora;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.cocode.focora.internal.FocoraOverlayView;
import com.cocode.focora.internal.FocoraPrefs;

import java.util.ArrayList;
import java.util.List;

public final class Focora {
    private final Activity activity;
    private final List<FocoraStep> steps;
    private final FocoraTheme theme;
    private final String tutorialKey;
    private final boolean resetOnStart;
    private final boolean dismissOnBackPress;
    private final boolean dismissOnTapOutside;
    private final long startDelayMs;
    private final FocoraListener listener;
    private final LifecycleOwner lifecycleOwner;
    private final AnimationStyle globalAnimationStyle;

    private int currentStepIndex = 0;
    private FocoraOverlayView overlayView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;

    private Focora(Builder b) {
        this.activity = b.activity;
        this.steps = new ArrayList<>(b.steps);
        this.theme = b.theme != null ? b.theme : FocoraTheme.defaultLight();
        this.tutorialKey = b.tutorialKey;
        this.resetOnStart = b.resetOnStart;
        this.dismissOnBackPress = b.dismissOnBackPress;
        this.dismissOnTapOutside = b.dismissOnTapOutside;
        this.startDelayMs = b.startDelayMs;
        this.listener = b.listener;
        this.lifecycleOwner = b.lifecycleOwner;
        this.globalAnimationStyle = b.globalAnimationStyle;
    }

    public void start() {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;
        if (steps.isEmpty()) return;

        if (tutorialKey != null && resetOnStart) {
            FocoraPrefs.reset(activity, tutorialKey);
        }

        if (tutorialKey != null && FocoraPrefs.hasCompleted(activity, tutorialKey)) return;

        if (lifecycleOwner != null) attachLifecycle();

        if (startDelayMs > 0) {
            mainHandler.postDelayed(this::beginFirstStep, startDelayMs);
        } else {
            beginFirstStep();
        }
    }

    public void next() {
        if (!isRunning || overlayView == null) return;
        overlayView.requestNextStep();
    }

    public void goToStep(int index) {
        if (!isRunning || index < 0 || index >= steps.size()) return;
        currentStepIndex = index;
        showStep(currentStepIndex);
    }

    public void dismiss() {
        if (isRunning) completeSession();
    }

    public static void reset(@NonNull Activity context, @NonNull String tutorialKey) {
        FocoraPrefs.reset(context, tutorialKey);
    }

    public boolean isRunning() { return isRunning; }
    public int getStepCount() { return steps.size(); }
    public int getCurrentStepIndex() { return isRunning ? currentStepIndex : -1; }

    private void beginFirstStep() {
        currentStepIndex = 0;
        isRunning = true;
        showStep(0);
    }

    private void showStep(int index) {
        if (index >= steps.size()) {
            completeSession();
            return;
        }

        FocoraStep step = steps.get(index);
        View target = step.getTarget();

        if (target == null || target.getVisibility() != View.VISIBLE) {
            Log.w("Focora", "Skipping step " + index + " — target is not visible.");
            currentStepIndex++;
            showStep(currentStepIndex);
            return;
        }

        if (target.getWidth() == 0 || target.getHeight() == 0) {
            target.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    target.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    renderStep(step, index);
                }
            });
        } else {
            renderStep(step, index);
        }
    }

    private void renderStep(FocoraStep step, int index) {
        AnimationStyle anim = (step.getAnimationStyle() != AnimationStyle.EXPAND)
                ? step.getAnimationStyle() : globalAnimationStyle;

        if (overlayView == null) {
            overlayView = new FocoraOverlayView(
                    activity, theme, steps.size(), dismissOnBackPress,
                    this::onNextTapped, this::onSkipTapped
            );
            ViewGroup root = activity.findViewById(android.R.id.content);
            root.addView(overlayView);
            overlayView.animateEntrance(step, index, anim, () -> {
                if (listener != null) listener.onStepShown(index, step);
                if (step.getOnStepShownAction() != null) step.getOnStepShownAction().run();
            });
        } else {
            overlayView.animateTransition(step, index, anim, () -> {
                if (listener != null) listener.onStepShown(index, step);
                if (step.getOnStepShownAction() != null) step.getOnStepShownAction().run();
            });
        }

        boolean tapDismiss = step.isDismissOnTapOutside() || dismissOnTapOutside;
        overlayView.setDismissOnTapOutside(tapDismiss);
    }

    private void onNextTapped() {
        if (listener != null) listener.onStepDismissed(currentStepIndex);
        currentStepIndex++;
        showStep(currentStepIndex);
    }

    private void onSkipTapped() {
        if (listener != null) listener.onSkipped(currentStepIndex);
        completeSession();
    }

    private void completeSession() {
        isRunning = false;
        if (tutorialKey != null) FocoraPrefs.markCompleted(activity, tutorialKey);

        if (overlayView != null) {
            overlayView.animateExit(() -> {
                ViewGroup root = activity.findViewById(android.R.id.content);
                if (root != null && overlayView != null) root.removeView(overlayView);
                overlayView = null;
                mainHandler.removeCallbacksAndMessages(null);
                if (listener != null) listener.onCompleted();
            });
        } else {
            mainHandler.removeCallbacksAndMessages(null);
            if (listener != null) listener.onCompleted();
        }
    }

    private void attachLifecycle() {
        lifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                mainHandler.removeCallbacksAndMessages(null);
                if (overlayView != null) {
                    ViewGroup root = activity.findViewById(android.R.id.content);
                    if (root != null) root.removeView(overlayView);
                    overlayView = null;
                }
                isRunning = false;
                owner.getLifecycle().removeObserver(this);
            }
        });
    }

    public static final class Builder {
        private final Activity activity;
        private final List<FocoraStep> steps = new ArrayList<>();
        private FocoraTheme theme = null;
        private String tutorialKey = null;
        private boolean resetOnStart = false;
        private boolean dismissOnBackPress = true;
        private boolean dismissOnTapOutside = false;
        private long startDelayMs = 0L;
        private FocoraListener listener = null;
        private LifecycleOwner lifecycleOwner = null;
        private AnimationStyle globalAnimationStyle = AnimationStyle.EXPAND;

        public Builder(@NonNull Activity activity) {
            this.activity = activity;
            if (activity instanceof LifecycleOwner) {
                this.lifecycleOwner = (LifecycleOwner) activity;
            }
        }

        public Builder addStep(@NonNull FocoraStep step) { steps.add(step); return this; }
        public Builder addStep(@NonNull View target, @NonNull String title, @NonNull String description) {
            steps.add(new FocoraStep.Builder(target).title(title).description(description).build());
            return this;
        }
        public Builder theme(@NonNull FocoraTheme theme) { this.theme = theme; return this; }
        public Builder tutorialKey(@NonNull String key) { this.tutorialKey = key; return this; }
        public Builder resetOnStart(boolean reset) { this.resetOnStart = reset; return this; }
        public Builder dismissOnBackPress(boolean dismiss) { this.dismissOnBackPress = dismiss; return this; }
        public Builder dismissOnTapOutside(boolean dismiss) { this.dismissOnTapOutside = dismiss; return this; }
        public Builder startDelay(long ms) { this.startDelayMs = ms; return this; }
        public Builder listener(@NonNull FocoraListener listener) { this.listener = listener; return this; }
        public Builder lifecycleOwner(@NonNull LifecycleOwner owner) { this.lifecycleOwner = owner; return this; }
        public Builder animationStyle(@NonNull AnimationStyle style) { this.globalAnimationStyle = style; return this; }

        public Focora build() {
            if (steps.isEmpty()) throw new IllegalStateException("Focora: No steps added.");
            return new Focora(this);
        }
    }
}