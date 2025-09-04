📦 Spring Boot Export Demo

Це перша версія прикладу, який показує, як у Spring Boot 3.3 (Java 21) можна експортувати дані у три поширені формати:
    
    JSON
    CSV
    XML

У прикладі ми працюємо з сутністю Employee, яка має прості поля (id, name, age) і складніші (масив skills, вкладений об’єкт address, дати, metadata у вигляді Map).

1. JSON
 
🔹 Приклад результату
 
      [ {
       "id" : "dfc6e332-4145-4593-ba01-2d09d1526a41",
       "name" : "Alice",
       "age" : 30,
       "skills" : [ "Java", "Spring Boot" ],
       "address" : {
       "street" : "Main St",
       "city" : "Kyiv",
       "country" : "UA"
       },
       "hireDate" : "2025-09-03",
       "lastLogin" : "2025-09-03T15:24:43.798197",
       "metadata" : {
       "level" : "senior",
       "remote" : true
       }
       } ]

🔹 Пояснення

    JSON — нативний формат для Jackson, тому все серіалізується без проблем.
    Масив skills зберігається як ["Java", "Spring Boot"].
    Вкладений об’єкт address лишається вкладеним JSON.
    metadata (Map) зберігається як JSON-об’єкт.
    Дати (LocalDate, LocalDateTime) зберігаються у стандартному ISO-8601 форматі.

2. CSV
   
🔹 Приклад результату

       id,name,age,skills,street,city,country,hireDate,lastLogin,metadata
       "20d15f06-a7c0-4e04-8fb6-27766daf2296",Alice,30,"Java;Spring Boot","Main St",Kyiv,UA,2025-09-03,"2025-09-03T15:54:12.71391","{""remote"":true,""level"":""senior""}"

🔹 Пояснення

    CSV — це плоский табличний формат, тому ми зробили окремий DTO (EmployeeCsvDto) без вкладених структур.
    skills → автоматично перетворився у строку "Java;Spring Boot" (бо CSV не підтримує справжні масиви).
    address → ми «розклали» на окремі поля (street, city, country).
    metadata → серіалізовано у JSON-рядок, тому в CSV бачимо подвійні лапки і escaped-лапки:

        "{""remote"":true,""level"":""senior""}"


    Це норма для CSV, бо інакше JSON зламав би структуру.
    Дати серіалізуються як ISO-рядки.

3. XML
   
🔹 Приклад результату

    <employees>
       <employee>
       <id>f1e791ca-08f0-43d6-95e6-2a9a789fdca3</id>
       <name>Alice</name>
       <age>30</age>
       <skills>
       <skills>Java</skills>
       <skills>Spring Boot</skills>
       </skills>
        <address>
          <street>Main St</street>
          <city>Kyiv</city>
          <country>UA</country>
        </address>
        <hireDate>2025-09-03</hireDate>
        <lastLogin>2025-09-03T16:17:48.111594</lastLogin>
        <metadata>
          <level>senior</level>
          <remote>true</remote>
        </metadata>
      </employee>
    </employees>

🔹 Пояснення

    XML підтримує вкладені структури, тому address, metadata, skills виглядають охайно.
    Щоб уникнути некрасивого <List12><item>...</item></List12>, ми створили окремий DTO для XML:
        EmployeeXmlDto — для одного елемента.
        EmployeeListXmlDto — як обгортка (<employees>).

    Анотації:
        @JacksonXmlRootElement(localName = "employees") — визначає назву кореневого тегу.
        @JacksonXmlElementWrapper(useWrapping = false) — щоб не було <employees><employees>....
        @JacksonXmlProperty(localName = "employee") — визначає назву тегу для кожного елемента списку.

4. Висновки

        JSON → найгнучкіший, підходить для будь-яких вкладених структур.
        CSV → завжди плоский, тому робимо спеціальний DTO з розгортанням об’єктів і масивів у рядки.
        XML → підтримує вкладені структури, але потребує спеціальних DTO і Jackson-анотацій для красивого кореневого тегу.
        Дати (LocalDate, LocalDateTime) в усіх форматах серіалізуються через модуль jackson-datatype-jsr310.

