# Project Name

## Table of Contents
- [Project Description](#project-description)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Project Description

MyBatis Code Generator is a Spring Boot tool designed to simplify the process of generating MySQL database and MyBatis-related codes, including controllers, services, service implementations, and mappers. This tool aims to reduce the repetitive tasks involved in setting up database interactions and CRUD operations, allowing developers to focus on core functionalities.

## Features

- Generate MySQL database schema
- Create MyBatis mappers
- Generate service interfaces and implementations
- Create RESTful controllers for CRUD operations
- Configuration-driven setup for easy customization

## Installation

### Prerequisites
- Java JDK 8 or higher
- Maven (optional, if using Maven for build)

### Clone the Repository
```sh
git clone https://github.com/your-username/mb-code-generator-workspace
cd mb-code-generator-workspace
```sh
If you are using Maven, you can build the project with:
mvn clean install
```

Usage
This repo contains a demo project, you can whether:
a. use the demo to test the api directly or
b. set up the configuration in mb-code-generator-workspace/mb-code-generator/src/main/resources/application.yml and run the application, it will automatically generate code for your project

## Application Properties
Application properties inside mb-code-generator-workspace/mb-code-generator/src/main/resources/application.yml
```yml
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
spring.datasource.username=root
spring.datasource.password=yourpassword
```


## Contributing
1. Fork the repository
2. Create a new branch (git checkout -b feature-branch)
3. Make your changes
4. Commit your changes (git commit -m 'Add some feature')
5. Push to the branch (git push origin feature-branch)
6. Open a Pull Request

## Contact
Mai Zhang - yulezh@proton.me
Project Link: https://github.com/realmaimai/mb-code-generator-workspace
