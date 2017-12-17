# KursRabAuto

## Краткое описание
Данная программа служит для организации совместного выполнения курсовых работ.
KursRabAuto помогает обеспечить централизацию хранения файлов с курсовыми работами, а также их постоянный мониторинг.
Также каждый раз, когда студент загружает какое-то обновление к курсовой работе, преподавателю приходит оповещение на Telergam.
Сейчас это консольное приложение.

## Принципы функционирования

Обязятельно наличия Google-аккаунта у преподавателя и у студентов.
Работа с курсовой работой с помощью KursRabAuto осуществляется в слудеющем порядке:

1. Преподаватель создает курсовую работу на своем Google Drive и дает студентам, выполняющим ее, разрешения
  `$ java -jar KursRabAuto create "Иванов-Петров ХХ.YY" ivanov@gmail.com petrov@gmail.com`
2. Студент может просмотреть список доступных ему работ
  `$ java -jar KursRabAuto `viewDocuments`
3. Студент скачивает документ
  `$ java -jar KursRabAuto get "Иванов-Петров ХХ.YY"`
4. Студент, после добавления материала к работе обновляет ее на Диске преподавателя
  `$ java -jar KursRabAuto update`

**Важно:** запуск программы необходимо осуществлять из директории с документом.
После пункта 4 программа активирует телеграм-бота и отылает оповещение преподавателю.

## Внутреннее строение

Программа KursRabAuto написана на языке Java. Для написания использованные сторонние библиотеки

  - com.google.api-client:google-api-client:1.23.0
  - com.google.oauth-client:google-oauth-client-jetty:1.23.0
  - com.google.apis:google-api-services-drive:v3-rev92-1.23.0
  - com.github.pengrad:java-telegram-bot-api:3.5.0

Библиотеки получены с помощью Maven, программа скомпилироывана в Gradle.
Программа состоит из следующих классов
 - Main - основной, где проиходит распределение по задачам
 - ExecutionManager - менеджер, подготавливающий параметры для обработки
 - FileManager - Непосредственная работа с файлама Google Drive. Вызывается из ExecutionManager
 - DBG - Вывод отладочной информации
 - Authorizer - авторизация на сервисе. Вызывается единожды при работе программы
 - MessageSendetTelegramBot - телеграм-бот для оповещений

 Порядок работы: Main --> ExecutionManager --> Authorizer и FileManager