package ru.shtrm.serviceman.mvp.flat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.FlatStatusRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatStatusLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MessageLocalDataSource;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.util.MainUtil.ACTIVITY_PHOTO_MESSAGE;

public class FlatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private FlatFragment fragment;
    private static ImageView add_photo;
    private static String photoUuid;
    public static final String FLAT_UUID = "FLAT_UUID";
    public static final String HOUSE_UUID = "HOUSE_UUID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (FlatFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "FlatFragment");
        } else {
            fragment = FlatFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String flat_id = getIntent().getStringExtra("FLAT_UUID");
            Bundle b = new Bundle();
            b.putString(FLAT_UUID, flat_id);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "FlatFragment")
                    .commit();
        }

        new FlatPresenter(
                fragment,
                FlatRepository.getInstance(FlatLocalDataSource.getInstance()),
                EquipmentRepository.getInstance(EquipmentLocalDataSource.getInstance()),
                FlatStatusRepository.getInstance(FlatStatusLocalDataSource.getInstance()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "FlatFragment", fragment);
        }
    }

    /**
     * Handle different items of the navigation drawer
     *
     * @param item The selected item.
     * @return Selected or not.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    /**
     * Сохраняем фото
     *
     * @param requestCode The request code. See at {@link WorkFragment}.
     * @param resultCode  The result code.
     * @param data        The result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_PHOTO_MESSAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            photoUuid = java.util.UUID.randomUUID().toString();
                            MainUtil.storeNewImage(bitmap, getApplicationContext(),
                                    800, photoUuid.concat(".jpg"));
                            if (add_photo != null)
                                add_photo.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
        }
    }

    public static void createAddMessageDialog(final Activity activity, final Flat flat) {
        final View mView = LayoutInflater.from(activity).inflate(R.layout.message_add_dialog, null);
        add_photo = mView.findViewById(R.id.imageAddMessage);
        final EditText userEditText = mView.findViewById(R.id.userMessage);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(activity);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput.setPositiveButton("Отправить",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setNegativeButton("Отменить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activity.startActivityForResult(intent, ACTIVITY_PHOTO_MESSAGE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String messageText = userEditText.getText().toString();
                if (messageText.length()>3) {
                    Message message = new Message();
                    MessageLocalDataSource messageRepository = MessageLocalDataSource.getInstance();
                    User user = AuthorizedUser.getInstance().getUser();
                    String uuid = java.util.UUID.randomUUID().toString();
                    message.set_id(messageRepository.getLastId() + 1);
                    message.setUuid(uuid);
                    message.setUser(user);
                    message.setMessage(userEditText.getText().toString());
                    message.setFlat(flat);
                    message.setDate(new Date());
                    message.setCreatedAt(new Date());
                    message.setChangedAt(new Date());
                    messageRepository.saveMessage(message);
                    MainUtil.storePhotoMessage(message, photoUuid);
                    alertDialogAndroid.dismiss();
                }
                else {
                    TextView error = mView.findViewById(R.id.dialogError);
                    error.setText(R.string.error_message_title);
                }
            }
        });
    }
}