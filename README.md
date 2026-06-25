# JavaFX Calculator

A desktop calculator application developed in Java using JavaFX and Maven. The application supports standard arithmetic operations, advanced mathematical functions, calculation history, and persistence through a MariaDB database. Calculation history can also be exported to JSON and XML.

---

## Features

- Basic arithmetic operations (`+`, `-`, `*`, `/`)
- Percentage calculations
- Exponentiation (`^`)
- Square root
- Factorial
- Memory functions
  - Memory Recall
  - Memory Add
  - Memory Subtract
  - Memory Clear
- Calculation history
- Export history to JSON and XML
- Browse and delete previous calculations through the graphical interface

---

## Technologies

- Java
- JavaFX
- Maven
- MariaDB
- JDBC
- JSON
- XML
- JUnit
- SLF4J
- exp4j

---

## Project Structure

```
calculator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── docs/
└── pom.xml
```

---

## Main Components

| Component | Description |
|-----------|-------------|
| `App` | Application entry point |
| `CalculatorController` | Handles UI interactions and calculator logic |
| `DBConnector` | Manages MariaDB connection, calculation history and export |
| `HistoryController` | Displays calculation history from JSON |
| `XMLHistoryController` | Displays calculation history from XML |
| `handlers` | User input handling |
| `operators` | Mathematical operation implementations |

---

## Build and Run

From the project root:

```bash
cd calculator
mvn clean javafx:run
```

---

## Database Configuration

The application uses a MariaDB database for storing calculation history.

Before running the application, update the database connection settings in `DBConnector` to match your local environment.

The required database table is created automatically if it does not already exist.

---

## Screenshots

Screenshots of the application can be placed in the `docs` directory.

Example:

```
docs/
└── calculator.png
```

Then reference it in the README:

```markdown
![Calculator](docs/calculator.png)
```

---

## Learning Outcomes

This project demonstrates practical experience with:

- Object-Oriented Programming (OOP)
- JavaFX desktop application development
- MVC architecture
- Database integration with JDBC
- JSON and XML serialization
- Maven project management
- Unit testing with JUnit
- Exception handling
- GUI event handling
