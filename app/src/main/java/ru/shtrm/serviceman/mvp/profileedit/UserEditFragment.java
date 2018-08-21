package ru.shtrm.serviceman.mvp.profileedit;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.addquestion.AddQuestionFragment;
import ru.shtrm.serviceman.mvp.profile.UserDetailActivity;
import ru.shtrm.serviceman.util.MainUtil;

import static android.app.Activity.RESULT_OK;
import static ru.shtrm.serviceman.mvp.addquestion.AddQuestionFragment.ACTIVITY_PHOTO;
import static ru.shtrm.serviceman.mvp.addquestion.AddQuestionFragment.REQUEST_CAMERA_PERMISSION_CODE;
import static ru.shtrm.serviceman.mvp.profileedit.UserEditActivity.USER_ID;

public class UserEditFragment extends Fragment
        implements UserEditContract.View {
    private Activity mainActivityConnector = null;

    // View references.
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewWebsite;
    private TextView textViewPhone;
    private ImageView imageView;
    private Button submitButton;
    private FloatingActionButton editUser, addPhoto;
    private Toolbar toolbar;

    private UserEditContract.Presenter presenter;

    private String userUuid = null;
    private String imageName = null;
    private Bitmap userBitmap = null;
    private User user;
    private boolean owner = true;

    public UserEditFragment() {}

    public static UserEditFragment newInstance() {
        return new UserEditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view;
        Bundle b = getArguments();
        if (b!=null) {
            userUuid = b.getString(UserDetailActivity.USER_ID);
            if (userUuid!=null)
                user = UsersLocalDataSource.getInstance().getUserById(userUuid);
        }
        /*
        if (user==null) {
            user = UsersLocalDataSource.getInstance().getLastUser();
            if (user!=null) {
                AuthorizedUser.getInstance().setId(user.getId());
                if (savedInstanceState != null)
                    savedInstanceState.putString("userId", user.getId());
            }
        }*/
        //UsersLocalDataSource.getInstance().deleteUsers();
        view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        initViews(view);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userUuid == null || user == null) {
                    userUuid = java.util.UUID.randomUUID().toString();
                }
                if (user!=null && imageName == null)
                    imageName = user.getAvatar();

                presenter.saveUser(userUuid,
                        textViewName.getText().toString(),
                        textViewAddress.getText().toString(),
                        textViewWebsite.getText().toString(),
                        textViewPhone.getText().toString(),
                        imageName, userBitmap, user);
                AuthorizedUser.getInstance().setId(userUuid);
                Intent intent = new Intent(getContext(), UserDetailActivity.class);
                intent.putExtra(USER_ID, userUuid);
                startActivity(intent);
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter!=null)
            presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter!=null)
            presenter.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        /*
        UserEditActivity activity = (UserEditActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        textViewName = view.findViewById(R.id.profile_add_name);
        textViewAddress = view.findViewById(R.id.profile_add_address);
        textViewWebsite = view.findViewById(R.id.profile_add_website);
        textViewPhone = view.findViewById(R.id.profile_add_phone);
        imageView = view.findViewById(R.id.profile_add_image);
        submitButton = view.findViewById(R.id.profile_button_submit);
        editUser = view.findViewById(R.id.editUser);
        imageView = view.findViewById(R.id.profile_add_image);
        toolbar = view.findViewById(R.id.toolbar);
        addPhoto = view.findViewById(R.id.addPhoto);

        if (user!=null) {
            textViewName.setText(user.getName());
            textViewAddress.setText(user.getAddress());
            textViewWebsite.setText(user.getWebsite());
            textViewPhone.setText(user.getPhone());
            String path = MainUtil.getPicturesDirectory(mainActivityConnector.getApplicationContext());
            if (path!=null) {
                String avatar = user.getAvatar();
                if (avatar != null)
                    imageView.setImageBitmap(MainUtil.getBitmapByPath(path, avatar));
                    //path = path.concat(avatar);
            }
        }
    }

    @Override
    public void setPresenter(@NonNull UserEditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUserName(String name) {
        textViewName.setText(name);
    }

    @Override
    public void setUserAddress(String address) {
        textViewAddress.setText(address);
    }

    @Override
    public void setUserWebsite(String website) {
        Spannable spannable = new SpannableStringBuilder(website);
        spannable.setSpan(new URLSpan(website), 0, website.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWebsite.setText(spannable);
    }

    @Override
    public void showErrorMsg() {
        Snackbar.make(submitButton, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }

    /**
     * Check whether the camera permission has been granted.
     */
    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(mainActivityConnector, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            startPhotoActivity();
        }
    }


    /* To handle the permission grant result.
     * If the user denied the permission, show a dialog to explain
     * the reason why the app need such permission and lead he/her
     * to the system settings to grant permission.
     * @param requestCode The request code. See at {@link AddQuestionFragment#REQUEST_CAMERA_PERMISSION_CODE}
     * @param permissions The wanted permissions.
     * @param grantResults The results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPhotoActivity();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector)
                            .setTitle(R.string.require_permission)
                            .setMessage(R.string.require_permission)
                            .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Go to the detail settings of our application
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            mainActivityConnector.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                break;
            default:

        }
    }

    /**
     * Launch the camera
     */
    private void startPhotoActivity() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, ACTIVITY_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the scanning result.
     * @param requestCode The request code. See at {@link AddQuestionFragment}.
     * @param resultCode The result code.
     * @param data The result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_PHOTO:
                if (resultCode == RESULT_OK) {
                    // TODO сохраняем в базу
                    Image image = new Image();
                    String uuid = java.util.UUID.randomUUID().toString();
                    imageName = uuid.concat(".jpg");
                    image.setId(uuid);
                    image.setImageName(imageName);
                    image.setDate(new Date());
                    image.setTitle(getResources().getString(R.string.other));
                    if (data!=null && data.getData()!=null) {
                        InputStream inputStream;
                        try {
                            inputStream = mainActivityConnector.getApplicationContext()
                                    .getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            if (bitmap!=null) {
                                userBitmap = MainUtil.storeNewImage(bitmap, getContext(),
                                        800, image.getImageName());
                                imageView.setImageBitmap(userBitmap);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            default:
                break;
        }
    }
}
