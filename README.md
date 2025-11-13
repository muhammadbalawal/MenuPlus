# MenuPlus

**An intelligent menu helper app that makes dining smarter and safer.**

MenuPlus is an Android application that uses AI-powered analysis to provide personalized menu recommendations based on your dietary profile. Simply take a picture of a menu, and MenuPlus will analyze it for allergies, dietary restrictions, preferences, and even translate it to your preferred language.

---

## Features

### Authentication & Onboarding

-   **User Registration & Login** - Secure authentication via Supabase Auth
-   **Onboarding Flow** - Set up your dietary profile:
    -   Preferred language selection
    -   Allergies (critical safety information)
    -   Dietary restrictions (vegan, halal, kosher, etc.)
    -   Dislikes (foods you prefer to avoid)
    -   Preferences (foods you enjoy)

### Menu Scanning & Analysis

-   **OCR Text Extraction** - Extract text from menu images using Google Cloud Vision API
-   **AI-Powered Analysis** - Gemini AI analyzes menus with three distinct views:
    -   **Safe Menu** - Only items marked as safe (GREEN) for your dietary profile
    -   **Best Menu** - Top personalized recommendations based on your preferences
    -   **Full Menu** - Complete annotated menu with safety ratings (RED/YELLOW/GREEN)
-   **Menu Translation** - Automatic translation to your preferred language
-   **Safety Ratings** - Color-coded system:
    -   **RED**: Contains allergies or violates dietary restrictions (avoid)
    -   **YELLOW**: Contains dislikes or minor concerns (caution)
    -   **GREEN**: Safe and recommended (enjoy!)

### User Experience

-   **Premium Black & Gold Theme** - Modern, elegant UI design
-   **Saved Menus** - Access previously analyzed menus
-   **Profile Management** - View and edit your dietary preferences
-   **Settings** - Customize your app experience

---

## Architecture

MenuPlus follows **Clean Architecture** principles with a clear separation of concerns:

### Layer Structure

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
│  - Screens, ViewModels, Components │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│       Domain Layer (Business)        │
│  - Use Cases, Domain Models         │
└─────────────────────────────────────┘
              ↕
