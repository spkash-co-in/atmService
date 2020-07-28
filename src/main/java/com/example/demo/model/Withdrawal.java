package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Data
public class Withdrawal {
    public static final String INCORRECT_WITHDRAWAL = "Incorrect withdrawal : ";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds : ";
    private Long amount;
    private Map<Long, Long> dispensed = new TreeMap<Long,Long>(Collections.reverseOrder());
    private boolean isValid = true;
    private String validityStatus;
}
