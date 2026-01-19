package tech.sangdang.invoicer.common.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Accessors(fluent = true)
@AllArgsConstructor
public final class SearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private final Object value;
}
