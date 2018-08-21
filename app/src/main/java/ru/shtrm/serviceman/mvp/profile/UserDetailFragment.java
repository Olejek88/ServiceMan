package ru.shtrm.serviceman.mvp.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.customtabs.CustomTabsHelper;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.profileedit.UserEditActivity;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.profile.UserDetailActivity.USER_ID;

public class UserDetailFragment extends Fragment
        implements UserDetailContract.View {
    private Activity mainActivityConnector = null;

    // View references.
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewWebsite;
    private FloatingActionButton editUser;

    private UserDetailContract.Presenter presenter;

    private String website;
    private User user;
    private boolean owner = true;

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
        Bundle b = getArguments();
        if (b!=null) {
            String userUuid = b.getString(USER_ID);
            if (userUuid!=null)
                user = UsersLocalDataSource.getInstance().getUserById(userUuid);
        }
        if (user==null)
            user = UsersLocalDataSource.
                getInstance().getUserById(AuthorizedUser.getInstance().getId());

        if (user == null) {
            Intent intent = new Intent(getContext(), UserEditActivity.class);
            startActivity(intent);
            return null;
        }

        view = inflater.inflate(R.layout.fragment_view_user, container, false);
        initViews(view);

        editUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), UserEditActivity.class);
                    intent.putExtra(USER_ID, user.getId());
                    startActivity(intent);
                }
            });

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (website != null) {
                    CustomTabsHelper.openUrl(getContext(), website);
                }
            }
        });

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
        textViewName = view.findViewById(R.id.profile_add_name);
        textViewAddress = view.findViewById(R.id.profile_add_address);
        textViewWebsite = view.findViewById(R.id.profile_add_website);
        TextView textViewPhone = view.findViewById(R.id.profile_add_phone);
        ImageView imageView = view.findViewById(R.id.profile_add_image);
        editUser = view.findViewById(R.id.editUser);

        textViewName.setText(user.getName());
        textViewAddress.setText(user.getAddress());
        textViewWebsite.setText(user.getWebsite());
        textViewPhone.setText(user.getPhone());
        String path = MainUtil.getPicturesDirectory(mainActivityConnector.getApplicationContext());
        if (path != null) {
            String avatar = user.getAvatar();
            if (avatar != null)
                imageView.setImageBitmap(MainUtil.getBitmapByPath(path, avatar));
        }
    }

    @Override
    public void setPresenter(@NonNull UserDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUserName(String name) {
        textViewName.setText(name);
    }

    @Override
    public void setUserAddress(String address) {
        textViewAddress.setText(address);
    }

    @Override
    public void setUserWebsite(String website) {
        this.website = website;
        Spannable spannable = new SpannableStringBuilder(website);
        spannable.setSpan(new URLSpan(website), 0, website.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWebsite.setText(spannable);
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
