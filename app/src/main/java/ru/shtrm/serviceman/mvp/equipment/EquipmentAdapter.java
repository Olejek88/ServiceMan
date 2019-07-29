package ru.shtrm.serviceman.mvp.equipment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class EquipmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Equipment> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public EquipmentAdapter(@NonNull Context context, @NonNull List<Equipment> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EquipmentsViewHolder(inflater.inflate(R.layout.item_equipment,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Equipment item = list.get(position);
        EquipmentsViewHolder pvh = (EquipmentsViewHolder) holder;
        if (item.getEquipmentType() != null)
            pvh.textViewEquipmentTitle.setText(item.getEquipmentType().getTitle());
        else
            pvh.textViewEquipmentTitle.setText(R.string.equipment_unknown);
        pvh.textViewEquipmentType.setText(item.getEquipmentType().getTitle());
        // это работает если один счетчик на хату, иначе добавить метод
//        Measure lastMeasure = MeasureLocalDataSource.getInstance().getLastMeasureByFlat(item.getFlat());
        if (item.getInputDate() != null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getChangedAt());
            pvh.textViewEquipmentLastMeasure.setText(sDate);
        } else {
            pvh.textViewEquipmentLastMeasure.setText(R.string.no_last_time);
        }
        // пока убрал
        // pvh.textViewImage.setText(item.getEquipmentType().getTitle().substring(0,1));
        // TODO выдергивать последнее фото из фото?
//        PhotoEquipment photoEquipment = PhotoEquipmentLocalDataSource.getInstance().getLastPhotoByEquipment(item);
//        if (photoEquipment != null) {
//            pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
//                    MainUtil.getPicturesDirectory(context), photoEquipment.getUuid().concat(".jpg")));
//        } else {
            pvh.circleImageView.setImageResource(R.drawable.counter);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Update the data. Keep the data is the latest.
     *
     * @param list The data.
     */
    public void updateData(@NonNull List<Equipment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class EquipmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView textViewEquipmentTitle;
        AppCompatTextView textViewEquipmentType;
        AppCompatTextView textViewEquipmentLastMeasure;
        AppCompatTextView textViewImage;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        EquipmentsViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewEquipmentTitle = itemView.findViewById(R.id.textViewEquipmentTitle);
            textViewEquipmentType = itemView.findViewById(R.id.textViewEquipmentType);
            textViewEquipmentLastMeasure = itemView.findViewById(R.id.textViewEquipmentLastMeasure);
            textViewImage = itemView.findViewById(R.id.textViewEquipment);
            circleImageView = itemView.findViewById(R.id.imageViewEquipment);

            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }
}
