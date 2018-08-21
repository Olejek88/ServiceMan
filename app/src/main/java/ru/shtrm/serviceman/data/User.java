package ru.shtrm.serviceman.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @Expose
    @SerializedName("name")
    private String name;

    @PrimaryKey
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("website")
    private String website;

    @Expose
    @SerializedName("address")
    private String address;

    @Expose
    @SerializedName("avatar")
    private String avatar;

    @Expose
    @SerializedName("rating")
    private String rating;

    @Expose
    @SerializedName("questions")
    private RealmList<Question> questions;

    public RealmList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(RealmList<Question> questions) {
        this.questions = questions;
    }

    @Expose
    @SerializedName("answers")
    private RealmList<Answer> answers;

    public RealmList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(RealmList<Answer> answers) {
        this.answers = answers;
    }

    @Expose
    @SerializedName("tricks")
    private RealmList<Trick> tricks;

    public RealmList<Trick> getTricks() {
        return tricks;
    }

    public void setTricks(RealmList<Trick> tricks) {
        this.tricks = tricks;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public String getStats() {
        String stats = " [Q: ".
                concat(Integer.toString(questions.size())).
                concat(" A: ").
                concat(Integer.toString(answers.size())).
                concat(" T: ").
                concat(Integer.toString(tricks.size())).
                concat("]");
        if (rating!=null)
            stats = stats.concat(" R: ").concat(rating);
        return stats;
    }
}
