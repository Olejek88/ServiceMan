package ru.shtrm.serviceman.mvp.equipment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.EquipmentStatusRepository;
import ru.shtrm.serviceman.data.source.EquipmentTypeRepository;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.EquipmentStatusLocalDataSource;
import ru.shtrm.serviceman.data.source.local.EquipmentTypeLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.HouseLocalDataSource;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.equipment.EquipmentFragment.ACTIVITY_PHOTO;
import static ru.shtrm.serviceman.mvp.flat.FlatActivity.FLAT_UUID;
import static ru.shtrm.serviceman.mvp.flat.FlatActivity.HOUSE_UUID;

public class AddEquipmentFragment extends Fragment {
    private Activity mainActivityConnector = null;

    private EquipmentRepository equipmentRepository;
    private HouseRepository houseRepository;
    private FlatRepository flatRepository;
    private EquipmentStatusRepository equipmentStatusRepository;
    private EquipmentTypeRepository equipmentTypeRepository;

    // View references.
    private AppCompatTextView editTextDate;
    private TextInputEditText editTextSerial;
    private Spinner editEquipmentType, editEquipmentStatus;
    private FloatingActionButton fab;
    private House house;
    private Flat flat;
    private ImageView imageView;
    private String photoUuid;
    Calendar myCalendar;
    private Bitmap storeBitmap=null;

    public AddEquipmentFragment() {}

    public static AddEquipmentFragment newInstance() {
        return new AddEquipmentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_equipment, container, false);
        Bundle b = getArguments();
        if (b != null) {
            String flatUuid = b.getString(FLAT_UUID);
            String houseUuid = b.getString(HOUSE_UUID);
            if (flatUuid != null) {
                flat = flatRepository.getFlat(flatUuid);
                house = houseRepository.getHouse(houseUuid);
            }
        }

        if (flat == null || house == null)
            if (getFragmentManager() != null)
                getFragmentManager().popBackStack();

        initViews(view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImm();
                String title = editTextSerial.getText().toString();
                if (title.length() < 5) {
                    showTitleError();
                    return;
                }
                int res = storeEquipment();
                if (res==0) {
                    if (getActivity()!=null)
                        getActivity().finishActivity(0);
                }
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRepository();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initViews(View view) {
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar !=null) {
            mToolbar.setTitle("Добавить оборудование");
        }

        imageView = view.findViewById(R.id.equipment_add_image);
        editTextSerial = view.findViewById(R.id.editTextSerial);
        editTextDate = view.findViewById(R.id.editTextDate);
        fab = view.findViewById(R.id.fab);
        myCalendar = Calendar.getInstance();
        editEquipmentType = view.findViewById(R.id.editEquipmentType);
        editEquipmentStatus = view.findViewById(R.id.editEquipmentStatus);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, ACTIVITY_PHOTO);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }}
        );

        List<EquipmentStatus> equipmentStatuses = equipmentStatusRepository.getEquipmentStatuses();
        EquipmentStatusListAdapter statusAdapter = new EquipmentStatusListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, equipmentStatuses,R.color.colorPrimaryDark);
        editEquipmentStatus.setAdapter(statusAdapter);

        List<EquipmentType> equipmentTypes = equipmentTypeRepository.getEquipmentTypes();
        EquipmentTypeListAdapter typeAdapter = new EquipmentTypeListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, equipmentTypes);
        editEquipmentType.setAdapter(typeAdapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mainActivityConnector, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void showTitleError() {
        Snackbar.make(fab, R.string.wrong_title, Snackbar.LENGTH_SHORT).show();
    }

    int storeEquipment() {
        Equipment equipment = new Equipment();
        equipment.setUuid(java.util.UUID.randomUUID().toString());
        equipment.set_id(equipmentRepository.getLastId()+1);
        equipment.setChangedAt(new Date());
        equipment.setCreatedAt(new Date());
        if (myCalendar.getTime()!=null)
            equipment.setTestDate(myCalendar.getTime());
        else
            equipment.setTestDate(new Date());
        equipment.setSerial(editTextSerial.getText().toString());
        equipment.setHouse(house);
        equipment.setFlat(flat);
        equipment.setEquipmentStatus((EquipmentStatus) editEquipmentStatus.getSelectedItem());
        equipment.setEquipmentType((EquipmentType) editEquipmentType.getSelectedItem());
        equipmentRepository.addEquipment(equipment);
        Toast.makeText(mainActivityConnector, "Успешно добавлено оборудование", Toast.LENGTH_SHORT).show();
        if (storeBitmap!=null) {
            MainUtil.storePhotoEquipment(equipment,photoUuid);
        }
        return 0;
    }

    /**
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager)
                mainActivityConnector.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null && imm.isActive()) {
            imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
        checkRepository();
    }

    void checkRepository() {
        if (equipmentRepository == null)
            equipmentRepository = EquipmentRepository.getInstance
                    (EquipmentLocalDataSource.getInstance());
        if (flatRepository == null)
            flatRepository = FlatRepository.getInstance
                    (FlatLocalDataSource.getInstance());
        if (houseRepository == null)
            houseRepository = HouseRepository.getInstance
                    (HouseLocalDataSource.getInstance());
        if (equipmentStatusRepository == null)
            equipmentStatusRepository = EquipmentStatusRepository.getInstance
                    (EquipmentStatusLocalDataSource.getInstance());
        if (equipmentTypeRepository == null)
            equipmentTypeRepository = EquipmentTypeRepository.getInstance
                    (EquipmentTypeLocalDataSource.getInstance());
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
                            storeBitmap=bitmap;
                            photoUuid = java.util.UUID.randomUUID().toString();
                            MainUtil.storeNewImage(bitmap, getContext(),
                                    800, photoUuid.concat(".jpg"));
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
        }
    }
}
