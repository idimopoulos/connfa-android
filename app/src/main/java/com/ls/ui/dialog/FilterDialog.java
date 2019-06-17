package com.ls.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.data.Level;
import com.ls.drupalcon.model.data.Track;
import com.ls.ui.adapter.FilterDialogAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FilterDialog extends DialogFragment {

    private static final String ARG_TRACKS = "ARG_TRACKS";
    private static final String ARG_EXP_LEVEL = "ARG_EXP_LEVEL";

    private static List<Level> mLevelList;
    private static List<Track> mTrackList;
    private static List<List<Long>> mSelectedIds;

    private ExpandableListView mListView;
    private FilterDialogAdapter mAdapter;

    private OnFilterApplied mListener;

    public interface OnFilterApplied {
        void onNewFilterApplied();
    }

    public static FilterDialog newInstance(@NonNull String[] tracks, @NonNull String[] expLevels) {
        FilterDialog filterDialog = new FilterDialog();

        Bundle args = new Bundle();
        args.putStringArray(ARG_TRACKS, tracks);
        args.putStringArray(ARG_EXP_LEVEL, expLevels);
        filterDialog.setArguments(args);

        mLevelList = new ArrayList<>();
        mTrackList = new ArrayList<>();
        mSelectedIds = new ArrayList<>();

        return filterDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFilterApplied) activity;
    }

    public void setData(List<Level> levelList, List<Track> trackList) {
        if (levelList != null && trackList != null) {
            mLevelList.addAll(levelList);
            mTrackList.addAll(trackList);
        }
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);

        initViews(view);

        builder.setView(view);
        Dialog result = builder.create();
        result.requestWindowFeature(Window.FEATURE_NO_TITLE);
        result.setCanceledOnTouchOutside(true);

        return result;
    }

    private void initViews(View view) {
        initList(view);
        initButtons(view);
    }

    private void initList(final View view) {
        mListView = view.findViewById(R.id.listView);

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        mListView.setIndicatorBoundsRelative(width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_left),
                width - (int) getResources().getDimension(R.dimen.exp_list_indicator_bounds_right));

        List<String> listDataHeader = new ArrayList<>();
        listDataHeader.add(getActivity().getString(R.string.exp_levels));
        listDataHeader.add(getActivity().getString(R.string.tracks));

        String[] expLevels = new String[0];
        String[] tracks = new String[0];

        if (getArguments() != null) {
            expLevels = getArguments().getStringArray(ARG_EXP_LEVEL);
            tracks = getArguments().getStringArray(ARG_TRACKS);
        }

        HashMap<String, String[]> listDataChild = new HashMap<>();
        listDataChild.put(listDataHeader.get(0), expLevels);
        listDataChild.put(listDataHeader.get(1), tracks);

        mAdapter = new FilterDialogAdapter(getActivity(), listDataHeader, listDataChild);
        mAdapter.setData(mLevelList, mTrackList);

        mSelectedIds = loadSelectedIds();
        mAdapter.setCheckedPositions(mSelectedIds);
        mAdapter.setListener(new FilterDialogAdapter.Listener() {
            @Override
            public void onGroupClicked(int groupPosition) {
                if (mListView.isGroupExpanded(groupPosition)) {
                    mListView.collapseGroup(groupPosition);
                } else {
                    mListView.expandGroup(groupPosition);
                }
            }

            @Override
            public void onChildClicked(int groupPosition, int childPosition) {
                mAdapter.setClicked(groupPosition, childPosition);
            }
        });

        mListView.setAdapter(mAdapter);
        for (int i = 0; i < mSelectedIds.size(); i++) {
            List<Long> ids = mSelectedIds.get(i);
            if (!ids.isEmpty()) {
                mListView.expandGroup(i);
            }
        }
    }

    private void initButtons(View view) {
        TextView txtApply = view.findViewById(R.id.btnApply);
        txtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter();
                dismissAllowingStateLoss();
            }
        });

        TextView txtClear = view.findViewById(R.id.btnClear);
        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
                dismissAllowingStateLoss();
            }
        });
    }

    private void applyFilter() {
        saveSelectedItems(mAdapter.getSelectedIds());
        if (mListener != null) {
            mListener.onNewFilterApplied();
        }
    }

    public void clearFilter() {
        clearSelectedItems();
        saveSelectedItems(mSelectedIds);
        if (mListener != null) {
            mListener.onNewFilterApplied();
        }
    }

    private void clearSelectedItems() {
        if (mSelectedIds != null && !mSelectedIds.isEmpty()) {
            mSelectedIds.get(0).clear();
            mSelectedIds.get(1).clear();
        }
    }

    private void saveSelectedItems(List<List<Long>> selectedIds) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            PreferencesManager.getInstance().saveExpLevel(selectedIds.get(0));
            PreferencesManager.getInstance().saveTracks(selectedIds.get(1));
        }
    }

    @NonNull
    private List<List<Long>> loadSelectedIds() {
        List<List<Long>> selectedIds = new ArrayList<>();
        selectedIds.add(PreferencesManager.getInstance().loadExpLevel());
        selectedIds.add(PreferencesManager.getInstance().loadTracks());
        return selectedIds;
    }
}
