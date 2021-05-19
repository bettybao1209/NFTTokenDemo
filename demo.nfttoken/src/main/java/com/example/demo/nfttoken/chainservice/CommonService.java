package com.example.demo.nfttoken.chainservice;

import com.example.demo.nfttoken.model.TokenTx;
import io.neow3j.crypto.Base64;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.NeoApplicationLog;
import io.neow3j.protocol.core.response.NeoBlockCount;
import io.neow3j.protocol.core.response.NeoGetApplicationLog;
import io.neow3j.protocol.core.response.NeoGetBlock;
import io.neow3j.protocol.core.stackitem.StackItem;
import io.neow3j.types.Hash160;
import io.neow3j.types.Hash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonService {

    @Autowired
    private Neow3j neow3j;
    @Autowired
    private Hash160 contract;

    public BigInteger getRemoteBlockIndex() throws IOException {
        NeoBlockCount blockCount = neow3j.getBlockCount().send();
        BigInteger blockIndex = blockCount.getBlockCount().subtract(BigInteger.valueOf(1));
        return blockIndex;
    }

    public List<TokenTx> getTransactions(BigInteger blockIndex) throws IOException {
        NeoGetBlock block = neow3j.getBlock(blockIndex, true).send();
        List<TokenTx> tokenTxes = block.getBlock().getTransactions().stream().map(transaction -> {
            TokenTx tokenTx = new TokenTx();
            tokenTx.setTxHash(transaction.getHash().toString());
            tokenTx.setTimeStamp(transaction.getBlockTime());
            return tokenTx;
        }).collect(Collectors.toList());
        return tokenTxes;
    }

    public List<TokenTx> getTransferLogs(TokenTx transaction) throws IOException {
        List<NeoApplicationLog.Execution.Notification> notifications = getNotifications(transaction.getTxHash());
        List<TokenTx> tokenTxes = notifications.stream().map(notification -> {
            TokenTx tokenTx = new TokenTx();
            List<StackItem> state = (ArrayList<StackItem>)notification.getState().getValue();
            tokenTx.setFrom((state.get(0)).getValue() == null ? "": state.get(0).getAddress());
            tokenTx.setTo((state.get(1)).getValue() == null ? "": state.get(1).getAddress());
            tokenTx.setAmount(state.get(2).getInteger().longValue());
            tokenTx.setTokenId(Base64.encode(state.get(3).getByteArray()));
            tokenTx.setTxHash(transaction.getTxHash());
            tokenTx.setTimeStamp(transaction.getTimeStamp());
            tokenTx.setContractHash(contract.toString());
            return tokenTx;
        }).collect(Collectors.toList());
        return tokenTxes;
    }

    private List<NeoApplicationLog.Execution.Notification> getNotifications(String txHash) throws IOException{
        NeoGetApplicationLog logs = neow3j.getApplicationLog(new Hash256(txHash)).send();
        List<NeoApplicationLog.Execution> executions = logs.getApplicationLog().getExecutions();
        List<NeoApplicationLog.Execution.Notification> notifications = executions.stream().flatMap(execution -> execution.getNotifications().stream())
                .filter(notification ->
                        notification.getEventName().equalsIgnoreCase("transfer") && notification.getContract().equals(contract))
                .collect(Collectors.toList());
        return notifications;
    }
}
