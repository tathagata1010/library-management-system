package com.dev.library.controller.web3j;

import com.dev.library.utility.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.librarycontract_updated.LibraryContract_updated;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@RestController
@RequestMapping("/contract")
public class Web3jController {

    @PostMapping("/deploy")
    public ResponseEntity<String> deployContract() throws Exception {
        Web3j web3 = Web3j.build(new HttpService(Constants.RPC_URL));

        Credentials credentials = Credentials.create(Constants.CREDENTIAL);


        LibraryContract_updated libraryContract= LibraryContract_updated.deploy( web3,
                credentials,
                new DefaultGasProvider()
        ).send();


        return ResponseEntity.ok().body(libraryContract.getContractAddress());

    }

}
