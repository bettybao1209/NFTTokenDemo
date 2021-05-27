package com.example.demo.nfttoken.config;

import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.types.Hash160;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {

    @Bean
    public Hash160 contract(){
        return new Hash160("0x914076d848ac73e1170747a47d1b612c686365ac");
    }

    @Bean
    public Neow3j neow3j(){
        return Neow3j.build(new HttpService("http://localhost:10332"));
    }

    @Bean
    public Account account(){
        return Account.fromWIF("L4eXZr8gbVC52cxw24X8s1ohYdqG2AZh3UFqjATEKpsEFR44JpHx");
    }

    @Bean
    public Wallet wallet(){
        return Wallet.withAccounts(account());
    }
}
