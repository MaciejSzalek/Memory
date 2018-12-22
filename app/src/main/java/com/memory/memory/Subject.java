package com.memory.memory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Maciej Szalek on 2018-11-21.
 */

@DatabaseTable(tableName = Subject.TABLE_SUBJECT)
public class Subject {

    public static final String TABLE_SUBJECT = "subject_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SUBJECT = "subject";

    public Subject(){}

    @DatabaseField(columnName = COLUMN_ID,generatedId = true)
    private Integer id;

    @DatabaseField(columnName = COLUMN_SUBJECT)
    private String subject;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
