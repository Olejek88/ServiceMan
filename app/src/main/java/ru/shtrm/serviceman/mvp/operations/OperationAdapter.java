package ru.shtrm.serviceman.mvp.operations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.local.OperationLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.WorkStatusLocalDataSource;
import ru.shtrm.serviceman.mvp.equipment.EquipmentContract;

public class OperationAdapter extends BaseAdapter {

    // The OnMessageClickListener of listener.
    //private OnOperationItemClickListener listener;

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Operation> list;

    public OperationAdapter(@NonNull Context context, @NonNull List<Operation> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setItemlist(List<Operation> operationList) {
        list = operationList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Operation getItem(int position) {
        Operation item = null;
        if (list.size() > 0) {
            item = list.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnOperationClickListener(OnOperationItemClickListener listener) {
        //this.listener = listener;
    }

    public interface OnOperationItemClickListener {
        public void onCheckboxClicked(int position, Operation item);

    }

    public List<Operation> getUpdatedList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_operation, parent, false);
            OperationViewHolder pvh = new OperationViewHolder(view);
            view.setTag(pvh);
        }
        final Operation item = list.get(position);
        OperationViewHolder pvh = (OperationViewHolder) view.getTag();
        if (item != null && pvh != null) {
            pvh.textViewOperationDescription.setText(item.getOperationTemplate().getDescription());
            if (item.getWorkStatus().getUuid().equals(WorkStatus.Status.COMPLETE)) {
                pvh.checkBox.setChecked(true);
                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getChangedAt());
                pvh.textViewOperationTitle.setText(view.getContext().getString(
                        R.string.operation_title, item.getOperationTemplate().getTitle(),sDate));
            }
            else {
                pvh.checkBox.setChecked(false);
                pvh.textViewOperationTitle.setText(item.getOperationTemplate().getTitle());
            }

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
        return view;
    }

    public class OperationViewHolder {
        AppCompatTextView textViewOperationTitle;
        AppCompatTextView textViewOperationDescription;
        CheckBox checkBox;

        //private OnRecyclerViewItemClickListener listener;
        OperationViewHolder(View itemView) {
            super();
            textViewOperationDescription = itemView.findViewById(R.id.textViewOperationDescription);
            textViewOperationTitle = itemView.findViewById(R.id.textViewOperationTitle);
            checkBox = itemView.findViewById(R.id.operationCheckbox);
        }
    }
}
