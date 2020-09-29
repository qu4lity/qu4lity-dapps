package it.eng.sidrestapi.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "fabric")
public class HLFConfigPropertiesBean{


//    public static String WALLET_PATH;
    private String organizationName;
    private String channelName;
    private String userName;
    private String chaincodeName;
    private String networkFilename;

//    public String getWalletPath() {
//        return WALLET_PATH;
//    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getNetworkFilename() {
        return networkFilename;
    }

    public void setNetworkFilename(String networkFilename) {
        this.networkFilename = networkFilename;
    }

//    public void setWalletPath(String walletPath) {
//        HLFConfigPropertiesBean.WALLET_PATH = walletPath;
//    }


}
