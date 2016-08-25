package com.simonmawole.app.androidnanodegree.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simonmawole.app.androidnanodegree.R;
import com.simonmawole.app.androidnanodegree.activity.MovieActivity;
import com.simonmawole.app.androidnanodegree.data.MovieContentProvider;
import com.simonmawole.app.androidnanodegree.developer.Developer;
import com.simonmawole.app.androidnanodegree.end_point.MovieService;
import com.simonmawole.app.androidnanodegree.model.MovieModel;
import com.simonmawole.app.androidnanodegree.utility.Helpers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MySyncAdapter extends AbstractThreadedSyncAdapter {
    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute) * 60 * 4 = 4 hours
    public static final int SYNC_INTERVAL = 60 * 60 * 4;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final int NOTIFICATION_ID = 1992;

    private Gson gson;
    private Retrofit retrofit;
    private MovieService movieService;
    private Call<MovieModel> call;
    private List<MovieModel.MovieResult> mList;
    private boolean fetchTopRated = true;

    public MySyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        //fetch popular movies
        fetchMovies("popular");

        //display notification
        displayNotification();

    }

    public void fetchMovies(final String category){
        if(Helpers.isConnected(getContext())){
            gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            movieService = retrofit.create(MovieService.class);

            call = movieService.getMovies(category, Developer.MOVIES_API_KEY);

            call.enqueue(new retrofit2.Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    try {
                        mList = response.body().results;

                        //Add to database
                        for(int i = 0; i < mList.size(); i++){
                            MovieModel.MovieResult model = mList.get(i);

                            ContentValues values = new ContentValues();
                            values.put("movie_id", model.id);
                            values.put("overview", model.overview);
                            values.put("original_language", model.original_language);
                            values.put("original_title", model.original_title);
                            values.put("poster_path", model.poster_path);
                            values.put("release_date", model.release_date);
                            values.put("vote_average", model.vote_average);
                            if(category.equalsIgnoreCase("popular")){
                                values.put("popular", 1);
                            } else {
                                values.put("top_rated", 1);
                            }
                            getContext().getContentResolver().insert(
                                    MovieContentProvider.Movie.CONTENT_URI, values);
                        }

                        Helpers.printLog("Fetch "+category+" Movie Complete",
                                mList.size() + "Inserted");

                        if(fetchTopRated) {
                            fetchMovies("top_rated");
                            fetchTopRated = false;
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Helpers.printLog("SYNC-ADAPTER","no internet connection");
        }

    }

    private void displayNotification() {
        Log.e("MY LOGS","notification");
        Context context = getContext();

        boolean displayNotifications = true;
        /*TODO add check notification if enable or disabled
        * */

        if ( displayNotifications ) {

            /*TODO add check if there is new data*/
            boolean newData = true;

            if (newData) {

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("New movies")
                                    .setContentText("Check out the new movies");

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MovieActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    //NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

            }
        }
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with MySyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }

        return newAccount;
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MySyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, "com.simonmawole.app.androidnanodegree", true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);

    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
