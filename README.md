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

## Chercher un patient
### 1. mise a jour de la classe web PatientController
```java
public class PatientController {
    private PatientRepository patientRepository;
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "0") int page,
                        @RequestParam(name="size", defaultValue = "4") int size,
                        @RequestParam(name="keyword", defaultValue = "") String keyword){
        Page<Patient> pagePatients=patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("listePatients",pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }
}
```
### 2. mise a jour de la template patients.html
```html
<form method="get" th:action="${index}">
        <label>Keyword</label>
        <input type="text" name="keyword" th:value="${keyword}"/>
        <button type="submit" class="btn btn-info">Chercher</button>
</form>

<ul class="nav nav-pills">
                <li th:each="page,status: ${pages}">
                <a th:href="@{/index(page=${status.index},keyword=${keyword})}"
                   th:class="${currentPage==status.index} ? 'btn btn-info ms-1' : 'btn btn-outline-info ms-1'"
                   th:text="${status.index+1}"></a>
</ul>
```
![Voir l'image](/images/chercher.png)

## Supprimer un patient a partir du template
### 1. mise a jour de la classe web PatientController: ajout de la method delete
```java
@GetMapping("/delete")
    public String delete(Long id,String keyword,int page)
    {
        patientRepository.deleteById(id);
        return "redirect:/index?page"+page+"&keyword="+keyword;
    }

```
### 2. mise a jour de la template patients.html
```html
 <td>
      <a onclick="return confirm('etes vous sure?')"
      th:href="@{delete(id=${p.id}, keyword=${keyword},page=${currentPage})}" class="btn btn-danger">Delete</a>
 </td>

```
![Voir l'image](/images/delete1.png)
la Supression du patent Ahmed: id=4
![Voir l'image](/images/delete2.png)


## CReation de template 1
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" >
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="\webjars\bootstrap\5.3.5\css\bootstrap.min.css">
    <script src="/webjars/bootstrap/5.3.5/js/bootstrap.bundle.js"></script>
</head>
<body>
<!-- A grey horizontal navbar that becomes vertical on small screens -->
<nav class="navbar navbar-expand-sm bg-dark navbar-dark">

    <div class="container-fluid">
        <!-- Links -->
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#"></a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">Patients</a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" th:href="@{/formPatients}">nouveau</a></li>
                    <li><a class="dropdown-item" th:href="@{/index}">Chercher</a></li>
                </ul>
            </li>
        </ul>
        <ul class="navbar-nav ">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">[Username]</a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" th:href="@{/formPatients}">Log out</a></li>
                </ul>
            </li>
        </ul>
    </div>

</nav>
<section layout:fragment="content1">

</section>
</body>
</html>
```
## creation de formPatients.html pour inserer a pratir d'un formulaire
```html 
<!DOCTYPE html>
<html lang="en"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="template1"
>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form Patients</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.5/css/bootstrap.min.css">
</head>
<body>
<div layout:fragment="content1">
    <div class="col-md-6 offset-3">
        <form method="post" th:action="@{/save}">
            <div >
                <label for="nom">Nom</label>
                <input id="nom" class="form-control" type="text" name="nom" th:value="${patient.nom}">
                <span th:errors="${patient.nom}"></span>
            </div>
            <div>
                <label>Date Naissance</label>
                <input class="form-control" type="date" name="dateNaissance" th:checked="${patient.dateNaissance}">
                <span th:errors="${patient.dateNaissance}"></span>
            </div>
            <div>
                <label>Malade</label>
                <input type="checkbox" name="malade" th:value="${patient.malade}">
                <span th:errors="${patient.malade}"></span>
            </div>
            <div>
                <label>Score</label>
                <input class="form-control" type="text" name="score" th:value="${patient.score}">
                <span th:errors="${patient.score}"></span>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>

        </form>
    </div>
</div>

</body>

</html>
```
## mise a jour de PatientController, pour afficher le formulaire et inserer a partir d'une formulaire
```java
    @GetMapping("/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/save")
    public String save(Model model,Patient patient){
        patientRepository.save(patient);
        return "formPatients";
    }
```
L'ajout du Navbar:
![Voir l'image](/images/navbar.png)
Voici le formulaire:
![Voir l'image](/images/formPatient.png)
