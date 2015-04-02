package com.smashingboxes.fb_labs_app.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * A NetworkRequestManager for interfacing with the Volley network request
 * queue.
 *
 * @author Austin Lanier
 */
public class NetworkRequestManager {

    private static final NetworkRequestManager mManager = new NetworkRequestManager();

    private static RequestQueue requestQueue;

    private NetworkRequestManager() {
    }

    /**
     * Returns the NetworkRequestManager singleton
     */
    public static NetworkRequestManager getInstance() {
        return mManager;
    }

    public static void initQueue(Context context) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context
                    .getApplicationContext());
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            throw new NullPointerException(
                    "The request queue is null.  "
                            + "You must call NetworkRequestManager.initQueue before accessing the queue.");

        return requestQueue;
    }

    public void cancelAllForTag(Object tag) {
        getRequestQueue().cancelAll(tag);
    }


}
