package ru.shtrm.serviceman.mvp.abonents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.mvp.object.ObjectActivity;
import ru.shtrm.serviceman.util.DensityUtil;
import ru.shtrm.serviceman.util.MainUtil;

public class WorkFragment extends Fragment implements AbonentsContract.View {
    private Activity mainActivityConnector = null;

    private static final int LEVEL_CITY = 0;
    private static final int LEVEL_STREET = 1;
    private static final int LEVEL_HOUSE = 2;
    private static final int LEVEL_FLAT = 3;
    private static final int LEVEL_INFO = 4;

    public final static int ACTIVITY_PHOTO = 100;

    private FloatingActionButton make_photo;
    private FloatingActionButton back;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private ObjectAdapter flatAdapter;
    private StreetAdapter streetAdapter;
    private HouseAdapter houseAdapter;
    private File photoFile;
    private String photoUuid;

    private int currentLevel = LEVEL_CITY;
    private House currentHouse;
    private Street currentStreet;

/*
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    */
    //private TextView mTitle;

    private TextView mObjectTitle;
    private TextView mObjectDate;
    //private ImageView mImage;
    //private ImageView objectIcon;
    //private AppBarLayout mAppBarLayout;

    private AbonentsContract.Presenter presenter;

    // As a fragment, default constructor is needed.
    public WorkFragment() {
    }

    public static WorkFragment newInstance() {
        return new WorkFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_objects, container, false);
        initViews(contentView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentLevel) {
                    case LEVEL_INFO:
                        presenter.loadObjects(currentHouse);
                        break;
                    case LEVEL_FLAT:
                        presenter.loadHouses(currentStreet);
                        break;
                    case LEVEL_HOUSE:
                        presenter.loadStreets();
                        break;
                    case LEVEL_STREET:
                        break;
                    default:
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
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //mAppBarLayout.addOnOffsetChangedListener(this);
        //startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        presenter.loadStreets();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    /**
     * Init the views by findViewById.
     *
     * @param view The container view.
     */
    @Override
    public void initViews(View view) {
        make_photo = view.findViewById(R.id.fab_photo);
        back = view.findViewById(R.id.back);
        emptyView = view.findViewById(R.id.emptyView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //objectIcon = view.findViewById(R.id.object_icon);
        //mTitleContainer = view.findViewById(R.id.main_linearlayout_title);
        mObjectTitle = view.findViewById(R.id.object_name);
        mObjectDate = view.findViewById(R.id.object_date);
        //mTitle = view.findViewById(R.id.object_name);
        //mAppBarLayout = view.findViewById(R.id.main_appbar);
        //handleToolbarTitleVisibility(0);

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
    }

    /**
     * Set a presenter for this fragment(View),
     *
     * @param presenter The presenter.
     */
    @Override
    public void setPresenter(@NonNull AbonentsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Hide a RecyclerView when it is empty and show a empty view
     * to tell the uses that there is no data currently.
     *
     * @param toShow Hide or show.
     */
    @Override
    public void showEmptyView(boolean toShow) {
        if (toShow) {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Show flats with recycler view.
     *
     * @param list The data.
     */
    @Override
    public void showObjects(@NonNull final List<ZhObject> list) {
        currentLevel = LEVEL_FLAT;
        setHasOptionsMenu(true);
        if (flatAdapter == null) {
            flatAdapter = new ObjectAdapter(mainActivityConnector, list);
            flatAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    ZhObject flat = list.get(position);
                    Intent intent = new Intent(getActivity(), ObjectActivity.class);
                    intent.putExtra("OBJECT_UUID", String.valueOf(flat.getUuid()));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(flatAdapter);
        } else {
            flatAdapter.updateData(list);
            recyclerView.setAdapter(flatAdapter);
        }
        if (currentHouse.getHouseType() != null) {
            if (DensityUtil.getScreenHeight(mainActivityConnector) > 1280)
                MainActivity.toolbar.setSubtitle(currentHouse.getHouseType().getTitle());
            else
                MainActivity.toolbar.setTitle(currentHouse.getHouseType().getTitle());
        }
        mObjectDate.setText("фото не было");

        //mTitle.setText(currentHouse.getFullTitle());
        mObjectTitle.setText(currentHouse.getFullTitle());
        //MainActivity.toolbar.setSubtitle(null);
        mObjectDate.setVisibility(View.VISIBLE);

        make_photo.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        //showEmptyView(list.isEmpty());

    }

    public void showStreets(@NonNull final List<Street> list) {
        currentLevel = LEVEL_STREET;
        setHasOptionsMenu(false);
        if (streetAdapter == null) {
            streetAdapter = new StreetAdapter(mainActivityConnector, list);
            streetAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Street street = list.get(position);
                    currentStreet = street;
                    presenter.loadHouses(street);
                }
            });
            recyclerView.setAdapter(streetAdapter);
        } else {
            streetAdapter.updateData(list);
            recyclerView.setAdapter(streetAdapter);
        }
        if (streetAdapter.getItemCount() > 0) {
            if (list.get(0)!=null && list.get(0).getCity()!=null) {
                //mTitle.setText(list.get(0).getCity().getTitle());
                mObjectTitle.setText(list.get(0).getCity().getTitle());
                mObjectDate.setVisibility(View.GONE);
            }
        }
/*
        mImage.setImageResource(R.drawable.city);
        if (currentStreet != null) {
            MainActivity.toolbar.setTitle(currentStreet.getCity().getTitle());
            MainActivity.toolbar.setSubtitle(null);
        }
*/
        make_photo.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
    }

    public void showHouses(@NonNull final List<House> list) {
        currentLevel = LEVEL_HOUSE;
        setHasOptionsMenu(false);
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mainActivityConnector, list);
            houseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    House house = list.get(position);
                    currentHouse = house;
                    presenter.loadObjects(house);
                }
            });
            recyclerView.setAdapter(houseAdapter);
            mObjectDate.setVisibility(View.GONE);
        } else {
            houseAdapter.updateData(list);
            recyclerView.setAdapter(houseAdapter);
        }
        //mTitle.setText(currentStreet.getFullTitle());
        mObjectTitle.setText(currentStreet.getFullTitle());
        //mImage.setImageResource(R.drawable.street);
        MainActivity.toolbar.setTitle(currentStreet.getTitle());
        MainActivity.toolbar.setSubtitle(null);
        make_photo.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null)
            onDestroyView();
    }
/*

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
*/

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
                    switch (currentLevel) {
                        case LEVEL_FLAT:
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2; // половина изображения
                            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            if (bitmap != null) {
                                String uuid = java.util.UUID.randomUUID().toString();
                                MainUtil.storeNewImage(bitmap, getContext(),
                                        800, uuid.concat(".jpg"));
                                MainUtil.storePhotoHouse (currentHouse,uuid);
                                photoFile.delete();
                                //objectIcon.setImageBitmap(bitmap);
                                //mImage.setImageBitmap(bitmap);
                                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                                        format(new Date());
                                mObjectDate.setText(sDate);
                            }
                            break;
                        case LEVEL_INFO:
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void onActionAddAttribute() {

    }

    private void onActionChangeStatus() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionAddAttribute) {
            onActionAddAttribute();
            return true;
        } else if (id == R.id.actionChangeStatus) {
            onActionChangeStatus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

