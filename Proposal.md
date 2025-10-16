# MenuPlus: Project Documentation — Part A

## Introduction and Motivation

When people dine out, they often face uncertainty: unfamiliar ingredients, small-print allergen notes that are hard to read on a phone, and menus written in languages they do not speak. For anyone with food allergies, religious or ethical dietary rules, or strong dislikes, that uncertainty creates real risk and stress. MenuPlus removes that uncertainty by turning a single menu image into a readable, translated, and personalized menu that highlights what is safe and appealing while warning about hazards that should be avoided.

The idea came from common situations we have experienced with friends. QR menus were available only in the restaurant's language, or allergy icons were buried in footnotes that were easy to miss. People bounced between translation apps, search results, and vague memory of what a dish might contain. MenuPlus replaces that juggling with a single, focused experience where one import action yields a structured, filtered view of the menu.

MenuPlus is built for Android in Kotlin with Jetpack Compose. The app extracts text from an imported menu image, translates the text into the user's preferred language, and evaluates each dish against the user's stored profile. The app then renders a personalized menu that clearly communicates three things: which items are safe, which items require extra attention, and which items should be avoided.

## Benefits of the Project

Once implemented, MenuPlus will provide the following user-facing benefits and development-relevant properties.

**Safety first.** The system detects potential allergens and diet violations and flags them clearly before an order is placed.

**Quicker decisions.** Large menus are distilled into a short, readable list that emphasizes safe and relevant options.

**Language clarity.** Text is presented in the user's preferred language with a toggle to view the original text at any time.

**Personalization that respects control.** Results reflect allergies, dislikes, and dietary patterns while allowing users to adjust preferences at any time.

**Privacy by default.** Profiles are stored locally. Any cloud processing requires explicit, revocable consent.

**Accessible presentation.** High contrast, descriptive labels, semantic headers, and proper focus order ensure that the interface works well with screen readers.

These benefits make MenuPlus a practical assistant for daily use, a travel companion for foreign menus, and a safer option for anyone who must manage health or dietary constraints.

## Business Logic Requirements

This section defines the data structures and operational rules that determine how MenuPlus transforms a user's profile and a menu image into a reliable, personalized menu. The requirements are expressed so that another developer can reproduce the behavior deterministically.

### Core Entities and Data Needs

MenuPlus persists four core entities. All critical operations reference these models, and all associations are explicit.

#### UserProfile

Stores an immutable identifier, preferred language, allergies, dislikes, and one or more diet tags such as vegan or halal. A boolean field records whether the user has granted consent for any cloud analysis.

**Constraints.** Preferred language and id are required. Lists for allergies, dislikes, and diets may be empty but must not be null. Consent must default to false until the user opts in.

**Dependency.** No menu processing may begin without a valid UserProfile in local storage.

#### Restaurant

Represents a dining venue. At minimum, it contains name and optional fields for location and cuisine type.

**Constraints.** For a single user, the combination of restaurant name and location must be unique. Blank names are not permitted.

#### MenuCapture

Represents a single imported image of a menu. It stores a reference to the associated restaurant, the local URI of the image, a created timestamp, and a processing status.

**Processing status.** The status can be NEW, PARSED, or FAILED. Transitions are forward only, that is, NEW moves to PARSED or FAILED. Once PARSED or FAILED, the status does not change.

**Dependency.** Creating a NEW capture schedules the processing pipeline described below.

#### MenuItem

Represents a single dish or item discovered by parsing the imported image. Fields include title, optional description, optional price string, a list of detected ingredient keywords, a set of allergen tags derived from those keywords, a set of cuisine or property tags such as spicy or contains nuts, and a classification field for risk which is computed, not entered.

**Constraints.** Within a single restaurant, item titles must be unique after normalization. Normalization removes surrounding whitespace, collapses repeated spaces, and lowercases the string. Duplicates indicate a parsing error and must be discarded during processing.

All entities are persisted with Room. UserProfile and menu data must be readable offline. If cloud processing is allowed and network is unavailable, the capture remains queued until connectivity returns.

### Processing Pipeline and Risk Classification

