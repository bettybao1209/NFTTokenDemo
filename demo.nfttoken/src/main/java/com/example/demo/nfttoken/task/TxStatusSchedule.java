package com.example.demo.nfttoken.task;

import com.example.demo.nfttoken.chainservice.CommonService;
import com.example.demo.nfttoken.model.TokenTx;
import com.example.demo.nfttoken.dataservice.ChainService;
import com.example.demo.nfttoken.dataservice.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class TxStatusSchedule
{
    private static final Logger log = LoggerFactory.getLogger(TxStatusSchedule.class);

    @Autowired
    CommonService commonService;

    @Autowired
    ChainService chainService;
    @Autowired
    TradeService tradeService;

    @Scheduled(cron = "0/15 * * * * *")
    public void updateOnChainStatus() throws Exception{
        log.info("update on-chain status task begin");
        while(true){
            BigInteger remoteBLockHeight = commonService.getRemoteBlockIndex();
            log.info("#### remote block height: {}", remoteBLockHeight);

            long dbBlockHeight = chainService.selectBlockHeight() == null ? -1 : chainService.selectBlockHeight();

            if(dbBlockHeight >= remoteBLockHeight.longValue()){
                log.info("Waiting for block.");
                return;
            }
            long blockDiff = remoteBLockHeight.longValue() - dbBlockHeight;
            while(blockDiff-- > 0){
                List<TokenTx> tokenTxes = commonService.getTransactions(BigInteger.valueOf(++dbBlockHeight));
                List<TokenTx> data  = new ArrayList<>();
                for (TokenTx transaction: tokenTxes) {
                    data.addAll(commonService.getTransferLogs(transaction));
                }
                if(data.size() > 0) {
                    tradeService.batchInsert(data);
                }
            }
            chainService.insertBlock(remoteBLockHeight.longValue());
        }
    }
}
