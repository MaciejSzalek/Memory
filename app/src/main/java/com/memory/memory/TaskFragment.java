package com.memory.memory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciej Szalek on 2018-11-17.
 */

public class TaskFragment extends Fragment {

    private List<String> taskList = new ArrayList<>();
    private List<Integer> taskChecked = new ArrayList<>();
    private Map<String, Integer> taskMap = new HashMap<>();
    public ListView taskListView;
    public ArrayAdapter arrayAdapter;
    private DBHelper dbHelper;

    public ImageButton addButton;
    public ImageButton editButton;
    public ImageButton deleteButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){

        View taskFragment = inflater.inflate(R.layout.task_layout, container, false);

        taskListView = taskFragment.findViewById(R.id.task_list_view);
        addButton = taskFragment.findViewById(R.id.add_task_button);
        editButton = taskFragment.findViewById(R.id.edit_task_button);
        deleteButton = taskFragment.findViewById(R.id.delete_task_button);

        dbHelper = new DBHelper(getContext());
        getAllTasksFromSQL();
        setCheckStatus();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add ", Toast.LENGTH_SHORT).show();
                for(int i=0; i<10; i++){
                    Tasks tasks = new Tasks();
                    tasks.setTask("Subject" + Integer.toString(i));
                    tasks.setChecked(0);
                    try {
                        dbHelper.createOrUpdateStatus(tasks);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                getAllTasksFromSQL();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckStatus();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<taskListView.getCount(); i++){
                    if(taskListView.isItemChecked(i)){
                        String task = (String) taskListView.getItemAtPosition(i);
                        deleteTaskById(task);
                    }
                }
                getAllTasksFromSQL();
            }
        });


        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(taskListView.isItemChecked(position)){
                    taskListView.setItemChecked(position, false);
                }else{
                    taskListView.setItemChecked(position, true);
                }
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String task = taskList.get(position);
                //deleteTaskById(task);
                Integer taskId = taskMap.get((String)taskListView.getItemAtPosition(position));
                if(taskListView.isItemChecked(position)){
                    taskListView.setItemChecked(position, false);
                    updateCheckById(taskId, 0);

                }else{
                    taskListView.setItemChecked(position, true);
                    updateCheckById(taskId, 1);
                }
                Toast.makeText(getContext(), Integer.toString(taskId), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return taskFragment;
    }

    private void updateCheckById(Integer id, Integer check){
        try {
            dbHelper.updateCheckedById(id, check);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCheckStatus(){
        for(int i=0; i < taskChecked.size(); i++){
            Integer checked = taskChecked.get(i);
            if(checked == 0){
                taskListView.setItemChecked(i, false);
            }else{
                taskListView.setItemChecked(i, true);
            }
        }
    }

    private void deleteTaskById(String task){
        try {
            Tasks tasks = new Tasks();
            tasks.setId(taskMap.get(task));
            dbHelper.deleteTaskById(tasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getAllTasksFromSQL(){
        if(taskList.size() > 0){
            taskList.clear();
            taskChecked.clear();
            taskMap.clear();
        }
        try {
            List<Tasks> tList = dbHelper.getAllTasks();
            for(Tasks tasks: tList){
                taskList.add(tasks.getTask());
                taskChecked.add(tasks.getChecked());
                taskMap.put(tasks.getTask(), tasks.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(getContext() != null){
            Collections.sort(taskList);
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked,
                    taskList);
            taskListView.setAdapter(arrayAdapter);
        }
    }
}
