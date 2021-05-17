package com.example.demo.nfttoken.controller;

import com.example.demo.nfttoken.model.TokenState;
import com.example.demo.nfttoken.chainservice.ContractService;
import com.example.demo.nfttoken.model.TokenTx;
import com.example.demo.nfttoken.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/token")
public class NFTController {

    @Autowired
    ContractService contractService;
    @Autowired
    TradeService tradeService;

    @PostMapping("/mint")
    public @ResponseBody String mintToken(@RequestBody TokenState tokenInfo){
        String txHash = contractService.testMint(tokenInfo);
        System.out.println(txHash);
        return txHash;
    }

    @GetMapping("/properties")
    public TokenState getProperties(@RequestParam String tokenId){
        return contractService.testProperties(tokenId);
    }

    @GetMapping("/transfer")
    public String transferToken(@RequestParam String to, @RequestParam String tokenId, @RequestParam String data){
        String txHash = contractService.testTransfer(to, tokenId, data);
        return txHash;
    }

    @GetMapping("/tx/address")
    public List<TokenTx> selectTradeByAddress(@RequestParam String address){
        return tradeService.selectTradeByAddress(address);
    }

    @GetMapping("/tx/tokenId")
    public List<TokenTx> selectTradeByTokenId(@RequestParam String tokenId){
        return tradeService.selectTradeByTokenId(tokenId);
    }
}
