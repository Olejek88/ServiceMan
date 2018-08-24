package ru.shtrm.serviceman.mvp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.HouseLocalDataSource;
import ru.shtrm.serviceman.data.source.local.StreetLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.abonents.AbonentsPresenter;
import ru.shtrm.serviceman.mvp.profile.UserDetailContract;
import ru.shtrm.serviceman.mvp.user.UserAdapter;
import ru.shtrm.serviceman.mvp.user.UserContract;
import ru.shtrm.serviceman.mvp.user.UserListAdapter;
import ru.shtrm.serviceman.mvp.user.UserPresenter;

public class LoginActivity  extends AppCompatActivity{

    private UserContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        initViews();

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkUser();
            }
        });
    }

    public void initViews() {
        Spinner userSelect = findViewById(R.id.user_select);
        EditText pinCode = findViewById(R.id.login_pin);

        presenter = new UserPresenter(UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

        List<User> users = presenter.loadUsers();
        UserListAdapter adapter = new UserListAdapter(this,
                R.layout.item_user, users);
        userSelect.setAdapter(adapter);
    }

    void checkUser () {
        setResult(RESULT_OK);
        finish();
    }
}
