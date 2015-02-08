package com.liang.albums.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.liang.albums.R;
import com.liang.albums.app.AlbumsApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liang on 15/2/7.
 */
public class PickerActivity extends FragmentActivity {
    public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
    public static final Uri PLACE_PICKER = Uri.parse("picker://place");

    private static final int SEARCH_RADIUS_METERS = 1000;
    private static final int SEARCH_RESULT_LIMIT = 50;
    private static final String SEARCH_TEXT = "Restaurant";
    private static final int LOCATION_CHANGE_THRESHOLD = 50; // meters

    private static final Location SAN_FRANCISCO_LOCATION = new Location("") {{
        setLatitude(37.7750);
        setLongitude(-122.4183);
    }};

    private FriendPickerFragment friendPickerFragment;
    private PlacePickerFragment placePickerFragment;
    private LocationListener locationListener;

    private Map<String,String> mFriends;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickers);

        mFriends = new HashMap<>();
        getMyFriends();

        Bundle args = getIntent().getExtras();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragmentToShow = null;
        Uri intentUri = getIntent().getData();

        if (FRIEND_PICKER.equals(intentUri)) {
            if (savedInstanceState == null) {
                friendPickerFragment = new FriendPickerFragment(args);
                friendPickerFragment.setFriendPickerType(FriendPickerFragment.FriendPickerType.TAGGABLE_FRIENDS);
            } else {
                friendPickerFragment = (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);;
            }

            friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
                @Override
                public void onError(PickerFragment<?> fragment, FacebookException error) {
                    PickerActivity.this.onError(error);
                }
            });
            friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
                @Override
                public void onDoneButtonClicked(PickerFragment<?> fragment) {
                    finishActivity();
                }
            });
            fragmentToShow = friendPickerFragment;

        } else if (PLACE_PICKER.equals(intentUri)) {
            if (savedInstanceState == null) {
                placePickerFragment = new PlacePickerFragment(args);
            } else {
                placePickerFragment = (PlacePickerFragment) manager.findFragmentById(R.id.picker_fragment);
            }
            placePickerFragment.setOnSelectionChangedListener(new PickerFragment.OnSelectionChangedListener() {
                @Override
                public void onSelectionChanged(PickerFragment<?> fragment) {
                    finishActivity(); // call finish since you can only pick one place
                }
            });
            placePickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
                @Override
                public void onError(PickerFragment<?> fragment, FacebookException error) {
                    PickerActivity.this.onError(error);
                }
            });
            placePickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
                @Override
                public void onDoneButtonClicked(PickerFragment<?> fragment) {
                    finishActivity();
                }
            });
            fragmentToShow = placePickerFragment;
        } else {
            // Nothing to do, finish
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        manager.beginTransaction().replace(R.id.picker_fragment, fragmentToShow).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FRIEND_PICKER.equals(getIntent().getData())) {
            try {
                friendPickerFragment.loadData(false);
            } catch (Exception ex) {
                onError(ex);
            }
        } else if (PLACE_PICKER.equals(getIntent().getData())) {
            try {
                Location location = null;
                Criteria criteria = new Criteria();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String bestProvider = locationManager.getBestProvider(criteria, false);
                if (bestProvider != null) {
                    location = locationManager.getLastKnownLocation(bestProvider);
                    if (locationManager.isProviderEnabled(bestProvider) && locationListener == null) {
                        locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                boolean updateLocation = true;
                                Location prevLocation = placePickerFragment.getLocation();
                                if (prevLocation != null) {
                                    updateLocation = location.distanceTo(prevLocation) >= LOCATION_CHANGE_THRESHOLD;
                                }
                                if (updateLocation) {
                                    placePickerFragment.setLocation(location);
                                    placePickerFragment.loadData(true);
                                }
                            }
                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                            }
                            @Override
                            public void onProviderEnabled(String s) {
                            }
                            @Override
                            public void onProviderDisabled(String s) {
                            }
                        };
                        locationManager.requestLocationUpdates(bestProvider, 1, LOCATION_CHANGE_THRESHOLD,
                                locationListener, Looper.getMainLooper());
                    }
                }
                if (location == null) {
                    String model = Build.MODEL;
                    if (model.equals("sdk") || model.equals("google_sdk") || model.contains("x86")) {
                        // this may be the emulator, pretend we're in an exotic place
                        location = SAN_FRANCISCO_LOCATION;
                    }
                }
                if (location != null) {
                    placePickerFragment.setLocation(location);
                    placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
                    placePickerFragment.setSearchText(SEARCH_TEXT);
                    placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
                    placePickerFragment.loadData(false);
                } else {
                    onError(getResources().getString(R.string.no_location_error), true);
                }
            } catch (Exception ex) {
                onError(ex);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationListener != null) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
    }

    private void onError(Exception error) {
        String text = getString(R.string.exception, error.getMessage());
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void onError(String error, final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_dialog_title).
                setMessage(error).
                setPositiveButton(R.string.error_dialog_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finishActivity) {
                            finishActivity();
                        }
                    }
                });
        builder.show();
    }

    private void finishActivity() {
        AlbumsApp app = (AlbumsApp) getApplication();
        if (FRIEND_PICKER.equals(getIntent().getData())) {
            if (friendPickerFragment != null) {
                List<GraphUser> tmp = friendPickerFragment.getSelection();
//                Log.d("picker activity ", tmp.get(0).getName());
                for(int i=0;i<tmp.size();++i){
                    tmp.get(i).setId(mFriends.get(tmp.get(i).getName()));
                }
                app.setSelectedUsers(tmp);
            }
        } else if (PLACE_PICKER.equals(getIntent().getData())) {
            if (placePickerFragment != null) {
//                app.setSelectedPlace(placePickerFragment.getSelection());  useless for this app
            }
        }
        setResult(RESULT_OK, null);
        finish();
    }

    private void getMyFriends(){
        new Request(
                Session.getActiveSession(),
                "/me?fields=friends",//"/me/taggable_friends", //TAGGABLE_FRIENDS
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        try {
                            GraphObject object = response.getGraphObject();
                            Log.d("getMyFriends", object.getInnerJSONObject().toString());
                            if (object != null) {
                                JSONArray dataArray = new JSONArray(object.getProperty("data").toString());
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);

                                    String name = dataObject.getString("name");
                                    String id = dataObject.getString("id");
                                    mFriends.put(name, id);
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                }
        ).executeAsync();
    }

}
