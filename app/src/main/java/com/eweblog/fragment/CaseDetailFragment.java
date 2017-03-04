package com.eweblog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eweblog.R;
import com.eweblog.Utils;
import com.eweblog.common.UserInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by erginus on 2/24/2017.
 */

public class CaseDetailFragment extends DialogFragment {
    TextView txtStartDate,txtPreviousDate ,txtCaseTitle, txtCaseType, txtCourtName,txtCaseNumber, txtStatus, txtNextDate,txtDecidedFee, txtRetain, txtOpposite,
             txtComment, txtSmsAlertStatus, txtSmsAlertNo,txtAssignedTo;
    String startDate,previousDate, caseTitle,caseType,courtName,caseNumber,status,nextDate,decideFee,retained,counsellor
            ,comment,smsAlertStatus,smsAlertNumber,assignedTo;
    LinearLayout ll_assignedTo,ll_sms_alert_no,ll_sms_alert_status;
    boolean smsAlert;
    ScrollView mainLayout;
    CaseDetailListener caseDetailListener;
    Button exit,submit;
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    DateFormat dateFormatter2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    public CaseDetailFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            caseDetailListener = (CaseDetailListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }
    public static CaseDetailFragment newInstance( String startDate,String previousDate,String caseTitle,String caseType,String courtName,
                                                  String caseNumber,String status,String nextDate,String decidedFee,String retained,String counsellor
            ,String comment,int smsAlertStatus,String smsAlertNumber,String assignedTo) {
        CaseDetailFragment frag = new CaseDetailFragment();
        Bundle args = new Bundle();
        args.putString(UserInfo.CASE_START_DATE,startDate);
        args.putString(UserInfo.CASE_PREVIOUS_DATE,previousDate);
        args.putString(UserInfo.CASE_TITLE, caseTitle);
        args.putString(UserInfo.CASE_TYPE, caseType);
        args.putString(UserInfo.COURT_NAME,courtName);
        args.putString(UserInfo.CASE_NUMBER, caseNumber);
        args.putString(UserInfo.CASE_STATUS_NAME, status);
        args.putString(UserInfo.CASE_NEXT_DATE, nextDate);
        args.putString(UserInfo.DECIDED_FEE,decidedFee);
        args.putString(UserInfo.CASE_RETAINED_NAME, retained);
        args.putString(UserInfo.CASE_OPPOSITE_COUNSELLOR_NAME, counsellor);
        args.putString(UserInfo.CASE_DETAIL_COMMENT,comment);
        args.putInt(UserInfo.SMS_ALERT,smsAlertStatus);
        args.putString(UserInfo.SMS_ALERT_NUMBER,smsAlertNumber);
        args.putString(UserInfo.CASE_ASSIGNED_BY,assignedTo);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_case_detail, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        inflateLayout(view);
        getIntentInformation();
        setIntentInformation();
    }
    public void inflateLayout(View rootview)
    {

        mainLayout=(ScrollView) rootview.findViewById(R.id.main_layout);

        txtStartDate=(TextView)rootview.findViewById(R.id.textView_start_date);
        txtPreviousDate=(TextView)rootview.findViewById(R.id.textView_previous_date);
        txtCaseTitle=(TextView)rootview.findViewById(R.id.textView_title);
        txtCaseType=(TextView)rootview.findViewById(R.id.textView_type);
        txtCourtName=(TextView)rootview.findViewById(R.id.textView_name);
        txtCaseNumber=(TextView)rootview.findViewById(R.id.textView_number);
        txtStatus=(TextView)rootview.findViewById(R.id.textView_position);
        txtNextDate=(TextView)rootview.findViewById(R.id.textView_next_date);
        txtDecidedFee=(TextView)rootview.findViewById(R.id.textView_decided_fee);
        txtRetain=(TextView)rootview.findViewById(R.id.textView_retainNm);
        txtOpposite=(TextView)rootview.findViewById(R.id.textView_cname);
        txtComment=(TextView)rootview.findViewById(R.id.textView_comment);
        txtSmsAlertStatus=(TextView)rootview.findViewById(R.id.textView_sms_alert_status);
        txtSmsAlertNo=(TextView)rootview.findViewById(R.id.textView_sms_alert_no);
        txtAssignedTo=(TextView) rootview.findViewById(R.id.textView_assigned);

        ll_assignedTo=(LinearLayout) rootview.findViewById(R.id.ll_assigned_to);
        ll_sms_alert_status=(LinearLayout) rootview.findViewById(R.id.ll_sms_alert_status);
        ll_sms_alert_no=(LinearLayout) rootview.findViewById(R.id.ll_sms_alert_no);
        exit=(Button)rootview.findViewById(R.id.exit);
        submit=(Button)rootview.findViewById(R.id.submit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                caseDetailListener.response(0);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                caseDetailListener.response(1);
            }
        });

    }
    public void setIntentInformation()
    {
        //caseArray= (List<CaseListModel>) getIntent().getSerializableExtra("list1");

        mainLayout.setVisibility(View.VISIBLE);
        if(!startDate.equalsIgnoreCase(""))
        {
            try {
                startDate=dateFormatter2.format(dateFormatter.parse(startDate));
                txtStartDate.setText(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else
        {
            txtStartDate.setText("N/A");
        }
        if(!previousDate.equalsIgnoreCase(""))
        {
            try {
                previousDate=dateFormatter2.format(dateFormatter.parse(previousDate));
                txtPreviousDate.setText(previousDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else
        {
            txtPreviousDate.setText("N/A");
        }
        if(!caseTitle.equalsIgnoreCase(""))
        {
            txtCaseTitle.setText(caseTitle);
        }
        else
        {
            txtCaseTitle.setText("N/A");
        }
        if(!caseType.equalsIgnoreCase(""))
        {
            txtCaseType.setText(caseType);
        }
        else
        {
            txtCaseType.setText("N/A");
        }
        if(!courtName.equalsIgnoreCase(""))
        {
            txtCourtName.setText(courtName);
        }
        else
        {
            txtCourtName.setText("N/A");
        }
        if(!caseNumber.equalsIgnoreCase(""))
        {
            txtCaseNumber.setText(caseNumber);
        }
        else
        {
            txtCaseNumber.setText("N/A");
        }
        if(!status.equalsIgnoreCase(""))
        {
            txtStatus.setText(status);
        }
        else
        {
            txtStatus.setText("N/A");
        }
          if(!decideFee.equalsIgnoreCase(""))
          {
              txtDecidedFee.setText(decideFee);
          }
          else
          {
              txtDecidedFee.setText("N/A");
          }
        if(!counsellor.equalsIgnoreCase(""))
        {
            txtOpposite.setText(counsellor);
        }
        else
        {
            txtOpposite.setText("N/A");
        }
        if(!retained.equalsIgnoreCase(""))
        {
            txtRetain.setText(retained);
        }
        else
        {
            txtRetain.setText("N/A");
        }
        if(Utils.getUserPreferencesBoolean(getActivity(), UserInfo.CORPORATE_OR_NOT))
        {
            ll_assignedTo.setVisibility(View.VISIBLE);
            txtAssignedTo.setText(assignedTo);
        }
        else
        {
            ll_assignedTo.setVisibility(View.GONE);
        }
        if(Utils.getUserPreferencesBoolean(getActivity(), UserInfo.SMS_ALERT))
        {
            ll_sms_alert_status.setVisibility(View.VISIBLE);
            if(smsAlert) {
                txtSmsAlertStatus.setText(smsAlertStatus);
                ll_sms_alert_no.setVisibility(View.VISIBLE);
                txtSmsAlertNo.setText(smsAlertNumber);
            }
            else
            {
                txtSmsAlertStatus.setText(smsAlertStatus);
                ll_sms_alert_no.setVisibility(View.GONE);
            }
        }
        else
        {
            ll_sms_alert_no.setVisibility(View.GONE);
            ll_sms_alert_status.setVisibility(View.GONE);
        }
        if(!comment.equalsIgnoreCase(""))
        {
            txtComment.setText(comment);
        }
        else
        {
            txtComment.setText("N/A");
        }
        if(!nextDate.equalsIgnoreCase(""))
        {
            try {
                nextDate=dateFormatter2.format(dateFormatter.parse(nextDate));
                txtNextDate.setText(nextDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            txtNextDate.setText("N/A");
        }
    }
    public void getIntentInformation()
    {
        startDate=getArguments().getString(UserInfo.CASE_START_DATE);
        previousDate=getArguments().getString(UserInfo.CASE_PREVIOUS_DATE);
        caseTitle=getArguments().getString(UserInfo.CASE_TITLE);
        caseType=getArguments().getString(UserInfo.CASE_TYPE);
        courtName=getArguments().getString(UserInfo.COURT_NAME);
        caseNumber=getArguments().getString(UserInfo.CASE_NUMBER);
        status=getArguments().getString(UserInfo.CASE_STATUS_NAME);
        nextDate=getArguments().getString(UserInfo.CASE_NEXT_DATE);
        counsellor=getArguments().getString(UserInfo.CASE_OPPOSITE_COUNSELLOR_NAME).trim();
        retained=getArguments().getString(UserInfo.CASE_RETAINED_NAME).trim();
        decideFee=getArguments().getString(UserInfo.DECIDED_FEE);
        comment=getArguments().getString(UserInfo.CASE_DETAIL_COMMENT);
        if(Utils.getUserPreferencesBoolean(getActivity(), UserInfo.SMS_ALERT)) {
            int smsAlertInteger = getArguments().getInt(UserInfo.SMS_ALERT);
            if (smsAlertInteger == 1) smsAlert = true;
            else smsAlert = false;
            if (smsAlert) {
                smsAlertStatus = "Yes";
                smsAlertNumber = getArguments().getString(UserInfo.SMS_ALERT_NUMBER);
            } else {
                smsAlertStatus = "No";
            }
        }
        if(Utils.getUserPreferencesBoolean(getActivity(), UserInfo.CORPORATE_OR_NOT)) {
            assignedTo = getArguments().getString(UserInfo.CASE_ASSIGNED_BY);
        }

    }
public interface CaseDetailListener
{
    public void response(int i);
}
}
