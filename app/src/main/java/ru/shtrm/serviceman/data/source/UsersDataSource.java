package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.User;

public interface UsersDataSource {

    List<User> getUsers();

    User getUser(@NonNull String userUuid);

}
