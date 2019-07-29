package ru.shtrm.serviceman.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.DefectRepository;
import ru.shtrm.serviceman.data.source.DefectTypeRepository;
import ru.shtrm.serviceman.data.source.local.DefectLocalDataSource;
import ru.shtrm.serviceman.data.source.local.DefectTypeLocalDataSource;
import ru.shtrm.serviceman.mvp.defecttype.DefectTypeAdapter;
import ru.shtrm.serviceman.retrofit.SManApiFactory;
import ru.shtrm.serviceman.retrofit.serial.DefectSerializer;
import ru.shtrm.serviceman.retrofit.serial.PhotoSerializer;

public class Defect extends RealmObject implements ISend, IBaseRecord {
    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private Organization organization;
    private User user;
    private String title;
    private Date date;
    private Task task;
    private Equipment equipment;
    private DefectType defectType;
    private int status;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public Defect() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        sent = false;
        Date createDate = new Date();
        date = createDate;
        createdAt = createDate;
        changedAt = createDate;
        user = AuthorizedUser.getInstance().getUser();
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(Defect.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static List<Defect> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(Defect.class));
        Call<List<Defect>> call = SManApiFactory.getDefectService().getData(lastUpdate);
        try {
            Response<List<Defect>> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showDialogNewDefect(final Context context, LayoutInflater inflater, ViewGroup parent, final Equipment equipment) {

        View addDefectLayout;
        Spinner defectTypeSpinner;
        DefectTypeLocalDataSource defectTypeLocalDataSource = DefectTypeLocalDataSource.getInstance();
        DefectTypeRepository defectTypeRepository = DefectTypeRepository.getInstance(defectTypeLocalDataSource);
        final DefectTypeAdapter defectTypeAdapter = new DefectTypeAdapter(defectTypeRepository.getAllDefectTypes());

        addDefectLayout = inflater.inflate(R.layout.add_defect_dialog, parent, false);
        defectTypeSpinner = addDefectLayout.findViewById(R.id.spinner_defect_type);
        defectTypeSpinner.setAdapter(defectTypeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Укажите дефект");
        builder.setView(addDefectLayout);
        builder.setIcon(R.drawable.ic_icon_warnings);
        builder.setCancelable(false);
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = builder.create();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView defectDescription = v.getRootView().findViewById(R.id.add_new_comment);
                if (defectDescription.getText().toString().equals("")) {
                    Toast.makeText(context, "Укажите суть дефекта!", Toast.LENGTH_LONG).show();
                    return;
                }

                DefectType currentDefectType = null;
                Spinner spinner = v.getRootView().findViewById(R.id.spinner_defect_type);
                int position = spinner.getSelectedItemPosition();
                if (position != AdapterView.INVALID_POSITION) {
                    currentDefectType = (DefectType) spinner.getAdapter().getItem(position);
                }

                UUID uuid = UUID.randomUUID();
                Date date = new Date();
                User user = AuthorizedUser.getInstance().getUser();

                DefectLocalDataSource defectLocalDataSource = DefectLocalDataSource.getInstance();
                DefectRepository defectRepository = DefectRepository.getInstance(defectLocalDataSource);

                long nextId = defectRepository.getLastId() + 1;
                Defect defect = new Defect();
                defect.set_id(nextId);
                defect.setUuid(uuid.toString().toUpperCase());
                defect.setOrganization(user.getOrganization());
                defect.setUser(user);
                defect.setTitle(defectDescription.getText().toString());
                defect.setDate(date);
                defect.setTask(null);
                defect.setEquipment(equipment);
                defect.setDefectType(currentDefectType);
                defect.setDefectStatus(Defect.Status.NOT_PROCESSED);
                defect.setCreatedAt(date);
                defect.setChangedAt(date);
                defectRepository.addDefect(defect);

                // отправляем в очередь на отправку
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .registerTypeAdapter(Defect.class, new DefectSerializer())
                        .serializeNulls()
                        .create();

                UpdateQuery query = new UpdateQuery(Defect.class.getSimpleName(), null, null, gson.toJson(defect), defect.getChangedAt());
                UpdateQuery.addToQuery(query);
                dialog.dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(listener);
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public int getDefectStatus() {
        return status;
    }

    public void setDefectStatus(int status) {
        this.status = status;
    }

    public class Status {
        public static final int NOT_PROCESSED = 0;
        public static final int PROCESSED = 1;
    }

}
