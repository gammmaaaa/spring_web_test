package ru.t1.java.demo.util;

import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.DataSourceErrorLogDto;
import ru.t1.java.demo.mapper.DataSourceErrorLogMapper;
import ru.t1.java.demo.model.DataSourceErrorLog;

@Component
public class DataSourceErrorLogMapperImpl implements DataSourceErrorLogMapper {

    @Override
    public DataSourceErrorLog toEntity(DataSourceErrorLogDto dataSourceErrorLogDto) {
        return DataSourceErrorLog.builder()
                .stacktrace(dataSourceErrorLogDto.getStacktrace())
                .message(dataSourceErrorLogDto.getMessage())
                .methodSignature(dataSourceErrorLogDto.getMethodSignature())
                .build();
    }

    @Override
    public DataSourceErrorLogDto toDTO(DataSourceErrorLog dataSourceErrorLog) {
        return DataSourceErrorLogDto.builder()
                .stacktrace(dataSourceErrorLog.getStacktrace())
                .message(dataSourceErrorLog.getMessage())
                .methodSignature(dataSourceErrorLog.getMethodSignature())
                .build();
    }
}
