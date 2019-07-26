package ru.shtrm.serviceman.mvp.equipment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;
import ru.shtrm.serviceman.data.source.MeasureRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.data.source.local.MeasureLocalDataSource;
import ru.shtrm.serviceman.data.source.local.OperationLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.operations.OperationAdapter;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.equipment.EquipmentActivity.EQUIPMENT_UUID;

public class EquipmentFragment extends Fragment implements EquipmentContract.View {
    public final static int ACTIVITY_PHOTO = 100;
    protected BarChart mChart;
    Calendar myCalendar;
    private Activity mainActivityConnector = null;
    private EquipmentContract.Presenter presenter;
    private Equipment equipment;
    private GpsTrackRepository gpsTrackRepository;
    private MeasureRepository measureRepository;
    private EquipmentRepository equipmentRepository;
    private ListView listView;
    private ListView listView_archive;
    private CircleImageView circleImageView;
    private TextInputEditText textInputMeasure;
    private TextView textViewPhotoDate;
    private File photoFile;
    private String photoUuid;
    private boolean firstMeasureClick = false;

    public EquipmentFragment() {
    }

    public static EquipmentFragment newInstance() {
        return new EquipmentFragment();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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
        checkRepository();
        if (b != null) {
            String equipmentUuid = b.getString(EQUIPMENT_UUID);
            if (equipmentUuid != null)
                equipment = EquipmentLocalDataSource.getInstance().getEquipmentByUuid(equipmentUuid);
        }
        view = inflater.inflate(R.layout.fragment_equipment, container, false);
        if (equipment != null) {
            initViews(view);
        } else {
            equipmentRepository.deleteEmptyEquipment();
            if (getActivity() != null)
                getActivity().finishActivity(0);
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null)
            presenter.subscribe();
        checkRepository();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null)
            presenter.unsubscribe();
    }

    @Override
    public void initViews(View view) {
        //final AppCompatEditText editTextSerial = view.findViewById(R.id.editTextEquipmentSerial);
        final Spinner statusSpinner = view.findViewById(R.id.spinnerEquipmentStatus);
        final TextView textViewDate = view.findViewById(R.id.textViewEquipmentDate);
        final LinearLayout equipment_measure = view.findViewById(R.id.equipment_measure);
        final LinearLayout equipment_measure_input = view.findViewById(R.id.equipment_measure_input);

        FloatingActionButton enter_measure = view.findViewById(R.id.enter_measure);
        FloatingActionButton make_photo = view.findViewById(R.id.make_photo);
        FloatingActionButton fab_delete = view.findViewById(R.id.fab_delete);
        FloatingActionButton add_comment = view.findViewById(R.id.add_comment);

        myCalendar = Calendar.getInstance();
        textViewPhotoDate = view.findViewById(R.id.textViewPhotoDate);
        textInputMeasure = view.findViewById(R.id.addMeasureValue);
        circleImageView = view.findViewById(R.id.imageViewEquipment);

        initChart(view);

        //editTextSerial.setText(equipment.getSerial());
        //textViewType.setText(equipment.getEquipmentType().getTitle());
        //textViewStatus.setText(equipment.getEquipmentStatus().getTitle());
        SimpleDateFormat sDf = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        if (equipment.getTestDate() != null)
            textViewDate.setText(sDf.format(equipment.getTestDate()));
        else
            textViewDate.setText(R.string.no_last_time);
        //textViewEquipment.setText(equipment.getEquipmentType().getTitle().substring(0, 1));

        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        if (mToolbar != null) {
            if (equipment.getEquipmentType() != null) {
                mToolbar.setTitle(equipment.getEquipmentType().getTitle());
            } else {
                mToolbar.setTitle(R.string.equipment_unknown);
            }

            mToolbar.setLogo(R.drawable.baseline_settings_white_24dp);
            mToolbar.setTitleMarginStart(1);
        }

//        if (photoEquipment != null) {
//            textViewPhotoDate.setText(sDf.format(photoEquipment.getCreatedAt()));
//            // TODO заменить на ?
//            circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
//                    MainUtil.getPicturesDirectory(mainActivityConnector),
//                    photoEquipment.getUuid().concat(".jpg")));
//        } else {
            circleImageView.setImageResource(R.drawable.counter);
            textViewPhotoDate.setText("нет фото");
//        }

        final List<EquipmentStatus> equipmentStatuses = presenter.loadEquipmentStatuses();
        EquipmentStatusListAdapter adapter = new EquipmentStatusListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, equipmentStatuses, R.color.colorPrimaryDark);
        statusSpinner.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                textViewDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mainActivityConnector, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        enter_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstMeasureClick) {
                    // если есть данные - вводим их, иначе сохраняем оборудование
                    if (textInputMeasure.getText().toString().length() > 0 &&
                            textInputMeasure.getText().toString().indexOf('.') > -1)
                        createMeasure();
                    else
                        Toast.makeText(mainActivityConnector,
                                "Значение не добавлено. Или вы его не ввели или не добавили разделитель (.).",
                                Toast.LENGTH_LONG).show();
                    mChart.refreshDrawableState();
                    // не дать возможность вводить по несколько раз
/*
                    storeEditEquipment(editTextSerial.getText().toString(),
                            (EquipmentStatus) statusSpinner.getSelectedItem());
*/
                    if (getActivity() != null) {
                        getActivity().finishActivity(0);
                        getActivity().onBackPressed();
                    }
                } else {
                    firstMeasureClick = true;
                    equipment_measure.setVisibility(View.VISIBLE);
                    equipment_measure_input.setVisibility(View.VISIBLE);
                }
            }
        });

        make_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUuid = java.util.UUID.randomUUID().toString();
                    photoFile = MainUtil.createImageFile(photoUuid, mainActivityConnector);
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(mainActivityConnector,
                                "ru.shtrm.serviceman.fileprovider",
                                photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, ACTIVITY_PHOTO);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EquipmentActivity.createAddMessageDialog(mainActivityConnector);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipmentRepository.deleteEquipment(equipment);
                if (getActivity() != null)
                    getActivity().finishActivity(0);
            }
        });

        // TODO когда определимся с фото здесь будет грид с последними фото
        //gridView.setAdapter(new ImageGridAdapter(mainActivityConnector, photoFlat));
        //gridView.invalidateViews();
        //gridView.setVisibility(View.INVISIBLE);
        listView = view.findViewById(R.id.list_view);
        listView_archive = view.findViewById(R.id.list_view_archive);
