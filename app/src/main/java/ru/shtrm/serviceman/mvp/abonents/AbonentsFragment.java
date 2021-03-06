package ru.shtrm.serviceman.mvp.abonents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.object.ObjectActivity;

public class AbonentsFragment extends Fragment implements AbonentsContract.View {
    public static final int LEVEL_CITY = 0;
    public static final int LEVEL_STREET = 1;
    public static final int LEVEL_HOUSE = 2;
    public static final int LEVEL_FLAT = 3;
    public static final int LEVEL_INFO = 4;
    private Activity mainActivityConnector = null;
    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton back;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private ObjectAdapter flatAdapter;
    private StreetAdapter streetAdapter;
    private HouseAdapter houseAdapter;

    private int currentLevel = LEVEL_CITY;
    private House currentHouse;
    private Street currentStreet;
    private Object currentObject;

    private AbonentsContract.Presenter presenter;

    public AbonentsFragment() {}

    public static AbonentsFragment newInstance() {
        return new AbonentsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_streets, container, false);

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

        // The function of BottomNavigationView is just as a filter.
        // We need not to build a fragment for each option.
        // Filter the data in presenter and then show it.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_users:
                        break;
                    case R.id.nav_map:
                        break;
                    case R.id.nav_tasks:
                        break;
                }
                //presenter.loadAbonents();
                return true;
            }
        });

        // Set true to inflate the options menu.
        setHasOptionsMenu(true);
        presenter.loadStreets();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        switch (currentLevel) {
            case LEVEL_FLAT:
                presenter.loadObjects(currentHouse);
                break;
            case LEVEL_HOUSE:
                presenter.loadHouses(currentStreet);
                break;
            case LEVEL_STREET:
                presenter.loadStreets();
                break;
            default:
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item == null) {
            return false;
        }
        switch (item.getItemId()) {
            default:
                break;
        }
        return true;
    }

    /**
     * Init the views by findViewById.
     * @param view The container view.
     */
    @Override
    public void initViews(View view) {
        back =  view.findViewById(R.id.back);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        emptyView =  view.findViewById(R.id.emptyView);
        recyclerView =  view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * Set a presenter for this fragment(View),
     * @param presenter The presenter.
     */
    @Override
    public void setPresenter(@NonNull AbonentsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Hide a RecyclerView when it is empty and show a empty view
     * to tell the uses that there is no data currently.
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
     * @param list The data.
     */
    @Override
    public void showObjects(@NonNull final List<ZhObject> list) {
        currentLevel = LEVEL_FLAT;
        if (flatAdapter == null) {
            flatAdapter = new ObjectAdapter(mainActivityConnector, list);
            flatAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Object object = list.get(position);
                    Intent intent = new Intent(getContext(), ObjectActivity.class);
                    intent.putExtra(ObjectActivity.OBJECT_UUID, list.get(position).get_id());
                    startActivity(intent);
                    currentObject = object;
                }
            });
            recyclerView.setAdapter(flatAdapter);
        } else {
            flatAdapter.updateData(list);
            recyclerView.setAdapter(flatAdapter);
        }
    }

    public void showStreets(@NonNull final List<Street> list) {
        currentLevel = LEVEL_STREET;
        if (streetAdapter == null) {
            streetAdapter = new StreetAdapter(mainActivityConnector, list);
            streetAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Street street = list.get(position);
                    presenter.loadHouses(street);
                    currentStreet = street;
                }
            });
            recyclerView.setAdapter(streetAdapter);
        } else {
            streetAdapter.updateData(list);
            recyclerView.setAdapter(streetAdapter);
        }
    }

    public void showHouses(@NonNull final List<House> list) {
        currentLevel = LEVEL_HOUSE;
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mainActivityConnector, list);
            houseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    House house = list.get(position);
                    presenter.loadObjects(house);
                    currentHouse = house;
                }
            });
            recyclerView.setAdapter(houseAdapter);
        } else {
            houseAdapter.updateData(list);
            recyclerView.setAdapter(houseAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    @Override
    public House getCurrentHouse() {
        return currentHouse;
    }

    @Override
    public Street getCurrentStreet() {
        return currentStreet;
    }
}

