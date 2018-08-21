package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;

public interface AnswersDataSource {

    Observable<List<Answer>> getAnswers(String questionId);

    Observable<Answer> getAnswer(@NonNull final String id);

    Answer getAnswerById(@NonNull final String id);

    void saveAnswer(@NonNull Answer answer);

    void deleteAnswer(@NonNull String id);

    Observable<List<Answer>> refreshAnswers(String questionId);

    void updateAnswerTitle(@NonNull String id, @NonNull String title);

    void updateAnswerText(@NonNull String id, @NonNull String text);

    Observable<List<Answer>> searchAnswers(@NonNull String keyWords);

    void saveAnswer(@NonNull final Answer answer, @NonNull final ArrayList<Image> images);

    void voteUpAnswer(@NonNull Answer answer);

    void voteDownAnswer(@NonNull Answer answer);
}
