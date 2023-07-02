# Practical Work - Web Application with Jakarta EE using the Spring Framework

This project consists of a web application that accesses RESTful web services developed with the Spring Framework. The application allows managing the services provided by an **Animal Hotel**. Data persistence is done through MySQL.

Branch 'final' -> Represents the **final version**. 

## Application Features

- Profile Selection: The application allows choosing between two user profiles, Hotel Administrator and Customer.
- Customer Management: The application allows registering, viewing, updating, and deleting hotel customers.
- Animal Management: Through the application, it is possible to register the animals staying at the hotel, including details such as name, species, age, and breed. Additionally, it is possible to associate each animal with a specific customer to facilitate tracking.
- Room Management: The application allows managing the available rooms in the hotel, including information such as room number, price, and type.
- Reservations and Accommodation: With the application, it is possible to create and manage reservations for hotel customers. Through the association between customers, animals, and rooms, it is possible to record the check-in and check-out dates of each stay and choose the room type, facilitating control and organization of reservations.
- Feeding: In the application, it is possible to associate the feeding type with each pet. There are different categories and respective qualities, such as "Dry Food," "Canned Food," and "Raw Food." The user has the possibility to make a reservation for their pet based on the desired feeding type.
- Statistics and Reports: In addition to the management functionalities, the application provides features for generating statistics and reports on the hotel's operation. This includes information such as the number of stays per species in the hotel, from all time or selecting a date, and also the number of animals that have an associated menu.

## Technologies Used

- Jakarta EE (formerly known as Java EE)
- Spring Framework
- MySQL

## Configuration Environment

To run this application, follow the instructions below:

1. Make sure that the Java Development Kit (JDK) is installed. You can download it from: [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

2. Install Apache Maven. You can download Maven from: [Download Maven](https://maven.apache.org/download.cgi). Follow the appropriate installation instructions for your operating system.

3. Configure the MySQL Server and create a database for the application. You can download MySQL from: [Download MySQL](https://dev.mysql.com/downloads/installer/). Follow the installation instructions and create an empty database for the application.

4. Clone this repository to your local development environment:

```bash
git clone [repository-url]
```

5. In the src/main/resources/application.properties file, configure the database connection properties. Make sure to provide the correct URL, username, and password.

6. Open the terminal or command prompt, navigate to the root directory of the cloned project, and run the following command to compile and run the application:

```bash
mvn spring-boot:run
```

7. The application will start and will be available at localhost:8080.


## Demonstration Video

Click the image below to watch a demo video showcasing the features and functionality of the application:

[![Pets Hotel Page](https://example.com/demo-video-thumbnail.jpg)](https://example.com/demo-video-link)

The demo video provides a visual walkthrough of the application's usage and demonstrates how it can benefit users. It showcases the key features, user interface, and overall user experience. Watch the demo to get a better understanding of how the application works and its capabilities.

Feel free to reach out if you have any questions or need further assistance.








