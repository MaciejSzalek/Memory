package com.memory.memory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Maciej Szalek on 2018-11-21.
 */

@DatabaseTable(tableName = "task_table")
public class Tasks {

    public Tasks(){}

    @DatabaseField(columnName = "id",generatedId = true)
    private Integer id;

    @DatabaseField(columnName = "task")
    private String task;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
}
