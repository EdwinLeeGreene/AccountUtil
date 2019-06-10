package safetynet.accountmerger;

import java.util.Set;
import java.util.stream.Collectors;

public class Account {
    
    private final String application;
    private final Set<String> emails;
    private String name;
    
    public Account(String application, Set<String> emails, String name) {
        this.application = application;
        this.emails = sanitizeEmails(emails);
        this.name = name;
    }

    public String getApplication() {
        return application;
    }
    
    public Set<String> getEmails() {
        return emails.stream().collect(Collectors.toSet());
    }
    
    public String getName() {
        return name;
    }
    
    private Set<String> sanitizeEmails(Set<String> emails) {
        return emails.stream().map(email -> email.toLowerCase().trim()).collect(Collectors.toSet());
    }
}
