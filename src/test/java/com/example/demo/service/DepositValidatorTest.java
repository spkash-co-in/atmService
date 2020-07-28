package com.example.demo.service;

import com.example.demo.model.Deposit;
import com.example.demo.service.DepositValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class DepositValidatorTest {
    @Autowired
    private DepositValidator depositValidator;
    @Test
    public void parseInvalidDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-1010-55-1-0");
        depositValidator.validate(deposit);
        log.info("deposit {}", deposit);
        assertTrue(deposit.getValidityStatus().startsWith(Deposit.INVALID_DEPOSIT_ITEM));
        assertFalse(deposit.isValid());
    }
    @Test
    public void parseIllegalDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-10,10-5,5-1,1-0x");
        depositValidator.validate(deposit);
        log.info("deposit {}", deposit);
        assertTrue(deposit.getValidityStatus().startsWith(Deposit.INVALID_DEPOSIT_ITEM));
        assertFalse(deposit.isValid());
    }
    @Test
    public void parseValidZeroDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-10,10-5,5-1,1-0");
        depositValidator.validate(deposit);
        log.info("deposit {}", deposit);
        assertTrue(deposit.getValidityStatus().startsWith(Deposit.DEPOSIT_AMOUNT_CANNOT_BE_ZERO));
        assertFalse(deposit.isValid());
    }
    @Test
    public void parseValidDeposit() {
        Deposit deposit = new Deposit();
        deposit.setDepositValue("20-10,10-5,5-1,1-1");
        depositValidator.validate(deposit);
        log.info("deposit {}", deposit);
        assertTrue(deposit.isValid());
    }
}
