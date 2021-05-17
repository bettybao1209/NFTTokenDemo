package com.example.demo.nfttoken.service;

import com.example.demo.nfttoken.mapper.ChainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChainService {

    @Autowired
    ChainMapper chainMapper;

    public Long selectBlockHeight(){
        return chainMapper.selectBlockHeight();
    }

    public void insertBlock(long blockIndex){
        chainMapper.insertBlock(blockIndex);
    }


}
