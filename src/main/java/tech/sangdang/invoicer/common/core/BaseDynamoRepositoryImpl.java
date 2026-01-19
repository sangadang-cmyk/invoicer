package tech.sangdang.invoicer.common.core;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@RequiredArgsConstructor
public class BaseDynamoRepositoryImpl<T> implements QueryDynamoRepository<T> {
    protected final DynamoDbTemplate db;
    private final Class<T> entityClass;

    @Override
    public List<T> find(FindQuery query) {
        var expressionBuilder = Expression.builder();
        expressionBuilder = this.applyCriteria(query.searchCriteria(), expressionBuilder);
        var expression = expressionBuilder.build();

        var scanBuilder = ScanEnhancedRequest.builder();
        scanBuilder.filterExpression(expression);
        scanBuilder = this.applyPagination(scanBuilder, query.pagination());
        var scanRequest = scanBuilder.build();

        return db.scan(scanRequest, entityClass).items().stream().toList();
    }

    @Override
    public Optional<T> findOne(FindQuery query) {
        return this.find(
                FindQuery.builder()
                        .searchCriteria(query.searchCriteria())
                        .pagination(new FindQueryPagination(0, 1))
                        .build()
        ).stream().findFirst();
    }

    private ScanEnhancedRequest.Builder applyPagination(ScanEnhancedRequest.Builder scanBuilder, FindQueryPagination pagination) {
        if (pagination != null) {
            int limit = pagination.getSize();
            scanBuilder.limit(limit);
        }
        return scanBuilder;
    }

    private Expression.Builder applyCriteria(List<SearchCriteria> criteria, Expression.Builder expressionBuilder) {
        if(criteria == null || criteria.isEmpty()) {
            return expressionBuilder;
        }
        StringJoiner expressionString = new StringJoiner(" AND ");
        Map<String, AttributeValue> values = new HashMap<>();
        Map<String, String> names = new HashMap<>();

        for (int i = 0; i < criteria.size(); i++) {
            SearchCriteria c = criteria.get(i);
            String valPlaceholder = ":val" + i;
            String namePlaceholder = "#name" + i;

            String op = switch (c.operation()) {
                case SearchOperation.EQUALS -> namePlaceholder + " = " + valPlaceholder;
                case SearchOperation.NOT_EQUALS -> namePlaceholder + " != " + valPlaceholder;
                case SearchOperation.GREATER_THAN -> namePlaceholder + " > " + valPlaceholder;
                case SearchOperation.LESS_THAN -> namePlaceholder + " < " + valPlaceholder;
            };

            expressionString.add(op);
            names.put(namePlaceholder, c.key());
            values.put(valPlaceholder, AttributeValue.builder().s(c.value().toString()).build());
        }

        return expressionBuilder
                .expression(expressionString.toString())
                .expressionNames(names)
                .expressionValues(values);
    }
}
