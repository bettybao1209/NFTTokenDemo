package com.example.demo.nfttoken.chainservice;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.nfttoken.model.TokenState;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.Hash160;
import io.neow3j.contract.SmartContract;
import io.neow3j.crypto.Base64;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.ByteStringStackItem;
import io.neow3j.protocol.core.methods.response.NeoInvokeFunction;
import io.neow3j.protocol.core.methods.response.NeoSendRawTransaction;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.transaction.Signer;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ContractService {

    @Autowired private Hash160 contract;
    @Autowired private Neow3j neow3j;
    @Autowired private Wallet wallet;
    @Autowired private Account account;
    @Autowired private CommonService commonService;

    public String testMint(TokenState tokenInfo) {
        ContractParameter data = ContractParameter.string(JSONObject.toJSONString(tokenInfo));

        String txHash = "";
        try {
            NeoSendRawTransaction response = new SmartContract(contract, neow3j)
                    .invokeFunction("mint", data)
                    .wallet(wallet)
                    .signers(Signer.calledByEntry(account.getScriptHash()))
                    .sign()
                    .send();
            txHash = response.getResult().toString();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return txHash;
    }

    public String testBatchMint(TokenState tokenInfo, long amount) {
        ContractParameter data = ContractParameter.string(JSONObject.toJSONString(tokenInfo));
        ContractParameter amountData = ContractParameter.integer(BigInteger.valueOf(amount));

        String txHash = "";
        try {
            NeoInvokeFunction testResponse = new SmartContract(contract, neow3j)
                    .callInvokeFunction("mintInBatch", Arrays.asList(data, amountData), Signer.calledByEntry(account.getScriptHash()));
            String systemFee = testResponse.getResult().getGasConsumed();

            NeoSendRawTransaction response = new SmartContract(contract, neow3j)
                    .invokeFunction("mintInBatch", data, amountData)
                    .wallet(wallet)
                    .signers(Signer.calledByEntry(account.getScriptHash()))
                    .sign()
                    .send();
            txHash = response.getResult().toString();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return txHash;
    }

    public TokenState testProperties(String tokenId) {
       TokenState tokenInfo = null;
        try {
            NeoInvokeFunction response = new SmartContract(contract, neow3j)
                    .callInvokeFunction("properties", Arrays.asList(ContractParameter.byteArray(Base64.decode(tokenId))));
            List<StackItem> items = response.getInvocationResult().getStack();
            if(items.size() > 0){
                HashMap<StackItem, StackItem> stackItems = (HashMap<StackItem, StackItem>)items.get(0).getValue();
                Map<String, String> stackToStrMap = new HashMap<>();
                for(Map.Entry<StackItem, StackItem> entry: stackItems.entrySet()){
                    String key = entry.getKey().getString();
                    if(key.equalsIgnoreCase("owner")){
                        stackToStrMap.put(key, entry.getValue().getAddress());
                    }else{
                        stackToStrMap.put(key, entry.getValue().getString());
                    }
                }
                tokenInfo = JSONObject.parseObject(JSONObject.toJSONString(stackToStrMap), TokenState.class);
            }
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return tokenInfo;
    }

    public String testTransfer(String to, String tokenId, String data) {
        ContractParameter transferTo = ContractParameter.hash160(Hash160.fromAddress(to));
        ContractParameter tokenIdArray = ContractParameter.byteArray(Base64.decode(tokenId));
        ContractParameter dataStr = ContractParameter.string(data);

        String txHash = "";
        try {
            NeoSendRawTransaction response = new SmartContract(contract, neow3j)
                    .invokeFunction("transfer", transferTo, tokenIdArray, dataStr)
                    .wallet(wallet)
                    .signers(Signer.calledByEntry(account.getScriptHash()))
                    .sign()
                    .send();
            txHash = response.getResult().toString();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return txHash;
    }

    public List<String> testTokensOf(String owner){
        ContractParameter ownerHash = ContractParameter.hash160(Hash160.fromAddress(owner));
        List<String> tokenIds = new ArrayList<>();
        try {
            NeoInvokeFunction response = new SmartContract(contract, neow3j)
                    .callInvokeFunction("tokensOf", Arrays.asList(ownerHash));
            List<StackItem> items = response.getInvocationResult().getStack();
            if(items.size() > 0){
                tokenIds = items.get(0).getIterator().stream().map(stackItem -> Base64.encode(stackItem.getByteArray())).collect(Collectors.toList());
            }
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return tokenIds;
    }

    public List<String> testTokens(){
        List<String> tokenIds = new ArrayList<>();
        try {
            NeoInvokeFunction response = new SmartContract(contract, neow3j)
                    .callInvokeFunction("tokens");
            List<StackItem> items = response.getInvocationResult().getStack();
            if(items.size() > 0){
                tokenIds = items.get(0).getIterator().stream().map(stackItem -> Base64.encode(stackItem.getByteArray())).collect(Collectors.toList());
            }
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }
        return tokenIds;
    }
}
