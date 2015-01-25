package com.liang.albums.service;

import android.app.Service;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.db.InstagramProvider;
import com.liang.albums.db.SocialColums;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.model.FeedModel;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.Feed;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.exception.SocialAuthException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/1/13.
 * 用于维护离线图片库
 */
public class UpdateContentsService extends Service implements SocialEventsHandler{

    private static final String TAG = "UpdateContentsService";


    private UpdateContentsServiceBinder mBinder = new UpdateContentsServiceBinder();
    private Handler mMainHandler = new Handler();

    private ContentResolver mContentResolver;
    private SocialAccountsReceiver mReceiver;

    private List<Feed> mInstagramList;

    public List<Feed> getInstagramList(){
        synchronized (mInstagramList){
            return mInstagramList;
        }
    }

    private enum SocialAccountEnum{
        INSTAGRAM,
        FACEBOOK,
        FLICKER
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mContentResolver = getContentResolver();

        mInstagramList = new ArrayList<>();

        mReceiver = new SocialAccountsReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        intentFilter.addAction(Constants.Broadcasts.ACTION_CONTENTLIST_CHANGED);
        registerReceiver(mReceiver, intentFilter);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class UpdateContentsServiceBinder extends Binder{
        public UpdateContentsService getService(){
            return UpdateContentsService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        mMainHandler.postDelayed(rUpdateList, 1000);//1000*60*5);
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {
        Log.d(TAG, "onSignIn");
        mMainHandler.postDelayed(rUpdateList, 0);
    }

    @Override
    public void onSignOut(String account) {

    }

    @Override
    public void onContentListChanged(String account) {

    }

    private Runnable rUpdateList = new Runnable() {

        @Override
        public void run() {

            SocialAuthAdapter mInstagramAuth =
                    AlbumsApp.getInstance().getAuthInstagramAdapter();
            if(AlbumsApp.getInstance().getPreferenceUtil()
                    .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)) {
                try {
                    mInstagramAuth.getFeedsAsync(new FeedDataListener());
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                    AlbumsApp.getInstance().getPreferenceUtil()
                            .setPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false);
                }

            }
            mMainHandler.postDelayed(this, 1000 * 60);
        }
    };

    private final class FeedDataListener implements SocialAuthListener<List<Feed>> {

        @Override
        public void onExecute(String provider, List<Feed> feedList) {

            Log.d("Custom-UI", "Receiving Data");

            if (feedList != null && feedList.size() > 0) {

                ArrayList<FeedModel> tmpList = new ArrayList<>();

                Log.d(TAG, "Feed List size = "+feedList.size());
                for(int i=0;i<feedList.size();++i){
                    Log.d(TAG, "Feed List [" + i + "] = " +feedList.get(i).getMessage());
                    FeedModel model = new FeedModel();
                    model.setDATE(feedList.get(i).getCreatedAt());
                    model.setFROM(feedList.get(i).getFrom());
                    model.setID(feedList.get(i).getId());
                    model.setMESSAGE(feedList.get(i).getMessage());
                    model.setSCREENNAME(feedList.get(i).getScreenName());

                    tmpList.add(model);
                }

                switch (provider){
                    case "instagram":
                        synchronized (mInstagramList){
                            if(mInstagramList.size()!=feedList.size()){
                                mInstagramList = feedList;
                                updateDatabase(SocialAccountEnum.INSTAGRAM);
                                notifyUpdate(SocialAccountEnum.INSTAGRAM);
                            }
                        }
                        break;
                    case "facebook":
                        break;
                    case "flicker":
                        break;
                }

            } else {
                Log.d("AlbumsShow", "Feed List Empty");
            }
        }

        @Override
        public void onError(SocialAuthError e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private void notifyUpdate(SocialAccountEnum act){
        // sendbroadcast
        Intent intent = new Intent();
        intent.setAction(Constants.Broadcasts.ACTION_CONTENTLIST_CHANGED);
        switch (act){
            case INSTAGRAM:
                intent.putExtra(Constants.Intent.EX_ACCOUNT,
                        Constants.Intent.ActivityIntents.ACTIVITY_INTENT_INSTAGRAM);
                break;
            case FACEBOOK:
                intent.putExtra(Constants.Intent.EX_ACCOUNT,
                        Constants.Intent.ActivityIntents.ACTIVITY_INTENT_FACEBOOK);
                break;
            case FLICKER:
                intent.putExtra(Constants.Intent.EX_ACCOUNT,
                        Constants.Intent.ActivityIntents.ACTIVITY_INTENT_FLICKR);
                break;
        }
        sendBroadcast(intent);
    }

    private List<FeedModel> feedListToModelList(List<Feed> list){
        ArrayList<FeedModel> tmpList = new ArrayList<>();
        for(int i=0;i<list.size();++i){
            Log.d("AlbumsShow", "Feed List [" + i + "] = " +list.get(i).getMessage());
            FeedModel model = new FeedModel();
            model.setDATE(list.get(i).getCreatedAt());
            model.setFROM(list.get(i).getFrom());
            model.setID(list.get(i).getId());
            model.setMESSAGE(list.get(i).getMessage());
            model.setSCREENNAME(list.get(i).getScreenName());

            tmpList.add(model);
        }
        return tmpList;
    }

    private void updateDatabase(SocialAccountEnum act){
        ContentProvider provider;
        List<FeedModel> list = new ArrayList<>();
        Uri uri = Uri.EMPTY;


        switch (act){
            case INSTAGRAM:
//                provider = mInstagramProvider;
                list = feedListToModelList(getInstagramList());
                uri = InstagramProvider.CONTENT_URI;
                break;
            case FACEBOOK:
//                provider = mInstagramProvider; // fix me
                break;
            case FLICKER:
//                provider = mInstagramProvider; // fix me
                break;
            default:
//                provider = mInstagramProvider; // fix me
        }

        // update database
        for(int i=0; i<list.size(); ++i){
            ContentValues value = new ContentValues();
            value.put(SocialColums.IMG_ID, list.get(i).getID());
            value.put(SocialColums.DATE, list.get(i).getDATE().getTime());
            value.put(SocialColums.FROM, list.get(i).getFROM());
            value.put(SocialColums.MESSAGE, list.get(i).getMESSAGE());
            value.put(SocialColums.SCREEN_NAME, list.get(i).getSCREENNAME());

            //provider.insert(uri, value);
            mContentResolver.insert(uri, value);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
