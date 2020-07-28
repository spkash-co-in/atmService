package com.example.demo.service;

import com.example.demo.model.Deposit;
import com.example.demo.config.ServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class DepositValidator {
    public static final String DELIM = ". ";
    @Autowired
    ServerProperties serverProperties;
    public static final String COMMA = ",";
    public static final String HYPHEN = "-";

    public Deposit validate(final Deposit deposit) {
        Map<Long, Long> deposited = new TreeMap<Long, Long>(Collections.reverseOrder());
            if (isNonNullDelimitedString(deposit)) {
                Arrays.stream(deposit.getDepositValue().split(COMMA))
                        .map(s -> s.split(HYPHEN))
                        .forEach(el -> {
                            if (el.length==2) {
                                try {
                                    Long key = Long.parseLong(el[0]);
                                    Long value = Long.parseLong(el[1]);
                                    if (value==0) {
                                        setInvalidStatus(deposit, Deposit.DEPOSIT_AMOUNT_CANNOT_BE_ZERO.concat(String.join("-", el)).concat(DELIM));
                                    }
                                    if (value<0) {
                                        setInvalidStatus(deposit, Deposit.INCORRECT_DEPOSIT_AMOUNT.concat(String.join("-", el)).concat(DELIM));
                                    }
                                    if (serverProperties.getDenominations().containsKey(key)) {
                                        deposited.put(key,value);

                                    } else {
                                        setInvalidStatus(deposit, Deposit.INVALID_DENOMINATION.concat(String.join("-", el)).concat(DELIM));
                                    }

                                } catch (Exception ex) {
                                    setInvalidStatus(deposit,  Deposit.INVALID_DEPOSIT_ITEM.concat(ex.getMessage().concat(DELIM)));
                                }
                            } else {
                                setInvalidStatus(deposit, Deposit.INVALID_DEPOSIT_ITEM.concat(String.join("-", el)).concat(DELIM));
                            }
                        });
            }
            deposit.setDenominations(deposited);
        return deposit;
    }

    private void setInvalidStatus(Deposit deposit, String errorMsg) {
        deposit.setValid(false);
        deposit.setValidityStatus(errorMsg);
        log.error(deposit.getValidityStatus());
    }

    private boolean isNonNullDelimitedString(Deposit deposit) {
        return deposit.getDepositValue() != null
                && !deposit.getDepositValue().isBlank()
                && !deposit.getDepositValue().isEmpty()
                && deposit.getDepositValue().contains(HYPHEN);
    }
}