The processing pipeline is deterministic and idempotent. Given the same UserProfile and the same imported image, the app must produce the same structured results.

#### Text extraction

The image picker hands off a local URI. The app extracts blocks of text from the image and segments them into candidate lines that may contain item names, descriptions, and prices. Segmentation rules are based on common menu structures, for example, bold or uppercase lines often denote titles while following lines that begin with dash or contain commas often denote descriptions or ingredients.

#### Translation

If the extracted text language differs from the user's preferred language, the app computes a translation for titles and descriptions while preserving the original text. The user can toggle between translated and original text at any time on the item detail view. Translated and original copies are both persisted so that re-opening a menu does not require retranslation.

#### Ingredient and allergen derivation

The parser searches for keywords and phrases that map to ingredients and common allergen families. It also evaluates patterns such as contains eggs, may contain nuts, cooked in peanut oil, or processed in a facility with shellfish. The system records the source of each allergen tag, either keyword mapping or phrase mapping, so that the explanation shown to the user cites what was found.

#### Dietary and preference evaluation

The system compares each item's derived tags and ingredients with the user's diets and dislikes. For example, an item marked with pork violates halal or kosher, and an item containing animal products violates vegan. Dislikes such as dislike cilantro do not indicate a safety problem but do affect relevance and color.

#### Risk classification with deterministic precedence

Each item receives a risk classification of GREEN (safe), YELLOW (mixed), or RED (danger). Classification is rule based and uses strict precedence so the outcome does not depend on any confidence score.

**RED: danger**

The item matches any user allergy. Direct matches include explicit allergen mentions such as contains peanuts or contains milk. Family matches include ingredient names that imply the allergen, for example pesto implies pine nuts, or ghee implies dairy. Diet violations that the user has marked as strict, such as vegan or halal, are also RED. For example, beef stock in a soup violates vegan. Statements that indicate unavoidable cross-contact, such as fried in peanut oil, are RED. The explanation lists the triggering phrases and their mapping, for example, "peanut oil maps to peanut allergen."

**YELLOW: mixed or caution**

The item does not meet RED conditions but still requires attention. Cases include may contain statements and processed in a facility statements, ambiguous ingredients such as fish sauce where the language was unclear, or an item that contains a strong dislike without an allergen. A common example is spicy items for a user who dislikes spicy food. YELLOW can also reflect a diet conflict that is not marked strict. The explanation lists the cautionary phrases and suggests requesting clarification from staff.

**GREEN: safe**

No allergens matched. No strict diet rule is violated. Dislikes do not appear, or they are optional and can be omitted as a clearly separate component, for example dressing on the side. If the user has marked likes, the presence of liked tags can be noted as a positive hint but does not affect risk status.

**Precedence rules**

RED outranks YELLOW and GREEN. YELLOW outranks GREEN. If both a like and a dislike are present, the dislike sets YELLOW unless an allergen is present which sets RED. If both a cautionary statement and a diet violation exist and the diet is strict, the result is RED. The algorithm is a pure function over the item's tags and the profile's sets, which allows unit tests to assert behavior across a table of cases.

#### Ordering and presentation

Items are shown in groups by risk classification, with GREEN first, then YELLOW, then RED. Within each group, items can be ordered alphabetically or by presence of liked tags. Ordering must be deterministic. Users can filter to show only safe items or only caution items.

The outcome of this pipeline is a set of MenuItem records with stable titles, translated text if needed, derived allergen and property tags, and a computed risk classification that can be reproduced.

## User Interface Requirements

The interface communicates complex evaluations in a calm and legible way. Screens must preserve state, avoid destructive back navigation, and present explicit feedback for loading and error conditions. The app uses an image picker only. There is no in-app camera.

### Onboarding

The onboarding flow creates or updates the UserProfile. The user selects a preferred language, allergies, dislikes, and diet tags using selectable chips and text fields. The Continue action remains disabled until a preferred language is selected. Lists may be empty but not null. When onboarding completes, the profile is persisted locally.

### Import Menu Image

The user chooses an image from gallery or file pickers. If cloud analysis is disabled in settings or consent has not been granted, the app runs only local steps and enqueues cloud steps for later. If cloud analysis is enabled and network is available, the app proceeds immediately. The screen shows a concise status view while the pipeline executes.

