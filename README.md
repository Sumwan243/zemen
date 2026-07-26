# ZEMEN ዘመን — Ethiopian Calendar App

## Setup

### Prerequisites
- Android SDK (API 34)
- JDK 17+
- Kotlin 1.9.22+

### Build
```bash
./gradlew assembleDebug
```

### Install
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Unit Tests
```bash
./gradlew test
```

## Project Structure
```
app/src/main/java/com/surafel/zemen/
├── calendar/          # Pure Kotlin calendar logic (no Android deps)
│   ├── EthiopianCalendar.kt
│   ├── BahraHasab.kt
│   └── GeezNumerals.kt
├── ui/
│   ├── home/          # Today screen + Pagume mode
│   ├── calendar/      # Month grid
│   ├── converter/     # Drum scroller converter
│   ├── feasts/        # Seasons & holidays
│   ├── settings/      # App configuration
│   ├── theme/         # Colors, typography, theme
│   └── navigation/    # Bottom tab bar
├── widget/            # Glance home screen widgets
├── data/              # DataStore, holiday repository
└── MainActivity.kt

app/src/main/res/
├── font/              # abyssinica_sil.ttf (Geez script)
├── raw/               # holidays_en.json, holidays_am.json
├── values/            # strings.xml (English)
├── values-am/         # strings.xml (Amharic)
└── xml/               # Widget metadata
```

## Notes
- The `abyssinica_sil.ttf` font must be placed in `res/font/`.
  Download from: https://software.sil.org/abyssinica/
