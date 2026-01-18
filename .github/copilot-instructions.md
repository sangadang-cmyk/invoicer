# Copilot Instructions for Invoicer

## Project Overview

- **Architecture:**  
  - Java 25, Spring Boot 4, Maven, AWS ECS Fargate deployment.
  - Modular structure:  
    - `modules/invoice` contains API controllers (`api/`), service layer (`app/service/`), DTOs (`app/dto/req|res/`), domain models (`domain/`), repository interfaces (`domain/repository/`), and infrastructure implementations (`infra/`).
    - Entry point: [src/main/java/tech/sangdang/invoicer/InvoicerApplication.java](src/main/java/tech/sangdang/invoicer/InvoicerApplication.java).

- **Service Layer Pattern:**  
  - All service functions accept a single DTO parameter (see `app/dto/req/`).
  - DTOs are named using the pattern:  
    - Commands: `<Action>Command` (e.g., `CreateInvoiceCommand`)
    - Queries: `<Action>Query` (e.g., `GetInvoiceByIdQuery`)
  - Service functions always return `void`, regardless of implied return values.

- **Controllers:**  
  - API endpoints are split by user/admin/internal in `api/`.
  - Implementation classes are in `api/impl/`.

## Developer Workflows

- **Build:**  
  - Use Maven wrapper:  
    ```
    ./mvnw clean package
    ```
- **Test:**  
  - Run all tests:  
    ```
    ./mvnw test
    ```
  - Cucumber tests:  
    - Feature files: [src/test/resources/tech.sangdang/Invoice.feature](src/test/resources/tech.sangdang/Invoice.feature)
    - Steps: [src/test/java/tech/sangdang/steps/InvoiceSteps.java](src/test/java/tech/sangdang/steps/InvoiceSteps.java)

- **Deploy:**  
  - GitHub Actions workflow: [deployment_workflow.yml](.github/workflows/deployment_workflow.yml)
  - Builds Docker image, pushes to AWS ECR, deploys to ECS using [task-definition.json](task-definition.json).

## Conventions & Patterns

- **DTOs:**  
  - Located in `app/dto/req/` and `app/dto/res/`.
  - Always empty for new service functions (no fields/methods).
  - Annotated with Lombok (`@Data`, `@NoArgsConstructor`).

- **Service Functions:**  
  - Located in `app/service/`.
  - Only one parameter (DTO), no primitive parameters.
  - No return value (`void` only).

- **Repository Pattern:**  
  - Interface in `domain/repository/`, implementation in `infra/`.

- **Mapping:**  
  - Use MapStruct for DTO/entity mapping ([app/mapper/InvoiceMapper.java](src/main/java/tech/sangdang/invoicer/modules/invoice/app/mapper/InvoiceMapper.java)).

## Integration Points

- **AWS:**  
  - Uses AWS credentials from environment variables or GitHub secrets.
  - ECS deployment via Fargate.
  - ECR for Docker images.

- **Spring Cloud AWS:**  
  - Configured in `application.yml` and `application-local.yml`.

## Examples

- To add a new service function, create an empty DTO in `app/dto/req/`, then add a `void` method to the relevant service class in `app/service/`, accepting only the DTO.

---

Please review and let me know if any section is unclear or missing details specific to your workflows or architecture. I can iterate further based on your feedback.

- Be extremely concise. Sacrifice grammar for the sake of concision. 