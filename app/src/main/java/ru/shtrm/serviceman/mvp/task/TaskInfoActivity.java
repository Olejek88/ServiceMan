package ru.shtrm.serviceman.mvp.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.mvp.equipment.EquipmentActivity;
import ru.shtrm.serviceman.rfid.RfidDialog;
import ru.shtrm.serviceman.rfid.RfidDriverBase;
import ru.shtrm.serviceman.rfid.Tag;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.rfid.RfidDialog.TAG;

public class TaskInfoActivity extends AppCompatActivity {
    public static final String TASK_UUID = "TASK_UUID";
    private TaskInfoFragment fragment;
    private RfidDialog rfidDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (TaskInfoFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "TaskInfoFragment");
        } else {
            fragment = TaskInfoFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String task_id = getIntent().getStringExtra("TASK_UUID");
            Realm realm = Realm.getDefaultInstance();
            Task task = realm.where(Task.class).equalTo("uuid", task_id).findFirst();
            if (task != null) {
                Tag tag = new Tag();
                tag.loadData(task.getEquipment().getTag());
                String expectedTagId = tag.getTagId();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                boolean ask_tags = sp.getBoolean("without_tags_mode", true);
                if (!ask_tags && expectedTagId != null && !expectedTagId.equals("")) {
                    runRfidDialog(expectedTagId, task_id);
                } else {
                    Intent intent = new Intent(this, EquipmentActivity.class);
                    intent.putExtra("EQUIPMENT_UUID", task.getEquipment().getUuid());
                    startActivity(intent);
                }
            }
            realm.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void runRfidDialog(String expectedTagId, final String uuid) {
        Tag tag = new Tag();
        if (!expectedTagId.contains(":")) {
            // TODO старые и ошибочные метки пока пускаем
            Bundle b = new Bundle();
            b.putString(TASK_UUID, uuid);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "TaskInfoFragment")
                    .commit();
            return;
        }
        tag.loadData(expectedTagId);
        final String expectedTag = tag.getTagId();

        Log.d(TAG, "Ожидаемая метка: " + expectedTagId);
        Handler handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(android.os.Message message) {
                if (message.what == RfidDriverBase.RESULT_RFID_SUCCESS) {
                    String[] tagIds = (String[]) message.obj;
                    if (tagIds == null) {
                        Toast.makeText(getApplicationContext(), "Не верное оборудование!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    String tagId = tagIds[0].substring(4);
                    Log.d(TAG, "Ид метки получили: " + tagId);
                    if (!expectedTag.equals(tagId)) {
                        Toast.makeText(getApplicationContext(), "Не верное оборудование!", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Bundle b = new Bundle();
                        b.putString(TASK_UUID, uuid);
                        fragment.setArguments(b);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.view_pager, fragment, "TaskInfoFragment")
                                .commit();
                    }
                } else {
                    Log.d(TAG, "Ошибка чтения метки!");
                    Toast.makeText(getApplicationContext(), "Ошибка чтения метки.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                // закрываем диалог
                rfidDialog.dismiss();
                return false;
            }
        });
        rfidDialog = new RfidDialog();
        rfidDialog.setHandler(handler);
        rfidDialog.readMultiTagId(tag.getTagDriver(this), tag.getTagId());
        rfidDialog.show(this.getFragmentManager(), TAG);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "TaskInfoFragment", fragment);
        }
    }
}