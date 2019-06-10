package safetynet.accountmerger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
    	Path path = Paths.get("src/main/resources/accounts.json").toAbsolutePath();
    	String json = new String(Files.readAllBytes(path));
    	List<Any> accounts = JsonIterator.deserialize(json).asList();
    	ArrayList<Account> accountList = new ArrayList();
    	ArrayList usernameList = new ArrayList();
        
    	for(int i = 0; i < accounts.size(); i++) {
    		String[] emails = accounts.get(i).get("emails").as(String[].class);
    		HashSet emailsSet = new HashSet(Arrays.asList(emails));
    		accountList.add(new Account(accounts.get(i).toString("application"), emailsSet, accounts.get(i).toString("name"))); 
        }
    	
    	for(int i = 0; i < accounts.size(); i++) {
    		String name = accounts.get(i).get("name").toString();
    		String[] emails = accounts.get(i).get("emails").as(String[].class);
    		for(int j = 0; j < accountList.size(); j++) {
    			accountList.get(j).addToApplicationUsers(name, emails);
    		}
    	}
        
    	String outputJson = JsonStream.serialize(accountList);
    	System.out.println(outputJson);
    	
    	// Also, I added the functionality from the next sprint; getting the command to open an application based on the user
    	// This demonstrates it:
    	System.out.println(accountList.get(0).getUserConfiguration(accountList.get(0).name));
    	
    	// And here is the basics for the application keys
    	accountList.get(0).showApplicationKey();
    }

    
 
}
