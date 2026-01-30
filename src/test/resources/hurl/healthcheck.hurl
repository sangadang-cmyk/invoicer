GET {{host}}/actuator/health
x-deployment-target: test
HTTP 200

[Asserts]
jsonpath "$.status" == "UP"