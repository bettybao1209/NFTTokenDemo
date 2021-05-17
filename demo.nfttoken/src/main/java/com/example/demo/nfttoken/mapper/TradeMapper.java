package com.example.demo.nfttoken.mapper;

import com.example.demo.nfttoken.model.TokenTx;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeMapper {

    void batchInsert(List<TokenTx> tokenTxes);

    List<TokenTx> selectTradeByAddress(String address);

    List<TokenTx> selectTradeByTokenId(String address);
}
