package ru.shtrm.serviceman.mvp.abonents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.mvp.object.ObjectActivity;
import ru.shtrm.serviceman.util.DensityUtil;

public class WorkFragment extends Fragment implements AbonentsContract.View {
    public final static int ACTIVITY_PHOTO = 100;
    private static final int LEVEL_CITY = 0;
    private static final int LEVEL_STREET = 1;
    private static final int LEVEL_HOUSE = 2;
    private static final int LEVEL_FLAT = 3;
    private static final int LEVEL_INFO = 4;
    private Activity mainActivityConnector = null;
    private FloatingActionButton back;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private ObjectAdapter objectAdapter;
    private StreetAdapter streetAdapter;
    private HouseAdapter houseAdapter;

    private int currentLevel = LEVEL_CITY;
    private House currentHouse;
    private Street currentStreet;

    private TextView mObjectTitle;
    private TextView mObjectDate;

    private AbonentsContract.Presenter presenter;

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
        back = view.findViewById(R.id.back);
        emptyView = view.findViewById(R.id.emptyView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mObjectTitle = view.findViewById(R.id.object_name);
        mObjectDate = view.findViewById(R.id.object_date);
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
        if (objectAdapter == null) {
            objectAdapter = new ObjectAdapter(mainActivityConnector, list);
            objectAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    ZhObject flat = list.get(position);
                    Intent intent = new Intent(getActivity(), ObjectActivity.class);
                    intent.putExtra("OBJECT_UUID", String.valueOf(flat.getUuid()));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(objectAdapter);
        } else {
            objectAdapter.updateData(list);
            recyclerView.setAdapter(objectAdapter);
        }

        if (currentHouse.getHouseType() != null) {
            String text = currentHouse.getHouseType().getTitle();
            Activity activity = getActivity();
            if (activity != null) {
                Toolbar toolbar = ((MainActivity) activity).getToolbar();
                if (DensityUtil.getScreenHeight(mainActivityConnector) > 1280) {
                    toolbar.setSubtitle(text);
                } else {
                    toolbar.setTitle(text);
                }
            }
        }

        mObjectDate.setText("фото не было");

        //mTitle.setText(currentHouse.getFullTitle());
        mObjectTitle.setText(currentHouse.getFullTitle());
        //MainActivity.toolbar.setSubtitle(null);
        mObjectDate.setVisibility(View.VISIBLE);
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
            if (list.get(0) != null && list.get(0).getCity() != null) {
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
            mObjectDate.setVisibility(View.GONE);
            recyclerView.setAdapter(houseAdapter);
        } else {
            houseAdapter.updateData(list);
            recyclerView.setAdapter(houseAdapter);
        }
        //mTitle.setText(currentStreet.getFullTitle());
        mObjectTitle.setText(currentStreet.getFullTitle());
        //mImage.setImageResource(R.drawable.street);
        Activity activity = getActivity();
        if (activity != null) {
            Toolbar toolbar = ((MainActivity) activity).getToolbar();
            toolbar.setTitle(currentStreet.getTitle());
            toolbar.setSubtitle(null);
        }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