-----------------------------------------------------------------------------------------------------------------------------------

## Localization in CSV Export

### Як працює
- Використовується Spring `MessageSource`, який читає файли з ресурсів:  
  src/main/resources/messages/messages_en.properties
  src/main/resources/messages/messages_uk.properties

pgsql
Copy code
- У цих файлах зберігаються тільки **ключі для заголовків CSV**, наприклад:
```properties
# messages_en.properties
export.school.header.id=School ID
export.school.header.name=School Name
export.school.header.city=City
export.school.header.studentsCount=Number of Students

# messages_uk.properties
export.school.header.id=Ідентифікатор
export.school.header.name=Назва школи
export.school.header.city=Місто
export.school.header.studentsCount=Кількість учнів
```

- DTO (SchoolCsvDto) містить тільки дані (id, name, city, studentsCount).
- SchoolExportTranslationService читає ключі з MessageSource і формує локалізовані заголовки.
- SchoolCsvExporter мапить DTO → Map з локалізованими ключами та будує CSV з правильними заголовками.

Приклад результату
EN (/api/schools/export?lang=en):

csv
```
School ID,School Name,City,Number of Students
1,Kyiv School #1,Kyiv,500
2,Lviv Gymnasium,Lviv,350
```

UK (/api/schools/export?lang=uk):

csv
```
Ідентифікатор,Назва школи,Місто,Кількість учнів
1,Kyiv School #1,Kyiv,500
2,Lviv Gymnasium,Lviv,350
```

Project Structure (скорочено)
```
org.example.filedemo
├── config
│     └── MessageConfig.java
│
├── controller
│     └── SchoolExportController.java
│
├── dto
│     └── SchoolCsvDto.java
│
├── service
│     ├── AbstractExportTranslationService.java
│     ├── SchoolExportTranslationService.java
│     ├── SchoolCsvExporter.java
│     └── SchoolExportService.java
│
└── resources/messages
├── messages_en.properties
└── messages_uk.properties
```


---
## 📥 Import CSV → Database
Як працює

- CSV-файл завантажується через REST API (/api/schools/import).
- Контролер (SchoolImportController) делегує логіку сервісу (SchoolImportService).
- Сервіс читає файл за допомогою Jackson CsvMapper, мапить у DTO (SchoolCsvDto), потім у Entity (School), і зберігає через SchoolRepository у Postgres.

DTO (для імпорту)
```
public record SchoolCsvDto(
Long id,
String name,
String city,
Integer studentsCount
) {}
```

Entity (зберігається у БД)
```
@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String name;
    private String city;
    private Integer studentsCount;
}
```

### Приклад CSV файлу

importfile/schools.csv
```
id,name,city,studentsCount
1,Kyiv School #1,Kyiv,500
2,Lviv Gymnasium,Lviv,350
```

Виклик API (Postman / curl)
```
curl --location 'http://localhost:8080/api/schools/import' \
--header 'Content-Type: multipart/form-data' \
--form 'file=@"/Users/nchykur/IdeaProjects/my_projects/file-demo/importfile/schools.csv"'
```


✅ Результат:

- Spring Boot парсить файл.
- Дані зберігаються у таблиці schools.

У Postgres після запиту:
```
SELECT * FROM schools;
```

отримаємо:
    
    id |      name        | city | students_count
    ----+------------------+------+----------------
    1 | Kyiv School #1   | Kyiv |            500
    2 | Lviv Gymnasium   | Lviv |            350

Project Structure (оновлено)
```
org.example.filedemo
├── importfile
│    ├── controller
│    │     └── SchoolImportController.java
│    ├── entity
│    │     └── School.java
│    ├── repository
│    │     └── SchoolRepository.java
│    └── service
│          └── SchoolImportService.java
│
└── resources/importfile/schools.csv
```