# CustomeSQLDatabase

## Overview

This project is a prototype of a lightweight Database Management System (DBMS) implemented in Java. It provides a console-based interface for SQL query execution, features two-factor user authentication, and manages transactions with ACID properties. Data is stored persistently in a custom text format.

## Features

- **SQL Query Support**: Handles `CREATE`, `SELECT`, and `INSERT` SQL commands.
- **Two-Factor Authentication**: Implements user authentication with ID, password, and a CAPTCHA.
- **Transaction Management**: ACID-compliant transactions supporting `BEGIN TRANSACTION`, `COMMIT`, and `ROLLBACK`.
- **Persistent Storage**: Stores data in a custom text format with user-defined delimiters.

## Requirements

- Java Development Kit (JDK) - any version is acceptable
- Standard Java libraries (no third-party libraries used)

## Getting Started

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Keval-Gandevia/CustomSQLDatabase.git
   ```

2. **Navigate to the Project Directory**

   ```bash
   cd CustomSQLDatabase
   ```

3. **Compile the Code**

   ```bash
   javac -d bin src/*.java
   ```

4. **Compile the Code**

   ```bash
   java -cp bin Main
   ```

## Usage

1. **Register**  
   Users must register first with their email, password, and CAPTCHA. Registration data is saved in an authentication file (.txt).

2. **Login**  
   Authenticate with your registered email, password, and CAPTCHA.

3. **Execute SQL Queries**  
   Enter SQL commands to create tables, insert data, and select records.

4. **Manage Transactions**  
   Use `BEGIN TRANSACTION`, `COMMIT`, and `ROLLBACK` commands to handle transactions.