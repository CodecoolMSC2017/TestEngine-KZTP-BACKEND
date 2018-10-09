package com.kztp.testengine.model.enums;

public enum VoteEnum {
    VOTE_POSITIVE(1),
    VOTE_NEGATIVE(2);
    private final int value;
    VoteEnum(int value) {
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
