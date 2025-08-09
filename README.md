# ToysMarket 🧸

Веб-приложение для продажи игрушек, построенное на Spring Boot.

## 🚀 Технологии

- **Backend**: Spring Boot 3.5.3, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5.3.3
- **База данных**: MySQL
- **Сборка**: Maven
- **Java**: 21

## 📋 Требования

- Java 21 или выше
- Maven 3.6+
- MySQL 8.0+

## ⚙️ Настройка проекта

### 1. Клонирование репозитория
```bash
git clone <repository-url>
cd toysmarket
```

### 2. Настройка базы данных
Создайте базу данных MySQL и пользователя:

```sql
CREATE DATABASE toysmarket;
CREATE USER 'toysmarket_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON toysmarket.* TO 'toysmarket_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Конфигурация приложения
Скопируйте файл конфигурации и заполните настройки:

```bash
cp src/main/resources/application.example.properties src/main/resources/application.properties
```

Отредактируйте `application.properties`:
```properties
spring.application.name=toysmarket
server.port=8080
spring.jpa.open-in-view=false

# Загрузка файлов
file.upload-dir=uploads/images/
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=5MB

# База данных
spring.datasource.url=jdbc:mysql://localhost:3306/toysmarket
spring.datasource.username=toysmarket_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### 4. Создание директории для загрузок
```bash
mkdir -p uploads/images
```

## 🏃‍♂️ Запуск приложения

### Через Maven
```bash
./mvnw spring-boot:run
```

### Через IDE
1. Импортируйте проект как Maven проект
2. Запустите класс `ToysmarketApplication`

Приложение будет доступно по адресу: http://localhost:8080

## 📁 Структура проекта

```
src/main/java/com/toysmarket/toysmarket/
├── config/          # Конфигурационные классы
├── controllers/     # REST и Web контроллеры
├── models/          # JPA сущности
├── repo/           # Управление сущностями
├── service/        # Бизнес-логика
└── ToysmarketApplication.java
```

## 🔐 Безопасность

- Настроен Spring Security для аутентификации
- Пароли хешируются с помощью BCrypt
- Загрузка файлов ограничена по размеру (5MB)

## 📝 Основные функции

- ✅ Регистрация и аутентификация пользователей
- ✅ Просмотр каталога товаров
- ✅ Загрузка и отображение изображений
- ✅ Карточки товаров с детальной информацией
- ✅ Профили пользователей

## 🛠️ Разработка

### Добавление новых зависимостей
Добавьте зависимости в `pom.xml` и выполните:
```bash
./mvnw clean install
```

### Структура базы данных
Приложение автоматически создает необходимые таблицы при первом запуске:
- `t_users` - пользователи
- `t_roles` - роли пользователей
- `t_users_roles` - связь пользователей и ролей
- `post_cards` - карточки товаров
- `post_card_images` - изображения товаров
- `t_users_cards` - связь пользователей и товаров

## 🚨 Важные заметки

1. **НЕ** добавляйте в репозиторий файл `application.properties` с реальными данными
2. Используйте `application.example.properties` как шаблон
3. Директория `uploads/` не отслеживается Git'ом
4. Убедитесь, что у приложения есть права на запись в директорию `uploads/`


## 📄 Лицензия

Этот проект создан в учебных целях. 