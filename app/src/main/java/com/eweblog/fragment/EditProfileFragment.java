package com.eweblog.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.eweblog.R;
import com.eweblog.CorporateUserMainActivity;
import com.eweblog.Utils;
import com.eweblog.common.MapAppConstant;
import com.eweblog.common.MultipartRequest;
import com.eweblog.common.Prefshelper;
import com.eweblog.common.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditProfileFragment extends Fragment
{
    EditText edtName, edtLastName, edtContact,edtEmail,edtCity,edtState;
    File f;
    Button btn_submit;
    private static final int PHOTO_PICKER_ID = 1;

    ProgressDialog pDialog;
    ImageView imageView_profile;
    LinearLayout llProfilePic;

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2500;
    String  u_name,l_name,contact,email, filename,state,city;

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

       setInformation();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               submit();
            }
        });
                return  rootview;
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
                                if(serverMessage.contains("|"))
                                {

                                    Toast.makeText(getActivity(),  serverMessage.replace("|"," "),Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), serverMessage.replace("|", ""), Toast.LENGTH_LONG).show();
                                }
                            }
                            if (serverCode.equalsIgnoreCase("1")) {
                                try {
                                    if ("1".equals(serverCode)) {
                                        JSONObject object1 = object.getJSONObject("data");

                                        u_name=object1.getString("user_name");
                                        contact=object1.getString("user_contact");
                                        email=object1.getString("user_email");
                                        l_name=object1.getString("user_last_name");
                                        state=object1.getString(Prefshelper.USER_STATE_OF_PRACTISE);
                                        city=object1.getString(Prefshelper.USER_CITY_OF_PRACTISE);

                                        }
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_NAME,u_name);
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_EMAIL,email);
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_CONTACT,contact);
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_LAST_NAME,l_name);
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_STATE_OF_PRACTISE,state);
                                    Utils.storeUserPreferences(getActivity(),Prefshelper.USER_CITY_OF_PRACTISE,city);
                                }

                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent intent=new Intent(getActivity(),  MainAcitivity.class);
                                startActivity(intent);
                                getActivity().finish();

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

                        params.put("user_id", Utils.getUserPreferences(getActivity(),Prefshelper.USER_ID));
                        params.put("user_security_hash",Utils.getUserPreferences(getActivity(),Prefshelper.USER_SECURITY_HASH));
                        params.put("first_name", u_name);
                        params.put("user_contact", contact);
                        params.put("user_email",email);
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICKER_ID);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
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
                        Bitmap bitmap = null;

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        Bitmap bmp = BitmapFactory.decodeFile(fil.getAbsolutePath(),bitmapOptions);
//
                        Log.e("edit", "new image path is " + fil.getAbsolutePath());
                        Log.e("bitmap",""+bmp);
                        Log.e("Result Ok",""+data);

                        compressImage(fil.getAbsolutePath());
                        f= new File(filename);
                        uploadImage();
//
                    }

                    break;
                case PHOTO_PICKER_ID:
                    if (requestCode == PHOTO_PICKER_ID && resultCode == Activity.RESULT_OK && null != data) {
                        Log.e("Result Ok",""+data);
                        Uri selectedImage = data.getData();
                        Log.e("selected image", "" + selectedImage);
                        Log.e("selected image", "" + getPath(selectedImage));
                        compressImage(getPath(selectedImage));
                        f= new File(filename);
                        uploadImage();

                    }

                    break;
            }
        } catch (Exception e)
        {
            Log.d("krvrrusbviuritiribtr", e.getMessage());
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
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

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
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
    public void uploadImage()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstant.API + "profile_image");
            HashMap params = new HashMap<String, String>();

            params.put("user_id", Utils.getUserPreferences(getActivity(),Prefshelper.USER_ID));
            params.put("user_security_hash",Utils.getUserPreferences(getActivity(),Prefshelper.USER_SECURITY_HASH));
            MultipartRequest sr = new MultipartRequest(MapAppConstant.API +"profile_image", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.d("file", f + "");
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    String  img=object1.getString("user_profile_image_url");
                                  //  prefHelper.setProfileImage(img);
                                    Log.d("response",img);

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
        edtName.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_NAME));
        edtLastName.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_LAST_NAME));
        edtEmail.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_EMAIL));
        edtContact.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_CONTACT));
        edtCity.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_CITY_OF_PRACTISE));
        edtState.setText(Utils.getUserPreferences(getActivity(),Prefshelper.USER_STATE_OF_PRACTISE));
    }

    public void submit()
    {
        boolean cancelSignup = false;
        View focusView = null;

        u_name = edtName.getText().toString();
        contact = edtContact.getText().toString();
        email =edtEmail.getText().toString();
        l_name=edtLastName.getText().toString();
        state=edtState.getText().toString();
        city=edtCity.getText().toString();

        if (TextUtils.isEmpty(u_name)) {
            edtName.setError("Name is required");
            focusView = edtName;
            cancelSignup = true;
        }
        if (TextUtils.isEmpty(contact)) {
            edtContact.setError("Contact Number is Required");
            focusView = edtContact;
            cancelSignup = true;
        }
        else if (!Utils.isValidPhone((contact))) {
            edtContact.setError("Mobile number must be of digits 10.");
            focusView = edtContact;
            cancelSignup = true;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Field must not be empty.");
            focusView = edtEmail;
            cancelSignup = true;
        }
        else if (!Utils.isValidEmail(email)) {
            edtEmail.setError("Invalid Email.");
            focusView = edtEmail;
            cancelSignup = true;
        }
        else if(TextUtils.isEmpty(state))
        {
            edtState.setError("Field must not be empty.");
            focusView = edtState;
            cancelSignup = true;
        }
        else if(TextUtils.isEmpty(city))
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
                    params.put("user_id", Utils.getUserPreferences(getActivity(),Prefshelper.USER_ID));
                    params.put("user_security_hash",Utils.getUserPreferences(getActivity(),Prefshelper.USER_SECURITY_HASH));
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
}
