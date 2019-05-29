package ru.shtrm.serviceman.mvp.user;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.User;


public class UserListAdapter extends RealmBaseAdapter<User> {

    private final LayoutInflater flater;
    private final int mResource;

    public UserListAdapter(Context context, @LayoutRes int resource, RealmResults<User> users) {
        super(users);
        mResource = resource;
        flater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = flater.inflate(mResource, parent, false);
        User item = getItem(position);

        AppCompatTextView textViewTitle;
        AppCompatTextView textViewAvatar;
        textViewTitle = view.findViewById(R.id.textViewUserName);
        textViewAvatar = view.findViewById(R.id.textViewAvatar);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        if (item != null) {
            textViewTitle.setText(item.getName());
            textViewAvatar.setText(item.getName().substring(0, 1));
        }

        return view;
    }
}
