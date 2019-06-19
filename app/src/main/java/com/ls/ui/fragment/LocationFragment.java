package com.ls.ui.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdateRequest;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.drupalcon.model.data.Location;
import com.ls.drupalcon.model.managers.LocationManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class LocationFragment extends Fragment implements CustomMapFragment.OnActivityCreatedListener {

    private static final int ZOOM_LEVEL = 15;
    private static final int TILT_LEVEL = 0;
    private static final int BEARING_LEVEL = 0;

    public static final String TAG = "LocationsFragment";
    private GoogleMap mGoogleMap;

    private UpdatesManager.DataUpdatedListener updateListener = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<UpdateRequest> requests) {
            replaceMapFragment();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_location, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateListener);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        Model.instance().getUpdatesManager().registerUpdateListener(updateListener);
        replaceMapFragment();
    }

    @Override
    public void onActivityCreated(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        new LoadLocations().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadLocations extends AsyncTask<Void, Void, List<Location>> {
        @Override
        protected List<Location> doInBackground(Void... params) {
            LocationManager locationManager = Model.instance().getLocationManager();
            return locationManager.getLocations();
        }

        @Override
        protected void onPostExecute(List<Location> locations) {
            hideProgressBar();
            fillMapViews(locations);
        }
    }

    private void fillMapViews(List<Location> locations) {
        if (mGoogleMap == null) return;

        if (locations == null || locations.isEmpty()) {
            TextView textViewAddress = Objects.requireNonNull(getView()).findViewById(R.id.txtAddress);
            textViewAddress.setText(getString(R.string.placeholder_location));
        }

        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                Location location = locations.get(i);
                LatLng position = new LatLng(location.getLat(), location.getLon());
                mGoogleMap.addMarker(new MarkerOptions().position(position));

                if (i == 0) {
                    CameraPosition camPos = new CameraPosition(position, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
                    fillTextViews(location);
                }
            }
        }

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
    }

    private void fillTextViews(Location location) {
        if (getView() == null) {
            return;
        }

        TextView txtAmsterdam = getView().findViewById(R.id.txtPlace);
        TextView txtAddress = getView().findViewById(R.id.txtAddress);

        String locationName = location.getName();

        txtAmsterdam.setText(locationName);

        String address = location.getAddress();

        address = address.replace(",", "\n");

        txtAddress.setText(address.trim());
    }

    private void replaceMapFragment() {
        CustomMapFragment mapFragment = CustomMapFragment.newInstance(LocationFragment.this);
        LocationFragment
                .this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolder, mapFragment)
                .commitAllowingStateLoss();
    }

    private void hideProgressBar() {
        if (getView() != null) {
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}
