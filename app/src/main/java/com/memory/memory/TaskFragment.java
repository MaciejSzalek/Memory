package com.memory.memory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private DBHelper dbHelper;
    public DialogManager dialogManager;
    private SpeechManager speechManager;
    private EventBus bus = EventBus.getDefault();

    public ImageButton addButton;
    public ImageButton editButton;
    public ImageButton deleteButton;

    private List<String> taskList = new ArrayList<>();
    private Map<String, Integer> taskMap = new HashMap<>();
    private Map<String, Integer> taskCheckedMap = new HashMap<>();
    public ListView taskListView;
    public ArrayAdapter arrayAdapter;

    private String positionStr;

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
                showNewPositionDialog();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechManager = new SpeechManager(getActivity(), getContext());
                speechManager.promptSpeechInput();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllMarkedDialog();
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer taskId = taskMap.get(taskList.get(position));
                if(taskListView.isItemChecked(position)){
                    updateTaskCheckedById(taskId, 1);
                }else{
                    updateTaskCheckedById(taskId, 0);
                }
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Integer taskId = taskMap.get(taskList.get(position));
                String posStr = taskList.get(position);
                showEditDialog(taskId, position, posStr);
                return false;
            }
        });
        return taskFragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.send_list:
                Toast.makeText(getContext(), "Send ToDo list", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.select_all:
                for(int i=0; i < taskList.size(); i++){
                    Integer productId = taskMap.get(taskList.get(i));
                    updateTaskCheckedById(productId, 1);
                }
                getAllTasksFromSQL();
                setCheckStatus();
                return true;
            case R.id.unselect_all:
                for(int i=0; i < taskList.size(); i++){
                    Integer productId = taskMap.get(taskList.get(i));
                    updateTaskCheckedById(productId, 0);
                }
                getAllTasksFromSQL();
                setCheckStatus();
                return true;
            case R.id.delete_selected:
                showDeleteAllMarkedDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void getMessageEvent(Events.EventToDo events){
        addNewPosition(events.getToDo());
        getAllTasksFromSQL();
        setCheckStatus();
    }

    @Override
    public void onResume(){
        super.onResume();
        getAllTasksFromSQL();
        setCheckStatus();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!bus.isRegistered(this)){
            bus.register(this);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        bus.unregister(this);
    }

    private void addNewPosition(String str){
        Tasks tasks = new Tasks();
        tasks.setTask(str);
        tasks.setChecked(0);
        try {
            dbHelper.createNewTasks(tasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateTaskById(Integer id, String posStr){
        try {
            dbHelper.updateTaskById(id, posStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTaskCheckedById(Integer id, Integer check){
        try {
            dbHelper.updateTaskCheckedById(id, check);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteAllMarked(){
        for(int i=0; i<taskListView.getCount(); i++){
            if(taskListView.isItemChecked(i)){
                String task = (String) taskListView.getItemAtPosition(i);
                deleteTaskById(task);
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

    private void setCheckStatus(){
        for(int i=0; i < taskList.size(); i++){
            String task = taskList.get(i);
            Integer checked = taskCheckedMap.get(task);
            if(checked == 0){
                taskListView.setItemChecked(i, false);
            }else{
                taskListView.setItemChecked(i, true);
            }
        }
    }
    private void getAllTasksFromSQL(){
        if(taskList.size() > 0){
            taskList.clear();
            taskMap.clear();
            taskCheckedMap.clear();
        }
        try {
            List<Tasks> tList = dbHelper.getAllTasks();
            for(Tasks tasks: tList){
                taskList.add(tasks.getTask());
                taskMap.put(tasks.getTask(), tasks.getId());
                taskCheckedMap.put(tasks.getTask(), tasks.getChecked());
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

    private void showEditDialog(final Integer id,final int position, final String posStr){
        String title = getResources().getString(R.string.edit_todo);
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                updateTaskById(id, positionStr);
                updateTaskCheckedById(id, 0);
                taskListView.setItemChecked(position, false);
                getAllTasksFromSQL();
                setCheckStatus();
            }

            @Override
            public void setDeleteButton() {
                deleteTaskById(posStr);
                getAllTasksFromSQL();
                setCheckStatus();
            }

            @Override
            public void setPositionString(String positionString) {
                positionStr = positionString;
            }
        };
        dialogManager.editPositionDialogBuilder(title, posStr, action);
    }

    private void showNewPositionDialog(){
        String title = getResources().getString(R.string.new_todo);
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                if(positionStr != null){
                    addNewPosition(positionStr);
                }
                getAllTasksFromSQL();
                setCheckStatus();
            }
            @Override
            public void setDeleteButton() {}
            @Override
            public void setPositionString(String positionString) {
                positionStr = positionString;
            }
        };
        dialogManager.addNewPositionDialogBuilder(title, action);
    }

    private void showDeleteAllMarkedDialog(){
        dialogManager = new DialogManager(getContext());
        DialogManager.Action action = new DialogManager.Action() {
            @Override
            public void setConfirmButton() {
                deleteAllMarked();
                getAllTasksFromSQL();
            }

            @Override
            public void setDeleteButton() {}
            @Override
            public void setPositionString(String positionString) {}
        };
        dialogManager.deleteAllCheckedDialogBuilder(action);
    }
}
