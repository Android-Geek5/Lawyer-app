package com.eweblog.common;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MultipartRequest extends Request<String> {
    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    private final Response.Listener<String> mListener;
    private final File file;
    private final HashMap<String, String> params;

    public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, File file, HashMap<String, String> params)
    {
        super(Method.POST, url, errorListener);

        mListener = listener;
        this.file = file;
        this.params = params;
        buildMultipartEntity();
        buildMultipartEntity2();

    }



    private void buildMultipartEntity()
    {
        entity.addBinaryBody("user_profile_image", file, ContentType.create("image/jpeg"), file.getName());
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));

        try
        {
            for ( String key : params.keySet() ) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }
    private void buildMultipartEntity2()
    {
        entity.addBinaryBody("image_file", file, ContentType.create("image/jpeg"), file.getName());
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));

        try
        {
            for ( String key : params.keySet() ) {
                entity.addPart(key, new StringBody(params.get(key)));
            }
        }
        catch (UnsupportedEncodingException e)
        {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }



    @Override
    public String getBodyContentType()
    {
        return entity.build().getContentType().getValue();
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        headers.put("Accept", "application/json");

        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.build().writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }}