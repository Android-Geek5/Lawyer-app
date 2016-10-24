package com.eweblog.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eweblog.R;
import com.eweblog.SelectDateActivity;


public class AboutUS extends Fragment {
    ImageView imgFb;
    LinearLayout llContactUs;


    public AboutUS() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_about_us, container, false);
       SelectDateActivity.txtTitle.setText("About Us");
        imgFb=(ImageView)rootview.findViewById(R.id.imageView_fb);
        llContactUs=(LinearLayout)rootview.findViewById(R.id.ll_call);
        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/erginuss/?fref=ts"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        return  rootview;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contact_us);
        TextView txtMobile=(TextView)dialog.findViewById(R.id.textView_mobile);
        TextView txtLandline=(TextView)dialog.findViewById(R.id.textView_landline);
        TextView txtCancel=(TextView)dialog.findViewById(R.id.textView_cancel);

        txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:"+"+91-977-913-5856";
                intent.setData(Uri.parse(temp));
                startActivity(intent);
            }
        });
        txtLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:"+"+91-172-4020155";
                intent.setData(Uri.parse(temp));
                startActivity(intent);
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(getFragmentManager().getBackStackEntryCount() > 0) {

                            SelectDateActivity.txtTitle.setText("Home");
                            getFragmentManager().popBackStack();

                    }

                    return true;

                }

                return false;
            }
        });
    }

}
