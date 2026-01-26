#!/bin/bash
echo "INIT SCRIPT START"

# I have to disable this or it will validate endpoints by calling them
awslocal configure set cli_follow_urlparam false

region="ap-southeast-1"
cognito_user_pool_id="${region}_localpoolid"
cognito_swagger_client_id="local-swagger-client-id"
cognito_classroomapi_client_id="local-classroomapi-client-id"
cognito_admin_group_name="ADMIN"
cognito_user_group_name="USER"
cognito_admin_account_id="danganhsang09@gmail.com"
cognito_user_account_id="danganhsang2003@gmail.com"
cognito_account_password="Sang2003@"

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
  --scopes "ScopeName=create,ScopeDescription=Create invoice" \
          "ScopeName=default,ScopeDescription=Default scope" \
          "ScopeName=delete,ScopeDescription=Delete invoice" \
          "ScopeName=read-any,ScopeDescription=Read any invoice" \
          "ScopeName=update,ScopeDescription=Update invoice"
echo "[END] Create invoicer resource server"

echo "[START] Create swagger app client"
awslocal cognito-idp create-user-pool-client \
  --user-pool-id "${cognito_user_pool_id}" \
  --client-name "_custom_id_:${cognito_swagger_client_id}" \
  --generate-secret \
  --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH \
  --callback-urls="http://localhost:8080/swagger-ui/oauth2-redirect.html" \
  --allowed-o-auth-scopes "openid" "email" "profile" "aws.cognito.signin.user.admin" "phone" \
  --allowed-o-auth-flows "code" \
  --allowed-o-auth-flows-user-pool-client \
  --supported-identity-providers "COGNITO"
echo "[END] Create swagger app client"

echo "[START] Create classroomApi app client"
awslocal cognito-idp create-user-pool-client \
  --user-pool-id "${cognito_user_pool_id}" \
  --client-name "_custom_id_:${cognito_classroomapi_client_id}" \
  --generate-secret \
  --callback-urls="http://localhost:8080/swagger-ui/oauth2-redirect.html" \
  --allowed-o-auth-scopes "invoicer-api/create" \
                          "invoicer-api/default" \
                          "invoicer-api/delete" \
                          "invoicer-api/read-any" \
                          "invoicer-api/update" \
  --allowed-o-auth-flows "client_credentials" \
  --allowed-o-auth-flows-user-pool-client \
  --supported-identity-providers "COGNITO"
echo "[END] Create classroomApi app client"

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
    --region ap-southeast-1
echo "Dynamodb table created"

echo "INIT SCRIPT END"