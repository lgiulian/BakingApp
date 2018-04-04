package com.lgiulian.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by iulian on 3/24/2018.
 */

public class BakingStep implements Parcelable {
    private static final String TAG = BakingStep.class.getSimpleName();

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    private BakingStep(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public BakingStep() {}

    /** Method reads all the baking steps from a json array
     * @return the list of baking steps
     */
    public static ArrayList<BakingStep> getAllBakingSteps(JSONArray stepsArray){
        ArrayList<BakingStep> steps = new ArrayList<>();
        try {
            if (stepsArray != null) {
                for (int i = 0; i < stepsArray.length(); i++) {
                    BakingStep bakingStep = getStepFromJsonObject(stepsArray.getJSONObject(i));
                    steps.add(bakingStep);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return steps;
    }

    private static BakingStep getStepFromJsonObject(JSONObject recipeJsonObject) {
        BakingStep step = new BakingStep();
        step.setId(recipeJsonObject.optInt("id"));
        step.setShortDescription(recipeJsonObject.optString("shortDescription"));
        step.setDescription(recipeJsonObject.optString("description"));
        step.setVideoURL(recipeJsonObject.optString("videoURL"));
        step.setThumbnailURL(recipeJsonObject.optString("thumbnailURL"));

        Log.v(TAG, step.toString());

        return step;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return "BakingStep{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator<BakingStep> CREATOR = new Parcelable.Creator<BakingStep>() {

        @Override
        public BakingStep createFromParcel(Parcel source) {
            return new BakingStep(source);
        }

        @Override
        public BakingStep[] newArray(int size) {
            return new BakingStep[size];
        }
    };
}
