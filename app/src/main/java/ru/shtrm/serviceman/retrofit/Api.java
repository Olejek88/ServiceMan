package ru.shtrm.serviceman.retrofit;

public class Api {

    // Base API
    public static final String API_BASE = "http://shtrm.ru/";

    // Get status of a specific number
    public static final String QUESTION_STATE = API_BASE + "query";

    // Get question by id
    public static final String QUESTION_ID = API_BASE + "question";

    // Get answer by id
    public static final String ANSWER_ID = API_BASE + "answer";

    // Get answers of defined question
    public static final String ANSWERS_ID = API_BASE + "answers";
}
