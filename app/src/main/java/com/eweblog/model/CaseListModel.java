package com.eweblog.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CaseListModel implements Serializable{
   private String caseTitle, caseNumber, courtName, caseType, caseId, caseStartDate, casePrevDate, counsellorName, counsellorContact, retainName,
    retainContact, caseStatus , comment, nextDate;
   private ArrayList<String> nextDt, prevDt, comments;
 List<CaseListModel> list, arrayList;

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

    public ArrayList<String> getNextDateArray() {
        return nextDt;
    }

    public void setNextDateArray(ArrayList name) {
        this.nextDt = name;
    }


    public ArrayList<String> getPrevDateArray() {
        return prevDt;
    }

    public void setPrevDateArray(ArrayList name) {
        this.prevDt = name;
    }

    public ArrayList<String> getNCommentsArray() {
        return comments;
    }

    public void setCommentsArray(ArrayList name) {
        this.comments = name;
    }

    public List<CaseListModel> getCaseList() {
        return list;
    }

    public void setCaseList(List<CaseListModel> name) {
        this.list = name;
    }

    public List<CaseListModel> getCaseArrayList() {
        return arrayList;
    }

    public void setArrayCaseList(List<CaseListModel> name) {
        this.arrayList = name;
    }


}
