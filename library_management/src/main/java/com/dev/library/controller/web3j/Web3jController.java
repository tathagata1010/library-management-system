package com.dev.library.controller.web3j;

import com.dev.library.config.ApplicationProperties;
import com.dev.library.utility.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

@RestController
@RequestMapping("/contract")
public class Web3jController {

    private final ApplicationProperties applicationProperties;

    public Web3jController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("/deploy")
    public ResponseEntity<String> deployContract() throws Exception {
        Web3j web3 = Web3j.build(new HttpService(applicationProperties.getRpcURL()));

        Credentials credentials = Credentials.create(applicationProperties.getWalletCredential());
        EthBlock block = web3.ethGetBlockByNumber(
                DefaultBlockParameterName.LATEST,
                false
        ).send();
        ContractGasProvider contractGasProvider = new StaticGasProvider(
                web3.ethGasPrice().send().getGasPrice(),
                block.getBlock().getGasLimit()
        );

        LibraryContract_updated libraryContract= LibraryContract_updated.deploy( web3,
                credentials,
//                new DefaultGasProvider()
                contractGasProvider
        ).send();


        return ResponseEntity.ok().body(libraryContract.getContractAddress());

    }

}
