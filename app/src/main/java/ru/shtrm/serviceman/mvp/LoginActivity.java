package ru.shtrm.serviceman.mvp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
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
    private UsersLocalDataSource usersLocalDataSource;

    private Spinner userSelect;
    private EditText pinCode;
    private TextView loginError;

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
                User user = (User)userSelect.getSelectedItem();
                checkUser(user.getUuid(), pinCode.getText().toString());
            }
        });
    }

    public void initViews() {
        userSelect = findViewById(R.id.user_select);
        pinCode = findViewById(R.id.login_pin);
        loginError = findViewById(R.id.login_error);
        loginError.setBackgroundColor(getResources().getColor(R.color.red));

        presenter = new UserPresenter(UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

        pinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loginError.setVisibility(View.GONE);
            }
        });

        List<User> users = presenter.loadUsers();
        UserListAdapter adapter = new UserListAdapter(this,
                R.layout.item_user, users);
        userSelect.setAdapter(adapter);
    }

    void checkUser (String userUuid, String pin) {
        setResult(RESULT_OK);
        usersLocalDataSource = UsersLocalDataSource.getInstance();
        if (usersLocalDataSource!=null) {
            if (usersLocalDataSource.checkUser(userUuid, pin)) {
                AuthorizedUser aUser = AuthorizedUser.getInstance();
                aUser.setId(userUuid);
                finish();
            }
            else
                loginError.setVisibility(View.VISIBLE);
        }
    }
}
