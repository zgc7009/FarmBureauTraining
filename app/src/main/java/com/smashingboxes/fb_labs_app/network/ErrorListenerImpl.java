package com.smashingboxes.fb_labs_app.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public abstract class ErrorListenerImpl implements Response.ErrorListener {

    private static final String TAG = ErrorListenerImpl.class.getSimpleName();

    private final Context context;

    public ErrorListenerImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public interface NetworkErrorStatus {
        int BAD_REQUEST = 400;
        int UNAUTHORIZED = 401;
        int PAYMENT_REQUIRED = 402;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int UNPROCESSABLE_ENTITY = 422;
        int SERVER_ERROR = 500;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            showToast(error.toString());
        } else if (error instanceof AuthFailureError) {
            Log.d(TAG, error.toString());
        } else if (error instanceof ServerError) {
            Log.d(TAG, error.toString());
        } else if (error instanceof NetworkError) {
            Log.d(TAG, error.toString());
        } else if (error instanceof ParseError) {
            Log.d(TAG, error.toString());
        }

        finalizeError(error);
    }

    public abstract void finalizeError(VolleyError e);

    public static ErrorListenerImpl getDefaultErrorListener(Context context) {
        return new ErrorListenerImpl(context) {

            @Override
            public void finalizeError(VolleyError e) {
                // Do nothing
            }
        };
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}