# Swift Code Application

This project is a Spring Boot application that loads bank branch data from a CSV file, processes it, and provides a REST API to query the data.

## Features

- Load data from a CSV file and store it in PostgreSQL database.
- RESTful API.
- Tests.

## Setting Up the Project

Follow these steps to set up the project:

1. **Clone the repository**:

    ```bash
    git clone https://github.com/fidren/swift_code
    ```
    
2. **Navigate to the project directory**:

    ```bash
    cd swift_code
    ```

3. **Run the project using Docker Compose**:
   
    ```bash
    docker compose up --build
    ```

4. **Access the application:**
   - The application will start on default port `8080`. You can for example access the API via postman.

## Testing the Project

To run the tests, use the following command:

```bash
mvn test
```

## API Endpoints
| HTTP Method | Endpoint | Description |
| --- | --- | --- |
| GET | /v1/swift-codes/{swift-code} | Retrieve details of a single SWIFT code whether for a headquarters or branches. |
| GET | /v1/swift-codes/country/{countryISO2code} | Return all SWIFT codes with details for a specific country (both headquarters and branches). |
| POST | /v1/swift-codes | Adds new SWIFT code entries to the database for a specific country. |
| DELETE | /v1/swift-codes/{swift-code} | Deletes swift-code data if swiftCode matches the one in the database. |

## Author
**Wojciech Mikula**
