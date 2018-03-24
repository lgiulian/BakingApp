package com.lgiulian.bakingapp.model;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by iulian on 3/24/2018.
 */

public class BakingStep {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    /** Method reads all the baking steps from a json reader
     * @param reader
     * @return the list of baking steps
     */
    public static ArrayList<BakingStep> getAllBakingSteps(JsonReader reader){
        ;
        ArrayList<BakingStep> steps = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                steps.add(readEntry(reader));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return steps;
    }

    /** Method used to read a single baking step from a json reader
     * @param reader
     * @return a baking step
     */
    private static BakingStep readEntry(JsonReader reader) {
        int id = 0;
        String shortDescription = null;
        String description = null;
        String videoURL = null;
        String thumbnailURL = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "shortDescription":
                        shortDescription = reader.nextString();
                        break;
                    case "description":
                        description = reader.nextString();
                        break;
                    case "videoURL":
                        videoURL = reader.nextString();
                        break;
                    case "thumbnailURL":
                        thumbnailURL = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BakingStep(id, shortDescription, description, videoURL, thumbnailURL);
    }

    public BakingStep(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
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
}
