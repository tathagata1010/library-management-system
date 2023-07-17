package com.dev.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperties {

    @Value("${web3j.rpc-url}")
    private String rpcURL;

    @Value("${web3j.wallet-credential}")
    private String walletCredential;

    @Value("${web3j.contract-address}")
    private String contractAddress;

    public String getRpcURL() {
        return rpcURL;
    }

    public String getWalletCredential() {
        return walletCredential;
    }

    public String getContractAddress() {
        return contractAddress;
    }
}
