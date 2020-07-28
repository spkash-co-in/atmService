package com.example.demo.service;

import com.example.demo.model.Deposit;
import com.example.demo.config.ServerProperties;
import com.example.demo.model.Withdrawal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static com.example.demo.model.Withdrawal.INCORRECT_WITHDRAWAL;
import static com.example.demo.model.Withdrawal.INSUFFICIENT_FUNDS;

@Service
@Slf4j
public class AtmService {
    @Autowired private ServerProperties props;
    @Autowired private DepositValidator depositValidator;
    private final Map<Long, Long> balances = new TreeMap<Long, Long>(Collections.reverseOrder());
    public void withdraw(Withdrawal withdrawal) {
        Long amount = withdrawal.getAmount();
        if (amount <=0) {
            setInvalidWithdrawal(withdrawal, INCORRECT_WITHDRAWAL.concat(String.valueOf(amount)));
        } else {
            synchronized (balances) {
                Long totalBalance = getTotalBalance();
                log.info(" Total Balance {} Amount {} ", totalBalance, amount);
                if (totalBalance >= amount) {
                    processDispensing(withdrawal, amount);
                } else {
                    setInvalidWithdrawal(withdrawal, INSUFFICIENT_FUNDS.concat("Available balance = ").concat(String.valueOf(totalBalance)));
                }
            }
        }
    }

    private void setInvalidWithdrawal(Withdrawal withdrawal, String errorMsg) {
        withdrawal.setValid(false);
        withdrawal.setValidityStatus(errorMsg);
    }

    private void processDispensing(Withdrawal withdrawal, Long amount) {
        for(Map.Entry<Long,Long> denominationEntry : balances.entrySet()) {
            if (amount>0) {
                Long denomination = denominationEntry.getKey();
                Long notes = denominationEntry.getValue();
                log.info(" Amount {} in iteration {}", amount, denomination);
                Long multiple = amount / denomination;
                if (multiple>0) {
                    withdrawal.getDispensed().put(denomination,multiple);
                    amount = amount - denomination * multiple;
                    if (multiple < notes) {
                        denominationEntry.setValue(notes - multiple);
                    } else {
                        denominationEntry.setValue(0L);
                    }
                }
            }
        }
    }
    private Long getTotalBalance() {
        return balances.entrySet().stream().mapToLong(entry -> entry.getKey() * entry.getValue()).sum();
    }
    public void deposit(Deposit deposit) {
        depositValidator.validate(deposit);
        if (deposit.isValid()) {
            synchronized (balances) {
                balances.entrySet().forEach(el -> processDeposit(el,deposit));
            }
            log.info("Balances after deposit : {}", balances);
        } else {
            log.error("Invalid deposit {}", deposit);
        }
    }

    private void processDeposit(Map.Entry<Long, Long> el, Deposit deposit) {
        log.info("Deposit : {}", deposit);
        log.info("Pre deposit : {}",String.valueOf(el));
        Long key = el.getKey();
        Long val = el.getValue();
        Long depNotes = deposit.getDenominations().get(key);
        if (depNotes!=null) {
            el.setValue(val + depNotes);
        }
        log.info("Post deposit : {}",String.valueOf(el));
    }

    @PostConstruct
    public void initialize() {
        log.info("Server properties {}", props);
        balances.putAll(props.getDenominations());
        log.info("balances {}", balances);
    }
    public void printBalances() {
        log.info("Balances {}", balances);
    }
    public Map<Long, Long> getBalances() {
        return Collections.unmodifiableMap(balances);
    }
}
