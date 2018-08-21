package ru.shtrm.serviceman.mvp.trickdetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.source.TricksRepository;
import ru.shtrm.serviceman.util.MainUtil;

public class TrickDetailsFragment extends Fragment
        implements TrickDetailsContract.View {
    private Activity mainActivityConnector = null;

    private RecyclerView recyclerView;
    private AppCompatTextView userName;
    private AppCompatTextView userStatus;
    private AppCompatTextView userStats;
    private ImageView imageView;

    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_delete;
    private TrickDetailsAdapter adapter;
    private TrickDetailsContract.Presenter presenter;

    public TrickDetailsFragment() {}

    public static TrickDetailsFragment newInstance() {
        return new TrickDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trick_details, container, false);
        initViews(view);

        setHasOptionsMenu(true);

        return view;
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
    public void onDestroy() {
        super.onDestroy();
        TricksRepository.destroyInstance();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            exit();

        } else if (id == R.id.action_delete) {
            showDeleteAlertDialog();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {
        TrickDetailsActivity activity = (TrickDetailsActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab_edit = view.findViewById(R.id.fab);

        RelativeLayout user_info = view.findViewById(R.id.user_detail);
        userName = user_info.findViewById(R.id.profile_name);
        userStatus = user_info.findViewById(R.id.profile_status);
        userStats = user_info.findViewById(R.id.profile_stats);
        imageView = user_info.findViewById(R.id.profile_image);
    }

    /**
     * Bind the presenter to view.
     * @param presenter The presenter. See at {@link TrickDetailsPresenter}
     */
    @Override
    public void setPresenter(@NonNull TrickDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Show the Trick details.
     * @param trick The Trick. See at {@link Trick}
     */
    @Override
    public void showTrickDetails(@NonNull Trick trick) {
        if (adapter == null) {
            adapter = new TrickDetailsAdapter(mainActivityConnector, trick);
            recyclerView.setAdapter(adapter);

            fab_delete = recyclerView.findViewById(R.id.fab_delete);
            if (fab_delete!=null) {
                fab_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteAlertDialog();
                    }
                });
            }
        }
        if (trick.getUser()!=null) {
            userStatus.setText(R.string.user_master);
            userStats.setText(trick.getUser().getStats());
            userName.setText(trick.getUser().getName());

            String path = MainUtil.getPicturesDirectory(mainActivityConnector.getApplicationContext());
            if (path != null) {
                String avatar = trick.getUser().getAvatar();
                if (avatar != null)
                    imageView.setImageBitmap(MainUtil.getBitmapByPath(path, avatar));
            }
        }
    }

    /**
     * Finish the activity.
     */
    @Override
    public void exit() {
        mainActivityConnector.onBackPressed();
    }

    /**
     * Show a dialog when user select the DELETE option menu item.
     */
    private void showDeleteAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector).create();
        dialog.setTitle(R.string.delete);
        dialog.setMessage(getString(R.string.delete_trick_message));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.deleteTrick();
            }
        });
        dialog.show();
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
