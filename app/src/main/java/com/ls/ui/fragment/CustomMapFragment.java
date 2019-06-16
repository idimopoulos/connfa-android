package com.ls.ui.fragment;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private OnActivityCreatedListener mListener;

    private void setOnActivityCreatedListener(OnActivityCreatedListener listener) {
        mListener = listener;
    }

    public static CustomMapFragment newInstance(OnActivityCreatedListener listener) {
        CustomMapFragment mapFragment = new CustomMapFragment();
        mapFragment.setOnActivityCreatedListener(listener);

        return mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mListener.onActivityCreated(googleMap);
    }

    public interface OnActivityCreatedListener {
        void onActivityCreated(GoogleMap googleMap);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mListener != null) {
            getMapAsync(this);
        }
    }
}
