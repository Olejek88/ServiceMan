package ru.shtrm.serviceman.mvp.equipment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.app.App;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;
import ru.shtrm.serviceman.data.source.MeasureRepository;
import ru.shtrm.serviceman.data.source.PhotoEquipmentRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MeasureLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoEquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.abonents.WorkFragment.REQUEST_CAMERA_PERMISSION_CODE;
import static ru.shtrm.serviceman.mvp.equipment.EquipmentActivity.EQUIPMENT_ID;

public class EquipmentFragment extends Fragment implements EquipmentContract.View {
    private Activity mainActivityConnector = null;
    public final static int ACTIVITY_PHOTO = 100;

    private EquipmentContract.Presenter presenter;
    private Equipment equipment;
    private PhotoEquipment photoEquipment;

    private GpsTrackRepository gpsTrackRepository;
    private PhotoEquipmentRepository photoEquipmentRepository;
    private MeasureRepository measureRepository;

    private CircleImageView circleImageView;
    private TextInputEditText textInputMeasure;
    private TextView textViewPhotoDate;

    private FloatingActionButton enter_measure;
    private FloatingActionButton make_photo;
    protected BarChart mChart;

    public EquipmentFragment() {
    }

    public static EquipmentFragment newInstance() {
        return new EquipmentFragment();
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
        if (b != null) {
            String equipmentUuid = b.getString(EQUIPMENT_ID);
            if (equipmentUuid != null)
                equipment = EquipmentLocalDataSource.getInstance().getEquipmentByUuid(equipmentUuid);
        }
        view = inflater.inflate(R.layout.fragment_equipment, container, false);
        if (equipment != null) {
            photoEquipment = PhotoEquipmentLocalDataSource.getInstance().getLastPhotoByEquipment(equipment);
            initViews(view);
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.subscribe();
        if (photoEquipmentRepository == null)
            photoEquipmentRepository = PhotoEquipmentRepository.getInstance
                    (PhotoEquipmentLocalDataSource.getInstance());
        if (gpsTrackRepository == null)
            gpsTrackRepository = GpsTrackRepository.getInstance
                    (GpsTrackLocalDataSource.getInstance());
        if (measureRepository == null)
            measureRepository = MeasureRepository.getInstance
                    (MeasureLocalDataSource.getInstance());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null)
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
        TextView textViewSerial = view.findViewById(R.id.textViewEquipmentSerial);
        TextView textViewDate = view.findViewById(R.id.textViewEquipmentDate);
        TextView textViewStatus = view.findViewById(R.id.textViewEquipmentStatus);
        TextView textViewType = view.findViewById(R.id.textViewEquipmentTitle);
        TextView textViewEquipment = view.findViewById(R.id.textViewEquipment);
        Spinner statusSpinner = view.findViewById(R.id.equipment_status);
        //GridView gridView = view.findViewById(R.id.gridview);

        textViewPhotoDate = view.findViewById(R.id.textViewPhotoDate);
        textInputMeasure = view.findViewById(R.id.addMeasureValue);
        circleImageView = view.findViewById(R.id.imageViewEquipment);
        make_photo = view.findViewById(R.id.make_photo);
        enter_measure = view.findViewById(R.id.enter_measure);

        initChart(view);

        textViewSerial.setText(equipment.getSerial());
        textViewType.setText(equipment.getEquipmentType().getTitle());
        textViewStatus.setText(equipment.getEquipmentStatus().getTitle());
        //textViewEquipment.setText(equipment.getEquipmentType().getTitle().substring(0, 1));

        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        if (mToolbar !=null) {
            Flat flat = equipment.getFlat();
            if (flat!=null) {
                mToolbar.setSubtitle(flat.getFullTitle());
                mToolbar.setTitle(equipment.getEquipmentType().getTitle());
            }
        }
        if (photoEquipment != null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                    format(photoEquipment.getCreatedAt());
            textViewPhotoDate.setText(sDate);
            // TODO заменить на ?
            circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(mainActivityConnector),
                    photoEquipment.getUuid().concat(".jpg")));
        } else {
            circleImageView.setImageResource(R.drawable.counter);
            textViewPhotoDate.setText("нет фото");
        }

        List<EquipmentStatus> equipmentStatuses = presenter.loadEquipmentStatuses();
        EquipmentStatusListAdapter adapter = new EquipmentStatusListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, equipmentStatuses,R.color.colorPrimaryDark);
        statusSpinner.setAdapter(adapter);

        enter_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeasure();
                mChart.refreshDrawableState();
            }
        });
        make_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });

        // TODO когда определимся с фото здесь будет грид с последними фото
        //gridView.setAdapter(new ImageGridAdapter(mainActivityConnector, photoFlat));
        //gridView.invalidateViews();
        //gridView.setVisibility(View.INVISIBLE);
        //recyclerView = view.findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void createMeasure() {
        Measure measure = new Measure();
        measure.set_id(measureRepository.getLastId()+1);
        measure.setValue(Double.valueOf(textInputMeasure.getText().toString()));
        measure.setChangedAt(new Date());
        measure.setCreatedAt(new Date());
        measure.setDate(new Date());
        measure.setEquipment(equipment);
        measure.setUser(UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getId()));
        measure.setUuid(java.util.UUID.randomUUID().toString());
        MeasureLocalDataSource.getInstance().addMeasure(measure);
        Toast.makeText(mainActivityConnector, "Успешно добавлено значение", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(@NonNull EquipmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null)
            onDestroyView();
    }

    void initChart(View view) {
        mChart = view.findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setMaxVisibleValueCount(30);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawLabels(false);
/*
        leftAxis.setLabelCount(8);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
*/

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setSpaceTop(15f);
        setData();
    }

    private void setData() {
        int count;
        ArrayList <String> xVals = new ArrayList<>();
        XAxis xAxis = mChart.getXAxis();

        List<Measure> measures = MeasureLocalDataSource.getInstance().getMeasuresByEquipment(equipment);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM", Locale.US);

        count = measures.size();
        for (int i = 0; i < count; i++) {
            Measure val = measures.get(i);
            if (val != null) {
                Date dateVal = val.getDate();
                if (dateVal != null) {
                    xVals.add(simpleDateFormat.format(dateVal));
                } else {
                    xVals.add("00.00");
                }
            }
        }
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xVals));

        List<BarEntry> yVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            yVals.add(new BarEntry(i, (float) measures.get(i).getValue()));
        }

        BarDataSet set1 = new BarDataSet(yVals, "DataSet");
        //set1.setBarSpacePercent(35f);
        BarData data = new BarData(set1);
        data.setValueTextSize(10f);
        mChart.setData(data);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private ArrayList <String>mValues;
        MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues.get((int) value);
        }
    }

    /**
     * Check whether the camera permission has been granted.
     */
    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(mainActivityConnector, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            startPhotoActivity();
        }
    }

    /**
     * Launch the camera
     */
    private void startPhotoActivity() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, ACTIVITY_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * To handle the permission grant result.
     * If the user denied the permission, show a dialog to explain
     * the reason why the app need such permission and lead he/her
     * to the system settings to grant permission.
     *
     * @param requestCode  The request code.
     * @param permissions  The wanted permissions.
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
                    hideImm();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityConnector);
                    builder.setTitle(R.string.require_permission);
                    builder.setMessage(R.string.require_permission);
                    builder.setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
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
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            default:
        }
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
            case ACTIVITY_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            String uuid = java.util.UUID.randomUUID().toString();
                            MainUtil.storeNewImage(bitmap, getContext(),
                                    800, uuid.concat(".jpg"));
                            PhotoEquipment photoEquipment = new PhotoEquipment();
                            User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getId());
                            photoEquipment.set_id(photoEquipmentRepository.getLastId());
                            photoEquipment.setEquipment(equipment);
                            photoEquipment.setUuid(uuid);
                            photoEquipment.setCreatedAt(new Date());
                            photoEquipment.setChangedAt(new Date());
                            photoEquipment.setUser(user);
                            if (gpsTrackRepository.getLastTrack() != null) {
                                photoEquipment.setLattitude(gpsTrackRepository.getLastTrack().getLatitude());
                                photoEquipment.setLongitude(gpsTrackRepository.getLastTrack().getLongitude());
                            } else {
                                photoEquipment.setLattitude(App.defaultLatitude);
                                photoEquipment.setLongitude(App.defaultLongitude);
                            }
                            photoEquipmentRepository.savePhotoEquipment(photoEquipment);
                            circleImageView.setImageBitmap(bitmap);
                            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                                    format(photoEquipment.getCreatedAt());
                            textViewPhotoDate.setText(sDate);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void hideImm() {
        InputMethodManager imm = (InputMethodManager)
                mainActivityConnector.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(make_photo.getWindowToken(), 0);
        }
    }
}
