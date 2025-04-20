# Activité Pratique N°3 - Spring MVC, Spring Data JPA Thymeleaf

## creation de la classe Patient

```java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Date dateNaissance;
    private boolean malade;
    private int score;
}
```
## creation de la classe PatientRepository

```java
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
```
## creation des instances de Patient
### utilisant le constructeur sans paramètres
```java
Patient patient2 = new Patient();
		patient2.setNom("karim");
		patient2.setDateNaissance(new Date());
		patient2.setMalade(false);
		patient2.setScore(5);
```
### utilisant le constructeur avec paramètres
```java
Patient patient1 = new Patient(null, "ahmed", new Date(), true, 10);
```
### utilisant le constructeur avec Builder
```java
Patient patient3 = Patient.builder()
				.nom("souad")
				.dateNaissance(new Date())
				.malade(true)
				.score(8)
				.build();
```
## Enregister les instances de Patient
```java
patientRepository.save(patient1);
patientRepository.save(patient2);
patientRepository.save(patient3);
```
![Voir l'image](/images/addPatients.png)

## Ceation de la template patients.html
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patients</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.5/css/bootstrap.min.css">
</head>
<body>
<div class="p-3">
    <div class="card">
        <div class="card-header">Liste patients</div>
        <div class="card-body">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Date de naissance</th>
                    <th>Malade</th>
                    <th>score</th>
                </tr>
                <tr th:each="p : ${listePatients}">
                    <td th:text="${p.id}"></td>
                    <td th:text="${p.nom}"></td>
                    <td th:text="${p.dateNaissance}"></td>
                    <td th:text="${p.malade}"></td>
                    <td th:text="${p.score}"></td>
                </tr>
                </thead>
            </table>
        </div>

    </div>
</div>


</body>
```
![Voir l'image](/images/listePatients.png)

## Migrer vers MySQL
### 1. Ajouter la dépendance MySQL dans le fichier pom.xml
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```
### 2. Configurer le fichier application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/patients-bd?createDatabaseIfNotExist=true
spring.datasource.username=redone
spring.datasource.password=redone
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```
## added Pagination
### 1. mise a jour de la classe web PatientController
```java
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "0") int page,
                        @RequestParam(name="size", defaultValue = "4") int size){
        Page<Patient> pagePatients=patientRepository.findAll(PageRequest.of(page,size));
        model.addAttribute("listePatients",pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "patients";
    }
}
```
### 2. mise a jour de la template patients.html
```html
<ul class="nav nav-pills">
     <li th:each="page,status: ${pages}">
     <a th:href="@{/index(page=${status.index})}" 
     th:class="${currentPage==status.index} ? 'btn btn-info ms-1' : 'btn btn-outline-info ms-1'"
     th:text="${status.index+1}"></a>
</ul>
```
![Voir l'image](/images/pagination.png)




