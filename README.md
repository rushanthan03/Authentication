# Authentication

A Spring Boot application for authentication and authorization.

## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Installation

### Prerequisites

- Java JDK 11 or higher
- Gradle 6.0 or higher
- PostgreSQL

### Steps

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    cd your-repo-name
    ```

2. **Configure the database:**

    Ensure that PostgreSQL is installed and running. Create a database for the application.

    ```sql
    CREATE DATABASE your_db_name;
    ```

3. **Update the application properties:**

    Edit `src/main/resources/application.properties` with your database and email configuration.

    ```properties
    spring.application.name=auth
    server.port=8080

    # DbConfigurations
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.jdbc.batch_size=5
    spring.jpa.properties.hibernate.order_inserts=true
    spring.jpa.hibernate.ddl-auto=update
    spring.sql.init.mode=always
    spring.jpa.defer-datasource-initialization=true
    spring.servlet.multipart.max-file-size=100MB
    spring.servlet.multipart.max-request-size=100MB

    springdoc.swagger-ui.operationsSorter=method
    springdoc.swagger-ui.path=/swagger-ui.html

    auth.openapi.prod-url=https://auth/
    url=http://localhost:8080/

    auth.web.jwtSecret=2345678910abcdefghijklmnopqrrknglrnglsnflkenfklefeklfeklnf
    notify.base.url=your_notify_url

    # Spring health actuator endpoint properties
    management.endpoints.web.base-path=/auth-app
    management.endpoints.web.path-mapping.health=healthcheck

    # Forgot password otp expiry time
    user.verify.token.expiray.time=600000

    # Email properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@example.com
    spring.mail.password=your_email_password
    spring.mail.protocol=smtp
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```

4. **Replace placeholders:**

    - Replace `your_db_name`, `your_db_username`, and `your_db_password` with your actual database name, username, and password.
    - Replace `your_email@example.com` and `your_email_password` with your actual email address and password.
    - Replace `your_notify_url` with your actual Notify account URL.

5. **Build the project:**

    ```bash
    ./gradlew build
    ```

6. **Run the project:**

    ```bash
    ./gradlew bootRun
    ```

## Usage

### Running the Application

To start the application, use the following command:

```bash
./gradlew bootRun


## Accessing the Application
Once the application is running, you can access it at http://localhost:8080.

## API Documentation
The Swagger UI can be accessed at http://localhost:8080/swagger-ui.html.

A Spring Boot application for authentication and authorization.

## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Installation

### Prerequisites

- Java JDK 11 or higher
- Gradle 6.0 or higher
- PostgreSQL

### Steps

1. **Clone the repository:**

    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    cd your-repo-name
    ```

2. **Configure the database:**

    Ensure that PostgreSQL is installed and running. Create a database for the application.

    ```sql
    CREATE DATABASE your_db_name;
    ```

3. **Update the application properties:**

    Edit `src/main/resources/application.properties` with your database and email configuration.

    ```properties
    spring.application.name=auth
    server.port=8080

    # DbConfigurations
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.jdbc.batch_size=5
    spring.jpa.properties.hibernate.order_inserts=true
    spring.jpa.hibernate.ddl-auto=update
    spring.sql.init.mode=always
    spring.jpa.defer-datasource-initialization=true
    spring.servlet.multipart.max-file-size=100MB
    spring.servlet.multipart.max-request-size=100MB

    springdoc.swagger-ui.operationsSorter=method
    springdoc.swagger-ui.path=/swagger-ui.html

    auth.openapi.prod-url=https://auth/
    url=http://localhost:8080/

    auth.web.jwtSecret=2345678910abcdefghijklmnopqrrknglrnglsnflkenfklefeklfeklnf
    notify.base.url=your_notify_url

    # Spring health actuator endpoint properties
    management.endpoints.web.base-path=/auth-app
    management.endpoints.web.path-mapping.health=healthcheck

    # Forgot password otp expiry time
    user.verify.token.expiray.time=600000

    # Email properties
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@example.com
    spring.mail.password=your_email_password
    spring.mail.protocol=smtp
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```

4. **Replace placeholders:**

    - Replace `your_db_name`, `your_db_username`, and `your_db_password` with your actual database name, username, and password.
    - Replace `your_email@example.com` and `your_email_password` with your actual email address and password.
    - Replace `your_notify_url` with your actual Notify account URL.

5. **Build the project:**

    ```bash
    ./gradlew build
    ```

6. **Run the project:**

    ```bash
    ./gradlew bootRun
    ```

## Usage

### Running the Application

To start the application, use the following command:

```bash
./gradlew bootRun


## Accessing the Application
Once the application is running, you can access it at http://localhost:8080.

## API Documentation
The Swagger UI can be accessed at http://localhost:8080/swagger-ui.html.
