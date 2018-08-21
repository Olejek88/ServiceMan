package ru.shtrm.serviceman.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Answer extends RealmObject {

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName("date")
    private Date date;

    private RealmList<Image> images;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(int voteUp) {
        this.voteUp = voteUp;
    }

    public int getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(int voteDown) {
        this.voteDown = voteDown;
    }

    @Expose
    @SerializedName("voteUp")
    private int voteUp;

    @Expose
    @SerializedName("voteDown")
    private int voteDown;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public double getRating () {
        double rate=0.0;
        // up = 0, down = 0 -> 3
        // up = 1, down = 0 -> 5
        // up > 0, down >= 0 -> up*5/(up+down)
        if (voteUp==0 && voteDown==0) rate=3.0;
        if (voteUp==1 && voteDown==0) rate=5.0;
        if (voteUp>0 && voteDown>=0) rate=voteUp*5/(voteUp+voteDown);
        return rate;
    }
}
