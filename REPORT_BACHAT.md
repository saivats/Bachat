# Smart Expense Tracker for Students

## Project Name
**Bachat**

## 1. INTRODUCTION

### 1.1 Background of Project
Students who move to a new city often face difficulty in managing daily expenses such as food, travel, rent contribution, books, and entertainment. Without a proper tracking system, it becomes hard to know where money is being spent. The Bachat project is a simple Java-based command line application designed to help students record and monitor their daily expenses in an organized way.

### 1.2 Problem Statement
Arsh has moved to a new city for studies and needs an easy system to record expenses, manage a monthly budget, and analyze spending patterns. Manual tracking in notebooks is time-consuming and error-prone. Therefore, a computerized solution is needed.

### 1.3 Objective of Project
- To create a simple menu-driven Java application for expense tracking.
- To store expense records permanently using file handling.
- To allow the user to add, edit, delete, and view expenses.
- To set a monthly budget and display warnings when the limit is crossed.
- To generate category-wise spending insights for better financial planning.

### 1.4 Scope of Project
- The project is limited to a single-user CLI application.
- It manages student expense records and monthly budget.
- It uses text and CSV files for data storage.
- It does not include graphical interface, database, or online synchronization.

### 1.5 Gantt Chart (Text Table)
| Week | Activity |
|------|----------|
| Week 1 | Problem identification and requirement collection |
| Week 2 | System design and class planning |
| Week 3 | Coding main modules and menu-driven system |
| Week 4 | File handling and exception handling |
| Week 5 | Testing and debugging |
| Week 6 | Report preparation and final submission |

## 2. LITERATURE REVIEW

### 2.1 Existing System
Many students use notebooks, spreadsheets, or mobile note applications to write down expenses. Some may use general calculator applications or banking SMS history to estimate spending.

### 2.2 Limitations
- Manual records can be lost or forgotten.
- Spreadsheets are not always beginner-friendly.
- Existing methods do not automatically provide budget warnings.
- It is difficult to get category-wise analysis without proper software logic.
- Many available applications are complex for basic student use.

## 3. SYSTEM DESIGN

### 3.1 Use Case (Text Format)
**Actor:** Student User

**Use Cases:**
- Create user profile
- Add expense
- Edit expense
- Delete expense
- View all expenses
- Set monthly budget
- View total spending
- View category-wise breakdown
- Check budget status and warning

### 3.2 Class Diagram (Text Representation)
```text
+----------------+
|     Main       |
+----------------+
| +main()        |
+----------------+
        |
        | uses
        v
+-------------------+        +-------------------+
|  ExpenseManager   |<------>|    FileHandler    |
+-------------------+        +-------------------+
| -expenses         |        | +saveExpenses()   |
| +addExpense()     |        | +loadExpenses()   |
| +editExpense()    |        | +saveBudget()     |
| +deleteExpense()  |        | +loadBudget()     |
| +getTotalSpending()|       | +saveUser()       |
| +getCategoryWiseBreakdown()| +loadUser()       |
+-------------------+        +-------------------+
        |
        | manages
        v
+-------------------+
|      Expense      |
+-------------------+
| -id               |
| -amount           |
| -category         |
| -date             |
| -note             |
+-------------------+

+-------------------+        +-------------------+
|   BudgetManager   |<------>|    FileHandler    |
+-------------------+        +-------------------+
| -monthlyBudget    |
| +setMonthlyBudget()|
| +getBudgetWarning()|
+-------------------+

+-------------------+
|       User        |
+-------------------+
| -name             |
| -city             |
+-------------------+
```

### 3.3 Sequence Diagram (Step Flow)
```text
1. User starts the application.
2. Main class loads user profile, expenses, and budget through FileHandler.
3. Main displays menu options.
4. User selects an operation such as Add Expense.
5. Main collects input from the user.
6. ExpenseManager processes the expense data.
7. FileHandler stores updated expense data into expenses.csv.
8. BudgetManager checks total spending against monthly budget.
9. System shows confirmation and warning message if needed.
10. User continues until Exit is selected.
```

## 4. IMPLEMENTATION

### 4.1 Development Environment
- Operating System: Windows
- Language: Java
- IDE: IntelliJ IDEA or any text editor
- JDK Version: JDK 17 or compatible version

### 4.2 Technologies Used
- Core Java
- OOP concepts
- ArrayList collection
- File handling using `BufferedReader` and `BufferedWriter`
- Exception handling using `try-catch`
- CLI-based interaction using `Scanner`

### 4.3 Java Module Description
- **Main.java**: Controls the program flow and menu system.
- **User.java**: Stores the student profile details.
- **Expense.java**: Represents one expense record.
- **ExpenseManager.java**: Manages add, edit, delete, and analysis of expenses.
- **BudgetManager.java**: Handles budget saving and warning logic.
- **FileHandler.java**: Reads and writes data from files.

### 4.4 Code Structure
```text
Bachat Project
|-- Main.java
|-- User.java
|-- Expense.java
|-- ExpenseManager.java
|-- BudgetManager.java
|-- FileHandler.java
|-- REPORT_BACHAT.md
|-- expenses.csv           (created automatically at runtime)
|-- budget.txt             (created automatically at runtime)
|-- user.txt               (created automatically at runtime)
```

## 5. OUTPUT SNAPSHOTS

### 5.1 Startup Screen
```text
======================================
      Welcome to Bachat
 Smart Expense Tracker for Students
======================================
Let's set up your profile.
Enter your name: Arsh
Enter your city: Pune
Profile saved successfully.
Hello, Arsh from Pune.
```

### 5.2 Add Expense
```text
1. Add Expense
Enter amount: 250
Enter category: Food
Enter date (YYYY-MM-DD): 2026-03-18
Enter note: Lunch at college canteen
Expense added successfully with ID: 1
```

### 5.3 View All Expenses
```text
ID | Amount | Category | Date | Note
-----------------------------------------------
1 | 250.00 | Food | 2026-03-18 | Lunch at college canteen
2 | 80.00 | Travel | 2026-03-18 | Auto fare
```

### 5.4 Set Monthly Budget
```text
Enter monthly budget: 5000
Monthly budget saved successfully.
```

### 5.5 Category-wise Breakdown
```text
Category-wise Breakdown:
Food : 1250.00
Travel : 620.00
Books : 950.00
```

### 5.6 Budget Warning
```text
Monthly budget: 5000.00
Current spending: 5400.00
Remaining budget: -400.00
Warning: Budget exceeded by 400.00
```

## Conclusion
The Bachat project provides a simple and effective expense tracking system for students. It uses object-oriented programming concepts, file handling, and exception handling to create a complete Java CLI application. The system helps users control spending, monitor budget, and view financial insights in a beginner-friendly format.
