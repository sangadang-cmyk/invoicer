package tech.sangdang.invoicer.common;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Accessors(fluent = true)
@SuperBuilder
public class FindQuery {
    List<SearchCriteria> searchCriteria;
    
    @Builder.Default
    FindQueryPagination pagination = new FindQueryPagination();

    public static FindQuery empty() {
        return FindQuery.builder().build();
    }

}