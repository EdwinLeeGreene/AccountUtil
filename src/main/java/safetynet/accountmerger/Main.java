package safetynet.accountmerger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

public class Main {
	
	/*
		Given a listing of accounts, from a variety of applications, merge the accounts to create a listing of people.
		
		Assumptions:
		  If two accounts share an email then they were created by the same person
		  If two names are the same they don't necessarily refer to the same person
		  If the names differ when merging, pick one
	
		Application goals
		  1: Reads and parses the accounts.json file
		  2: Merges the accounts
		  3: Prints the merged accounts to the console in Json structure
		
		An account consists of 
		{
		  application: string,
		  emails: [string],
		  name: string
		}
		
		The person consists of
		{
		  applications: [string],
		  emails: [string],
		  name: string
		}
		
		Example:
		
		Input
		[
		  {
		    application: "x",
		    emails: ["a", "b", "c"],
		    name: "Person 1"
		  },
		  {
		    application: "y",
		    emails: ["c", "d"],
		    name: "Person 1"
		  }
		]
		
		Output
		[
		  {
		    applications: ["x", "y"],
		    emails: ["a", "b", "c", "d"],
		    name: "Person 1"
		  }
		]
		
    */

    public static void main(String[] args) throws IOException {
        List<Any> accounts = loadAccountFile();
        List<Account> accountList = accounts.stream().map(a -> anyToAccount(a)).collect(Collectors.toList());
        Set<Person> personList = mergeAccounts(accountList);
        String outputJson = JsonStream.serialize(personList);
        System.out.println(outputJson);
    }
    
    private static List<Any> loadAccountFile() throws IOException {
        Path path = Paths.get("src/main/resources/accounts.json").toAbsolutePath();
        String json = new String(Files.readAllBytes(path));
        return JsonIterator.deserialize(json).asList();
    }

    private static Account anyToAccount(Any accountAny) {
        String[] emails = accountAny.get("emails").as(String[].class);
        Set<String> emailsSet = new HashSet<String>(Arrays.asList(emails));
        return new Account(accountAny.toString("application"), emailsSet, accountAny.toString("name")); 
    }
    
    private static Set<Person> mergeAccounts(List<Account> accountList) {
        Map<UUID, Person> idPersonMap = new HashMap<>();
        Map<String, UUID> emailIdMap = new HashMap<>();
        for(Account account : accountList) {
            Set<UUID> foundIds = new HashSet<>();
            for(String email : account.getEmails()) {
                if(emailIdMap.containsKey(email)) {
                    foundIds.add(emailIdMap.get(email));
                } 
            }
            
            Person person = new Person(account.getApplication(), account.getEmails(), account.getName());
            if(!foundIds.isEmpty()) {
                mergeToSuperSetPerson(foundIds, idPersonMap, person, emailIdMap);
            }
            
            person.getEmails().stream().forEach(email -> emailIdMap.put(email, person.getId()));
            idPersonMap.put(person.getId(), person);
        }
        
        return idPersonMap.values().stream().collect(Collectors.toSet());
    }
    
    private static void mergeToSuperSetPerson(Set<UUID> ids, Map<UUID, Person> idPersonMap, Person superSetPerson, Map<String, UUID> emailIdMap) {
        Set<Person> personsToCombine = ids.stream().map(id -> idPersonMap.get(id)).collect(Collectors.toSet());
        ids.stream().forEach(idPersonMap::remove);
        for(Person person : personsToCombine) {
            person.getApplications().stream().forEach(superSetPerson::addApplication);
            person.getEmails().stream().forEach(email -> {
                superSetPerson.addEmail(email);
                emailIdMap.remove(email);
            });
        }   
    }
 
}
