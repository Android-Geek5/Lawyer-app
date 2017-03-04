package com.eweblog.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.eweblog.MainAcitivity;
import com.eweblog.OTPScreenActivity;
import com.eweblog.R;
import com.eweblog.Utils;
import com.eweblog.common.UserInfo;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.MultipartRequest;
import com.eweblog.common.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment
{
    EditText edtName, edtLastName, edtContact,edtEmail,edtCity,edtState,edtCity2,edtState2;
    File f;
    Button btn_submit;
    private static final int PHOTO_PICKER_ID = 1002;
    private static final int PHOTO_PICKER_ID2 = 1003;
    public static final int MULTIPLE_PERMISSIONS = 100;
    ProgressDialog pDialog;
    ImageView imageView_profile;
    LinearLayout llProfilePic;

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1001;
    String strContact,userName, userEmail, userContact,filename,
            userEmailVerified, userMobileVerified,userStatus, imgUrl,userLastName,userStateOfPractice,userCityofPractice,userStateOfPractice2,userCityofPractice2;
    int groupId;
    int corporatePlansId=0;
    int parentId=0;
    View rootview;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragmnet_edit_profile, container, false);
        MainAcitivity.txtTitle.setText("Edit Profile");

        inflateLayout(rootview);
      /*  if(!(prefHelper.getProfileImage().equalsIgnoreCase("")))
        {
            Picasso.with(getActivity()).load(prefHelper.getProfileImage()).into(imageView_profile);
        }

        edtName.setText(prefHelper.getName());
        edtContact.setText(prefHelper.getContact());
        edtDateOfBirth.setText(prefHelper.getDOB());*/

        checkPermission();
                return  rootview;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)+ ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.CAMERA)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MULTIPLE_PERMISSIONS);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MULTIPLE_PERMISSIONS);
                }
            }
        } else {
            setInformation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionAnother = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if(cameraPermission && readExternalFile && permissionAnother)
                    {
                        setInformation();
                    }
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                new String[]{Manifest.permission
                                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MULTIPLE_PERMISSIONS);
                    }
                }

        }
    }
    public void profile_update()
    {
            try {
                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();
                String URL_EDIT_PROFILE=MapAppConstant.API + MapAppConstant.EDIT_PROFILE;
                Log.e("Edit Profile URL",URL_EDIT_PROFILE);
                StringRequest sr = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.e("Edit Profile response",""+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String serverCode = object.getString("code");
                            String serverMessage = object.getString("message");
                            Utils.showToast(getActivity(),serverMessage.replace(" | "," "));
                            if (serverCode.equalsIgnoreCase("0")) {
                             /*   if(serverMessage.contains("|"))
                                {

                                    Toast.makeText(getActivity(),  serverMessage.replace("|"," "),Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), serverMessage.replace("|", ""), Toast.LENGTH_LONG).show();
                                }*/
                            }
                            if (serverCode.equalsIgnoreCase("1")) {
                                try {
                                   // if ("1".equals(serverCode)) {
                                        JSONObject object1 = object.getJSONObject("data");

                                    JSONObject jsonObject=object.getJSONObject("data");
                                    userName=jsonObject.getString(UserInfo.USER_NAME);
                                    userEmail=jsonObject.getString(UserInfo.USER_EMAIL);
                                    userContact=jsonObject.getString(UserInfo.USER_CONTACT);
                                    userEmailVerified=jsonObject.getString(UserInfo.USER_EMAIL_VERIFICATION_STATUS);
                                    userMobileVerified=jsonObject.getString(UserInfo.USER_MOBILE_VERIFICATION_STATUS);
                                    userStatus=jsonObject.getString(UserInfo.USER_STATUS);
                                    userStateOfPractice=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE);
                                    userCityofPractice=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE);
                                    userStateOfPractice2=jsonObject.getString(UserInfo.USER_STATE_OF_PRACTISE1);
                                    userCityofPractice2=jsonObject.getString(UserInfo.USER_CITY_OF_PRACTISE1);
                                    userLastName=jsonObject.getString(UserInfo.USER_LAST_NAME);
                                    parentId=jsonObject.getInt(UserInfo.USER_PARENT_ID);
                                    groupId=jsonObject.getInt(UserInfo.GROUP_ID);
                                    if(jsonObject.getString(UserInfo.USER_PROFILE_IMAGE).equalsIgnoreCase(""))
                                    {imgUrl="";}
                                    else {
                                        imgUrl = jsonObject.getString(UserInfo.USER_PROFILE_IMAGE_URL);
                                    }
                                    if((groupId==4 || groupId==5)&& parentId==0) //Check if paid or business user
                                        corporatePlansId=jsonObject.getInt(UserInfo.CORPORATE_PLANS_ID);
                                    //userSpecialization=jsonObject.getString(UserInfo.USER_SPECIALIZATION);
                                    // }


                                }

                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Utils.saveTypeOfUser(getActivity(),groupId,parentId);
                                Utils.checkSmsAlert(getActivity(),corporatePlansId);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_EMAIL,userEmail);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_NAME,userName);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_CONTACT,userContact);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATUS,userStatus);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_PROFILE_IMAGE_URL,imgUrl);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_LAST_NAME,userLastName);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE,userStateOfPractice);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE,userCityofPractice);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE1,userStateOfPractice2);
                                Utils.storeUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE1,userCityofPractice2);
                                //Utils.storeUserPreferences(getActivity(),UserInfo.USER_SPECIALIZATION,userSpecialization);
                                if(userEmailVerified.equalsIgnoreCase("1"))
                                    Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_EMAIL_VERIFICATION_STATUS,true);
                                else
                                    Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_EMAIL_VERIFICATION_STATUS,false);
                                if(userMobileVerified.equalsIgnoreCase("1")) {
                                    Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_MOBILE_VERIFICATION_STATUS, true);
                                    Intent intent = new Intent(getActivity(), MainAcitivity.class);
                                    startActivity(intent);
                                    getActivity().finish(); //Update the header by killing activity
                                }
                                else {
                                    Utils.storeUserPreferencesBoolean(getActivity(), UserInfo.USER_MOBILE_VERIFICATION_STATUS, false);
                                    Intent intent = new Intent(getActivity(), OTPScreenActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                Utils.storeUserPreferences(getActivity(), UserInfo.GROUP_ID,String.valueOf(groupId));

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        ;
                        //  VolleyLog.d("", "Error: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "Timeout Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                        } else if (error instanceof ServerError) {
                            VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                        } else if (error instanceof NetworkError) {
                            VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                        } else if (error instanceof ParseError) {
                            VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                        }
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put(UserInfo.USER_ID, Utils.getUserPreferences(getActivity(), UserInfo.USER_ID));
                        params.put(UserInfo.USER_SECURITY_HASH,Utils.getUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH));
                        params.put(UserInfo.USER_FIRST_NAME, userName);
                        params.put(UserInfo.USER_LAST_NAME,userLastName);
                        params.put(UserInfo.USER_CONTACT, userContact);
                        params.put(UserInfo.USER_STATE_OF_PRACTISE,userStateOfPractice);
                        params.put(UserInfo.USER_CITY_OF_PRACTISE,userCityofPractice);
                        params.put(UserInfo.USER_STATE_OF_PRACTISE1,userStateOfPractice2);
                        params.put(UserInfo.USER_CITY_OF_PRACTISE1,userCityofPractice2);
                        //params.put("user_email",email);
                        Log.e("EDIT PROFILE REQUEST",params.toString());
                        return params;
                    }
                };
                sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                sr.setShouldCache(true);
                VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    public void dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Upload From");
        dialog.setContentView(R.layout.dialog_pop_up_gallery_camera);

        dialog.setTitle("Select an Option...");
        TextView txt_gallry=(TextView)dialog.findViewById(R.id.textView_gallery);
        TextView txt_camera=(TextView)dialog.findViewById(R.id.textView_camera);

        txt_gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT <19){
                    Intent intent = new Intent();
                    intent.setType("image/jpeg");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICKER_ID);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent,PHOTO_PICKER_ID2);
                }
            /*    Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICKER_ID);*/
            }
        });
        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File fil = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        });
        dialog.show();
    }
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE:

                    if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK ) {

                        File fil = new File(Environment.getExternalStorageDirectory().toString());
                        for (File temp : fil.listFiles()) {
                            if (temp.getName().equals("temp.jpg")) {
                                fil = temp;
                                break;
                            }
                        }
                       /* Bitmap bitmap = null;

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        Bitmap bmp = BitmapFactory.decodeFile(fil.getAbsolutePath(),bitmapOptions);
//
                        Log.e("edit", "new image path is " + fil.getAbsolutePath());
                        Log.e("bitmap",""+bmp);
                        Log.e("Result Ok",""+data);
*/
                        //compressImage(fil.getAbsolutePath());
                       // f= new File(filename);
                       // uploadImage();
                        filename=fil.getAbsolutePath();
                        Log.e("edit", "new image path is " + fil.getAbsolutePath());
                        Log.e("Result Ok",""+data);
                        compressImage(filename);
                        f= new File(filename);
                        uploadImage();
                    }

                    break;
                case PHOTO_PICKER_ID:
                    if (requestCode == PHOTO_PICKER_ID && resultCode == Activity.RESULT_OK && null != data) {
                        Log.e("Result Ok",""+data);
                        Uri selectedImage=null;
                            selectedImage = data.getData();
                       // Uri selectedImage = data.getData();
                        Log.e("selected image", "" + selectedImage);
                        //Log.e("selected image", "" + getPath(selectedImage));
                        //compressImage(getPath(selectedImage));

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                               filename= getRealPathFromURI_API19(getActivity(),selectedImage);
                        }
                        else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        {
                           filename= getRealPathFromURI_API11to18(getActivity(),selectedImage);
                        }
                        compressImage(filename);
                      /*  String encodedString ="";
                        try {

                            BitmapFactory.Options options = null;
                            options = new BitmapFactory.Options();
                            options.inSampleSize = 1;
                            Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            // Must compress the Image to reduce image size to make upload easy
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);

                            byte[] byte_arr = stream.toByteArray();
                            // Encode Image to String
                            encodedString = Base64.encodeToString(byte_arr, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        f= new File(filename);

                        uploadImage();

                    }
                case PHOTO_PICKER_ID2:
                    if (requestCode == PHOTO_PICKER_ID2 && resultCode == Activity.RESULT_OK && null != data) {
                        Log.e("Result Ok",""+data);
                        Uri selectedImage=null;
                            selectedImage = data.getData();
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // Check for the freshest data.
                            getActivity().getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
                        // Uri selectedImage = data.getData();
                        Log.e("selected image", "" + selectedImage);
                        //Log.e("selected image", "" + getPath(selectedImage));
                        //compressImage(getPath(selectedImage));

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                            filename= getRealPathFromURI_API19(getActivity(),selectedImage);
                        }
                        else if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        {
                            filename= getRealPathFromURI_API11to18(getActivity(),selectedImage);
                        }
                        compressImage(filename);
                        f= new File(filename);

                        uploadImage();

                    }
                    break;
            }
        }

    public void uploadImage()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            String PROFILE_IMAGE=MapAppConstant.API + MapAppConstant.PROFILE_IMAGE_CHANGE;
            Log.e("EDIT PROFILE IMAGE URL", PROFILE_IMAGE);
            HashMap params = new HashMap<String, String>();
            params.put("user_id", Utils.getUserPreferences(getActivity(), UserInfo.USER_ID));
            params.put("user_security_hash",Utils.getUserPreferences(getActivity(), UserInfo.USER_SECURITY_HASH));
            MultipartRequest sr = new MultipartRequest(PROFILE_IMAGE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.e("file", f + "");
                    Log.e("EDIT PROFILE IMAGE URL",response.toString());

                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage=object.getString("message");
                        if (serverCode.equalsIgnoreCase("0") || serverCode.equalsIgnoreCase("1")) {
                                Utils.showToast(getActivity(),serverMessage);
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    String  img=object1.getString("user_profile_image_url");
                                    Utils.showToast(getActivity(),serverMessage);
                                    Utils.storeUserPreferences(getActivity(), UserInfo.USER_PROFILE_IMAGE_URL,img);
                                    Intent intent=new Intent(getActivity(),MainAcitivity.class);
                                    startActivity(intent);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());

                    }
                }

            }, f, params);
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
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

                        getFragmentManager().popBackStack();
                        //CorporateUserMainActivity.txtTitle.setText("Home");
                        MainAcitivity.txtTitle.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });
    }


    public void inflateLayout(View rootview)
    {
    edtName =(EditText)rootview.findViewById(R.id.fname);
    edtLastName=(EditText) rootview.findViewById(R.id.lname);
    edtContact =(EditText)rootview.findViewById(R.id.mobile);
    edtEmail=(EditText)rootview.findViewById(R.id.email);
    edtCity=(EditText) rootview.findViewById(R.id.city);
    edtState=(EditText) rootview.findViewById(R.id.state);
        edtCity2=(EditText) rootview.findViewById(R.id.city2);
        edtState2=(EditText) rootview.findViewById(R.id.state2);
    imageView_profile = (ImageView)rootview.findViewById(R.id.imageView_profile);
    llProfilePic=(LinearLayout)rootview.findViewById(R.id.ll_profilePic);

    llProfilePic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog();
        }
    });
    btn_submit=(Button) rootview.findViewById(R.id.email_sign_in_button);
}

    public void setInformation()
    {
        String imageProfileUrl=Utils.getUserPreferences(getActivity(), UserInfo.USER_PROFILE_IMAGE_URL);
       Log.e("IMAGE URL",imageProfileUrl);

        if(!(imageProfileUrl).equalsIgnoreCase(""))
        {
            Picasso.with(getActivity()).load(imageProfileUrl).into(imageView_profile);
        }
        edtName.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_NAME));
        edtLastName.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_LAST_NAME));
        edtEmail.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_EMAIL));
        edtEmail.setEnabled(false);
        edtContact.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_CONTACT));
        edtCity.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE));
        edtState.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE));
        edtCity2.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_CITY_OF_PRACTISE1));
        edtState2.setText(Utils.getUserPreferences(getActivity(), UserInfo.USER_STATE_OF_PRACTISE1));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit()
    {
        boolean cancelSignup = false;
        View focusView = null;

        userName = edtName.getText().toString();
        userContact = edtContact.getText().toString();
        userEmail =edtEmail.getText().toString();
        userLastName=edtLastName.getText().toString();
        userStateOfPractice=edtState.getText().toString();
        userCityofPractice=edtCity.getText().toString();
        userStateOfPractice2=edtState2.getText().toString();
        userCityofPractice2=edtCity2.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            edtName.setError("Name is required");
            focusView = edtName;
            cancelSignup = true;
        }
        if (TextUtils.isEmpty(userContact)) {
            edtContact.setError("Contact Number is Required");
            focusView = edtContact;
            cancelSignup = true;
        }
        else if (!Utils.isValidPhone((userContact))) {
            edtContact.setError("Mobile number must be of digits 10.");
            focusView = edtContact;
            cancelSignup = true;
        }
        if (TextUtils.isEmpty(userEmail)) {
            edtEmail.setError("Field must not be empty.");
            focusView = edtEmail;
            cancelSignup = true;
        }
        else if (!Utils.isValidEmail(userEmail)) {
            edtEmail.setError("Invalid Email.");
            focusView = edtEmail;
            cancelSignup = true;
        }
        else if(TextUtils.isEmpty(userStateOfPractice))
        {
            edtState.setError("Field must not be empty.");
            focusView = edtState;
            cancelSignup = true;
        }
        else if(TextUtils.isEmpty(userCityofPractice))
        {
            edtCity.setError("Field must not be empty.");
            focusView = edtCity;
            cancelSignup = true;
        }
        if (cancelSignup) {
            // error in login
            focusView.requestFocus();
        } else {
            profile_update();
        }
    }
    public void getSpecialisations()
    {
        /*try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();
            String URL_GET_SPECIALIZATION=MapAppConstant.API + MapAppConstant.GET_SPECIALIZATION;
            Log.e("Specialization URL",URL_GET_SPECIALIZATION);
            StringRequest sr = new StringRequest(Request.Method.POST, URL_GET_SPECIALIZATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("Specialization response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Utils.showToast(getActivity(),serverMessage.replace(" | "," "));
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray object1 = object.getJSONArray("data");
                                    HashMap<Integer,String> hash = new HashMap<Integer,String>();
                                    if(object1.length()>0)
                                    {
                                        for(int i=0;i<object1.length();i++)
                                        {
                                            JSONObject object2=object1.getJSONObject(i);
                                            hash.put(object2.getInt("specialization_id"),object2.getString("specialization_name"));
                                        }
                                    }
                                    Log.e("HASHMAP",hash.toString());
                                    Set<Map.Entry<Integer, String>> set = hash.entrySet();

                                    for (Map.Entry<Integer,String> me : set) {
                                        Log.e("Hashmap","Key :"+me.getKey() +" Name : "+ me.getValue());

                                    }
                                }
                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", Utils.getUserPreferences(getActivity(),UserInfo.USER_ID));
                    params.put("user_security_hash",Utils.getUserPreferences(getActivity(),UserInfo.USER_SECURITY_HASH));
                    Log.e("Specialization Request",params.toString());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }

*/
    }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri){
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{ id }, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }


        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = { MediaStore.Images.Media.DATA };
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if(cursor != null){
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
    public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
       filename = getFilename();
       // filename=filePath;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
