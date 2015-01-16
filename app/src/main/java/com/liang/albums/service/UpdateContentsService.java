package com.liang.albums.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.liang.albums.app.AlbumsApp;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.model.FeedModel;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.util.Constants;

import org.brickred.socialauth.Feed;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 15/1/13.
 * 用于维护离线图片库
 */
public class UpdateContentsService extends Service implements SocialEventsHandler{

    private SocialAccountsReceiver mReceiver;
    private UpdateContentsServiceBinder mBinder = new UpdateContentsServiceBinder();
    private Handler mMainHandler = new Handler();

    private ArrayList<FeedModel> mInstagramList;

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new SocialAccountsReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        registerReceiver(mReceiver, intentFilter);

        mInstagramList = new ArrayList<FeedModel>();
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
        mMainHandler.postDelayed(rUpdateList, 1000);//1000*60*5);
        return START_STICKY;
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {

    }

    @Override
    public void onSignOut(String account) {

    }

    @Override
    public void onContentListChanged(String account) {

    }

    private Runnable rUpdateList = new Runnable() {
        private SocialAuthAdapter mInstagramAuth =
                AlbumsApp.getInstance().getAuthInstagramAdapter();

        @Override
        public void run() {
            // do while sleep...
            mInstagramAuth.getFeedsAsync(new FeedDataListener());
        }
    };

    private final class FeedDataListener implements SocialAuthListener<List<Feed>> {

        @Override
        public void onExecute(String provider, List<Feed> feedList) {

            Log.d("Custom-UI", "Receiving Data");

            if (feedList != null && feedList.size() > 0) {

                ArrayList<FeedModel> tmpList = new ArrayList<FeedModel>();

                Log.d("AlbumsShow", "Feed List size = "+feedList.size());
                for(int i=0;i<feedList.size();++i){
                    Log.d("AlbumsShow", "Feed List [" + i + "] = " +feedList.get(i).getMessage());
                    FeedModel model = new FeedModel();
                    model.setDATE(feedList.get(i).getCreatedAt());
                    model.setFROM(feedList.get(i).getFrom());
                    model.setID(feedList.get(i).getId());
                    model.setMESSAGE(feedList.get(i).getMessage());
                    model.setSCREENNAME(feedList.get(i).getScreenName());

                    tmpList.add(model);
                }

                synchronized (mInstagramList){
                    mInstagramList = tmpList;
                    notifyUpdate();
                }

            } else {
                Log.d("AlbumsShow", "Feed List Empty");
            }
        }

        @Override
        public void onError(SocialAuthError e) {
        }
    }


    private void notifyUpdate(){
        // sendbroadcast
    }
}
