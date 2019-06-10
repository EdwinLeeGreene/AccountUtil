package safetynet.accountmerger;

import java.io.File;

public class AdobeApplication {
    // TODO Add real path to default config file once it is delivered
    private static final File defaultConfigFile = new File("/path/to/defaultConfig");
    private static final String applicationKey = "KYCTIOE18948";
    private static final String adobeConfiguration = "C:\\adobe.config";
    
    private final Account account;
    public AdobeApplication(Account account) {
        this.account = account;
    }
    
    public String getUserConfiguration(String username) {
        File userConfigFile = new File(adobeConfiguration);
        File configFile = userConfigFile.exists() ? userConfigFile : defaultConfigFile;
        return("./".concat("adobe -c").concat(configFile.getAbsolutePath()).concat(" -u ").concat(username));
    }
    
    
    public void showApplicationKey() {
        System.out.println(applicationKey);
    }

}
