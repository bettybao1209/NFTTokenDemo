package com.example.demo.nfttoken.model;

import lombok.Data;

@Data
public class TokenState {

    private String tokenId;
    private String name;
    private String uri;
    private String desc;
    private String owner;

    private String creator;
    private TokenType type;

    private long createdTime;
}
