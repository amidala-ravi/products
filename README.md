# Supply Chain API
####
API Document Link: API document which used to develop the service
[productsApi.yaml](productsApi.yaml)
### How to Build and Run the Application
Below command will be used to build the service

```./gradlew clean build```

Below command will be used to Run the service in local

```./gradlew bootRun```
###Prerequisites to Run the application in local
To bring the service up in local below setup required
* Gradle 7
* JDK 11
* AWS CLI
* Postman
* Docker Localstack for AWS resources
  ``docker run -it -p 4567-4578:4567-4578 -p 8081:8080 atlassianlabs/localstack``
  

* create DynamoDB table schema in local with the below script
``aws dynamodb create-table \
--table-name product.catalog \
--attribute-definitions \
AttributeName=id,AttributeType=S \
--key-schema \
AttributeName=id,KeyType=HASH \
--provisioned-throughput \
ReadCapacityUnits=10,WriteCapacityUnits=5 \
--endpoint-url http://localhost:4569``
  
* To create dynamoddb table schema in AWS we can use cloudformation scripts, i have
  created the cloudformation script [dynamoddb-schema.yaml](dynamoddb-schema.yaml) 
   
###Once the service is up and running we can use below endpoints to make the calls through postman

POST endpoint

``http://localhost:8080/supply-chain``

To GET All the details

``http://localhost:8080/supply-chain``

To GET Details based on the product id

``http://localhost:8080/supply-chain/{id}``

To Delete Details based on the product id

``http://localhost:8080/supply-chain/{id}``

###Design 

Due to less time I have not added retry mechanism for if any failure happens in the system and cache to get the all records from DB 

![supplyDesign](/supplyDesign.png "supplyDesign")

####Improvements can be done in the design 
* By using cache mechanism we can load all the records to cache based on schedule 
  time which saves us the time when we want to load all the records from the DB
  
* While saving the record if any issue occurs we can retry or we can configure the SQS 
Queue to reprocess the message later.
* If we configure Eureka load balancer we can scale the service based on the load
* DynamoDB also can be changed to ON DEMAND if load is un predictable


####Below Quality Tools configured
* Jacoco
  ![jacoco report](/jacocoReport.png "jacocoReport")
* Junit
  ![junit report](/junitTestReport.png "junitReport")
  

  




  
