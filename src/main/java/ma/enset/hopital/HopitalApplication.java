package ma.enset.hopital;

import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {
	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(HopitalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Patient patient1 = new Patient(null, "ahmed", new Date(), true, 10);
		Patient patient2 = new Patient();
		patient2.setNom("karim");
		patient2.setDateNaissance(new Date());
		patient2.setMalade(false);
		patient2.setScore(5);
		Patient patient3 = Patient.builder()
				.nom("souad")
				.dateNaissance(new Date())
				.malade(true)
				.score(8)
				.build();
//		patientRepository.save(patient1);
//		patientRepository.save(patient2);
//		patientRepository.save(patient3);


	}
}
