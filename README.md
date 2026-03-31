# Focora

<p align="center">
  <img src="assets/focora_banner.png" alt="Focora Banner" width="100%" />
</p>

<p align="center">
  <a href="https://jitpack.io/#cocodestudio/Focora"><img src="https://jitpack.io/v/cocodestudio/Focora.svg" alt="JitPack" /></a>
  <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg" alt="API Level" /></a>
  <a href="https://github.com/cocodestudio/Focora/blob/main/LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License" /></a>
  <a href="https://github.com/cocodestudio/Focora/releases"><img src="https://img.shields.io/github/v/release/cocodestudio/Focora" alt="Latest Release" /></a>
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Java" />
</p>

<p align="center">
  <b>A premium, zero-dependency Android feature discovery library.</b><br/>
  Shape-morphing spotlights. Fully animated. Deeply customizable. Drop in without changing a line of your existing code.
</p>

---

## Table of Contents

- [Why Focora](#why-Focora)
- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Step Configuration](#step-configuration)
- [Theming](#theming)
- [Animation Styles](#animation-styles)
- [Focora Shapes](#focora-shapes)
- [Tooltip Position](#tooltip-position)
- [Callbacks & Listener](#callbacks--listener)
- [Session Controls](#session-controls)
- [Accessibility](#accessibility)
- [Advanced Usage](#advanced-usage)
    - [Custom Tooltip View](#custom-tooltip-view)
    - [Dark Mode Theme](#dark-mode-theme)
    - [RTL Support](#rtl-support)
    - [One-time Tutorial](#one-time-tutorial)
    - [Debug / Reset Seen Flag](#debug--reset-seen-flag)
    - [Programmatic Control](#programmatic-control)
- [Full API Reference](#full-api-reference)
    - [Focora.Builder](#focorabuilder)
    - [FocoraStep.Builder](#focorastepbuilder)
    - [FocoraTheme.Builder](#focorathemebuilder)
    - [FocoraListener](#focoralistener)
- [FAQ](#faq)
- [Contributing](#contributing)
- [License](#license)

---

## Why Focora

Most feature discovery libraries give you a basic spotlight and a text box. Focora was built differently:

| Concern | Others | Focora |
|---|---|---|
| Dependencies | Material, Kotlin, etc. | **None. Pure Java.** |
| Tooltip | Hardcoded XML layout | **100% programmatic, fully themeable** |
| Focora shapes | Rectangle only | **Rounded rect, circle, pill, rect** |
| Animation | Fade in/out | **Expand, fade, pulse, slide, none** |
| Lifecycle | Manual cleanup required | **Auto-detaches on Activity destroy** |
| Touch forwarding | Blocks all touches | **Forwards taps through the spotlight** |
| Fonts | Hardcoded font files | **Bring your own `Typeface`** |
| RTL | Not supported | **Full RTL layout support** |
| Accessibility | None | **TalkBack announcements built in** |
| "Show once" | None or manual | **Built-in `tutorialKey` persistence** |

---

## Features

- **Shape-morphing spotlight** — smoothly animates between rounded rect, circle, pill, and rect shapes as steps advance
- **4 animation styles** — Expand, Fade, Pulse, Slide, or None (for reduced motion)
- **Fully programmatic tooltip** — no XML layouts, no resource conflicts with your app
- **Deep theming** — colors, corner radius, elevation, text sizes, typefaces, button styles, arrow, step dots — all configurable
- **Per-step customization** — each step can override shape, animation style, tooltip position, and corner radius independently
- **Custom tooltip view** — replace the built-in tooltip with any `View` for a step
- **One-time tutorial** — persist "seen" state automatically with a `tutorialKey`
- **Lifecycle-aware** — automatically removes itself if the Activity is destroyed
- **Touch forwarding** — taps inside the spotlight reach the actual view underneath
- **Back press handling** — configurable dismiss-on-back behavior
- **Outside tap dismiss** — optional per-session and per-step
- **Step indicator dots** — animated dot row showing progress
- **Skip button** — optional, fully labeled
- **Arrow pointer** — triangular arrow connecting tooltip to spotlight
- **Start delay** — delay before overlay appears
- **Reduced motion support** — respects system "Disable animations" setting
- **RTL support** — arrow and layout mirror automatically
- **TalkBack / Accessibility** — step content announced via `announceForAccessibility`
- **Zero dependencies** — no Material, no Kotlin, no Hilt, no third-party libraries
- **API 23+** — supports Android 6.0 and above
- **ProGuard/R8 safe** — consumer rules included

---

## Preview
<video src="https://github.com/user-attachments/assets/a91936e8-737f-46a4-b84b-38f9aa8daf8d" width="100%" autoplay loop muted playsinline></video>

## Installation

### Step 1 — Add JitPack to your root `build.gradle`

```gradle
allprojects {
    repositories {
        // ... your existing repos
        maven { url 'https://jitpack.io' }
    }
}
```

If you are using `settings.gradle` (newer projects):

```gradle
dependencyResolutionManagement {
    repositories {
        // ... your existing repos
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2 — Add the dependency

```gradle
dependencies {
    implementation 'com.github.cocodestudio:Focora:1.0.0'
}
```

That's it. No other setup required.

---

## Quick Start

The absolute minimum to show a 2-step tutorial:

```java
new Focora.Builder(this)
    .addStep(btnSave, "Save your work", "Tap here to save your progress at any time.")
    .addStep(fabCreate, "Create new", "Start a brand new project from scratch.")
    .build()
    .start();
```

This uses the default light theme, `EXPAND` animation, `ROUNDED_RECT` spotlight shape, and `AUTO` tooltip positioning. No persistence — it shows every time.

---

## Step Configuration

Each step is built using `FocoraStep.Builder`. You can mix and match settings per step independently.

```java
FocoraStep step = new FocoraStep.Builder(yourView)
        .title("Your Title")
        .description("Your description text goes here.")
        .shape(FocoraShape.CIRCLE)
        .tooltipPosition(TooltipPosition.BELOW)
        .animationStyle(AnimationStyle.PULSE)
        .cornerRadius(16f)               // only applies if shape is ROUNDED_RECT
        .dismissOnTapOutside(true)       // this step dismisses if user taps outside
        .onShown(() -> {
            // fires after this step's entrance animation completes
            Log.d("Tour", "Step is now visible");
        })
        .build();
```

### `FocoraStep.Builder` Options

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `title(String)` | `String` | `""` | Bold title shown at the top of the tooltip |
| `description(String)` | `String` | `""` | Body text below the title |
| `shape(FocoraShape)` | enum | `ROUNDED_RECT` | Shape of the spotlight cutout |
| `tooltipPosition(TooltipPosition)` | enum | `AUTO` | Where the tooltip appears relative to the spotlight |
| `animationStyle(AnimationStyle)` | enum | inherits global | Per-step animation override |
| `cornerRadius(float)` | dp | `12f` | Corner radius when shape is `ROUNDED_RECT` |
| `dismissOnTapOutside(boolean)` | boolean | `false` | Tapping outside the spotlight+tooltip dismisses this step |
| `onShown(Runnable)` | Runnable | `null` | Callback fired when this step finishes its entrance animation |
| `customTooltipView(View)` | View | `null` | Replace the built-in tooltip with a fully custom view |

---

## Theming

All visual properties are controlled through `FocoraTheme`. Build one and pass it to the session builder.

```java
FocoraTheme theme = new FocoraTheme.Builder()

        // Overlay
        .overlayColor(Color.argb(210, 0, 0, 0))        // ARGB — alpha controls darkness

        // Focora
        .spotlightPadding(10f)                           // extra space around the target view (dp)
        .spotlightBorder(Color.WHITE, 2f)                // optional glowing border around the cutout

        // Tooltip card
        .tooltipBackgroundColor(Color.WHITE)
        .tooltipCornerRadius(20f)                        // dp
        .tooltipElevation(8f)                            // dp
        .tooltipMaxWidth(280)                            // dp, 0 = no limit

        // Title text
        .titleTextColor(Color.parseColor("#1A1A1A"))
        .titleTextSize(16f)                              // sp
        .titleTypeface(Typeface.create("sans-serif", Typeface.BOLD))

        // Description text
        .descTextColor(Color.parseColor("#666666"))
        .descTextSize(13f)                               // sp
        .descTypeface(Typeface.DEFAULT)

        // Next / Finish button
        .buttonBackgroundColor(Color.parseColor("#6200EE"))
        .buttonTextColor(Color.WHITE)
        .buttonCornerRadius(999f)                        // 999 = pill shape
        .nextButtonLabel("Next")
        .finishButtonLabel("Got it!")

        // Skip button
        .showSkipButton(true)
        .skipButtonLabel("Skip tour")

        // Arrow pointer (connects tooltip to spotlight)
        .showArrow(true)
        .arrowColor(Color.WHITE)                         // 0 = matches tooltip background
        .arrowSize(10f)                                  // dp

        // Step indicator dots
        .showStepIndicator(true)
        .stepIndicatorColors(
                Color.parseColor("#6200EE"),                 // active dot color
                Color.parseColor("#CCCCCC")                  // inactive dot color
        )
        .stepIndicatorSize(6f)                           // dp

        // Accessibility
        .respectReducedMotion(true)                      // skips animations if user disabled them

        .build();
```

Then pass it to your session:

```java
new Focora.Builder(this)
    .addStep(...)
    .theme(theme)
    .build()
    .start();
```

### Pre-built themes

Focora ships two ready-to-use themes:

```java
// Default light theme
FocoraTheme.defaultLight()

// Default dark theme
FocoraTheme.defaultDark()
```

### `FocoraTheme.Builder` Full Reference

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `overlayColor(int)` | `@ColorInt` | `#CC000000` | Background overlay color including alpha |
| `spotlightPadding(float)` | dp | `8f` | Padding around the target view inside the spotlight |
| `spotlightBorder(int, float)` | color + px | none | Optional border drawn around the spotlight cutout |
| `tooltipBackgroundColor(int)` | `@ColorInt` | `#FFFFFF` | Tooltip card fill color |
| `tooltipCornerRadius(float)` | dp | `20f` | Tooltip card corner rounding |
| `tooltipElevation(float)` | dp | `8f` | Card shadow elevation |
| `tooltipMaxWidth(int)` | dp | `280` | Max width of the tooltip card. `0` = unlimited |
| `titleTextColor(int)` | `@ColorInt` | `#1A1A1A` | Title text color |
| `titleTextSize(float)` | sp | `16f` | Title font size |
| `titleTypeface(Typeface)` | Typeface | bold | Custom typeface for the title |
| `descTextColor(int)` | `@ColorInt` | `#666666` | Description text color |
| `descTextSize(float)` | sp | `13f` | Description font size |
| `descTypeface(Typeface)` | Typeface | normal | Custom typeface for the description |
| `buttonBackgroundColor(int)` | `@ColorInt` | `#6200EE` | Next/Finish button background |
| `buttonTextColor(int)` | `@ColorInt` | `#FFFFFF` | Next/Finish button text color |
| `buttonCornerRadius(float)` | dp | `999f` | Button corner radius |
| `nextButtonLabel(String)` | String | `"Next"` | Label for the advance button |
| `finishButtonLabel(String)` | String | `"Got it"` | Label on the last step |
| `showSkipButton(boolean)` | boolean | `true` | Whether to show the Skip button |
| `skipButtonLabel(String)` | String | `"Skip"` | Skip button text |
| `showArrow(boolean)` | boolean | `true` | Whether to draw the pointer arrow |
| `arrowColor(int)` | `@ColorInt` | `0` | Arrow color. `0` inherits tooltip background color |
| `arrowSize(float)` | dp | `10f` | Arrow triangle size |
| `showStepIndicator(boolean)` | boolean | `true` | Whether to show dot progress indicators |
| `stepIndicatorColors(int, int)` | `@ColorInt` x2 | purple / grey | Active and inactive dot colors |
| `stepIndicatorSize(float)` | dp | `6f` | Dot diameter |
| `respectReducedMotion(boolean)` | boolean | `true` | Skip animations if system animations are disabled |

---

## Animation Styles

Set globally on the session or individually per step. Per-step setting always wins.

```java
// Global default for all steps
new Focora.Builder(this)
    .animationStyle(AnimationStyle.EXPAND)
    ...

// Per-step override
            new FocoraStep.Builder(view)
    .animationStyle(AnimationStyle.PULSE)
    ...
```

| Style | Description |
|-------|-------------|
| `EXPAND` | Focora expands from the center of the target outward with a decelerate curve. **Default.** |
| `FADE` | Focora fades in/out with alpha only. No size animation. |
| `PULSE` | Focora arrives at the target, then briefly overshoots its size and snaps back — a subtle "ping" effect. |
| `SLIDE` | Focora slides in from the direction of the previous step's position on screen. |
| `NONE` | Instant cut. No animation whatsoever. Recommended when `respectReducedMotion` detects the user has disabled system animations. |

---

## Focora Shapes

```java
new FocoraStep.Builder(view)
    .shape(FocoraShape.CIRCLE)
    ...
```

| Shape | Description |
|-------|-------------|
| `ROUNDED_RECT` | Rounded rectangle. Corner radius is configurable via `FocoraStep.Builder.cornerRadius()` or `FocoraTheme`. **Default.** |
| `CIRCLE` | Perfect circle that encloses the target bounds. Best for icon buttons, FABs, avatar images. |
| `PILL` | Fully rounded on the short sides (radius = height / 2). Best for wide buttons and tab bar items. |
| `RECT` | Sharp rectangle with zero rounding. Best for image previews or grid items. |

Focora **morphs the spotlight shape smoothly** as steps advance — so going from a `CIRCLE` step to a `PILL` step animates the cutout continuously.

---

## Tooltip Position

```java
new FocoraStep.Builder(view)
    .tooltipPosition(TooltipPosition.ABOVE)
    ...
```

| Position | Description |
|----------|-------------|
| `AUTO` | Focora calculates the best position based on available screen space. Prefers below, falls back to above, then failsafe top. **Default.** |
| `ABOVE` | Tooltip is always placed above the spotlight. |
| `BELOW` | Tooltip is always placed below the spotlight. |
| `LEFT` | Tooltip is placed to the left of the spotlight. |
| `RIGHT` | Tooltip is placed to the right of the spotlight. |

The arrow pointer automatically flips direction to match the tooltip position.

---

## Callbacks & Listener

Implement `FocoraListener` to respond to tutorial lifecycle events. Use the `Adapter` class to only override the methods you need.

```java
new Focora.Builder(this)
    .listener(new FocoraListener.Adapter() {

    @Override
    public void onStepShown(int stepIndex, FocoraStep step) {
        // Called after entrance animation completes for each step
        Log.d("Tour", "Now showing step " + stepIndex + ": " + step.getTitle());
    }

    @Override
    public void onStepDismissed(int stepIndex) {
        // Called when user taps Next or Finish
        Log.d("Tour", "Step " + stepIndex + " dismissed");
    }

    @Override
    public void onSkipped(int stepIndex) {
        // Called when user taps Skip or back press triggers dismiss
        Log.d("Tour", "Skipped at step " + stepIndex);
    }

    @Override
    public void onCompleted() {
        // Called when all steps are shown and the overlay exits
        showNextOnboardingScreen();
    }
})
        .build()
    .start();
```

### `FocoraListener` Methods

| Method | When it fires |
|--------|---------------|
| `onStepShown(int, FocoraStep)` | After each step's entrance animation finishes |
| `onStepDismissed(int)` | When the user taps Next or Finish on a step |
| `onSkipped(int)` | When the user taps Skip, taps outside (if enabled), or back press |
| `onCompleted()` | After the final step is dismissed and the overlay finishes its exit animation |

> **Note:** `onCompleted()` fires whether the user finished all steps **or** skipped — it always fires when the overlay is fully gone. Use `onSkipped()` to distinguish.

---

## Session Controls

### One-time tutorial

Pass a `tutorialKey` and Focora persists the "seen" state automatically. The tutorial will never show again after the user completes or skips it.

```java
new Focora.Builder(this)
    .tutorialKey("main_onboarding_v1")   // unique string key
    .addStep(...)
    .build()
    .start();
```

Increment the key (e.g., `"main_onboarding_v2"`) to re-show the tutorial after a significant app update.

### Start delay

Delay the overlay appearance — useful if your screen has an enter animation you don't want to interrupt.

```java
new Focora.Builder(this)
    .startDelay(600)   // milliseconds
    .addStep(...)
    .build()
    .start();
```

### Dismiss on back press

```java
new Focora.Builder(this)
    .dismissOnBackPress(true)   // default: true
    ...
```

### Dismiss on tap outside

```java
// For all steps in this session:
new Focora.Builder(this)
    .dismissOnTapOutside(true)
    ...

// Or per individual step:
            new FocoraStep.Builder(view)
    .dismissOnTapOutside(true)
    ...
```

---

## Accessibility

Focora has TalkBack support built in. After each step's entrance animation completes, it calls:

```java
announceForAccessibility(step.getTitle() + ". " + step.getDescription());
```

This causes TalkBack to read the step title and description aloud without the user having to navigate to the tooltip.

The overlay also requests focus for proper keyboard/D-pad navigation and supports `KEYCODE_BACK` for dismissal.

To disable animations for users who have turned off system animations:

```java
FocoraTheme theme = new FocoraTheme.Builder()
        .respectReducedMotion(true)   // default: true — reads Settings.Global.ANIMATOR_DURATION_SCALE
        .build();
```

When `ANIMATOR_DURATION_SCALE` is `0` on the device, all spotlight and tooltip animations are bypassed and steps switch instantly.

---

## Advanced Usage

### Custom Tooltip View

Replace the built-in tooltip card with any `View` for a specific step. Focora handles its positioning and animation the same way as the default.

```java
View myCustomCard = LayoutInflater.from(this)
        .inflate(R.layout.my_custom_tooltip, null, false);

// Set your content
myCustomCard.findViewById(R.id.myTitle).setText("Custom Tooltip!");

new FocoraStep.Builder(targetView)
    .title("")             // ignored when customTooltipView is set
    .description("")       // ignored when customTooltipView is set
    .customTooltipView(myCustomCard)
    .build();
```

> The custom view is sized by its own `wrap_content` measurement. Focora positions it using the same above/below logic as the built-in tooltip.

---

### Dark Mode Theme

```java
FocoraTheme darkTheme = new FocoraTheme.Builder()
        .overlayColor(Color.argb(180, 0, 0, 0))
        .tooltipBackgroundColor(Color.parseColor("#1E1E1E"))
        .titleTextColor(Color.WHITE)
        .descTextColor(Color.parseColor("#AAAAAA"))
        .buttonBackgroundColor(Color.parseColor("#BB86FC"))
        .buttonTextColor(Color.BLACK)
        .stepIndicatorColors(Color.parseColor("#BB86FC"), Color.parseColor("#444444"))
        .build();
```

Or use the shorthand:

```java
FocoraTheme.defaultDark()
```

To respond to the system dark mode automatically:

```java
int nightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
FocoraTheme theme = (nightMode == Configuration.UI_MODE_NIGHT_YES)
        ? FocoraTheme.defaultDark()
        : FocoraTheme.defaultLight();
```

---

### RTL Support

Focora automatically mirrors the tooltip layout and arrow pointer in right-to-left locales. No code changes needed. The library reads `ViewCompat.getLayoutDirection(view)` at runtime.

To test RTL in the emulator: `Settings → Developer Options → Force RTL layout direction`.

---

### One-time Tutorial

```java
Focora discovery = new Focora.Builder(this)
        .tutorialKey("feature_discovery_v1")
        .addStep(btnNew, "What's New", "Tap here to see the latest features.")
        .addStep(ivProfile, "Your Profile", "Manage your account from here.")
        .build();

// Shows only on first launch. Silent no-op on all subsequent launches.
discovery.start();
```

---

### Debug / Reset Seen Flag

During development you often want to re-trigger the tutorial every run without reinstalling the app.

```java
new Focora.Builder(this)
    .tutorialKey("onboarding_v1")
    .resetOnStart(BuildConfig.DEBUG)   // resets the flag every start in debug builds only
    .addStep(...)
    .build()
    .start();
```

Or reset manually at any time:

```java
Focora discovery = new Focora.Builder(this)
        .tutorialKey("onboarding_v1")
    ...
            .build();

// Reset the seen flag
discovery.reset(this);

// Then start
discovery.start();
```

---

### Programmatic Control

Hold a reference to the `Focora` instance to control it from outside:

```java
Focora discovery = new Focora.Builder(this)
        .addStep(view1, "Step 1", "Description one.")
        .addStep(view2, "Step 2", "Description two.")
        .addStep(view3, "Step 3", "Description three.")
        .build();

discovery.start();

// Advance to the next step manually (e.g., triggered by a ViewModel event)
discovery.next();

// Jump to a specific step by index
discovery.goToStep(2);

// Dismiss the tutorial entirely
discovery.dismiss();

// Check if the tutorial is currently visible
if (discovery.isRunning()) {
        // ...
        }

// Get total steps and current index
int total = discovery.getStepCount();
int current = discovery.getCurrentStepIndex();
```

---

## Full API Reference

### `Focora.Builder`

```java
new Focora.Builder(Activity activity)
```

| Method | Returns | Description |
|--------|---------|-------------|
| `addStep(FocoraStep)` | `Builder` | Add a fully configured step |
| `addStep(View, String, String)` | `Builder` | Shorthand: add a default rounded-rect step |
| `theme(FocoraTheme)` | `Builder` | Apply a custom theme to all steps |
| `tutorialKey(String)` | `Builder` | Key for "seen" persistence. Omit to always show. |
| `resetOnStart(boolean)` | `Builder` | Clear the seen flag every time `start()` is called |
| `dismissOnBackPress(boolean)` | `Builder` | Allow back press to dismiss. Default: `true` |
| `dismissOnTapOutside(boolean)` | `Builder` | Tap outside spotlight to dismiss. Default: `false` |
| `startDelay(long)` | `Builder` | Delay in ms before the overlay appears |
| `animationStyle(AnimationStyle)` | `Builder` | Global animation style for all steps |
| `listener(FocoraListener)` | `Builder` | Register lifecycle callbacks |
| `lifecycleOwner(LifecycleOwner)` | `Builder` | Override lifecycle owner for auto-cleanup |
| `build()` | `Focora` | Build the configured instance |

### `Focora` Instance Methods

| Method | Returns | Description |
|--------|---------|-------------|
| `start()` | `void` | Start the tutorial. Respects `tutorialKey` seen-guard. |
| `next()` | `void` | Advance to the next step programmatically |
| `goToStep(int)` | `void` | Jump to a specific step by zero-based index |
| `dismiss()` | `void` | Dismiss and complete the tutorial programmatically |
| `reset(Activity)` | `void` | Clear the "seen" flag for this tutorial's key |
| `isRunning()` | `boolean` | Whether the overlay is currently visible |
| `getStepCount()` | `int` | Total number of steps |
| `getCurrentStepIndex()` | `int` | Zero-based current step index, or `-1` if not running |

---

### `FocoraStep.Builder`

```java
new FocoraStep.Builder(View target)
```

| Method | Type | Default | Description |
|--------|------|---------|-------------|
| `title(String)` | String | `""` | Tooltip title |
| `description(String)` | String | `""` | Tooltip body |
| `shape(FocoraShape)` | enum | `ROUNDED_RECT` | Focora cutout shape |
| `tooltipPosition(TooltipPosition)` | enum | `AUTO` | Tooltip placement |
| `animationStyle(AnimationStyle)` | enum | global | Per-step animation override |
| `cornerRadius(float)` | dp | `12f` | Used when shape is `ROUNDED_RECT` |
| `dismissOnTapOutside(boolean)` | boolean | `false` | Per-step outside-tap dismiss |
| `onShown(Runnable)` | Runnable | `null` | Fires when step is fully visible |
| `customTooltipView(View)` | View | `null` | Replace built-in tooltip for this step |
| `build()` | `FocoraStep` | — | Build the step |

---

### `FocoraTheme.Builder`

See the [Theming](#theming) section for the full reference table.

```java
new FocoraTheme.Builder()
// ... configure
    .build()

// Or use a preset:
FocoraTheme.defaultLight()
FocoraTheme.defaultDark()
```

---

### `FocoraListener`

```java
public interface FocoraListener {
    void onStepShown(int stepIndex, FocoraStep step);
    void onStepDismissed(int stepIndex);
    void onSkipped(int stepIndex);
    void onCompleted();

    // Use the Adapter to override only what you need:
    abstract class Adapter implements FocoraListener {
        public void onStepShown(int stepIndex, FocoraStep step) {}
        public void onStepDismissed(int stepIndex) {}
        public void onSkipped(int stepIndex) {}
        public void onCompleted() {}
    }
}
```

---

## FAQ

**Q: Does Focora work with Fragments?**  
A: Yes. Pass `getActivity()` as the `Activity` parameter. For lifecycle binding, pass `this` (the Fragment) as the `lifecycleOwner`. The overlay is attached to the Activity's content view, so it renders above the Fragment correctly.

**Q: Does Focora conflict with my Material3 theme?**  
A: No. Focora uses zero Material theme attributes. The tooltip is 100% programmatic with its own colors.

**Q: The spotlight doesn't align correctly on some devices. Why?**  
A: Make sure the target view is fully laid out before calling `start()`. If you call it inside `onCreate()` before `onWindowFocusChanged`, use `startDelay(300)` or post to the view's own `ViewTreeObserver`. Focora includes a layout listener fallback, but a small delay is the safest approach.

**Q: Can I use Focora in a Kotlin project?**  
A: Absolutely. Focora is a Java library but is fully callable from Kotlin. The builder chains work naturally in Kotlin with no special interop needed.

**Q: The "seen" state isn't resetting after I call `reset()`.**  
A: Make sure you call `reset()` before `start()`, not after. Also verify you're passing the same `tutorialKey` string in both the `Builder` and the `reset()` call.

**Q: Can I show the tutorial again after a major app update?**  
A: Yes. Change your `tutorialKey` string. For example, from `"onboarding_v1"` to `"onboarding_v2"`. Old users will see the tutorial once more.

**Q: How do I remove the Skip button?**  
A: In your theme: `.showSkipButton(false)`.

**Q: Can I highlight a view inside a RecyclerView?**  
A: Yes, as long as the view is currently visible and laid out on screen. Focora calls `getLocationInWindow()` at the moment the step is rendered, so it works with any view that has a non-zero size and `VISIBLE` visibility.

---

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Write your changes in **Java only** — no Kotlin in the library module
4. Ensure no new dependencies are added to the library module
5. Test on API 21 and at least one recent API level
6. Submit a pull request with a clear description of what changed and why

For bug reports, open a GitHub Issue with your Android version, device, and a minimal code snippet that reproduces the issue.

---

## License

```
MIT License

Copyright (c) 2025 YourName

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/cocodestudio">Adeel Ahmad</a>
  &nbsp;·&nbsp;
  <a href="https://github.com/cocodestudio/Focora/issues">Report Bug</a>
  &nbsp;·&nbsp;
  <a href="https://github.com/cocodestudio/Focora/issues">Request Feature</a>
</p>
