#!/bin/bash
echo "INIT SCRIPT START"

# I have to disable this or it will validate endpoints by calling them
awslocal configure set cli_follow_urlparam false

region="ap-southeast-1"
cognito_user_pool_id="${region}_localpoolid"
cognito_swagger_authorization_code_id="local-swagger-ac-client-id"
cognito_swagger_client_credentials_id="local-swagger-cc-client-id"
cognito_admin_group_name="ADMIN"
cognito_user_group_name="USER"
cognito_admin_account_id="danganhsang09@gmail.com"
cognito_user_account_id="danganhsang2003@gmail.com"
cognito_account_password="Sang2003@"
lambda_zip_path="/etc/localstack/init/ready.d/lambda/Invoicer-HandleS3Uploads.zip"

echo "[START] Creating user pool"
awslocal cognito-idp create-user-pool \
    --pool-name Invoicer \
    --user-pool-tags "_custom_id_=$cognito_user_pool_id" \
    --region "$region"
echo "[END] Creating user pool"

echo "[START] Create invoicer resource server"
awslocal cognito-idp create-resource-server \
  --user-pool-id "${cognito_user_pool_id}" \
  --identifier "invoicer-api" \
  --name "Invoicer API" \
  --scopes "ScopeName=invoice:create,ScopeDescription=Create invoice" \
          "ScopeName=default,ScopeDescription=Default scope" \
          "ScopeName=invoice:delete:owned,ScopeDescription=Delete invoice" \
          "ScopeName=invoice:read:owned,ScopeDescription=Read any invoice" \
          "ScopeName=invoice:update:owned,ScopeDescription=Update invoice" \
          "ScopeName=invoice:write:owned,ScopeDescription=Write invoice"
echo "[END] Create invoicer resource server"

echo "[START] Create swagger app client: authorization code grant"
awslocal cognito-idp create-user-pool-client \
  --user-pool-id "${cognito_user_pool_id}" \
  --client-name "_custom_id_:${cognito_swagger_authorization_code_id}" \
  --generate-secret \
  --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH \
  --callback-urls="http://localhost:8080/swagger-ui/oauth2-redirect.html" \
  --allowed-o-auth-scopes "openid" "email" "profile" "aws.cognito.signin.user.admin" "phone" \
  --allowed-o-auth-flows "code" \
  --allowed-o-auth-flows-user-pool-client \
  --supported-identity-providers "COGNITO"
echo "[END] Create swagger app client: authorization code grant"

echo "[START] Create swagger app client: client credentials grant"
awslocal cognito-idp create-user-pool-client \
  --user-pool-id "${cognito_user_pool_id}" \
  --client-name "_custom_id_:${cognito_swagger_client_credentials_id}" \
  --generate-secret \
  --callback-urls="http://localhost:8080/swagger-ui/oauth2-redirect.html" \
  --allowed-o-auth-scopes "invoicer-api/invoice:create" \
                          "invoicer-api/default" \
                          "invoicer-api/invoice:delete:owned" \
                          "invoicer-api/invoice:read:owned" \
                          "invoicer-api/invoice:update:owned" \
                          "invoicer-api/invoice:write:owned" \
  --allowed-o-auth-flows "client_credentials" \
  --allowed-o-auth-flows-user-pool-client \
  --supported-identity-providers "COGNITO"
echo "[END] Create swagger app client: client credentials grant"

echo "Create groups start"
echo "Create ADMIN group start"
awslocal cognito-idp create-group \
    --group-name "$cognito_admin_group_name" \
    --user-pool-id "$cognito_user_pool_id" \
    --region "$region"
echo "Create ADMIN group end"
echo "Create USER group start"
awslocal cognito-idp create-group \
    --group-name "$cognito_user_group_name" \
    --user-pool-id "$cognito_user_pool_id" \
    --region "$region"
echo "Create USER group end"
echo "Create groups end"

echo "Create users start"
echo "Create admin user start"
awslocal cognito-idp admin-create-user \
    --user-pool-id "$cognito_user_pool_id" \
    --username "$cognito_admin_account_id" \
    --temporary-password "$cognito_account_password" \
    --user-attributes Name=email,Value="$cognito_admin_account_id" Name=email_verified,Value=true \
    --message-action SUPPRESS
awslocal cognito-idp admin-add-user-to-group \
    --user-pool-id "$cognito_user_pool_id" \
    --username "$cognito_admin_account_id" \
    --group-name "$cognito_admin_group_name"
echo "Create admin user end"
echo "Create user user start"
awslocal cognito-idp admin-create-user \
    --user-pool-id "$cognito_user_pool_id" \
    --username "danganhsang2003@gmail.com" \
    --temporary-password "Sang2003@" \
    --user-attributes Name=email,Value="danganhsang2003@gmail.com" Name=email_verified,Value=true \
    --message-action SUPPRESS
awslocal cognito-idp admin-add-user-to-group \
    --user-pool-id "$cognito_user_pool_id" \
    --username "$cognito_user_account_id" \
    --group-name "$cognito_user_group_name"
echo "Create user user end"
echo "Create users end"
    
echo "Creating dynamodb table"
awslocal dynamodb create-table \
    --table-name invoice \
    --attribute-definitions AttributeName=invoiceId,AttributeType=S \
    --key-schema AttributeName=invoiceId,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region ${region}
echo "Dynamodb table created"

echo "Create S3 bucket: invoicer-inbound"
awslocal s3api create-bucket --bucket invoicer-inbound --region ${region} --create-bucket-configuration LocationConstraint=ap-southeast-1
awslocal s3api put-bucket-cors \
  --bucket "invoicer-inbound" \
  --cors-configuration '{
    "CORSRules": [
      {
        "AllowedHeaders": ["*"],
        "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD"],
        "AllowedOrigins": ["*"],
        "MaxAgeSeconds": 3000
      }
    ]
  }' \
  --region ${region}
echo "Create S3 bucket: invoicer-permastore"
awslocal s3api create-bucket --bucket invoicer-permastore --region ${region} --create-bucket-configuration LocationConstraint=ap-southeast-1
echo "S3 bucket creation finished"

echo "[START] Create lambda for s3 bucket"
awslocal lambda create-function \
  --function-name "Invoicer-HandleS3Uploads" \
  --runtime nodejs24.x \
  --role arn:aws:iam::000000000000:role/superman \
  --handler index.handler \
  --zip-file fileb://${lambda_zip_path} \
  --region ${region}
echo "[END] Create lambda for s3 bucket"

echo "[START] Create S3 bucket notification to trigger lambda"
echo "Wait for lambda to be active, then add notification listener"
awslocal lambda wait function-active-v2 --function-name "Invoicer-HandleS3Uploads" --region ${region}
awslocal s3api put-bucket-notification-configuration \
  --bucket invoicer-inbound \
  --notification-configuration '{
                                  "LambdaFunctionConfigurations": [
                                    {
                                      "Id": "S3ProcessUpload",
                                      "LambdaFunctionArn": "arn:aws:lambda:ap-southeast-1:000000000000:function:Invoicer-HandleS3Uploads",
                                      "Events": ["s3:ObjectCreated:*"]
                                    }
                                  ]
                                }'
echo "[END] Create S3 bucket notification to trigger lambda"

echo "[START] Create SNS topic for invoice notifications"
awslocal sns create-topic --name invoicer-sns-topic --region ${region}
echo "[END] Create SNS topic for invoice notifications"

echo "INIT SCRIPT END"