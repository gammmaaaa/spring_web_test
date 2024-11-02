package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.service.DataSourceErrorLogService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSourceErrorLogServiceImpl implements DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Override
    public List<DataSourceErrorLog> getAllDataSourceErrors() {
        return dataSourceErrorLogRepository.findAll();
    }

    @Override
    public DataSourceErrorLog getDataSourceErrorById(long id) {
        return dataSourceErrorLogRepository.findById(id).orElse(null);
    }

    @Override
    public DataSourceErrorLog saveDataSourceError(DataSourceErrorLog dataSourceErrorLog) {
        return dataSourceErrorLogRepository.save(dataSourceErrorLog);
    }

    @Override
    public void deleteDataSourceErrorById(long id) {
        dataSourceErrorLogRepository.deleteById(id);
    }
}
