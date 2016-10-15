package com.lawyerapp.model;


import java.io.Serializable;

public class CaseListModel implements Serializable{
    String caseTitle, caseNumber, courtName, caseType, caseId, caseStartDate, casePrevDate, counsellorName, counsellorContact, retainName,
    retainContact, caseStatus , comment, nextDate;

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

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String name) {
        this.caseId = name;
    }

    public String getCaseStartDate() {
        return caseStartDate;
    }

    public void setCaseStartDate(String name) {
        this.caseStartDate = name;
    }

    public String getCasePrevDate() {
        return casePrevDate;
    }

    public void setCasePrevDate(String name) {
        this.casePrevDate = name;
    }

    public String getCounsellorName() {
        return counsellorName;
    }

    public void setCounsellorName(String name) {
        this.counsellorName = name;
    }

    public String getCounsellorContact() {
        return counsellorContact;
    }

    public void setCounsellorContact(String name) {
        this.counsellorContact = name;
    }

    public String getRetainName() {
        return retainName;
    }

    public void setRetainName(String name) {
        this.retainName = name;
    }

    public String getRetainContact() {
        return retainContact;
    }

    public void setRetainContact(String name) {
        this.retainContact = name;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String name) {
        this.caseStatus = name;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String name) {
        this.caseType = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String name) {
        this.comment = name;
    }

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String name) {
        this.nextDate = name;
    }

}
