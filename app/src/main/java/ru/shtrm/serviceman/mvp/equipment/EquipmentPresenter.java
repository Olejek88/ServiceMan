package ru.shtrm.serviceman.mvp.equipment;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.EquipmentStatusRepository;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;

public class EquipmentPresenter implements EquipmentContract.Presenter {

    @NonNull
    private EquipmentContract.View view;

    @NonNull
    private EquipmentStatusRepository equipmentStatusRepository;

    @NonNull
    private final TaskRepository TaskRepository;

    @NonNull
    private String equipment_uuid;

    public EquipmentPresenter(@NonNull EquipmentContract.View view,
                                @NonNull EquipmentRepository equipmentRepository,
                                @NonNull EquipmentStatusRepository equipmentStatusRepository,
                              @NonNull TaskRepository taskRepository,
                              String equipment_uuid) {
        this.view = view;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.TaskRepository = taskRepository;
        this.view.setPresenter(this);
        this.equipment_uuid = equipment_uuid;
    }

    @Override
    public void subscribe() {
        loadTasks();
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public void updateEquipmentStatus(Equipment equipment, EquipmentStatus equipmentStatus) {
        //equipmentRepository.updateEquipmentStatus(equipment, equipmentStatus);
    }

    @Override
    public List<EquipmentStatus> loadEquipmentStatuses() {
        return equipmentStatusRepository.getEquipmentStatuses();
    }

    @Override
    public List<Task> loadTasks() {
        Equipment equipment = EquipmentLocalDataSource.getInstance().getEquipmentByUuid(equipment_uuid);
        List<Task> tasks = TaskRepository.getTaskByEquipment(equipment, null);
        view.showTaskList(tasks);
        return tasks;
    }
}
