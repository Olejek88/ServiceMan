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
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.MeasureRepository;
import ru.shtrm.serviceman.data.source.MeasureTypeRepository;
import ru.shtrm.serviceman.data.source.local.MeasureLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MeasureTypeLocalDataSource;
import ru.shtrm.serviceman.mvp.measuretype.MeasureTypeAdapter;
import ru.shtrm.serviceman.retrofit.serial.MeasureSerializer;

public class Measure extends RealmObject implements ISend, IBaseRecord {
    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private Organization organization;
    private Equipment equipment;
    private User user;
    private MeasureType measureType;
    private Date date;
    private double value;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public Measure() {
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

        Number lastId = realm.where(Measure.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static void addToUpdateQuery(Measure measure) {
        // отправляем в очередь на отправку
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(Measure.class, new MeasureSerializer())
                .serializeNulls()
                .create();

        UpdateQuery query = new UpdateQuery(Measure.class.getSimpleName(), null, null, gson.toJson(measure), measure.getChangedAt());
        UpdateQuery.addToQuery(query);
    }

    public static void showDialogEnterMeasure(final Context context, LayoutInflater inflater, final ViewGroup parent, final Equipment equipment) {
        View addMeasureLayout;
        Spinner measureTypeSpinner;
        MeasureTypeLocalDataSource measureTypeLocalDataSource = MeasureTypeLocalDataSource.getInstance();
        MeasureTypeRepository measureTypeRepository = MeasureTypeRepository.getInstance(measureTypeLocalDataSource);
        final MeasureTypeAdapter measureTypeAdapter = new MeasureTypeAdapter(measureTypeRepository.getMeasureTypes());

        addMeasureLayout = inflater.inflate(R.layout.add_measure_dialog, parent, false);
        measureTypeSpinner = addMeasureLayout.findViewById(R.id.spinner_measure_type);
        measureTypeSpinner.setAdapter(measureTypeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Введите показания");
        builder.setView(addMeasureLayout);
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
                TextView measureValue = v.getRootView().findViewById(R.id.addMeasureValue);
                if (measureValue.getText().toString().equals("")) {
                    Toast.makeText(context, "Введите показания!", Toast.LENGTH_LONG).show();
                    return;
                }

                Double value;
                try {
                    value = Double.valueOf(measureValue.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(context, "Введите корректное значение!", Toast.LENGTH_LONG).show();
                    return;
                }

                MeasureType currentMeasureType = null;
                Spinner spinner = v.getRootView().findViewById(R.id.spinner_measure_type);
                int position = spinner.getSelectedItemPosition();
                if (position != AdapterView.INVALID_POSITION) {
                    currentMeasureType = (MeasureType) spinner.getAdapter().getItem(position);
                }

                UUID uuid = UUID.randomUUID();
                Date date = new Date();
                User user = AuthorizedUser.getInstance().getUser();

                MeasureLocalDataSource measureLocalDataSource = MeasureLocalDataSource.getInstance();
                MeasureRepository measureRepository = MeasureRepository.getInstance(measureLocalDataSource);

                long nextId = measureRepository.getLastId() + 1;
                Measure measure = new Measure();
                measure.set_id(nextId);
                measure.setUuid(uuid.toString().toUpperCase());
                measure.setOrganization(user.getOrganization());
                measure.setUser(user);
                measure.setValue(value);
                measure.setDate(date);
                measure.setEquipment(equipment);
                measure.setMeasureType(currentMeasureType);
                measure.setCreatedAt(date);
                measure.setChangedAt(date);
                measureRepository.addMeasure(measure);

                // отправляем в очередь на отправку
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .registerTypeAdapter(Measure.class, new MeasureSerializer())
                        .serializeNulls()
                        .create();

                UpdateQuery query = new UpdateQuery(Measure.class.getSimpleName(), null, null, gson.toJson(measure), measure.getChangedAt());
                UpdateQuery.addToQuery(query);
                dialog.dismiss();
                View rootButtonView = parent.findViewById(R.id.measure_button_layout);
                if (rootButtonView != null) {
                    rootButtonView.setVisibility(View.GONE);
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(listener);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }
}
