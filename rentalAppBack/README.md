Start the project

Go inside folder:

    cd rentalAPI

Configure the Database :

Update src/main/resources/application.properties to match your database setup:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

Install dependencies:

    mvn clean install

Launch API:

    mvn spring-boot:run

Routes Documentation:
    http://localhost:8080/swagger-ui/index.html

