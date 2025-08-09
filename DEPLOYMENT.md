# Инструкции по деплою ToysMarket

## 🛡️ Подготовка

### 1. Проверка безопасности перед деплоем

**Обязательно проверьте:**
- ❌ Файл `application.properties` НЕ должен попасть в репозиторий
- ❌ Пароли и ключи НЕ должны быть в коде
- ❌ Директория `uploads/` НЕ должна быть в репозитории
- ✅ Все секреты вынесены в переменные окружения
- ✅ Настроен правильный `.gitignore`

### 2. Переменные окружения для продакшн

Создайте файл `.env` (НЕ добавляйте в Git!):
```bash
# База данных
DB_HOST=your-prod-db-host
DB_PORT=3306
DB_NAME=toysmarket_prod
DB_USERNAME=prod_user
DB_PASSWORD=super_secure_password

# Приложение
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Файлы
UPLOAD_DIR=/opt/toysmarket/uploads/
MAX_FILE_SIZE=5MB

# Безопасность
JWT_SECRET=your-very-long-secret-key-here
```

### 3. Продакшн конфигурация

Создайте `application-prod.properties`:
```properties
# Продакшн профиль
spring.profiles.active=prod

# База данных
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:toysmarket}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}

# JPA настройки для продакшн
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Логирование
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
logging.level.com.toysmarket=INFO

# Файлы
file.upload-dir=${UPLOAD_DIR:uploads/images/}
spring.servlet.multipart.max-file-size=${MAX_FILE_SIZE:5MB}

# Сервер
server.port=${SERVER_PORT:8080}
server.error.whitelabel.enabled=false
```

## 🚀 Деплой

### Docker 

Создайте `Dockerfile`:
```dockerfile
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/toysmarket-*.jar app.jar
COPY uploads/ uploads/

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

Сборка и запуск:
```bash
# Сборка
./mvnw clean package -DskipTests
docker build -t toysmarket .

# Запуск
docker run -d \
  --name toysmarket \
  -p 8080:8080 \
  -v /opt/toysmarket/uploads:/app/uploads \
  --env-file .env \
  toysmarket

```

## 📊 Мониторинг

### Spring Boot Actuator
Добавьте в `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Конфигурация:
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

### Логирование
```properties
# Логи в файл
logging.file.name=/var/log/toysmarket/application.log
logging.file.max-size=10MB
logging.file.max-history=5

# Формат логов
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```