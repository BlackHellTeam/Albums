package com.liang.albums.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.interfaces.SocialEventsHandler;
import com.liang.albums.receiver.SocialAccountsReceiver;
import com.liang.albums.service.UpdateContentsService;
import com.liang.albums.util.Constants;
import com.liang.albums.view.JazzyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.brickred.socialauth.Feed;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liang on 15/1/3.
 */
public class AlbumsShowFragment extends PlaceholderFragment implements SocialEventsHandler {

    private static final String TAG = "AlbumsShowFragment";

    private JazzyViewPager mPager;
    private List<Feed> feedList;
    private int mCurrentItem = 0;

    private ProgressDialog mDialog;
    private DisplayImageOptions options;

    private ScheduledExecutorService mScheduledExecutorService;
    private SocialAccountsReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        mReceiver = new SocialAccountsReceiver(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGIN);
        intentFilter.addAction(Constants.Broadcasts.ACTION_LOGOUT);
        intentFilter.addAction(Constants.Broadcasts.ACTION_CONTENTLIST_CHANGED);
        getActivity().registerReceiver(mReceiver, intentFilter);

        feedList = AlbumsApp.getInstance().getContentService().getInstagramList();
//        refreshContent();
    }

    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d(TAG, "Session.StatusCallback "+state.toString());
            if(state.equals(SessionState.OPENED)){
                sendBroadcast(Constants.Broadcasts.ACTION_LOGIN);
            }else if(state.equals(SessionState.CLOSED)) {
                sendBroadcast(Constants.Broadcasts.ACTION_LOGOUT);
            }else if(state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
                // login failed
                Toast.makeText(getActivity(), "Login failed! Please check your network!", Toast.LENGTH_SHORT).show();
            }else if(state.equals(SessionState.OPENING)){
                Toast.makeText(getActivity(), "Trying to login!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 30, 30, TimeUnit.SECONDS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        mPager = (JazzyViewPager)rootView.findViewById(R.id.pager_albums_show);
        setupJazziness(JazzyViewPager.TransitionEffect.Stack);

        mDialog = new ProgressDialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setMessage("Loading...");


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (AlbumsApp.getInstance().getPreferenceUtil()
//                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
//            feedList = AlbumsApp.getInstance().getContentService().getInstagramList();
//        }
        refreshContent();
    }

    private void setupJazziness(JazzyViewPager.TransitionEffect effect)
    {
        mPager.setTransitionEffect(effect);
        mPager.setPageMargin(30);
        mPager.setScrollDurationFactor(5);

        mPager.setAdapter(new ImageAdapter());
        mPager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, mCurrentItem));
//		mJazzy.setOutlineEnabled(true);
    }

    @Override
    public void onSignIn(String account, Constants.SocialInfo.LoginStates state) {

    }

    @Override
    public void onSignOut(String account) {

    }

    @Override
    public void onContentListChanged(String account) {
        Log.d(TAG, "onContentListChanged");
        refreshContent();
    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            FragmentActivity tmp = getActivity();
            inflater = LayoutInflater.from(tmp);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return feedList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_pager_item);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading_pager_item);

            ImageLoader.getInstance().displayImage(feedList.get(position).getMessage(),
                    imageView, options,
                    new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout);
            mPager.setObjectForPosition(imageLayout, position);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


    private class ViewPagerTask implements Runnable{

        @Override
        public void run() {
            if(feedList!=null&&feedList.size()!=0) {
                mCurrentItem = (mCurrentItem + 1) % feedList.size();
                Log.d(TAG, "ViewPagerTask : currentitem = "+mCurrentItem );
                mPagerHandler.obtainMessage().sendToTarget();
            }
        }
    }

    private Handler mPagerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mPager.setCurrentItem(mCurrentItem, true);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void refreshContent(){
        Log.d(TAG, "refreshContent : ");
        UpdateContentsService svr = AlbumsApp.getInstance().getContentService();
        synchronized (feedList){
            if(feedList!=null) {
                feedList.clear();
            }
            feedList = svr.getInstagramList();
            List<Feed> tmp = svr.getFacebookList();
            for(int i=0;i<tmp.size();++i){
                feedList.add(tmp.get(i));
            }


            if(feedList.size()==0){
                Feed feed = new Feed();
                feed.setMessage("assets://desc_image.png");
                feedList.add(feed);
            }
        }

        Log.d(TAG, "refreshContent : feed list count "+feedList.size());
        mPager.setAdapter(new ImageAdapter());
        mPager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
    }

    private void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);//
        intent.putExtra(Constants.Intent.EX_ACCOUNT, Constants.SocialInfo.ACCOUNT_FACEBOOK);
        intent.putExtra(Constants.Intent.EX_LOGIN_STATES,
                Constants.SocialInfo.LoginStates.EX_LOGIN_SUCCESS);
        getActivity().sendBroadcast(intent);
    }
}
