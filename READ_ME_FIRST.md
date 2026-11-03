# Calculator: menus + swipe

## Open the complete project
1. Extract this ZIP.
2. In Android Studio, choose Open and select the calculator folder (contains settings.gradle.kts).
3. Let Gradle sync, then run on your phone/emulator.

## Or copy the changes into your existing project
Replace MainActivity.java and res/layout/activity_main.xml and res/menu/menu.xml.
Add CalculatorFragment.java, CalculatorEngine.java, CurrencyFragment.java to the SAME package as MainActivity: ma.ensaf.calculator.
Add res/layout/fragment_standard.xml, fragment_scientifique.xml, fragment_devise.xml, currency_item.xml.
In app/build.gradle.kts, add inside dependencies:

```kotlin
implementation("androidx.viewpager2:viewpager2:1.1.0")
```

Then click Sync Now. Do not add a second MainActivity class or keep the old activity layout.
Your original theme and Gradle/plugin versions are retained.

## Navigation
Swipe left: Standard → Scientifique → Devise.
Swipe right to return. Tap a tab or select a three-dot menu item to jump directly.
ViewPager2 implements horizontal page slides. The pages can scroll vertically on small/landscape screens.

## Calculations
C clears everything. ⌫ deletes a digit (replaces the old AC label).
Standard supports +, −, ×, ÷, %, ±, decimals.
Scientific also supports sin/cos/tan, DEG/RAD, √, x², xʸ, log (base 10), ln, π, e, reciprocal.
Enter a number, then tap a unary function; for powers enter 2, xʸ, 3, = to get 8.
Trigonometric functions use degrees initially; tap DEG to switch to radians.
Calculations execute left to right, like the original calculator: 2 + 3 × 4 = 20.
Percent divides the current number by 100; it does not implement a commercial-calculator percent convention.
Invalid real-number operations display Erreur. Type a new number or press C to recover.
Each calculator has separate state, retained when rotating or switching screens.

## Currency
This is an OFFLINE converter, with no live or built-in exchange rates.
Select source and target, enter the amount and the rate (target units per 1 source unit), then Convertir.
Formula: amount × rate. Changing the pair clears the rate to avoid using it for the wrong currencies.
Same-currency conversion uses 1 automatically.

## Verification
PASS: 23 standalone Java checks covering arithmetic, chained operations, operator replacement, decimal input, sign, deletion, scientific functions, invalid inputs, recovery and serialized state. Harness: verification/EngineCheck.java.
Android XML was parsed and resource references checked. No Android SDK/emulator is installed in the preparation environment, so APK compilation and device swipe/rotation checks still need Android Studio.