┌─────────────────────────────────────┐
│      Data Layer (Repositories)      │
│  - Remote APIs, Local Storage      │
└─────────────────────────────────────┘
```

### Design Patterns

-   **MVVM (Model-View-ViewModel)** - State management and UI logic separation
-   **Repository Pattern** - Abstraction layer for data sources
-   **Use Cases** - Single responsibility business logic
-   **Dependency Injection** - Hilt for clean dependency management
-   **Unidirectional Data Flow** - State-driven UI updates

---

## Tech Stack

### Core Technologies

-   **Kotlin** - Primary programming language
-   **Jetpack Compose** - Modern declarative UI framework
-   **Material 3** - Google's latest design system
-   **Android SDK** - Min SDK 24, Target SDK 36

### Architecture & Navigation

-   **Hilt** - Dependency injection framework
-   **Navigation Compose** - Type-safe navigation with Kotlin Serialization
-   **ViewModel & StateFlow** - Reactive state management
-   **Lifecycle-aware Components** - Proper Android lifecycle handling

### Backend & APIs

-   **Supabase** - Backend-as-a-Service for:
    -   Authentication (email/password)
    -   PostgreSQL database (user profiles, saved menus)
    -   Storage (menu images)
-   **Google Cloud Vision API** - OCR text extraction from menu images
-   **Firebase AI (Gemini)** - Menu analysis and personalization
    -   Model: `gemini-2.5-flash`

### Networking

-   **Retrofit** - HTTP client for REST APIs
-   **OkHttp** - HTTP client with interceptors
-   **Moshi** - JSON serialization/deserialization
-   **Ktor** - Required by Supabase SDK

### Image Loading

-   **Coil** - Image loading library for Compose

### Code Quality

-   **ktlint** - Kotlin linter with auto-formatting
-   **Kotlin Serialization** - Type-safe serialization

---

## Project Structure

```
app/src/main/java/com/example/emptyactivity/
├── data/
│   ├── remote/
│   │   ├── gemini/          # Gemini AI client
│   │   ├── supabase/        # Supabase client & DTOs
│   │   └── vision/          # Google Vision API client
│   └── repository/         # Repository implementations
│       ├── auth/
│       ├── ocr/
│       └── profile/
├── domain/
│   ├── model/               # Domain models (User, UserProfile, Language)
│   └── usecase/            # Business logic use cases
│       ├── auth/
│       ├── menu/
│       └── profile/
├── di/                      # Dependency injection modules
├── ui/
│   ├── components/          # Reusable UI components
│   ├── navigation/          # Navigation graphs & routes
│   ├── screens/            # Screen composables
│   │   ├── auth/           # Login, Register
│   │   ├── importmenu/     # Menu analysis screen
│   │   ├── ocr/            # OCR scanning screen
│   │   ├── onboarding/    # Onboarding flow
│   │   ├── profile/       # User profile
│   │   ├── savedmenu/     # Saved menus
│   │   └── settings/      # Settings
│   └── theme/              # App theming (Black & Gold)
└── util/                    # Utility classes
```

---

## Getting Started

### Prerequisites

-   **Android Studio** (Hedgehog or later recommended)
-   **JDK 17** or higher
-   **Android SDK** (API 24+)
-   **Google Cloud Vision API Key** (for OCR)
-   **Supabase Project** (for authentication and database)
-   **Firebase Project** (for Gemini AI)

### Setup Instructions

1. **Clone the repository**

    ```bash
    git clone https://github.com/muhammadbalawal/MenuPlus.git
    cd MenuPlus
    ```

2. **Configure API Keys**

    Create a `local.properties` file in the root directory:

    ```properties
    GCP_VISION_KEY=your_google_cloud_vision_api_key
    ```

    The Vision API key will be automatically injected into `BuildConfig.GCP_VISION_KEY`.

3. **Configure Supabase**

    Update `app/src/main/java/com/example/emptyactivity/data/remote/supabase/SupabaseClientProvider.kt` with your Supabase credentials:

    ```kotlin
    supabaseUrl = "https://your-project.supabase.co"
    supabaseKey = "your-anon-key"
    ```

4. **Configure Firebase**

    - Add your `google-services.json` to `app/` directory
    - Ensure Firebase AI (Gemini) is enabled in your Firebase project

5. **Sync and Build**

    - Open the project in Android Studio
    - Sync Gradle dependencies
    - Build the project (Ctrl+F9 / Cmd+F9)

6. **Run the App**
    - Connect an Android device or start an emulator (API 24+)
    - Click the "Run" button (Shift+F10 / Ctrl+R)

### Code Formatting

The project uses **ktlint** for code formatting. To format code:

```bash
./gradlew ktlintFormat
```

To check for linting issues:

```bash
./gradlew ktlintCheck
```

---

## Key Screens & Flows

### Authentication Flow

1. **Landing Screen** → Welcome screen for unauthenticated users
2. **Login/Register** → User authentication via Supabase
3. **Onboarding** → Set up dietary profile (first-time users)

### Main App Flow

1. **OCR Screen** → Take/select menu image → Extract text
2. **Import Menu Screen** → AI analysis with loading animation
3. **Results** → Three-tab view (Safe/Best/Full Menu)
4. **Saved Menus** → Access previously analyzed menus
5. **Profile** → View/edit dietary preferences
6. **Settings** → App configuration

### Navigation

-   **Type-safe Navigation** - All routes defined in `Route.kt` with `@Serializable`
-   **State-based Navigation** - Navigation graph changes based on authentication state
-   **Deep Linking** - Support for onboarding and signup deep links

---

## API Integrations

### Google Cloud Vision API

-   **Purpose**: OCR text extraction from menu images
-   **Endpoint**: `https://vision.googleapis.com/v1/images:annotate`
-   **Authentication**: API key via query parameter

### Firebase AI (Gemini)

-   **Purpose**: Menu analysis and personalization
-   **Model**: `gemini-2.5-flash`
-   **Features**:
    -   Safety rating analysis (RED/YELLOW/GREEN)
    -   Menu translation
    -   Personalized recommendations
    -   Allergy and restriction detection

### Supabase

-   **Authentication**: Email/password authentication
-   **Database**: PostgreSQL for user profiles and saved menus
-   **Storage**: Menu image storage (future feature)

---

## UI Theme

MenuPlus features a **premium black and gold theme**:

-   **Prestige Black** (`#000000`) - Primary background
-   **Royal Gold** (`#D4AF37`) - Accent color for highlights, buttons, and icons
-   **Gradient Text** - Gold gradient effects on titles
-   **Background Glow** - Subtle radial gradients for depth
-   **Material 3 Components** - Modern, accessible UI components

---

## Development Notes

### State Management

-   **StateFlow** - Used for reactive state management in ViewModels
-   **Unidirectional Data Flow** - UI observes state, ViewModel updates state
-   **Sealed Classes/Interfaces** - Type-safe state definitions

### Error Handling

-   **Result Wrapper** - Sealed class for success/error/loading states
-   **User-friendly Error Messages** - Displayed via AlertDialogs
-   **Logging** - Android Log for debugging

### Testing

-   Unit tests in `app/src/test/`
-   Android instrumented tests in `app/src/androidTest/`

---

## Team

-   **Malik Al-Shourbaji** - malik.shourbaji@gmail.com
-   **Muhammad Balawal Safdar** - muhammadbalawalsafdar@gmail.com

---

## License

This project is part of a course assignment. All rights reserved.

---

## Future Enhancements

-   [ ] Menu image storage and retrieval
-   [ ] Restaurant search and favorites
-   [ ] Social features (share menus with friends)
-   [ ] Offline mode support
-   [ ] Multi-language menu support expansion
-   [ ] Advanced filtering and sorting
-   [ ] Menu history and analytics

---

**Built with Kotlin, Jetpack Compose, and AI**
