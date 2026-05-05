
# Rutina Main Service

**Rutina Main Service** — микросервис для управления привычками пользователей. Отвечает за CRUD-операции с привычками, автоматическое завершение по таймеру и начисление очков.

Сервис является частью микросервисной системы **Rutina** и взаимодействует с:
- **Auth Service** — для получения информации о пользователях и обновления счётчиков
- **Gateway Service** — единая точка входа для клиентов
- **Android приложение** — мобильный клиент

---




## Что делает сервис

### Основные функции

| Функция | Endpoint | Метод |
|---------|----------|-------|
| Создание привычки | `/users/createHabits` | POST |
| Получение всех привычек пользователя | `/users/getHabits` | GET |
| Получение привычки по ID | `/users/getHabits/{habitId}` | GET |
| Удаление привычки | `/users/deleteHabits/{habitId}` | DELETE |
| Автоматическое завершение привычек | — | Scheduled |

### Бизнес-логика

1. **Создание привычки**:
   - Принимает `userId` из JWT-токена (через фильтр)
   - Создаёт привычку с указанным периодом формирования
   - Вычисляет `endedAt = now + formationPeriod`
   - Вызывает **Auth Service** для увеличения счётчика привычек (`incrementHabitsCount`)

2. **Автоматическое завершение** (каждые 60 секунд):
   - Находит все привычки, у которых `endedAt < now`
   - За каждую завершённую привычку начисляет **1 очко** через Auth Service
   - Удаляет завершённую привычку из БД
   - Вызывает **Auth Service** для уменьшения счётчика (`decrementHabitsCount`)

3. **Удаление привычки**:
   - Удаляет привычку по ID (только если принадлежит пользователю)
   - Уменьшает счётчик активных привычек

---

## Технологический стек

- **Kotlin**
- **Spring Boot 3.2.0**
- **Spring Data JPA** (Hibernate)
- **Spring Cloud OpenFeign** — для взаимодействия с Auth Service
- **Spring Scheduling** — для автоматического завершения привычек
- **PostgreSQL** — основная база данных
- **Jakarta Servlet API** — фильтр валидации JWT
- **Gradle** — сборка проекта

---

## Структура проекта

```
src/main/kotlin/com/example/demo/
├── client/
│   └── AuthClient.kt              # Feign-клиент к Auth Service
├── config/
│   └── FilterConfig.kt            # Конфигурация JWT фильтра
├── controller/
│   └── UserController.kt          # REST контроллер привычек
├── database/
│   ├── dao/
│   │   └── HabitsDao.kt           # Репозиторий привычек
│   └── entity/
│       ├── AbstractEntity.kt      # Базовый класс сущности
│       └── habits.kt              # Сущность привычки
├── filter/
│   └── JwtValidationFilter.kt     # Фильтр валидации JWT токенов
├── model/
│   ├── dto/
│   │   ├── rq/
│   │   │   └── CreateHabitsRq.kt  # DTO запроса на создание
│   │   └── rs/
│   │       ├── HabitsDto.kt       # DTO привычки
│   │       └── UserDto.kt         # DTO пользователя (из Auth)
│   ├── mapper/
│   │   └── HabitsMapper.kt        # Маппер сущность ↔ DTO
│   └── TypeOfHabits.kt            # Enum типов привычек
├── service/
│   ├── HabitsCompletionService.kt # Интерфейс завершения привычек
│   ├── UserService.kt             # Интерфейс сервиса привычек
│   └── impl/
│       ├── HabitsCompletionServiceImpl.kt  # Логика автозавершения
│       └── UserServiceImpl.kt             # CRUD привычек
└── MainApplication.kt             # Точка входа Spring Boot
```





---

## API

### `POST /users/createHabits`

Создание новой привычки.

**Заголовок:** `Authorization: Bearer {jwt_token}`

**Тело запроса:**
```json
{
  "name": "Бег по утрам",
  "description": "Бегать 30 минут каждое утро",
  "type": "Спорт",
  "formationPeriod": 30
}
```

