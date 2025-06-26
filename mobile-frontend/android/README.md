# Pet Clinic Android App

A simple Android application stub for the Pet Clinic demo, built with modern Android development best practices.

## Features

- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material Design 3**: Latest Material Design components and theming
- **Edge-to-Edge**: Modern Android UI with edge-to-edge display
- **Custom Theme**: Pet Clinic branded colors and styling

## Color Scheme

- **Background**: #F1F1F1 (Light gray)
- **Secondary**: #34302D (Dark brown)
- **Highlight/Primary**: #6DB33F (Green)

## Requirements

- Android Studio Hedgehog | 2023.1.1 or newer
- Android SDK 34
- Minimum SDK 24 (Android 7.0)
- Kotlin 1.9.0+

## Building

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the app on an emulator or physical device

## Project Structure

```
src/main/
├── java/com/example/petclinic/
│   ├── MainActivity.kt              # Main activity with Compose setup
│   └── ui/theme/
│       ├── Color.kt                 # Custom color definitions
│       ├── Theme.kt                 # Material 3 theme configuration
│       └── Type.kt                  # Typography definitions
├── res/
│   ├── values/
│   │   ├── strings.xml              # String resources
│   │   └── themes.xml               # XML theme definitions
│   ├── xml/
│   │   ├── backup_rules.xml         # Backup configuration
│   │   └── data_extraction_rules.xml # Data extraction rules
│   └── drawable/                    # App icons and drawables
└── AndroidManifest.xml              # App manifest with internet permissions
```

## Permissions

The app includes the following permissions:
- `INTERNET`: For network access
- `ACCESS_NETWORK_STATE`: For checking network connectivity

## Next Steps

This is a basic stub that can be extended with:
- Network layer (Retrofit/OkHttp)
- Navigation (Navigation Compose)
- State management (ViewModel)
- Dependency injection (Hilt)
- API integration with the Pet Clinic backend services
