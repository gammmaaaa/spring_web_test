package ru.t1.java.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_source_error_logs")
public class DataSourceErrorLog extends AbstractPersistable<Long> {

    @Column(name = "stacktrace")
    private String stacktrace;

    @Column(name = "message")
    private String message;

    @Column(name = "method_signature")
    private String methodSignature;


}
