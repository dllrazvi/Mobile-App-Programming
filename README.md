# Mobile Application Programming – Laboratory Summary

## Course Overview

The Mobile Application Programming course focuses on designing and developing complete CRUD-based mobile applications using both native and cross-platform technologies. Students build progressively on a single project throughout the semester, starting from user interface design and evolving into local persistence and remote server integration.

---

## Laboratory Breakdown

### Lab 1 – Project Setup and Requirements
- Discuss the functional requirements and overall goal of the application.
- Set up the mobile development environment (Android Studio / Xcode / VS Code).
- Define the project idea, which will be extended throughout the semester.

### Lab 2 – Native UI-Only CRUD Application
- Implement a mobile application using native technologies (Android with Java/Kotlin or iOS with Swift/Objective-C).
- Each CRUD operation (Create, Read, Update, Delete) must be implemented on a separate screen.
- The app uses in-memory or mock data (no database or server integration).

### Lab 3 – Cross-Platform UI-Only CRUD Application
- Re-implement the CRUD functionality using a cross-platform framework such as React Native, Flutter, or NativeScript.
- The same UI logic applies: one screen per CRUD operation, using in-memory data only.

### Lab 4 – CRUD with Local Database
- Extend either the native or cross-platform project to persist data locally.
- Integrate a local database (e.g., Room, SQLite, Realm).
- All CRUD operations must interact with the local database, fully offline-capable.

### Lab 5 – Server Integration (Client-Server Architecture)
- Implement remote data synchronization using a REST API.
- The application should:
  - Perform CRUD operations via HTTP requests to a custom backend server.
  - Fall back to local storage when offline and synchronize once connectivity is restored.
- The server must be implemented by the student and run locally or remotely (no cloud services).

---

## Functional Requirements for the Final Application

- The application must support all standard CRUD operations on business objects.
- The main screen should display a list of stored objects.
- Operations:
  - **Create**: Add new elements.
  - **Read**: View a list of elements.
  - **Update**: Modify existing elements.
  - **Delete**: Remove elements.
- Offline support:
  - Data must persist locally (survive restarts and offline usage).
  - Changes made while offline should be stored and synchronized automatically when back online.
- The app should notify the user when data is out-of-sync or when the server is unreachable.

---

## Technologies and Frameworks

- Native: Android (Kotlin)
- Cross-platform: React Native, Flutter, NativeScript
- Local storage: SQLite, Room, Realm
- Backend: Custom-built REST API (no cloud platforms)
- Version control: Git & GitHub (via IDE integration)


