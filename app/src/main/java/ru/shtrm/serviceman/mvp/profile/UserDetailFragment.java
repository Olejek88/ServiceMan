package ru.shtrm.serviceman.mvp.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.User;

public class UserDetailFragment extends Fragment
        implements UserDetailContract.View {
    private Activity mainActivityConnector = null;

    private UserDetailContract.Presenter presenter;
    private View view;
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
        view = inflater.inflate(R.layout.fragment_view_user, container, false);
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
        if (this.user!=null) {
            textViewName.setText(this.user.getName());
            textViewContact.setText(this.user.getContact());
        }
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

    @Override
    public void showUser(User user) {
        this.user = user;
        initViews(view);
    }
}
