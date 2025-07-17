## Backend Setup & Run Guide (AI-Health-Management)

This document walks a new contributor or evaluator through **environment preparation → database bootstrapping → service start-up → API access**.  Commands assume a POSIX shell (macOS / Linux); PowerShell equivalents work the same.

---

### 1  Prerequisites

| Component | Suggested version | Notes                                                                                  |
| --------- | ----------------- |----------------------------------------------------------------------------------------|
| **JDK**   | 17 LTS            | The code uses records and pattern matching—run `java -version` and confirm “17”.       |
| **Maven** | 3.9 +             | The repo contains `mvnw`; a global install is optional.                                |
| **MySQL** | 8.0.x             | Default encoding utf8mb4. MySQL 5.7 works after removing `CHECK` / `GENERATED` syntax. |
| **Redis** | 7.x or newer      | Use for Cache.                                                                         |
| **Git**   | any               | For cloning the repository.                                                            |

---

### 2  Get the source

```bash
git clone https://github.com/apexnewbie/ai-health-management.git
cd ai-health-management
```

---

### 3  Bootstrap the database

1. **Create an empty schema**

   ```sql
   CREATE DATABASE ai_health_db DEFAULT CHARSET utf8mb4;
   ```

2. **Import tables**

   Download or copy `ai_health_schema.sql` (merged script) and run:

   ```bash
   mysql -u root -p ai_health_db < ai_health_schema.sql
   ```

   After success, `SHOW TABLES;` should list `user`, `user_profile`, `user_activity`,
   `dietary_record`, `food_item`, and `user_mood`.

---

### 4  Configuration

The default settings live in `src/main/resources/application.properties`

```properties
spring.application.name=ai-health-management
spring.datasource.url=jdbc:mysql://localhost:3306/ai_health_db?useSSL=false&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=            # <- fill in if needed>
mybatis.configuration.map-underscore-to-camel-case=true

spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.database=0
```

---

### 5  Local run

```bash
./mvnw clean package -DskipTests        # compile
java -jar target/ai-health-management-0.0.1-SNAPSHOT.jar
```

Look for `Tomcat started on port(s): 8080` in the log.

---

### 6  Running the Project in IntelliJ IDEA (Optional)

1. **Open the Project**

   * `File → Open…` and select the `ai-health-management` folder
   * IntelliJ detects the `pom.xml` and imports the Maven project automatically
   * Wait for the “Maven import finished” notification (dependencies will be downloaded the first time)

2. **Start the Application**

   * Click the green ▶️  icon next to the run configuration name
   * Watch the *Run* tool window; when you see
     `Tomcat started on port(s): 8080 (http)`—the backend is live

3. **Verify**

   Open a browser at `http://localhost:8080/swagger-ui/index.html`.
   The Swagger UI should appear with all endpoints grouped by tag.

That’s it—no command-line Maven or Jar execution required; IntelliJ handles the build and class-path automatically. If you need to debug, simply rerun the same configuration in **Debug** mode (bug-icon).


---

### 7  Explore the API

Open a browser at:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger UI groups endpoints by Users / Diet / Food / Mood / Activity.

---



