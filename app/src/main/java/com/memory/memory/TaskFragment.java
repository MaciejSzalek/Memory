package com.memory.memory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maciej Szalek on 2018-11-17.
 */

public class TaskFragment extends Fragment {

    private List<String> taskList = new ArrayList<>();
    private Map<String, Integer> taskMap = new HashMap<>();
    public ListView taskListView;
    public ArrayAdapter arrayAdapter;
    private DBHelper dbHelper;

    public Button addButton;
    public Button editButton;
    public Button deleteButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){

        View taskFragment = inflater.inflate(R.layout.task_layout, container, false);

        taskListView = taskFragment.findViewById(R.id.task_list_view);
        addButton = taskFragment.findViewById(R.id.add_task_button);
        editButton = taskFragment.findViewById(R.id.edit_task_button);
        deleteButton = taskFragment.findViewById(R.id.delete_task_button);


        dbHelper = new DBHelper(getContext());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add ", Toast.LENGTH_SHORT).show();
                for(int i=0; i<10; i++){
                    Tasks tasks = new Tasks();
                    tasks.setTask("Subject" + Integer.toString(i));
                    try {
                        dbHelper.createOrUpdateStatus(tasks);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllTasksFromSQL();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<taskListView.getCount(); i++){
                    if(taskListView.isItemChecked(i)){
                        Tasks tasks = new Tasks();
                        tasks.setId(taskMap.get(taskListView.getItemAtPosition(i)));
                        try {
                            dbHelper.deleteTaskById(tasks);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                getAllTasksFromSQL();
            }
        });


        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "Click: " + taskList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String task = taskList.get(position);
                deleteTaskById(task);
                return false;
            }
        });
        return taskFragment;
    }

    private void deleteTaskById(String task){
        try {
            Tasks tasks = new Tasks();
            tasks.setId(taskMap.get(task));
            dbHelper.deleteTaskById(tasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getAllTasksFromSQL();
    }
    private void getAllTasksFromSQL(){
        if(taskList.size() > 0){
            taskList.clear();
        }
        try {
            List<Tasks> tList = dbHelper.getAllTasks();
            for(Tasks tasks: tList){
                taskList.add(tasks.getTask());
                taskMap.put(tasks.getTask(), tasks.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(getContext() != null){
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked,
                    taskList);
            taskListView.setAdapter(arrayAdapter);
        }
    }
}
