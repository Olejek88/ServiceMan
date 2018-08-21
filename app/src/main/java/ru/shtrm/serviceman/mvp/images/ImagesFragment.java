package ru.shtrm.serviceman.mvp.images;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.mvp.MainActivity;

public class ImagesFragment extends Fragment
        implements ImagesContract.View {
    private Activity mainActivityConnector = null;

    private ImagesContract.Presenter presenter;
    public ImagesFragment() {}

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {

        MainActivity activity = (MainActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        GridView gridView = view.findViewById(R.id.gridview);
        List<Image> images = presenter.getImages();
        gridView.setAdapter(new ImageGridWithTextAdapter(mainActivityConnector,images));
        gridView.invalidateViews();
    }

    /**
     * Bind presenter to fragment(view).
     * @param presenter The presenter. See at {@link ImagesPresenter}.
     */
    @Override
    public void setPresenter(@NonNull ImagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Finish current activity.
     */
    @Override
    public void showImagesList() {
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
