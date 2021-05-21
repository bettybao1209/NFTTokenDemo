package com.example.demo.nfttoken.model;

import lombok.Getter;

@Getter
public enum TokenType{

    PIC(1, "图片"),
    VIDEO(2, "视频"),
    AUDIO(3, "音频");

    private final int code;
    private final String desc;

    TokenType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
