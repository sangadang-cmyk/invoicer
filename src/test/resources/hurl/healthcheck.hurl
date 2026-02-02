GET {{host}}/actuator/health
x-deployment-target: {{deploymentTarget}}
HTTP 200

[Asserts]
jsonpath "$.status" == "UP"