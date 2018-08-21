package ru.shtrm.serviceman.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;

public interface RetrofitService {

    //@GET(Api.USER_QUERY)
    //Observable<UserRecognition> query(@Query("text") String number);

    @GET(Api.QUESTION_STATE)
    Observable<Question> getQuestionClosed(@Query("type") String type, @Query("id") String id);

    @GET(Api.QUESTION_ID)
    Observable<Question> getQuestion(@Query("id") String id);

    @GET(Api.ANSWER_ID)
    Observable<Answer> getAnswer(@Query("id") String id);

    @GET(Api.ANSWERS_ID)
    Observable<Answer> getAnswers(@Query("id") String questionId);
}
