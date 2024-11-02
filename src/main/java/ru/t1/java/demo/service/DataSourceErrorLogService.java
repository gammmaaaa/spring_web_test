package ru.t1.java.demo.service;

import ru.t1.java.demo.model.DataSourceErrorLog;

import java.util.List;

public interface DataSourceErrorLogService {
    List<DataSourceErrorLog> getAllDataSourceErrors();

    DataSourceErrorLog getDataSourceErrorById(long id);

    DataSourceErrorLog saveDataSourceError(DataSourceErrorLog dataSourceErrorLog);

    void deleteDataSourceErrorById(long id);
}