**Ответ:**
```json
{
  "id": 1,
  "name": "Бег по утрам",
  "description": "Бегать 30 минут каждое утро",
  "type": "Спорт",
  "formationPeriod": 30,
  "createdAt": "2026-05-01T10:00:00",
  "endedAt": "2026-05-01T10:30:00",
  "userId": 1
}
```

### `GET /users/getHabits`

Получение всех привычек пользователя.

**Заголовок:** `Authorization: Bearer {jwt_token}`

**Ответ:**
```json
[
  {
    "id": 1,
    "name": "Бег по утрам",
    "description": "Бегать 30 минут каждое утро",
    "type": "Спорт",
    "formationPeriod": 30,
    "createdAt": "2026-05-01T10:00:00",
    "endedAt": "2026-05-01T10:30:00",
    "userId": 1
  }
]
```

### `GET /users/getHabits/{habitId}`

Получение конкретной привычки.

**Заголовок:** `Authorization: Bearer {jwt_token}`

### `DELETE /users/deleteHabits/{habitId}`

Удаление привычки (только своей).

**Заголовок:** `Authorization: Bearer {jwt_token}`

---

## Межсервисное взаимодействие

### Feign-клиент к Auth Service

```kotlin
@FeignClient(name = "auth-service", url = "${auth.service.url}")
interface AuthClient {
    
    @GetMapping("/auth/validate")
    fun validateToken(@RequestHeader("Authorization") token: String): UserDto
    
    @PostMapping("/auth/user/{id}/increment-habits")
    fun incrementHabitsCount(@PathVariable("id") id: Long)
    
    @PostMapping("/auth/user/{id}/decrement-habits")
    fun decrementHabitsCount(@PathVariable("id") id: Long)
    
    @PostMapping("/auth/user/{id}/add-score")
    fun addScore(@PathVariable("id") id: Long, @RequestParam points: Int)
}
```

**Как работает:**
- При создании привычки → `POST /auth/user/{id}/increment-habits`
- При завершении привычки → `POST /auth/user/{id}/add-score` (очки) + `POST /auth/user/{id}/decrement-habits` (счётчик)
- При удалении привычки → `POST /auth/user/{id}/decrement-habits`

---

## JWT-валидация

Сервис не хранит пользователей. Все запросы проходят через `JwtValidationFilter`:

1. Извлекает токен из заголовка `Authorization`
2. Вызывает **Auth Service** через Feign для валидации
3. Получает `UserDto` (id, email, роль)
4. Сохраняет `userId` в атрибуты запроса
5. Контроллер использует `@RequestAttribute("userId")` для идентификации

---

## Логика завершения привычек

```kotlin
@Scheduled(fixedDelay = 60000)  // Каждые 60 секунд
@Transactional
override fun checkAndCompleteHabits() {
    val habitsToDelete = habitsDao.findAllByEndedAtBefore(LocalDateTime.now())
    
    if (habitsToDelete.isNotEmpty()) {
        habitsToDelete.forEach { completeHabit(it) }
    }
}

@Transactional
override fun completeHabit(habit: habits) {
    authClient.addScore(habit.userId, 1)       // Начислить очко
    habitsDao.delete(habit)                     // Удалить привычку
    authClient.decrementHabitsCount(habit.userId)  // Уменьшить счётчик
}
```

---

## Конфигурация

### `application.yaml`

```yaml
server:
  port: 8083

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/main_db
    username: postgres
    password: your_password

auth:
  service:
    url: http://localhost:8082  # URL Auth Service
```

---



## Зависимости проекта

| Сервис | Порт | Репозиторий |
|--------|------|-------------|
| Gateway Service | 8080 | [Rutina Gateway](https://github.com/ladatkoS/Rutina_Gateway_Service.git) |
| Auth Service | 8082 | [Rutina Auth](https://github.com/ladatkoS/Rutina_Auth_Service.git) |
| **Main Service** | **8083** | **этот репозиторий** |
| Android App | — | [Rutina Android](https://github.com/ladatkoS/Rutina_Android.git) |
| Neural Network | 8000 | [Rutina NN](https://github.com/AntonSlon/Rutina-neural-network.git) |

---
