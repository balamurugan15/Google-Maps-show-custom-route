package com.balamurugan.autotest;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Balamurugan M on 3/16/2016.
 */
class MyJavaScriptInterface {

    public interface IDateCallback {
        void setMaps( String temp);
    }

    private IDateCallback callerActivity;


    private Context ctx;

    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
        callerActivity = (IDateCallback)ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        Document doc = Jsoup.parse(html);
        Elements ul = doc.select("div#directions-panel");
        String a = ul.text();
        System.out.println("O_U_T_P_U_T:\n" + doc.text());
       // MapsActivity.jsonFinal = doc.text();
        callerActivity.setMaps( a );
    }

    public void receiveJSON(String json){
       // System.out.println("O_U_T_P_U_T:\n" + json);
    }

}
