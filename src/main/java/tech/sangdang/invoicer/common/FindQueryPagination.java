package tech.sangdang.invoicer.common;

import lombok.Getter;

@Getter
public class FindQueryPagination {
    Integer page = 0;
    Integer size = 10;
    
    public FindQueryPagination() {}

    public FindQueryPagination(Integer page, Integer size) {
        if (page < 0) {
            page = 0;
        }
        
        if (size <= 0) {
            size = 10;
        }

        this.page = page;
        this.size = size;
    }
}
