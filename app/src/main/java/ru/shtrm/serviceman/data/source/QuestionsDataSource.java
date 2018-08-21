package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.serviceman.data.Question;

public interface QuestionsDataSource {

    Observable<List<Question>> getQuestions();

    Observable<Question> getQuestion(@NonNull final String id);

    Question getQuestionById(@NonNull final String id);

    void saveQuestion(@NonNull Question question);

    void deleteQuestion(@NonNull String id);

    Observable<List<Question>> refreshQuestions();

    Observable<Question> refreshQuestion(@NonNull String id);

    boolean isQuestionExist(@NonNull String id);

    void updateQuestionTitle(@NonNull String id, @NonNull String title);

    void updateQuestionText(@NonNull String id, @NonNull String text);

    void updateQuestion(@NonNull String id, @NonNull String title, @NonNull String text);

    void updateQuestionClosed(@NonNull String id, boolean closed);

    Observable<List<Question>> searchQuestions(@NonNull String keyWords);

}
