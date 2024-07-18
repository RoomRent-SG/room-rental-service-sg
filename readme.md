#### Getting Started

This section guides you on how to run the application.

### Environment Variable

| Key                                       | Description                                                                                           |
|-------------------------------------------|-------------------------------------------------------------------------------------------------------|
| aws.accessKey                             | 	AWS access key ID for authentication.                                                                |
| aws.secretKey                             | 	AWS secret access key for authentication.                                                            |
| aws.s3.region                             | 	The AWS region where your S3 bucket resides.                                                         |
| aws.s3.bucketName                         | 	The name of the S3 bucket you want to interact with.                                                 |
| aws.cloudFront                            | 	Configuration options for AWS CloudFront if applicable (not shown here, refer to AWS documentation). |
| spring.servlet.multipart.max-file-size	   | Sets the maximum allowed size for individual uploaded files.                                          |
| spring.servlet.multipart.max-request-size | 	Sets the maximum allowed size for the entire multipart request (including all files).                |
| spring.servlet.multipart.enabled	         | Enables or disables multipart file upload functionality.                                              |
| spring.cache.type	                        | Defines the caching strategy (e.g., redis, caffeine).                                                 |
| spring.data.redis.host	                   | Hostname or IP address of the Redis server.                                                           |
| spring.data.redis.port                    | 	Port number of the Redis server.                                                                     |
| spring.cache.redis.time-to-live           | 	Default expiration time for cached items in seconds.                                                 |
| spring.cache.redis.se-key-prefix          | 	Enables or disables adding a prefix to cache keys.                                                   |
| spring.cache.redis.key-prefix             | 	The prefix to be used for cache keys (if enabled).                                                   |

### Running the application:

we have two variation to run

1. Containerise (*recommended)
2. [Standalone](#2-standalone)

#### 1. Containerise
_Coming Soon..._
#### 2. Standalone

#### Prerequisites:

* Java 17
* [Maven](https://formulae.brew.sh/formula/maven) `brew install maven`
* [mySQL](https://formulae.brew.sh/formula/mysql): `brew install mysql`
* [Redis](https://formulae.brew.sh/formula/redis): `brew install redis`

#### Running the application:
Clone or Download the Project: Obtain the project code using Git or download it as a zip archive.

#### Prepare environment variable
1. create `application.properties` in project directory
2. set `key=value`, mentioned in above table. [Environment Variable](#environment-variable)


#### Build the Project:
Using Maven: Navigate to the project directory in your terminal and run:

```mvn package```

#### Run the Application:

Navigate to the target directory (usually generated during build).
Execute the JAR file using the following command (replace application.jar with your actual JAR filename):

```java -jar application.jar```

Then go to [localhost:8080](http://localhost:8080/) to see the result.