package ru.shtrm.serviceman.mvp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.app.App;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.user.UserContract;
import ru.shtrm.serviceman.mvp.user.UserListAdapter;
import ru.shtrm.serviceman.mvp.user.UserPresenter;
import ru.shtrm.serviceman.retrofit.TokenTask;
import ru.shtrm.serviceman.rfid.RfidDialog;
import ru.shtrm.serviceman.rfid.Tag;
import ru.shtrm.serviceman.ui.PrefsActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG;

    static {
        TAG = "LoginActivity";
    }

    private Spinner userSelect;
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
                final RfidDialog rfidDialog;
                rfidDialog = new RfidDialog();
                final User user = (User) userSelect.getSelectedItem();
                final Tag tag = new Tag();
                tag.loadData(user.getPin());

                Handler.Callback callback = new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        String tagId = (String) msg.obj;
                        tagId = tagId.substring(4);
                        Log.d(TAG, "tagId: " + tagId);
                        if (tag.getTagId().equals(tagId)) {
                            // вошедшего пользователя устанавливаем как активного
                            AuthorizedUser aUser = AuthorizedUser.getInstance();
                            aUser.reset();
                            Realm realm = Realm.getDefaultInstance();
                            aUser.setUser(realm.copyFromRealm(user));
                            realm.close();
                            aUser.setValidToken(false);

                            // сохраняем uuid успешно вошедшего пользователя
                            SharedPreferences sp;
                            sp = getApplicationContext().getSharedPreferences("lastUser", MODE_PRIVATE);
                            sp.edit().putString("uuid", user.getUuid()).apply();

                            // запускаем поток для получения токена
                            if (App.isInternetOn(getApplicationContext())) {
                                new TokenTask(getApplicationContext(), user.getUuid(), user.getPin()).execute();
                            }

                            // завершаем окно входа
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Не верный код!", Toast.LENGTH_SHORT).show();
                            loginError.setVisibility(View.VISIBLE);
                        }

                        rfidDialog.dismiss();
                        return true;
                    }
                };
                Handler handler = new Handler(callback);
                rfidDialog.setHandler(handler);
                rfidDialog.readTagId(tag.getTagDriver(getApplicationContext()));
                rfidDialog.show(getFragmentManager(), RfidDialog.TAG);
            }
        });
    }

    public void initViews() {
        UserContract.Presenter presenter = new UserPresenter(UsersRepository.getInstance(UsersLocalDataSource.getInstance()));
        userSelect = findViewById(R.id.user_select);
        loginError = findViewById(R.id.login_error);
        loginError.setBackgroundColor(getResources().getColor(R.color.red));


//        pinCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    Button b = findViewById(R.id.loginButton);
//                    b.performClick();
//                    return true;
//                }
//                return false;
//            }
//        });
//        pinCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                loginError.setVisibility(View.GONE);
//                if (s.length() == 4) {
//                    Button b = findViewById(R.id.loginButton);
//                    b.performClick();
//                }
//            }
//
//        });
//        pinCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                }
//            }
//        });
//        pinCode.requestFocus();

        RealmResults<User> users = presenter.loadUsers();
        UserListAdapter adapter = new UserListAdapter(this, R.layout.item_user, users);
        userSelect.setAdapter(adapter);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("lastUser", MODE_PRIVATE);
        String lastUserUuid = sp.getString("uuid", null);
        if (lastUserUuid != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                User user = adapter.getItem(i);
                if (user != null && user.getUuid().equals(lastUserUuid)) {
                    userSelect.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Настройки").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(LoginActivity.this, PrefsActivity.class);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_SETTINGS);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onPause() {
        Log.d("xxxx", "LoginActivity:onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("xxxx", "LoginActivity:onResume()");
        super.onResume();
    }
}
