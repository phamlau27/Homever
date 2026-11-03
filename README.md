# 🏠 Homever

### Find Your Perfect Roommate. Discover Your Next Home.

**Homever** is a smart roommate matching and rental management platform designed to help people find compatible roommates, discover suitable rental accommodations, and manage their rental experience more efficiently.

The platform combines mobile technology, cloud infrastructure, and AI-powered recommendations to create a more convenient and personalized housing experience.

---

## 📌 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Technology Stack](#-technology-stack)
- [AI-Powered Matching](#-ai-powered-matching)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Development Team](#-development-team)
- [Project Information](#-project-information)

---

## 🌟 Overview

Finding a suitable roommate is not just about sharing a room. It is about finding someone whose lifestyle, habits, and preferences are compatible with yours.

Homever addresses common challenges in the rental market:

- Difficulty finding compatible roommates.
- Lack of personalized rental recommendations.
- Inefficient communication between people looking for shared accommodation.
- Challenges in managing rental information and agreements.

Homever aims to provide an integrated platform where users can search for rooms, find potential roommates, receive intelligent recommendations, and manage their rental experience.

---

## ✨ Key Features

### 👥 Roommate Matching
- Create personal profiles and describe living preferences.
- Evaluate compatibility based on multiple lifestyle criteria.
- Discover potential roommates through personalized recommendations.
- Receive AI-assisted matching suggestions.

### 🏡 Room Discovery
- Browse available rental accommodations.
- Search and filter rooms based on user preferences.
- View detailed room information.
- Explore suitable shared accommodation options.

### 📋 Rental Management
- Manage rental-related information.
- Support communication and coordination between tenants.
- Centralize important rental information.

### 🔐 Authentication
- User registration and login.
- Secure authentication and authorization.
- Support social login integration.

### 🔔 Notifications
- Receive real-time notifications.
- Stay updated on matching suggestions and relevant activities.

---

## 🏗️ System Architecture

Homever follows a cloud-based architecture designed to support mobile application services and scalable backend processing.

### High-Level Architecture

```text
                    ┌─────────────────────┐
                    │    Android App      │
                    │    (Kotlin)          │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Amazon Cognito  │
                    │ Authentication      │
                    └─────────────────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Amazon API       │
                    │    Gateway          │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       VPC Link      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Amazon ECS       │
                    │   Backend Services  │
                    └──────────┬──────────┘
                               │
                   ┌───────────┴───────────┐
                   ▼                       ▼
          ┌────────────────┐      ┌────────────────┐
          │  Amazon RDS    │      │   Amazon S3    │
          │   Database     │      │ File Storage   │
          └────────────────┘      └────────────────┘

                    ┌─────────────────────┐
                    │ Firebase Cloud      │
                    │ Messaging (FCM)     │
                    └─────────────────────┘
```

*Note: This diagram represents the planned high-level architecture. Individual integrations may vary depending on the implementation stage.*

---

## 🛠️ Technology Stack

| Category | Technologies |
|---|---|
| Mobile Development | Kotlin, Android |
| Authentication | Amazon Cognito |
| API Management | Amazon API Gateway |
| Backend Infrastructure | Amazon ECS |
| Networking | AWS VPC, VPC Link |
| Database | Amazon RDS |
| Object Storage | Amazon S3 |
| Notifications | Firebase Cloud Messaging (FCM) |
| AI / Recommendation | Deep Q-Network (DQN), Large Language Models (LLMs) |
| Version Control | Git, GitHub |

---

## 🤖 AI-Powered Matching

Homever explores AI-driven recommendations to improve roommate compatibility.

### Matching System

The matching process considers multiple lifestyle and personal preference criteria.

Key components include:

- **Multi-criteria matching:** Evaluates compatibility across 10 criteria.
- **DQN-based recommendations:** Explores personalized suggestions using Deep Q-Network techniques.
- **LLM-assisted weight adjustment:** Uses Large Language Models to support dynamic updates to matching criteria weights.

The goal is to provide more relevant roommate recommendations rather than relying solely on basic filtering.

---

## 📂 Project Structure

The Android application currently follows a modular structure:

```text
Homever/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/homever/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── ui/
│   │   │   │       ├── screens/
│   │   │   │       │   ├── LoginScreen.kt
│   │   │   │       │   └── OnboardingScreen.kt
│   │   │   │       └── theme/
│   │   │   │           ├── Color.kt
│   │   │   │           ├── Theme.kt
│   │   │   │           └── Type.kt
│   │   │   └── res/
│   │   ├── androidTest/
│   │   └── test/
│   │
│   └── build.gradle.kts
│
├── gradle/
│   └── wrapper/
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Before running the application, make sure you have:

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- Git

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/phamlau27/Homever.git
```

**2. Navigate to the project directory**

```bash
cd Homever
```

**3. Open the project**

Open the project in Android Studio and allow Gradle to synchronize dependencies.

**4. Run the application**

- Start an Android Emulator or connect a physical Android device.
- Select the `app` configuration.
- Click **Run**.

---

## 👨‍💻 Development Team

**Team Cooper – AISC'26**

Homever is developed as a team project for the Advanced Information Systems Contest (AISC'26).

---

## 📌 Project Information

| Attribute | Details |
|---|---|
| Project Name | Homever |
| Project Type | Mobile Application / Smart Rental Platform |
| Competition | Advanced Information Systems Contest (AISC'26) |
| Team | Cooper |
| Platform | Android |
| Repository | [GitHub – Homever](https://github.com/phamlau27/Homever) |

---

<p align="center">
  <b>Homever – Making Shared Living Better.</b>
  <br>
  Find a place. Find your people. Feel at home.
</p>
