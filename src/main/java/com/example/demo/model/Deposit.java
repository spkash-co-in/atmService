package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Deposit {
    public static final String INCORRECT_DEPOSIT_AMOUNT = "Incorrect deposit amount: ";
    public static final String DEPOSIT_AMOUNT_CANNOT_BE_ZERO = "Deposit amount cannot be zero : ";
    public static final String INVALID_DENOMINATION = "Invalid denomination : ";
    public static final String INVALID_DEPOSIT_ITEM = "Invalid deposit item : ";
    private String depositValue;
    private boolean isValid = true;
    private String validityStatus;
    private String depositStatus;
    private Map<Long, Long> denominations;
}
