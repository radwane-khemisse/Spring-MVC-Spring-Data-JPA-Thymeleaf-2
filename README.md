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

