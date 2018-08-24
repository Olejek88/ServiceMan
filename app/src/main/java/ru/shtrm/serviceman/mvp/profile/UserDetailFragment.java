package ru.shtrm.serviceman.mvp.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;

import static ru.shtrm.serviceman.mvp.profile.UserDetailActivity.USER_ID;

public class UserDetailFragment extends Fragment
        implements UserDetailContract.View {
    private Activity mainActivityConnector = null;

    private UserDetailContract.Presenter presenter;
    private User user;

    public UserDetailFragment() {}

    public static UserDetailFragment newInstance() {
        return new UserDetailFragment();
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
        user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getId());
        view = inflater.inflate(R.layout.fragment_view_user, container, false);
        if (user!=null)
            initViews(view);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter!=null)
            presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter!=null)
            presenter.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        TextView textViewName = view.findViewById(R.id.profile_name);
        TextView textViewContact = view.findViewById(R.id.profile_contact);
        textViewName.setText(user.getName());
        textViewContact.setText(user.getContact());
    }

    @Override
    public void setPresenter(@NonNull UserDetailContract.Presenter presenter) {
        this.presenter = presenter;
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
