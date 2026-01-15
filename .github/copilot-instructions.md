# Copilot Instructions for the Invoicer Project

Welcome to the Invoicer project! This document provides essential guidelines and conventions to help AI coding agents contribute effectively to this codebase. Please follow these instructions to ensure consistency and maintainability.

## Project Overview
The Invoicer project is a Java-based application for managing invoices. It follows a modular architecture, with the main components organized under the `src/main/java/tech/sangdang/invoicer/modules/invoice` directory. The key submodules include:

- **API Layer**: Handles HTTP requests and responses. Key files:
  - `InvoiceAdminController.java`
  - `InvoiceInternalController.java`
  - `InvoiceUserController.java`

- **Application Layer**: Contains business logic and service classes. Key files:
  - `InvoiceManagementService.java`
  - `InvoiceQueryService.java`
  - `InvoiceMapper.java`

- **Domain Layer**: Defines core domain models and interfaces. Key files:
  - `Invoice.java`
  - `InvoiceAllowedTypes.java`
  - `InvoiceStatus.java`
  - `repository/InvoiceRepositoryImpl.java`

- **Infrastructure Layer**: Implements data access and integration with external systems. Key files:
  - `infra/InvoiceRepositoryImpl.java`

## Developer Workflows

### Building the Project
This project uses Maven for build management. To build the project, run:

```
./mvnw clean install
```

### Running Tests
Tests are written using Cucumber and JUnit. To execute the tests, run:

```
./mvnw test
```

### Debugging
- The main application entry point is `InvoicerApplication.java`.
- Use your IDE's debugging tools to set breakpoints and inspect the application.

### Running the Application
To run the application locally, execute:

```
./mvnw spring-boot:run
```

### Deployment
Deployment configurations are managed using GitHub Actions. The workflow file is located at `.github/workflows/deployment_workflow.yml`.

## Project-Specific Conventions

1. **Package Structure**: Follow the existing modular structure under `src/main/java/tech/sangdang/invoicer/modules`.
2. **DTOs**: Data Transfer Objects (DTOs) are located under `app/dto/req` and `app/dto/res`. Use `InvoiceMapper` for mapping between domain models and DTOs.
3. **Testing**: Cucumber feature files are located in `test/resources/tech.sangdang`. Step definitions are in `test/java/tech/sangdang/steps`.
4. **Configuration**: Application configurations are managed in `application.yml` under `src/main/resources`.
5. **Repository Pattern**: Data access is implemented using the repository pattern. See `domain/repository` and `infra/InvoiceRepositoryImpl.java` for examples.

## External Dependencies
- **Spring Boot**: Used for application framework and dependency injection.
- **Maven**: Build and dependency management.
- **Cucumber**: Behavior-driven development (BDD) testing framework.

## Integration Points
- **Database**: The project interacts with a database. Initialization scripts are located in `test/resources/init-dynamodb-table.sh`.
- **Static Resources**: Static files are located in `src/main/resources/static`.
- **Templates**: HTML templates are located in `src/main/resources/templates`.

## Key Files and Directories
- `src/main/java/tech/sangdang/invoicer/`: Main source code directory.
- `src/main/resources/`: Configuration and resource files.
- `test/java/tech/sangdang/`: Test files and step definitions.
- `.github/workflows/`: CI/CD workflows.

## Notes for AI Agents
- Always adhere to the existing modular structure and naming conventions.
- Ensure all new code is covered by tests, preferably using Cucumber.
- Update `application.yml` cautiously to avoid breaking configurations.
- Follow the repository pattern for data access.

For any questions or clarifications, refer to the `HELP.md` file or consult the project maintainers.