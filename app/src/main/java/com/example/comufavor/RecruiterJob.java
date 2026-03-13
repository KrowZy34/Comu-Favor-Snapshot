package com.example.comufavor;

public class RecruiterJob extends Job {
    private static final long serialVersionUID = 1L;
    private int proposalsCount;

    public RecruiterJob(String title, String location, String price, String date, int proposalsCount) {
        super(title, location, price, date);
        this.proposalsCount = proposalsCount;
    }

    public int getProposalsCount() {
        return proposalsCount;
    }
}
