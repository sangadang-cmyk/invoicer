#!/bin/bash
table_name="invoicer_invoice"
partition_key="invoiceId"

awslocal dynamodb create-table \
    --table-name "$table_name" \
    --key-schema AttributeName="$partition_key",KeyType=HASH \
    --attribute-definitions AttributeName="$partition_key",AttributeType=S \
    --billing-mode PAY_PER_REQUEST

echo "DynamoDB table '$table_name' created successfully with partition key '$partition_key'"
echo "Executed init-dynamodb-table.sh"