package ru.t1.java.demo.kafka.handler;

import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.enums.TransactionStatusEnum;

import java.util.HashMap;
import java.util.List;

@Component
public class TransactionResultStatusFactory {
    HashMap<TransactionStatusEnum, TransactionResultHandler> map = new HashMap<>();

    public TransactionResultStatusFactory(List<TransactionResultHandler> impls) {
        impls.forEach(impl -> map.put(impl.getStatus(), impl));
    }

    public TransactionResultHandler getImpl(TransactionStatusEnum status) {
        return map.get(status);
    }
}
