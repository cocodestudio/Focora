# Contributing to Focora

Thank you for your interest in contributing to **Focora**! Focora is designed to be a lightweight, premium, zero-dependency Android feature discovery and onboarding library. We welcome contributions from the community—whether it's reporting a bug, proposing a new feature, optimizing performance, or improving documentation.

---

## Table of Contents

- [Core Principles](#core-principles)
- [Code of Conduct](#code-of-conduct)
- [Development Setup](#development-setup)
- [Project Architecture](#project-architecture)
- [Coding Guidelines](#coding-guidelines)
- [Making Changes](#making-changes)
- [Submitting a Pull Request](#submitting-a-pull-request)
- [Reporting Issues & Feature Requests](#reporting-issues--feature-requests)

---

## Core Principles

Every contribution must align with Focora's foundational pillars:

1. **Zero External Dependencies**: The `:Focora` library module must **never** pull in third-party libraries, AndroidX dependencies, Kotlin standard library, or Jetpack Compose. It must remain a pure Java library leveraging standard Android SDK APIs.
2. **Pure Java 11 Compatibility**: The codebase is written in Java and compiled with `JavaVersion.VERSION_11`. Do not introduce syntax or language features from newer Java versions that would break compatibility for older build environments or minSdk 23.
3. **100% Programmatic UI**: Focora does not use XML layout files for its internal views (`TooltipView`, `FocoraOverlayView`, `ArrowDrawable`). Everything is constructed programmatically with Android `View` classes to avoid layout inflation overhead, resource conflicts, and ID collisions with host applications.
4. **Memory & Lifecycle Safety**: Resources such as offscreen `Bitmap` instances, `ValueAnimator` objects, and handlers must be cleaned up properly. Focora must never cause memory leaks when an `Activity` is destroyed or a tutorial is dismissed.
5. **Accessibility & Inclusivity**: Features must respect system settings such as reduced motion (`Settings.Global.ANIMATOR_DURATION_SCALE`), TalkBack screen reader accessibility (`announceForAccessibility`), and RTL layouts.

---

## Development Setup

### Prerequisites
- **Android Studio**: Android Studio Hedgehog / Ladybug / Meerkat (or newer)
- **JDK**: JDK 17 or JDK 21 configured as the Gradle JDK
- **Android SDK**:
  - `compileSdk`: 36
  - `minSdk`: 23
  - `targetSdk`: 36

### Cloning & Building
1. Clone the repository:
   ```bash
   git clone https://github.com/cocodestudio/Focora.git
   cd Focora
   ```
2. Open the project in Android Studio or build via the command line:
   ```bash
   # Build the library module
   ./gradlew :Focora:assembleDebug

   # Run tests
   ./gradlew test

   # Assemble release AAR
   ./gradlew :Focora:assembleRelease

   # Build sample app
   ./gradlew :app:assembleDebug
   ```

### Running the Sample App
The `:app` module demonstrates Focora's capabilities (spotlight shapes, custom dark/light themes, animations, step indicators, and callbacks). You can deploy and run it directly onto an emulator or physical device running Android 6.0 (API 23) or higher.

---

## Project Architecture

The project consists of two modules:

- **`:Focora`** (Library Module):
  - `com.cocode.focora`: Public API surface (`Focora`, `FocoraStep`, `FocoraTheme`, `FocoraShape`, `TooltipPosition`, `AnimationStyle`, `StepIndicatorStyle`, `FocoraListener`).
  - `com.cocode.focora.internal`: Internal rendering and UI (`FocoraOverlayView`, `FocoraRenderer`, `TooltipView`, `ArrowDrawable`, `FocoraPrefs`).
  - `com.cocode.focora.utils`: Utilities for unit conversion (`FocoraUtils`) and accessibility/reduced-motion detection.
- **`:app`** (Sample Application Module):
  - Demonstrates usage with an Edge-to-Edge Activity, showcasing custom themes, various shapes, animations, and event listeners.

---

## Coding Guidelines

### 1. Zero External Dependencies
- Check `Focora/build.gradle` before adding imports.
- Use only standard Android framework classes (`android.graphics.*`, `android.view.*`, `android.widget.*`, `android.animation.*`).
- Avoid adding dependencies to `dependencies {}` in `Focora/build.gradle`.

### 2. Builder Pattern for Public APIs
- All configurable classes (`Focora`, `FocoraStep`, `FocoraTheme`) must maintain a fluent `Builder` API.
- Keep constructors `private` and force construction via the `Builder`.
- Provide sensible defaults for all builder fields so minimal configuration is needed for standard use cases.

### 3. Canvas & Rendering Operations
- When implementing new shapes or drawing logic in `FocoraRenderer`, reuse existing `Paint` and `Path` objects. Avoid allocating objects inside `onDraw()` or `render()`.
- Use `PorterDuffXfermode(PorterDuff.Mode.CLEAR)` for spotlight cutouts on an offscreen ARGB_8888 bitmap.
- Ensure `recycleBitmap()` is called when the overlay is detached or dimensions change.

### 4. Layout Math & Window Insets
- Avoid relying on `getLocationOnScreen()` directly without taking into account the relative positioning of the overlay view. Use `target.getLocationInWindow()` and overlay relative offsets to handle status bars, action bars, and edge-to-edge display cutouts accurately.

### 5. ProGuard / R8 Rules
- If you add or rename public classes, interfaces, or enums in the `com.cocode.focora` package, update `Focora/consumer-rules.pro` to ensure rules are preserved for consumer applications.

---

## Making Changes

1. **Create a branch**:
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/issue-description
   ```
2. **Make your changes**:
   - Keep changes focused and atomic.
   - Ensure clean formatting consistent with existing Java code.
   - Verify that your code compiles without warnings:
     ```bash
     ./gradlew :Focora:compileDebugJavaWithJavac
     ```
3. **Test thoroughly**:
   - Test your changes using the `:app` sample app on an emulator or physical device.
   - Verify that both light and dark themes look aesthetically pleasing.
   - Test in RTL (Right-to-Left) layout mode.
   - Test with "Disable animations" / reduced motion enabled in Android developer options to ensure graceful degradation.

---

## Submitting a Pull Request

1. **Commit Messages**:
   We follow [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat: add support for dashed spotlight borders`
   - `fix: correct pill shape radius calculation during animation`
   - `docs: update quick start guide in README`
   - `refactor: optimize offscreen bitmap reuse in renderer`
   - `perf: eliminate redundant layout passes in TooltipView`

2. **Push and Open PR**:
   - Push your branch to your fork.
   - Submit a Pull Request against the `main` branch of `cocodestudio/Focora`.
   - Provide a clear PR description detailing:
     - What was changed and why.
     - Screenshots or screen recordings (GIF/MP4) demonstrating visual changes.
     - Verification steps and devices tested on.

---

## Reporting Issues & Feature Requests

- **Bug Reports**: Open an issue on GitHub with steps to reproduce, device model, Android version, and logs if applicable.
- **Feature Requests**: Describe the proposed feature, why it is useful, and potential API design ideas.

Thank you for helping make Focora the best spotlight tutorial library for Android!
