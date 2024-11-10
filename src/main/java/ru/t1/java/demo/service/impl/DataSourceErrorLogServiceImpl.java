package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.DataSourceErrorLogDto;
import ru.t1.java.demo.mapper.DataSourceErrorLogMapper;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.service.DataSourceErrorLogService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSourceErrorLogServiceImpl implements DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private final DataSourceErrorLogMapper dataSourceErrorLogMapper;

    @Override
    public List<DataSourceErrorLogDto> getAllDataSourceErrors() {
        return dataSourceErrorLogRepository.findAll()
                .stream()
                .map(dataSourceErrorLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DataSourceErrorLogDto getDataSourceErrorById(long id) {
        return dataSourceErrorLogMapper.toDTO(dataSourceErrorLogRepository.findById(id).orElse(null));
    }

    @Override
    public DataSourceErrorLogDto saveDataSourceError(DataSourceErrorLogDto dataSourceErrorLogDto, String kafkaMessage) {
        DataSourceErrorLog dataSourceErrorLog = dataSourceErrorLogMapper.toEntity(dataSourceErrorLogDto);
        dataSourceErrorLog.setKafkaMessage(kafkaMessage);
        return dataSourceErrorLogMapper.toDTO(dataSourceErrorLogRepository.save(dataSourceErrorLog));
    }

    @Override
    public void deleteDataSourceErrorById(long id) {
        dataSourceErrorLogRepository.deleteById(id);
    }
}