### Personalized Menu

The main view displays cards for each item. Cards show the item title, an optional price string, and a short description. A toggle allows the user to show original text or translated text without leaving the screen. Each card shows a risk badge with color and icon that can be read by screen readers. Tapping the badge opens an explanation panel that lists the specific triggers, such as "contains peanuts from ingredient list" or "may contain shellfish noted by menu."

### Menu History

The user can open previously parsed menus and the system re-evaluates items against the current profile when the menu is opened. Re-evaluation is important since the user may have added new allergies or diets after the original parse.

### Profile and Settings

Profile lets users change language, allergies, dislikes, and diets. Settings exposes consent for cloud analysis, a link to the privacy policy, and options for strict diet mode. If consent is turned off, the app halts all cloud requests and queues captures that require them. Screen-reader support is verified with content descriptions, meaningful labels, and predictable focus order. Color coding is never the only way to communicate risk. Icons and text are always included.

All screens provide three explicit states: loading, success, and error. Error messages explain what happened and what the user can try next, such as choosing a clearer image or trying again when online.

## Validity Constraints and Operational Rules

The following constraints must hold or the application is considered inconsistent.

A valid UserProfile must exist before creating a MenuCapture. If a user tries to import without a profile, the app routes to onboarding.

MenuCapture records must store creation time, local image URI, and a restaurant reference. Status transitions are forward only.

Translations must preserve both original and translated text. The user can switch views without reprocessing.

Allergen and diet evaluations must document the phrases or tags that produced the result so the explanation panel can show exact evidence.

Risk classification must follow the precedence rules consistently with no random elements. The pure function that maps inputs to GREEN, YELLOW, or RED must be unit tested with a table of representative cases.

The app must present first results within ten seconds on a stable connection for a one page menu. Longer times fail the user experience acceptance criteria for performance.

Offline usage must work. If consent is granted but the device is offline, MenuCapture is queued and marked clearly. Opening a previously parsed menu must work fully without network access.

Deleting a profile removes locally stored personal data and clears any queued operations. Re-creating a profile starts fresh.

## Acceptance Criteria and Success Definition

MenuPlus is acceptable only if the following outcomes can be demonstrated in tests and during the live presentation.

### Profile creation and persistence

Users can create and edit a profile that includes preferred language, allergies, dislikes, and diets. The profile persists across restarts, and any menu view reflects updates immediately.

### Menu import and structured parsing

Importing an image creates a NEW capture, runs the pipeline, and results in PARSED with a structured set of items or FAILED with a specific reason. Structured items include titles, optional descriptions and prices, derived ingredient and allergen tags, and a computed risk classification.

### Translation integrity and toggle

When the menu language differs from the profile language, titles and descriptions are available in both original and translated forms. The user can switch the view without leaving the screen. Translated content is persisted to avoid repeated calls.

### Deterministic risk classification

Given the same profile and image, the same items receive the same GREEN, YELLOW, or RED result. RED is produced by any allergy match or strict diet violation. YELLOW is produced by caution phrases, ambiguous ingredients, or dislikes without allergens. GREEN is produced when none of the other conditions apply. The explanation panel lists the exact triggers that led to the result.

### Accessible, readable UI

Risk badges include color, icon, and text so that users who cannot rely on color alone still receive the message. All actionable elements have content descriptions and logical focus order so screen readers can navigate the interface. Font sizes and contrast meet Android accessibility recommendations.

### Performance and reliability

On a stable connection, first personalized results appear within ten seconds for a one page menu. If network is unavailable, the capture is queued and clearly labeled. Re-opening a previously parsed menu works offline.

### Privacy and consent enforcement

No image or text leaves the device unless consent is enabled in Settings. Turning consent off immediately blocks external calls and converts dependent steps into queued operations. The app provides a clear explanation of what data would be sent and for what purpose.

When these criteria are met, MenuPlus delivers on its purpose: it transforms a static menu image into a reliable, readable, and personalized set of dining options that respects the user's health, preferences, and privacy.
