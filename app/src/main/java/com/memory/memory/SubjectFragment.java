package com.memory.memory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej Szalek on 2018-11-17.
 */

public class SubjectFragment extends Fragment {

    private List<String> subjectList = new ArrayList<>();
    private ListView subjectListView;
    public ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){
        View subjectFragment = inflater.inflate(R.layout.subject_layout, container, false);

        subjectListView = subjectFragment.findViewById(R.id.subject_list_view);

        getSubjectList();

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Click: " + subjectList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        subjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Long click: " + subjectList.get(position), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return subjectFragment;
    }
    private void getSubjectList(){
        for(int i = 0; i<15; i++){
            subjectList.add(i, "Subject: " + Integer.toString(i));
        }
        if(getContext() != null){
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                    subjectList);
            subjectListView.setAdapter(arrayAdapter);
        }
    }
}
