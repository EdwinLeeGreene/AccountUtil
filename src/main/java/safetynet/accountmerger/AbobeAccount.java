package safetynet.accountmerger;

import java.io.File;
import java.util.Set;

public class AbobeAccount extends Account {

	private static String applicationKey = "KYCTIOE18948";
	
	public AbobeAccount(String application, Set emails, String name) {
		super(application, emails, name);
	}
	
	@Override
	public String getUserConfiguration(String username) {
		return("./".concat("adobe -c").concat(getAdobeConfiguration().getAbsolutePath()).concat(" -u ").concat(username));
	}
	
	private File getAdobeConfiguration() {
		return new File("C:/adobe.config");
	}
	
	@Override
	public void showApplicationKey() {
		System.out.println(applicationKey);
	}

}
