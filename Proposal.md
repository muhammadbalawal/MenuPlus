# MenuPlus: Project Documentation - Part A

## Introduction and Motivation

When people dine out, they constantly face uncertainty: unfamiliar ingredients, small-print allergens, foreign-language menus, and poor readability on mobile photos. For individuals with food allergies, religious dietary rules, or strict preferences, this uncertainty can turn a meal into a health or comfort risk. MenuPlus was conceived to remove that uncertainty by combining computer vision, translation, and personalized filtering inside a single Android app.

The idea emerged from a common frustration among our team—friends scanning QR-code menus that appeared only in another language or failing to find allergy symbols that restaurants hide in footnotes. Rather than switching between translation apps, allergy checkers, and search engines, we envisioned an experience where a single photo would instantly yield a safe, readable, and personalized menu.

MenuPlus is built with Kotlin and Jetpack Compose, emphasizing speed, accessibility, and privacy. The application detects text from photographed menus, translates it into the user's chosen language, and cross-checks ingredients against their stored profile. It highlights foods the user can safely enjoy and warns them of potential allergens or dislikes, providing a calm, informed dining experience instead of guesswork.

## Benefits of the Project

Once implemented, MenuPlus will:

1. Increase safety – automatically detect and flag allergens before the user orders.
2. Simplify decision-making – summarize large or complex menus into concise, color-coded recommendations.
3. Bridge language gaps – translate menu text into the user's native language within seconds.
4. Personalize experiences – adapt results over time based on liked or disliked dishes.
5. Protect privacy – store profiles locally and require explicit consent before any cloud analysis.
6. Enhance accessibility – use high-contrast visuals and screen-reader support for inclusive use.

These benefits position MenuPlus as both a practical everyday assistant and a safety tool for travelers, families, and anyone managing dietary restrictions.


---

## Data Needs and Core Entities

At the heart of MenuPlus are four essential data entities that allow the application to connect a user's personal preferences with the content of any restaurant menu they photograph. These entities must remain consistent across all operations to ensure accuracy, traceability, and reliable personalization.

### 1. UserProfile

Each user is represented by a profile that contains their name (optional), preferred language, dietary restrictions, allergen list, dislikes, and dietary pattern such as vegan or halal. This profile also records whether the user has granted consent for cloud processing.

* Validity constraints: every profile must include a preferred language and a unique identifier; allergy, dislike, and diet lists may be empty but can never be null.
* Dependency: the entire personalization and risk-scoring logic depends on this profile being complete and accessible before any menu analysis begins.

### 2. Restaurant

Represents a dining location or food provider. At minimum, it stores the restaurant's name, optional location, and cuisine type. When a user uploads multiple menus from the same restaurant, the relationship is preserved through this entity.

* Validity constraint: restaurant names must be unique for a given user and cannot be blank.

### 3. MenuCapture

Each time the user takes a photo or imports a menu, the app creates a capture record. It includes the restaurant reference, the local image URI, and a status flag indicating whether parsing was successful.

* Rule: capture status can only move forward from NEW to either PARSED or FAILED; backward or undefined transitions are invalid.
* Dependency: a successful capture automatically triggers text extraction, translation, and allergen detection.

### 4. MenuItem

Individual dishes derived from OCR and translation. Each item stores a title, optional description, optional price, detected allergens, matched dietary tags, and a confidence value from the parser.

* Validity constraint: within a single restaurant, menu item titles must be unique when confidence is above 0.8; duplicate titles indicate an OCR error and must be discarded.

These entities are persisted locally using Room and, where required, synchronized to cloud storage only after user consent. All critical operations reference these models, ensuring that user data and menu data can be cleanly joined through foreign-key relationships.

## User Interface Requirements

The user interface is designed to surface complex information—like allergy warnings or translations—in a calm and intuitive manner. Each view must present essential information without overwhelming the user, and transitions between screens must never lose or reset user data.

* Onboarding screens collect and validate the UserProfile. The form enforces that at least one language is chosen before proceeding. Allergies and dislikes are represented by tappable chips to minimize text entry errors. Invalid or blank submissions disable the "Continue" button, ensuring that the stored profile always meets the schema requirements.

* Capture screen integrates directly with CameraX. It must display a clear shutter icon, an offline badge when no network is detected, and a visible consent link before any cloud-based parsing is performed. If the user has not granted consent, cloud calls are blocked, and an explanatory message appears.

