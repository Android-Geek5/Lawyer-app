package com.erginus.lawyerapp;

/**
 * Created by Rajesh on 01-Oct-16.
 */
public class CaseListModel {
    String caseTitle, caseNumber, courtName, caseType;

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String name) {
        this.caseTitle = name;
    }
    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String name) {
        this.caseNumber = name;
    }
    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String name) {
        this.courtName = name;
    }
    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String name) {
        this.caseType = name;
    }

}
