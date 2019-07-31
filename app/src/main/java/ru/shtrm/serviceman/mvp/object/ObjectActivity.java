package ru.shtrm.serviceman.mvp.object;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Photo;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.ObjectRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.ObjectLocalDataSource;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.util.MainUtil;

public class ObjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static final String OBJECT_UUID = "OBJECT_UUID";
    public static final String HOUSE_UUID = "HOUSE_UUID";
    // контейнер для пути по которому сохраним файл фотографии, полученой через штатное приложение телефона
    public static String photoFile;
    // контейнер для хранения uuid модели к которой привяжем фотографию
    public static String objectUuid;
    // контейнер для хранения uuid модели фотографии
    public static String photoUuid;
    private ObjectFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (ObjectFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "ObjectFragment");
        } else {
            fragment = ObjectFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String object_id = getIntent().getStringExtra("OBJECT_UUID");
            Bundle b = new Bundle();
            b.putString(OBJECT_UUID, object_id);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "ObjectFragment")
                    .commit();
        }

        new ObjectPresenter(
                fragment,
                ObjectRepository.getInstance(ObjectLocalDataSource.getInstance()),
                EquipmentRepository.getInstance(EquipmentLocalDataSource.getInstance()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "ObjectFragment", fragment);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MainActivity.PHOTO_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    Photo.savePhoto(photoUuid, objectUuid);
                }

                break;
            default:
                break;
        }
    }
}