* Menu Highlighted page shows the personalized results. Each menu item card contains a translation toggle, a colored risk badge (green for safe, yellow for caution, red for danger), and a small tooltip describing which ingredient triggered the warning. Accessibility rules require that these badges include both color and icon indicators and that TalkBack reads the risk level aloud.

* Menu History and Profile pages rely on consistent data binding: when the user edits their allergies or diets, previously saved menus must be automatically re-evaluated the next time they are opened. This ensures the user's stored history never becomes misleading after profile changes.

All screens must handle three visual states—loading, success, and error—and display clear explanations rather than silent failures. This approach ensures that users can always understand what the app is doing and why.

## Validity Constraints and Operational Rules

The following global constraints define when the application is considered internally consistent:

1. A `UserProfile` must exist before any `MenuCapture` can be created. Attempts to capture a menu without a profile must redirect to onboarding.

2. Each `MenuCapture` must store its creation timestamp and be associated with one restaurant.

3. All detected allergens must originate from either the rule-based keyword list or the AI parser; the app must record which method was used so that users can trust and interpret warnings.

4. Translations must always retain a link to the original text, allowing the user to toggle between them; the original content cannot be discarded.

5. Every personalized menu must be reproducible given the same profile and menu data. If the output changes under identical conditions, the parser or risk logic is considered faulty.

6. All warnings and recommendations must appear within a reasonable timeframe on a stable connection; slower performance fails the acceptance criteria for user experience.

7. The system must remain functional offline. If a menu cannot be analyzed due to missing connectivity, the capture is queued with clear "waiting" status, never discarded.

These constraints ensure that MenuPlus operates reliably and safely, meeting the acceptance condition of a consistent, user-trustworthy dining assistant.

---

## Acceptance Criteria and Success Definition

For MenuPlus to be considered complete and acceptable by both functional and user-experience standards, the following criteria must be demonstrably met during testing and presentation:

### 1. Profile creation and persistence

A user must be able to create, edit, and save their profile containing a preferred language, allergies, dislikes, and diets. All values remain persistent between sessions, even after the app is closed or the device restarts. Any attempt to use camera or parsing features without a profile automatically triggers redirection to the onboarding screen. This ensures that every downstream operation has the context it needs to personalize results.

### 2. Accurate menu capture and parsing

When a user photographs or uploads a menu, the capture record transitions from NEW to PARSED once text extraction, translation, and allergen matching complete successfully. If parsing fails, the capture enters the FAILED state with an error message describing the cause—such as low-light, blur, or network timeout. A successful parse produces a structured list of MenuItem objects linked to the restaurant and the active user.

### 3. Translation and allergen detection integrity

Each displayed dish must include both its original and translated text. Allergen detection must identify at least 80 percent of known ingredients from the reference dataset used during testing. Every flagged allergen appears with its source (rule-based list or AI detection) and an explanation in plain language. Users must be able to toggle translation and review these details without leaving the menu screen.

### 4. Risk labeling and personalization accuracy

The application must generate a clear, three-level risk classification (Safe, Caution, Danger) for each menu item based on the user's stored preferences and allergies. This classification must remain consistent for identical inputs. The "Top Picks" section must always present items in the same order given the same data, demonstrating deterministic ranking logic rather than randomization.

### 5. Accessibility and usability compliance

All UI elements must pass accessibility checks: text-to-speech labels for icons, readable contrast ratios, and no information conveyed by color alone. Critical alerts (such as allergy warnings) must include text and icons and be announced via TalkBack. Error messages should appear in plain, unambiguous language.

### 6. Performance and reliability benchmarks

The time between capture and the first translated, personalized menu appearing must be reasonable on a stable connection. Offline captures must be queued for later processing, and reopening a previously parsed menu offline must succeed without any network dependency.

### 7. Data integrity and reproducibility

Given identical user profiles and menu images, the same processed results must be reproducible on any device running the same app version. This guarantees that personalization and parsing depend only on stored data, not on hidden state or random behavior.

### 8. Privacy and consent enforcement

No image or text may be transmitted to cloud services without the user's explicit, revocable consent. Disabling consent in settings immediately halts all external API calls and queues operations locally. This rule ensures compliance with privacy expectations and aligns with the motivation of creating a safe, user-controlled environment.

When all these conditions are satisfied, MenuPlus can be considered a successful implementation of its core promise: allowing any user to confidently understand, filter, and enjoy restaurant menus according to their personal health and taste preferences. The design ensures reliability, clarity, and ethical handling of data, achieving the project's stated goal of making dining safer, smarter, and more inclusive for everyone.
