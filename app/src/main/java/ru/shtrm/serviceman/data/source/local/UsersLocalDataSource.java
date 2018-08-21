package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersDataSource;
import ru.shtrm.serviceman.realm.RealmHelper;

public class UsersLocalDataSource implements UsersDataSource {

    @Nullable
    private static UsersLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private UsersLocalDataSource() {

    }

    public static UsersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<User>> getUsers() {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable
                .fromIterable(realm.copyFromRealm(
                        realm.where(User.class).findAllSorted("name", Sort.ASCENDING)))
                .toList()
                .toObservable();
    }

    public User getLastUser() {
        Realm realm = RealmHelper.newRealmInstance();
        User user = realm.copyFromRealm(realm.where(User.class).findFirst());
        realm.close();
        return user;
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user!=null)
            return Observable.just(realm.copyFromRealm(user));
        else
            return null;
    }

    @Override
    public User getUserById(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user!=null) {
            user = realm.copyFromRealm(user);
        }
        realm.close();
        return user;
    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        Realm rlm = RealmHelper.newRealmInstance();
        List<User> results = rlm.copyFromRealm(
                rlm.where(User.class)
                        .like("name","*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("phone", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("website", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAllSorted("name", Sort.ASCENDING));
        return Observable.fromIterable(results)
                .toList()
                .toObservable();
    }

    public User getAuthorisedUser() {
        Realm realm = RealmHelper.newRealmInstance();
        AuthorizedUser aUser = AuthorizedUser.getInstance();
        if (aUser!=null) {
            User user = realm.where(User.class).equalTo("id",
                    aUser.getId()).findFirst();
            if (user!=null) {
                return realm.copyFromRealm(user);
            }
        }
        return null;
    }

    public boolean isUserExist(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        User user =  realm.where(User.class).equalTo("id", id).findFirst();
        realm.close();
        return user != null;
    }

    public void deleteUser(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final User user =  realm.where(User.class).equalTo("id", id).findFirst();
        if (user!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    user.deleteFromRealm();
                }
            });
        }
        realm.close();
    }

    public void deleteUsers() {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(User.class).findAll().deleteAllFromRealm();
            }
        });
        realm.close();
    }

    /**
     * Save a user to database.
     * @param user See {@link User}
     */
    @Override
    public void saveUser(@NonNull final User user) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
        realm.close();
    }

    @Override
    public void addQuestion(@NonNull final Question question, final User user) {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.getQuestions().add(question);
                realm.copyToRealmOrUpdate(user);
            }
        });
        realm.close();
    }

    @Override
    public void addTrick(@NonNull final Trick trick, final User user) {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.getTricks().add(trick);
                realm.copyToRealmOrUpdate(user);
            }
        });
        realm.close();
    }

    @Override
    public void addAnswer(@NonNull final Answer answer, final User user) {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.getAnswers().add(answer);
                realm.copyToRealmOrUpdate(user);
            }
        });
        realm.close();
    }

}