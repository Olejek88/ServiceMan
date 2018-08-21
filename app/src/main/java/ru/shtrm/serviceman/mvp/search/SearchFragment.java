package ru.shtrm.serviceman.mvp.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.profile.UserDetailActivity;
import ru.shtrm.serviceman.mvp.questiondetails.QuestionDetailsActivity;

public class SearchFragment extends Fragment
        implements SearchContract.View {
    private Activity mainActivityConnector = null;

    private SearchView searchView;
    private RecyclerView recyclerView;

    private SearchResultsAdapter adapter;

    private SearchContract.Presenter presenter;

    public SearchFragment() {}

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.search(newText);
                return true;
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            hideImm();
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        SearchActivity activity = (SearchActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setPresenter(@NonNull SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showResult(final List<Question> questions, final List<User> users) {
        if (questions == null || users == null) {
            return;
        }
        if (adapter == null) {
            adapter = new SearchResultsAdapter(mainActivityConnector, questions, users);
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {

                    if (adapter.getItemViewType(position) == SearchResultsAdapter.ItemWrapper.TYPE_QUESTION) {

                        Intent intent = new Intent(getContext(), QuestionDetailsActivity.class);
                        intent.putExtra(QuestionDetailsActivity.QUESTION_ID,
                                questions.get(adapter.getOriginalIndex(position)).getId());
                        startActivity(intent);

                    } else if (adapter.getItemViewType(position) == SearchResultsAdapter.ItemWrapper.TYPE_USER) {
                        Intent intent = new Intent(getContext(), UserDetailActivity.class);
                        intent.putExtra(UserDetailActivity.USER_ID,
                                users.get(adapter.getOriginalIndex(position)).getId());
                        startActivity(intent);

                    }
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(questions, users);
        }
    }

    /**
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager)
                mainActivityConnector.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
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
}
