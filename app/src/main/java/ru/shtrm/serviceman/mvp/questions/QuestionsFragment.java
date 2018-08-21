package ru.shtrm.serviceman.mvp.questions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.addquestion.AddQuestionActivity;
import ru.shtrm.serviceman.mvp.questiondetails.QuestionDetailsActivity;
import ru.shtrm.serviceman.mvp.search.SearchActivity;

public class QuestionsFragment extends Fragment
        implements QuestionsContract.View {
    private Activity mainActivityConnector = null;

    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SwipeRefreshLayout refreshLayout;

    private QuestionsAdapter adapter;

    private QuestionsContract.Presenter presenter;

    private String selectedQuestionNumber;

    // As a fragment, default constructor is needed.
    public QuestionsFragment() {}

    public static QuestionsFragment newInstance() {
        return new QuestionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_questions, container, false);

        initViews(contentView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        startActivity(new Intent(getContext(), AddQuestionActivity.class));
            }
        });

        // The function of BottomNavigationView is just as a filter.
        // We need not to build a fragment for each option.
        // Filter the data in presenter and then show it.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        break;
                    case R.id.nav_users:
                        break;
                    case R.id.nav_questions:
                        break;
                    case R.id.nav_gallery:
                        break;
                }
                presenter.loadQuestions();

                return true;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshQuestions();
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
        //setLoadingIndicator(false);
        //TODO temporary remove
        //mainActivityConnector.sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            startActivity(new Intent(getContext(), SearchActivity.class));
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item == null || selectedQuestionNumber == null) {
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
        refreshLayout =  view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mainActivityConnector, R.color.colorPrimary));

        // ItemTouchHelper helps to handle the drag or swipe action.
        // In our app, we do nothing but return a false value
        // means the item does not support drag action.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // returning false means that we need not to handle the drag action
                return false;
            }

            //
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Make different reactions with different directions here.
                presenter.deleteQuestion(viewHolder.getLayoutPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // The callback when releasing the view, hide the icons.
                // ViewHolder's ItemView is the default view to be operated.
                // Here we call getDefaultUIUtil's function clearView to pass
                // specified view.
                getDefaultUIUtil().clearView(((QuestionsAdapter.QuestionViewHolder) viewHolder).layoutMain);
                ((QuestionsAdapter.QuestionViewHolder) viewHolder).imageViewAnswer.setVisibility(View.GONE);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // The callback when ViewHolder's status of drag or swipe action changed.
                if (viewHolder != null) {
                    // ViewHolder's ItemView is the default view to be operated.
                    getDefaultUIUtil().onSelected(((QuestionsAdapter.QuestionViewHolder) viewHolder).layoutMain);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
/*
                getDefaultUIUtil().onDraw(c, recyclerView, ((QuestionsAdapter.QuestionViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
                if (dX > 0) {
                    // Left swipe
                    ((QuestionsAdapter.QuestionViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((QuestionsAdapter.QuestionViewHolder) viewHolder).imageViewAnswer.setVisibility(View.VISIBLE);
                }

                if (dX < 0) {
                    // Right swipe
                    ((QuestionsAdapter.QuestionViewHolder) viewHolder).wrapperView.setBackgroundResource(R.color.deep_orange);
                    ((QuestionsAdapter.QuestionViewHolder) viewHolder).imageViewAnswer.setVisibility(View.GONE);
                }
*/
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Be called by ItemTouchHelper's onDrawOver function.
                // Draw with a canvas object.
                // The pattern will be above the RecyclerView
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((QuestionsAdapter.QuestionViewHolder) viewHolder).layoutMain, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Set a presenter for this fragment(View),
     * @param presenter The presenter.
     */
    @Override
    public void setPresenter(@NonNull QuestionsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Set the SwipeRefreshLayout as a indicator.
     * And the SwipeRefreshLayout is refreshing means our app is loading.
     * @param active Loading or not.
     */
    @Override
    public void setLoadingIndicator(final boolean active) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
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
     * Show packages with recycler view.
     * @param list The data.
     */
    @Override
    public void showQuestions(@NonNull final List<Question> list) {
        if (adapter == null) {
            adapter = new QuestionsAdapter(mainActivityConnector, list);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    Intent intent = new Intent(getContext(), QuestionDetailsActivity.class);
                    intent.putExtra(QuestionDetailsActivity.QUESTION_ID, list.get(position).getId());
                    startActivity(intent);
                }

            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
        showEmptyView(list.isEmpty());
    }

    /**
     * Work with the activity which fragment attached to.
     * Store the number which is selected.
     * @param id The selected question number.
     */
    public void setSelectedQuestion(@NonNull String id) {
        this.selectedQuestionNumber = id;
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

