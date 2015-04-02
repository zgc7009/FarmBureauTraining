package com.smashingboxes.fb_labs_app.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BaseRequest<T> extends Request<T> {

    public static interface ResponseParseStrategy<T> {
        public static final String KEY_META = "meta";
        public static final String DATA_KEY = "data";

        T parseXml(String rawResponse);
    }

    /**
     * A Gson Object for use in all nested parse strategies
     */
    private static Gson GSON;

    /**
     * Various Parse Strategies
     */
    public static final ResponseParseStrategy<String> NO_PARSE_STRAT;

    static {
        GSON = new Gson();
        NO_PARSE_STRAT = new NoParseStrategy();
    }

    private ResponseParseStrategy<T> mParseStrategy;
    private Listener<T> listener;
    private Map<String, String> params;

    /**
     * Constructs a BaseRequest for dispatching to the Volley Network Queue
     *
     * @param method         - the request method
     * @param url            - the request url
     * @param errorListener  - error response listener
     * @param mListener      - the network response listener
     * @param mParseStrategy - a parse strategy for returning different datatypes from the
     *                       provided json - if the pure JSON is desired pass
     *                       BaseRequest.NO_STRATEGY for the full json as a String.
     */
    public BaseRequest(int method, String url, ErrorListener errorListener,
                       Listener<T> mListener, ResponseParseStrategy<T> mParseStrategy) {
        super(method, url, errorListener);
        this.listener = mListener;
        this.mParseStrategy = mParseStrategy;
    }

    /**
     * Appends parameters to the base URL of a GET or DELETE request.
     *
     * @param baseURL
     * @param params
     * @return baseURL with formated parameters
     */
    public static String addParamsToRequestURL(String baseUrl,
                                               Map<String, String> params) {
        List<BasicNameValuePair> toAppend = new ArrayList<BasicNameValuePair>(
                params.size());

        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            toAppend.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }

        String paramStr = URLEncodedUtils.format(toAppend, "utf-8");
        String finalURL = baseUrl + "?" + paramStr;

        return finalURL;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String toParse;
        try {
            toParse = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            toParse = new String(response.data);
        }

        T mResponse = mParseStrategy.parseXml(toParse);

        return Response.success(mResponse,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public void setRequestParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    protected Map<String, String> getPostParams() throws AuthFailureError {
        return params;
    }

    /**
     * Returns the response sent from the server as a JSON string
     */
    private static class NoParseStrategy implements
            ResponseParseStrategy<String> {

        @Override
        public String parseXml(String rawResponse) {
            return rawResponse;
        }

    }

    public static class ObjectParseStrategy<T> implements
            ResponseParseStrategy<T> {

        private Class<T> clazz;

        public ObjectParseStrategy(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T parseXml(String rawResponse) {
            return GSON.fromJson(rawResponse, clazz);
        }

    }

}
