package com.example.libapijavier;

import java.io.Serializable;

public class DataWrapper<T> implements Serializable {
    private String dataList;
    private String title;
    public DataWrapper(String dataList, String title) {
        this.dataList = dataList;
        this.title = title;
    }
    public String getDataList() {
        return dataList;
    }
    public String getTitle() {
        return title;
    }
}
