package ru.shtrm.serviceman.data;

import java.util.List;

public class QuestionsAndUsersPairs {

    private List<Question> questions;
    private List<User> users;

    public QuestionsAndUsersPairs(List<Question> questions, List<User> users) {
        this.questions = questions;
        this.users = users;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}