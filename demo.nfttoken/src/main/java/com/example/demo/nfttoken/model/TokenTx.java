package com.example.demo.nfttoken.model;

import lombok.Data;

@Data
public class TokenTx {

    private Integer id;

    private String contractHash;
    private String from;
    private String to;
    private long amount;
    private String tokenId;
    private String txHash;
    private long timeStamp;
}
