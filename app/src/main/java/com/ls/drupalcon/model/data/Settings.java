package com.ls.drupalcon.model.data;

import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("titleMajor")
    private String titleMajor;

    @SerializedName("titleMinor")
    private String titleMinor;

    @SerializedName("timezone")
    private String timeZone;

    public String getTitleMajor() {
        return titleMajor;
    }

    public void setTitleMajor(String titleMajor) {
        this.titleMajor = titleMajor;
    }

    public String getTitleMinor() {
        return titleMinor;
    }

    public void setTitleMinor(String titleMinor) {
        this.titleMinor = titleMinor;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
