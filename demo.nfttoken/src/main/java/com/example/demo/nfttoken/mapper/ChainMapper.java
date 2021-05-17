package com.example.demo.nfttoken.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface ChainMapper {

    Long selectBlockHeight();

    void insertBlock(long blockIndex);
}
