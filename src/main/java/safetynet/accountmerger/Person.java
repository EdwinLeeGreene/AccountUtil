package safetynet.accountmerger;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.jsoniter.annotation.JsonIgnore;

public class Person {
	
	private final Set<String> applications = new HashSet<>();
	private final Set<String> emails;
	private final String name;
	
	@JsonIgnore
	private final UUID id = UUID.randomUUID();
	
	public Person(String application, Set<String> emails, String name) {
		this.name = name;
		applications.add(application);
		this.emails = emails;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void addApplication(String application) {
		applications.add(application);
	}
	
	public void addEmail(String email) {
		emails.add(email);
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getEmails() {
		return emails;
	}
	
	public Set<String> getApplications() {
		return applications;
	}
}

