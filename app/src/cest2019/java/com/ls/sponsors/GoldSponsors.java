package com.ls.sponsors;

import android.content.Context;


import com.ls.drupalcon.R;

import java.util.ArrayList;
import java.util.List;

public class GoldSponsors {

    public static List<SponsorItem> getSponsorsList(Context context) {
        List<SponsorItem> sponsors = new ArrayList<>();

        sponsors.add(new SponsorItem(context.getString(R.string.sponsor_1), R.drawable.sponsor_pl_psipw));
        sponsors.add(new SponsorItem(context.getString(R.string.sponsor_2), R.drawable.sponsor_sil_aecom));
        sponsors.add(new SponsorItem(context.getString(R.string.sponsor_3), R.drawable.sponsor_sil_aget));
        return sponsors;
    }

}
