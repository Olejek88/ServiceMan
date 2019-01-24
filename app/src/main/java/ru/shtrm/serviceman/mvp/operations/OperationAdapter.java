package ru.shtrm.serviceman.mvp.operations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.local.OperationLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.WorkStatusLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class OperationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Operation> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public OperationAdapter(@NonNull Context context, @NonNull List<Operation> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OperationViewHolder(inflater.inflate(R.layout.item_operation,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Operation item = list.get(position);
        final OperationViewHolder pvh = (OperationViewHolder) holder;
        pvh.textViewOperationTitle.setText(item.getOperationTemplate().getTitle());
        pvh.textViewOperationDescription.setText(item.getOperationTemplate().getDescription());
        if (item.getWorkStatus().getUuid().equals(WorkStatus.Status.COMPLETE))
            pvh.checkBox.setChecked(true);
        else
            pvh.checkBox.setChecked(false);

        pvh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WorkStatus complete = WorkStatusLocalDataSource.getInstance().
                        getWorkStatusByUuid(WorkStatus.Status.COMPLETE);
                if (complete!=null) {
                    OperationLocalDataSource.getInstance().setOperationStatus(item, complete);
                }
                pvh.checkBox.setChecked(true);
                boolean finish = TaskLocalDataSource.getInstance().checkAllOperationsComplete(item.getTask());
                if (finish) {
                    TaskLocalDataSource.getInstance().setTaskStatus(item.getTask(), complete);
                }
            }
        });

        pvh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set status
                WorkStatus complete = WorkStatusLocalDataSource.getInstance().
                        getWorkStatusByUuid(WorkStatus.Status.COMPLETE);
                if (complete!=null) {
                    OperationLocalDataSource.getInstance().setOperationStatus(item, complete);
                }
                pvh.checkBox.setChecked(true);
                boolean finish = TaskLocalDataSource.getInstance().checkAllOperationsComplete(item.getTask());
                if (finish) {
                    TaskLocalDataSource.getInstance().setTaskStatus(item.getTask(), complete);
                }
            }
        });
        pvh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set status
                WorkStatus complete = WorkStatusLocalDataSource.getInstance().
                        getWorkStatusByUuid(WorkStatus.Status.COMPLETE);
                if (complete!=null) {
                    OperationLocalDataSource.getInstance().setOperationStatus(item, complete);
                }
                boolean finish = TaskLocalDataSource.getInstance().checkAllOperationsComplete(item.getTask());
                if (finish) {
                    TaskLocalDataSource.getInstance().setTaskStatus(item.getTask(), complete);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

/*
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
*/

    /**
     * Update the data. Keep the data is the latest.
     * @param list The data.
     */
    public void updateData(@NonNull List<Operation> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class OperationViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textViewOperationTitle;
        AppCompatTextView textViewOperationDescription;
        CheckBox checkBox;

        //private OnRecyclerViewItemClickListener listener;

        OperationViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewOperationDescription = itemView.findViewById(R.id.textViewOperationDescription);
            textViewOperationTitle = itemView.findViewById(R.id.textViewOperationTitle);
            checkBox = itemView.findViewById(R.id.operationCheckbox);
            textViewOperationTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("z","z");
                }
            });
            //this.listener = listener;
            //itemView.setOnClickListener(this);
        }
    }
}
