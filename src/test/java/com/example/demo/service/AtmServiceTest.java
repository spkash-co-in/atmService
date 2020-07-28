package com.example.demo.service;


import com.example.demo.model.Deposit;
import com.example.demo.model.Withdrawal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AtmServiceTest {
    @Autowired
    private AtmService atmService;

    @Test
    public void whenYamlFileProvidedThenInjectSimpleMap() {
        atmService.initialize();
    }

    @Test
    public void testWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(75L);
        log.info("Withdrawal {}", withdrawal);
        atmService.withdraw(withdrawal);
        log.info("Withdrawal {}", withdrawal);
        atmService.printBalances();
    }

    @Test
    public void parseValidDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-10,10-5,5-1,1-0");
        atmService.deposit(deposit);
    }

    @Test
    public void parseInvalidDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-1010-55-1-0x");
        log.info("deposit {}", deposit);
        atmService.deposit(deposit);
    }

    @Test
    public void testInsufficientWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(785L);
        log.info("Withdrawal {}", withdrawal);
        atmService.withdraw(withdrawal);
        Assertions.assertTrue(withdrawal.getValidityStatus().startsWith(Withdrawal.INSUFFICIENT_FUNDS));
        log.info("Withdrawal {}", withdrawal);
        atmService.printBalances();
    }

    @Test
    public void testIncorrectWithdrawal() {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(-785L);
        log.info("Withdrawal {}", withdrawal);
        atmService.withdraw(withdrawal);
        Assertions.assertTrue(withdrawal.getValidityStatus().startsWith(Withdrawal.INCORRECT_WITHDRAWAL));
        log.info("Withdrawal {}", withdrawal);
        atmService.printBalances();
    }

}
