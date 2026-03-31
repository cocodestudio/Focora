package com.cocode.focora;

public interface FocoraListener {
    void onStepShown(int stepIndex, FocoraStep step);
    void onStepDismissed(int stepIndex);
    void onSkipped(int stepIndex);
    void onCompleted();

    abstract class Adapter implements FocoraListener {
        @Override public void onStepShown(int stepIndex, FocoraStep step) {}
        @Override public void onStepDismissed(int stepIndex) {}
        @Override public void onSkipped(int stepIndex) {}
        @Override public void onCompleted() {}
    }
}