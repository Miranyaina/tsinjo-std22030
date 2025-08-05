package com.tsinjo.demo.model;

public class Help {
    private Long id;
    private Beneficiary beneficiary;
    private String accidentDescription;

    public Help(Long id, Beneficiary beneficiary, String accidentDescription) {
        this.id = id;
        this.beneficiary = beneficiary;
        this.accidentDescription = accidentDescription;
    }
}
