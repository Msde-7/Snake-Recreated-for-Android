# Snake (Android)

A simple, fast Snake game built in Java for Android as a class final project. Move with on‑screen arrows, eat apples to grow, and chase a high score. Includes multiple color themes and a retro beep on each bite.

## Features
- Classic Snake gameplay rendered on a `SurfaceView`
- On‑screen arrow controls (up/left/right/down)
- Saved high score via `SharedPreferences`
- Six selectable themes (Default, GameBoy, Black & White, Halloween, IU, Black & Yellow)
- Lightweight, no permissions

## Build & Run
- Min SDK: 23 (Android 6.0)
- Target/Compile SDK: 33
- Language/Tools: Java 8, Android Gradle Plugin 8.1.1

Steps:
1. Open the project in Android Studio (Giraffe/Flamingo or newer).
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device.
   - Tested on 4.7" WXGA emulator; also runs on Pixel 3a / Pixel 7.

## Controls
- Tap the arrow buttons to change direction.
- Don’t hit the walls or yourself.
- Score increases by 1 per apple; high score is saved.

Tip: If testing with a mouse feels too fast, you can slow updates by increasing `gameUpdateSpeed` in `GameActivity`.

## Theming
Change theme from the main menu: Themes → pick a style. Theme affects background, snake, and apple colors during gameplay.

## Attribution
- `beep.mp3`: "beepsound" by Entershift — Creative Commons (`https://freesound.org/people/Entershift/sounds/704134/`)
- App icon created by the author

## License
Educational project. Feel free to fork for learning purposes.
