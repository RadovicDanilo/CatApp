# CatApp 🐾

**CatApp** is an Android application that lets users explore a catalog of cat breeds and test their knowledge through a dynamic quiz. It combines educational content with gamification features such as score tracking and a global leaderboard. Users can create a local profile, browse detailed breed information, and challenge themselves in a timed quiz with randomly generated questions based on real data from TheCatAPI.

## Features

* 🔐 **Local Account Creation** (name, nickname, email)
* 🐱 **Cat Breed Catalog** with searchable list and detailed info
* 🧠 **Knowledge Quiz** with 20 randomly generated, image-based questions
* 🏆 **Leaderboard** with global rankings via external API
* 📊 **User Profile** showing quiz history, best scores, and leaderboard rank
* 📸 **Image Gallery** for each breed with full-screen viewer
* 🌗 **Light/Dark Theme Support**
* 🔄 **Swipe Transitions & Animations**
* 🧭 **Material Design 3 UI & Navigation**

## Tech Stack

* **Kotlin**, **Jetpack Compose**
* **MVI Architecture**, **Coroutines**, **Flow**
* **Room** (local DB for breeds & quiz data)
* **DataStore** (for simple key-value storage)
* **Retrofit** + **OkHttp** (for API communication)
* **Hilt** (Dependency Injection)
* **Jetpack Navigation**
* **KotlinX Serialization**

## APIs Used

* **[TheCatAPI](https://developers.thecatapi.com/)** (for breed info and images)
* **[RMA Quiz Leaderboard API](https://rma.finlab.rs)** (for global score sharing)