/*
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
*/
        fillListViewOperations(equipment, 0);
        fillListViewOperations(equipment, 1);
    }

    void createMeasure() {
        Measure measure = new Measure();
        measure.set_id(measureRepository.getLastId() + 1);
        measure.setValue(Double.valueOf(textInputMeasure.getText().toString()));
        measure.setChangedAt(new Date());
        measure.setCreatedAt(new Date());
        measure.setDate(new Date());
        measure.setEquipment(equipment);
        measure.setUser(UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getUser().getUuid()));
        measure.setUuid(java.util.UUID.randomUUID().toString());
        MeasureLocalDataSource.getInstance().addMeasure(measure);
        Toast.makeText(mainActivityConnector, "Успешно добавлено значение", Toast.LENGTH_SHORT).show();
        // обновляем ярлык количества не отправленных измерений
        MainUtil.setBadges(mainActivityConnector);
    }

    void checkRepository() {
        if (gpsTrackRepository == null)
            gpsTrackRepository = GpsTrackRepository.getInstance
                    (GpsTrackLocalDataSource.getInstance());
        if (measureRepository == null)
            measureRepository = MeasureRepository.getInstance
                    (MeasureLocalDataSource.getInstance());
        if (equipmentRepository == null)
            equipmentRepository = EquipmentRepository.getInstance
                    (EquipmentLocalDataSource.getInstance());
    }

    void storeEditEquipment(String serial, EquipmentStatus equipmentStatus) {
        equipment.setChangedAt(new Date());
        if (myCalendar.getTime() != null)
            equipment.setTestDate(myCalendar.getTime());
        else
            equipment.setTestDate(new Date());
        equipment.setSerial(serial);
        equipment.setEquipmentStatus(equipmentStatus);
        equipmentRepository.addEquipment(equipment);
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
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setSpaceTop(15f);
        setData();
    }

    private void setData() {
        int count;
        ArrayList<String> xVals = new ArrayList<>();
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

    // Operations----------------------------------------------------------------------------------------
    private void fillListViewOperations(Equipment equipment, int type) {
        Activity activity = getActivity();
        List<Task> tasks;
        if (activity == null) {
            return;
        }
        if (type == 0)
            tasks = TaskLocalDataSource.getInstance().getTaskByEquipment(equipment, WorkStatus.Status.UN_COMPLETE);
        else
            tasks = TaskLocalDataSource.getInstance().getTaskByEquipment(equipment, WorkStatus.Status.COMPLETE);
        if (tasks.size() > 0) {
            List<Operation> operations = OperationLocalDataSource.getInstance().getOperationByTask(tasks.get(0));
            OperationAdapter operationAdapter = new OperationAdapter(activity, operations);
            if (type == 0) {
                listView.setAdapter(operationAdapter);
                //setListViewHeightBasedOnChildren(listView);
            }
            else {
                listView_archive.setAdapter(operationAdapter);
                setListViewHeightBasedOnChildren(listView_archive);
            }
        }
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private ArrayList<String> mValues;

        MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues.get((int) value);
        }
    }
}
