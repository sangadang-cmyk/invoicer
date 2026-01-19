package tech.sangdang.invoicer.common.core;

import java.util.List;
import java.util.Optional;

public interface QueryDynamoRepository<T> {
    List<T> find(FindQuery query);
    
    Optional<T> findOne(FindQuery query);
}
