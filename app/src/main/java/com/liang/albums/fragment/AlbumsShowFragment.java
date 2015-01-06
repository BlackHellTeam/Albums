package com.liang.albums.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
import com.liang.albums.util.PreferenceConstants;

import org.brickred.socialauth.Feed;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.List;

/**
 * Created by liang on 15/1/3.
 */
public class AlbumsShowFragment extends PlaceholderFragment {

    private SocialAuthAdapter authAdapter;
    private ImageView imgShow;
    private List<Feed> feedList;
    private ImageShowHandle handler;

    private ProgressDialog mDialog;
    private Bitmap bitImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authAdapter = AlbumsApp.getInstance().getAuthAdapter();

        handler = new ImageShowHandle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        imgShow = (ImageView)rootView.findViewById(R.id.img_albums_show);

        mDialog = new ProgressDialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setMessage("Loading...");


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(PreferenceConstants.LOGIN_INSTAGRAM, false)){
            authAdapter.getFeedsAsync(new FeedDataListener());
            mDialog.show();
        }

    }

    // To receive the feed response after authentication
    private final class FeedDataListener implements SocialAuthListener<List<Feed>> {

        @Override
        public void onExecute(String provider, List<Feed> t) {

            Log.d("Custom-UI", "Receiving Data");
            mDialog.dismiss();
            feedList = t;

            if (feedList != null && feedList.size() > 0) {
                handler.sendEmptyMessage(1);
                Log.d("AlbumsShow", "Feed List size = "+feedList.size());
                for(int i=0;i<feedList.size();++i){
                    Log.d("AlbumsShow", "Feed List [" + i + "] = " +feedList.get(i).getMessage());
                }
            } else {
                Log.d("AlbumsShow", "Feed List Empty");
            }
        }

        @Override
        public void onError(SocialAuthError e) {
        }
    }

    private class ImageShowHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            final String url = feedList.get(0).getMessage();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    Log.d("AlbumShow", "feedList message 0 : " + feedList.get(0).getMessage());
//                    Picasso.with(getActivity()).load(feedList.get(0).getMessage())
//                            .centerCrop()
//                            .into(imgShow);
                }
            };
            thread.run();
        }
    }

}
