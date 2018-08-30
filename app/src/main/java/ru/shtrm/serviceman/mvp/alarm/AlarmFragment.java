package ru.shtrm.serviceman.mvp.alarm;

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
import ru.shtrm.serviceman.data.Alarm;

public class AlarmFragment extends Fragment implements AlarmContract.View {
    private Activity mainActivityConnector = null;

    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private AlarmAdapter alarmAdapter;
    private AlarmContract.Presenter presenter;

    // As a fragment, default constructor is needed.
    public AlarmFragment() {}

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_alarms, container, false);

        initViews(contentView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void setPresenter(@NonNull AlarmContract.Presenter presenter) {
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
    public void showAlarms(@NonNull final List<Alarm> list) {
        if (alarmAdapter == null) {
            alarmAdapter = new AlarmAdapter(mainActivityConnector, list);
            recyclerView.setAdapter(alarmAdapter);
        } else {
            alarmAdapter.updateData(list);
            recyclerView.setAdapter(alarmAdapter);
        }
        showEmptyView(list.isEmpty());
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

