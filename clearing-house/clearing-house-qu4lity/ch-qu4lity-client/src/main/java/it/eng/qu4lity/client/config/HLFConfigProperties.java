package it.eng.qu4lity.client.config;

public interface HLFConfigProperties {

    String CONFIG_PROPERTIES = "config.properties";

    String getOrganizationName();

    String getChannelName();

    String getUserName();

    String getChaincodeName();

    String getCertFilename();

    String getKeystoreFilename();

    String getNetworkFilename();

    String getWalletPath();
}
