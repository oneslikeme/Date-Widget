# Date Widget

Home-screen widget showing the dates **29, 22, 14 and 7 days ago**, recalculated every day.

## iPhone (`ios/DateWidget.js`)
1. Install [Scriptable](https://scriptable.app) (free).
2. New script, paste `ios/DateWidget.js`, name it "Date Widget".
3. Long-press home screen > + > Scriptable > Medium widget > Edit Widget > pick the script.

## Android (`android/`)
1. Open `android/` in Android Studio (it generates the Gradle wrapper).
2. Run on a device/emulator (or Build > Build APK).
3. Long-press home screen > Widgets > Date Widget.

Refresh: Android sets an alarm just after midnight (30-min system update as fallback); iOS requests a refresh after midnight (timing is up to iOS, so it can lag a bit).
