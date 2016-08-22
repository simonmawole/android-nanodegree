package com.simonmawole.app.androidnanodegree.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.Toast;

import com.simonmawole.app.androidnanodegree.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by simon on 26-Jun-16.
 */

public class Helpers {

    public static Toast mToast = null;
    public static String LOG_TAG = null;

    /**
     * Display toast message
     *
     * @param context application context
     * @param message the message to be displayed
    * */
    public static void showToast(Context context, String message){
        mToast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Print logcat message
     *
     * @param title the title of the log, this help to know where the log is from
     * @param message the message passed to be printed in logcat
     * */
    public static void printLog(String title, String message){
        if(title == null) title = "NO TITLE";
        if(message != null) {
            LOG_TAG = "::::" + title;
            Log.i(LOG_TAG, message);
        } else {
            LOG_TAG = "::::" + " NULL TITLE";
            Log.i(LOG_TAG, " NULL MESSAGE");
        }
    }

    /**
     * Check network connectivity
     *
     * @param context
     * */
    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * User friendly date format
     *
     * @param date
     * */
    public static String getDateUserFormat(String date){
        String formatedDate = "";
        if(date != null) {
            if(date.length() != 0) {
                SimpleDateFormat userFormat = new SimpleDateFormat("d MMM yyyy");
                SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {

                    formatedDate = userFormat.format(sqlFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return formatedDate;
    }

}
