package com.example.demo.nfttoken.dataservice;

import com.example.demo.nfttoken.mapper.TradeMapper;
import com.example.demo.nfttoken.model.TokenTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    TradeMapper tradeMapper;

    public void batchInsert(List<TokenTx> tokenTxes){
        tradeMapper.batchInsert(tokenTxes);
    }

    public List<TokenTx> selectTradeByAddress(String address){
        return tradeMapper.selectTradeByAddress(address);
    }

    public List<TokenTx> selectTradeByTokenId(String tokenId){
        return tradeMapper.selectTradeByTokenId(tokenId);
    }
}
