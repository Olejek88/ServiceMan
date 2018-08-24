package ru.shtrm.serviceman.mvp.abonents;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class AbonentsFragment extends Fragment implements AbonentsContract.View {
    private Activity mainActivityConnector = null;

    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private FlatAdapter flatAdapter;
    private StreetAdapter streetAdapter;
    private HouseAdapter houseAdapter;

    private AbonentsContract.Presenter presenter;

    // As a fragment, default constructor is needed.
    public AbonentsFragment() {}

    public static AbonentsFragment newInstance() {
        return new AbonentsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_streets, container, false);

        initViews(contentView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getContext(), AddQuestionActivity.class));
            }
        });

        // The function of BottomNavigationView is just as a filter.
        // We need not to build a fragment for each option.
        // Filter the data in presenter and then show it.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_checkin:
                        break;
                    case R.id.nav_users:
                        break;
                    case R.id.nav_map:
                        break;
                    case R.id.nav_alarms:
                        break;
                }
                //presenter.loadAbonents();
                return true;
            }
        });

        // Set true to inflate the options menu.
        setHasOptionsMenu(true);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO здесь будет фильтрация и все такое из меню
        return true;
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
        fab =  view.findViewById(R.id.fab);
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
    public void showFlats(@NonNull final List<Flat> list) {
        if (flatAdapter == null) {
            flatAdapter = new FlatAdapter(mainActivityConnector, list);
            flatAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
//                    Intent intent = new Intent(getContext(), FlatActivity.class);
//                    intent.putExtra(FlatActivity.FLAT_ID, list.get(position).get_id());
//                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(flatAdapter);
        } else {
            flatAdapter.updateData(list);
        }
        showEmptyView(list.isEmpty());
    }

    public void showStreets(@NonNull final List<Street> list) {
        if (streetAdapter == null) {
            streetAdapter = new StreetAdapter(mainActivityConnector, list);
            streetAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Street street = list.get(position);
                    presenter.loadHouses(street);
                }
            });
            recyclerView.setAdapter(streetAdapter);
        } else {
            streetAdapter.updateData(list);
        }
        //showEmptyView(list.isEmpty());
    }

    public void showHouses(@NonNull final List<House> list) {
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mainActivityConnector, list);
            houseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    House house = list.get(position);
                    presenter.loadFlats(house);
                }
            });
            recyclerView.setAdapter(houseAdapter);
        } else {
            houseAdapter.updateData(list);
        }
        //showEmptyView(list.isEmpty());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }

}

