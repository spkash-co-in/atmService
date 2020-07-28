package com.example.demo.web.rest;

import com.example.demo.model.Deposit;
import com.example.demo.model.DepositInput;
import com.example.demo.model.WithdrawInput;
import com.example.demo.model.Withdrawal;
import com.example.demo.service.AtmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/atm")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AtmController {
    private AtmService atmService;
    @GetMapping("/balance")
    public Map<Long, Long> getBalance() {
        return atmService.getBalances();
    }
    @PostMapping("/deposit")
    public Deposit deposit(@RequestBody DepositInput depositInput) {
        Deposit deposit = new Deposit();
        deposit.setDepositValue(depositInput.getDepositString());
        atmService.deposit(deposit);
        return deposit;
    }
    @PostMapping("/withdraw")
    public Withdrawal withdraw(@RequestBody WithdrawInput withdrawInput) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(withdrawInput.getWithdrawalAmount());
        atmService.withdraw(withdrawal);
        return withdrawal;
    }
}
