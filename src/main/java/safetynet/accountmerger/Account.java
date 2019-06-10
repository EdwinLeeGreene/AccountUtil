package safetynet.accountmerger;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class Account implements Application {
	private final String application;
	private final Set emails;
	protected String name;
	private ArrayList usernames = new ArrayList();
	
	public Account(String application, Set emails, String name) {
		this.application = application;
		this.emails = emails;
		this.name = name;
	}
	
	public void addToApplicationUsers(String username, String[] emailArray) {
		for(int i = 0; i < emailArray.length; i++) {
				if(emails.contains(emailArray[i])) {
				usernames.add(username);
		}
		}
	}
	
	public ArrayList getUsernames() {
		return (ArrayList) usernames.stream().collect(Collectors.toList());
	}

	public String getApplication() {
		return application;
	}
	
	public Set getEmails() {
		return (Set) emails.stream().collect(Collectors.toSet());
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void showApplicationKey() {
		// Generic applications have no key; do nothing
	}

	@Override
	public String getUserConfiguration(String username) {
		return username.concat(" -c defaultConfig");
	}
	
 
}
