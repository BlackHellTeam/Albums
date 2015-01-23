package com.liang.albums.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;
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

import java.util.List;

/**
 * Created by liang on 15/1/3.
 */
public class AlbumsShowFragment extends PlaceholderFragment {

    private SocialAuthAdapter authAdapter;
    //private ViewPager mPager;
    private JazzyViewPager mPager;
    private List<Feed> feedList;

    private ProgressDialog mDialog;
    DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authAdapter = AlbumsApp.getInstance().getAuthInstagramAdapter();

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

        //handler = new ImageShowHandle();

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
        if (AlbumsApp.getInstance().getPreferenceUtil()
                .getPrefBoolean(Constants.PreferenceConstants.LOGIN_INSTAGRAM, false)){
            authAdapter.getFeedsAsync(new FeedDataListener());
            mDialog.show();
        }

    }

    private void setupJazziness(JazzyViewPager.TransitionEffect effect)
    {
        mPager.setTransitionEffect(effect);
        mPager.setPageMargin(30);
//		mJazzy.setOutlineEnabled(true);
    }

    // To receive the feed response after authentication
    private final class FeedDataListener implements SocialAuthListener<List<Feed>> {

        @Override
        public void onExecute(String provider, List<Feed> t) {

            Log.d("Custom-UI", "Receiving Data");
            mDialog.dismiss();
            feedList = t;

            if (feedList != null && feedList.size() > 0) {
                mPager.setAdapter(new ImageAdapter());
                mPager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
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

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(getActivity());
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

}
