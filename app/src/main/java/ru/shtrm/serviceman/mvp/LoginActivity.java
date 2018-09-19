package ru.shtrm.serviceman.mvp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Token;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.user.UserContract;
import ru.shtrm.serviceman.mvp.user.UserListAdapter;
import ru.shtrm.serviceman.mvp.user.UserPresenter;
import ru.shtrm.serviceman.retrofit.SManApiFactory;
import ru.shtrm.serviceman.retrofit.TokenTask;
import ru.shtrm.serviceman.util.MainUtil;

public class LoginActivity extends AppCompatActivity {

    private UserContract.Presenter presenter;
    private UsersLocalDataSource usersLocalDataSource;

    private Spinner userSelect;
    private EditText pinCode;
    private TextView loginError;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            AuthorizedUser.getInstance().setToken(token);
            unregisterReceiver(this);
        }
    };

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
                User user = (User) userSelect.getSelectedItem();
                String enteredPin = pinCode.getText().toString();
                String enteredPinMD5 = MainUtil.MD5(enteredPin);
                AuthorizedUser aUser = AuthorizedUser.getInstance();

                if (enteredPinMD5 != null && enteredPinMD5.equals(user.getPin())) {
                    aUser.setValidToken(false);
                    checkUser(user.getUuid(), pinCode.getText().toString());
                    // достаём ранее сохранённый токен
                    SharedPreferences sp = getApplicationContext().getSharedPreferences(user.getUuid(), MODE_PRIVATE);
                    String token = sp.getString("token", null);
                    // если токена нет, делаем запрос к серверу
                    if (token == null) {
                        TokenTask task = new TokenTask(getApplicationContext());
                        task.execute(user.getUuid(), user.getPin());
                        registerReceiver(receiver, new IntentFilter(Token.TOKEN_INTENT));
                    } else {
                        aUser.setToken(token);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                AuthorizedUser aUser = AuthorizedUser.getInstance();
                                if (SManApiFactory.pingService()) {
                                    Log.d("xxxx", "ping success");
                                    aUser.setValidToken(true);
                                } else {
                                    Log.d("xxxx", "ping failed");
                                    // TODO: проверить в чём дело
                                    registerReceiver(receiver, new IntentFilter(Token.TOKEN_INTENT));
                                    TokenTask task = new TokenTask(getApplicationContext());
                                    task.execute(aUser.getUser().getUuid(), aUser.getUser().getPin());
                                }
                            }
                        };
                        new Thread(runnable).start();
                    }
                }
            }
        });
    }

    public void initViews() {
        userSelect = findViewById(R.id.user_select);
        pinCode = findViewById(R.id.login_pin);
        loginError = findViewById(R.id.login_error);
        loginError.setBackgroundColor(getResources().getColor(R.color.red));

        presenter = new UserPresenter(UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

        pinCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Button b = findViewById(R.id.loginButton);
                    b.performClick();
                    return true;
                }
                return false;
            }
        });
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
                if (s.length() == 4) {
                    Button b = findViewById(R.id.loginButton);
                    b.performClick();
                }
            }

        });
        pinCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        pinCode.requestFocus();

        List<User> users = presenter.loadUsers();
        UserListAdapter adapter = new UserListAdapter(this, R.layout.item_user, users);
        userSelect.setAdapter(adapter);
    }

    void checkUser(String userUuid, String pin) {
        setResult(RESULT_OK);
        usersLocalDataSource = UsersLocalDataSource.getInstance();
        if (usersLocalDataSource != null) {
            if (usersLocalDataSource.checkUser(userUuid, pin)) {
                AuthorizedUser aUser = AuthorizedUser.getInstance();
                Realm realm = Realm.getDefaultInstance();
                User user = realm.where(User.class).equalTo("uuid", userUuid).findFirst();
                aUser.setUser(realm.copyFromRealm(user));
                realm.close();
                finish();
            } else
                loginError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
