package com.ls.ui.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.UpdateRequest;
import com.ls.drupalcon.model.UpdatesManager;
import com.ls.utils.NetworkUtils;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialMediaFragment extends Fragment {

    public static final String TAG = "SocialMediaFragment";
    private View rootView;
    private View mLayoutPlaceholder;

    private UpdatesManager.DataUpdatedListener updateReceiver = new UpdatesManager.DataUpdatedListener() {
        @Override
        public void onDataUpdated(List<UpdateRequest> requests) {
            updateData(requests);
        }
    };


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social_media, container, false);
        mLayoutPlaceholder = rootView.findViewById(R.id.layout_placeholder);
        return rootView;
    }

    private void updateData(List<UpdateRequest> requestIds) {
        for (UpdateRequest id : requestIds) {
            if (UpdateRequest.SETTINGS == id) {
                fillView();
                break;
            }
        }
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;

        Model.instance().getUpdatesManager().registerUpdateListener(updateReceiver);

        fillView();
    }

    private void fillView() {

        if (!NetworkUtils.isOn(Objects.requireNonNull(getActivity()))) {
            rootView.findViewById(R.id.list_view).setVisibility(View.GONE);
            rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            mLayoutPlaceholder.setVisibility(View.GONE);
            TextView emptyView = rootView.findViewById(R.id.EmptyView);
            emptyView.setText(R.string.NoConnectionMessage);
        }

        String searchQuery = PreferencesManager.getInstance().getTwitterSearchQuery();

        final SearchTimeline userTimeline = new SearchTimeline.Builder()
                .query(searchQuery)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(rootView.getContext())
                .setTimeline(userTimeline)
                .build();

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });

        ListView list = rootView.findViewById(R.id.list_view);

        list.setEmptyView(mLayoutPlaceholder);
        list.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        Model.instance().getUpdatesManager().unregisterUpdateListener(updateReceiver);
        super.onDestroyView();
    }
}
