# Super Snake Game

An upgraded and modular version of the classic Snake game, built with enhanced gameplay features, UI improvements, and modern object-oriented design principles.

---

## Features

- **High Score System**: Tracks and saves the top scores using `SharedPreferences`.
- **Leaderboard & UI Screens**: Includes title screen, game over screen, and restart functionality with the top 3 high scores.
- **Dynamic Snake Model**: Curved snake turns with head/body/tail modular rendering using the **Decorator Pattern**.
- **Power-Ups**: Collect a golden apple to become invincible and destroy obstacles for 5 seconds.
-  **Obstacles**: Includes 3 types:
  - Moving Obstacle (patrolling enemy)
  - Bomb Obstacle (removes score and snake length)
  - Wall Obstacle (causes instant game over)
- **Difficulty Progression**: More obstacles appear as score increases.
-  **Sound Engine**: Centralized audio management with background music and sound effects, implemented using the **Singleton Pattern**.

---


## Technologies & Concepts

- Java (Android Studio)
- Object-Oriented Programming (OOP)
- SharedPreferences for data persistence
- Modular class design for scalability and readability

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/super-snake-game.git
Open the project in Android Studio.
Build and run the app on an emulator or physical device.
