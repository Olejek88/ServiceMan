package ru.shtrm.serviceman.mvp.equipment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.EquipmentStatusRepository;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.EquipmentStatusLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MessageLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.util.MainUtil.ACTIVITY_PHOTO_MESSAGE;

public class EquipmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static final String EQUIPMENT_UUID = "EQUIPMENT_UUID";
    private static ImageView add_photo;
    private static String photoUuid;
    private static Bitmap storeBitmap=null;
    private static File photoFile;
    private String equipment_uuid;
    private EquipmentFragment fragment;

    public static void createAddMessageDialog(final Activity activity) {
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
                    photoUuid = java.util.UUID.randomUUID().toString();
                    photoFile = MainUtil.createImageFile(photoUuid, activity);
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(activity,
                                "ru.shtrm.serviceman.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        activity.startActivityForResult(intent, ACTIVITY_PHOTO_MESSAGE);
                    }
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.
                getResources().getColor(R.color.colorPrimaryDark));
        alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.
                getResources().getColor(R.color.colorPrimaryDark));
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
                    message.setFromUser(user);
                    // TODO: видимо нужно реализовать выше выбор пользователя которому отправляется сообщение
                    message.setToUser(null);
                    message.setText(userEditText.getText().toString());
                    message.setDate(new Date());
                    message.setCreatedAt(new Date());
                    message.setChangedAt(new Date());
                    messageRepository.saveMessage(message);
                    MainUtil.storeNewImage(storeBitmap, activity,
                            800, uuid.concat(".jpg"));
                    MainUtil.storePhotoMessage(message, photoUuid);
                    alertDialogAndroid.dismiss();
                    Toast.makeText(activity.getApplicationContext(),"Добавлен комментарий",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    TextView error = mView.findViewById(R.id.dialogError);
                    error.setText(R.string.error_message_title);
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (EquipmentFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "EquipmentFragment");
        } else {
            fragment = EquipmentFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            equipment_uuid = getIntent().getStringExtra("EQUIPMENT_UUID");
            Bundle b = new Bundle();
            b.putString(EQUIPMENT_UUID, equipment_uuid);
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "EquipmentFragment")
                    .commit();
        }

        new EquipmentPresenter(
                fragment,
                EquipmentRepository.getInstance(EquipmentLocalDataSource.getInstance()),
                EquipmentStatusRepository.getInstance(EquipmentStatusLocalDataSource.getInstance()),
                TaskRepository.getInstance(TaskLocalDataSource.getInstance()),
                equipment_uuid);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "EquipmentFragment", fragment);
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
}