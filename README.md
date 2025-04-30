# MyStressApp

Приложение для мониторинга уровня стресса на основе физиологических показателей.

## Описание

MyStressApp - это нативное Android-приложение, которое:
- Собирает данные с носимых устройств через BLE (GSR, температура кожи, акселерометр)
- Получает данные о пульсе и HRV через Google Fit
- Анализирует данные с помощью модели машинного обучения
- Отображает текущий уровень стресса пользователя

## Архитектура

Приложение построено по принципам чистой архитектуры:

```
app/
├── data/                 # Слой данных
│   ├── BleManager.kt    # Работа с BLE-устройствами
│   ├── FitManager.kt    # Работа с Google Fit
│   ├── TfliteModelInterpreter.kt # Работа с ML моделью
│   └── StressRepository.kt # Репозиторий для работы с данными
├── domain/              # Слой бизнес-логики
│   ├── model/          # Модели данных
│   └── usecase/        # Use cases
└── ui/                  # Слой представления
    ├── screens/        # Экраны приложения
    └── viewmodel/      # ViewModels
```

## Требования

- Android 6.0 (API 23) или выше
- Поддержка BLE
- Аккаунт Google с доступом к Google Fit
- BLE-устройство с поддержкой:
  - GSR (кожно-гальваническая реакция)
  - Температура кожи
  - Акселерометр

## Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/yourusername/MyStressApp.git
```

2. Откройте проект в Android Studio

3. Создайте файл `local.properties` и добавьте путь к SDK:
```properties
sdk.dir=/path/to/your/android/sdk
```

4. Соберите и запустите приложение

## Настройка

1. Разрешения:
   - BLUETOOTH
   - BLUETOOTH_SCAN
   - BLUETOOTH_CONNECT
   - ACTIVITY_RECOGNITION
   - BODY_SENSORS

2. Google Fit:
   - Войдите в аккаунт Google
   - Предоставьте доступ к данным о пульсе и HRV

3. BLE-устройство:
   - Включите Bluetooth
   - Подключите устройство через настройки приложения

## Использование

1. Запустите приложение
2. Войдите в аккаунт Google
3. Подключите BLE-устройство
4. Приложение автоматически начнет сбор и анализ данных
5. Текущий уровень стресса отображается на главном экране

## Разработка

### Структура проекта

- `app/src/main/java/com/example/stressmonitor/`
  - `data/` - работа с данными
  - `domain/` - бизнес-логика
  - `ui/` - пользовательский интерфейс
  - `di/` - внедрение зависимостей

### Зависимости

Основные зависимости:
- AndroidX Core, AppCompat
- Jetpack Compose
- Nordic BLE Library
- Google Fit API
- TensorFlow Lite

### Тестирование

```bash
./gradlew test  # Запуск unit-тестов
./gradlew connectedAndroidTest  # Запуск UI-тестов
```

## Лицензия

MIT License 