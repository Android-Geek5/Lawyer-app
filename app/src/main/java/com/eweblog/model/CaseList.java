package com.eweblog.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by erginus on 2/8/2017.
 */

public class CaseList implements Parcelable{
    int case_id;
    String case_date;
    String case_title;
    public CaseList()
    {

    }
    public CaseList(Parcel in) {
        case_id = in.readInt();
        case_date = in.readString();
        case_title = in.readString();
    }

    public static final Creator<CaseList> CREATOR = new Creator<CaseList>() {
        @Override
        public CaseList createFromParcel(Parcel in) {
            return new CaseList(in);
        }

        @Override
        public CaseList[] newArray(int size) {
            return new CaseList[size];
        }
    };

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public String getCase_date() {
        return case_date;
    }

    public void setCase_date(String case_date) {
        this.case_date = case_date;
    }

    public String getCase_title() {
        return case_title;
    }

    public void setCase_title(String case_title) {
        this.case_title = case_title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(case_id);
        parcel.writeString(case_date);
        parcel.writeString(case_title);
    }

}
