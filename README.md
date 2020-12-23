# LibraryInventoryArchitecture

Project Description: A library inventory system where we need to scan the books and send the details to store the book details in Library Inventory. The cases may be to insert new books and update the old books (that is already existed in the inventory system).

### How we proceed with the problem ? ###

Project Prerequisites:

1. A fully accessed kafka system.
2. Knowledge on Spring boot and Kafka.

####  Build the backend system : ####

We will proceed with the microservice architecture, First we will build the event producer where the scanning events will send through the kafka producer and the events will be received through the kafka consumer.

The first part of the problem is designing the producer ( as shown in fig. microservice 1). We will build the kafka producer through the spring boot application and the results need to be invoked through events.

The second part is building a consumer system where we need to read the information through the kafka topic and save the result in a database.

![alt text](https://github.com/drdcs/LibraryInventory/blob/main/LibraryInventoryArchitecture.png)
