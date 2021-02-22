## FetchRewardsChallenge
 This is a payment rewards management system.
 #### Project description: https://fetch-hiring.s3.us-east-1.amazonaws.com/points.pdf

#### ** Make sure you have Maven and Java 11 **

#### 1. Clone this repository
git clone https://github.com/kiransk18/FetchRewardsChallenge

#### 2. Change directory to this repo
cd FetchRewardsChallenge

#### 3. Install the dependencies of repo with mvn
mvn clean install

####  4. start the application
mvn spring-boot:run

** Note: port 8080 must be available. **

** Note: Download and Install Postman to make requests. **




**The PaymentRewardsService handles three types of requests. **

#### 5.1 Add Transaction request
   The Add Transaction request adds the transaction to the system and updates points. The Service will throw an exception if the total balance or payer's balance is    insufficient. Response contains transaction success message or the error message.
   
    request_url:  http://localhost:8080/api/v1/fetchrewards/addTransaction
    request_type : POST
    sample request body (JSON): 
    
    {
        "payer": "DANNON",
        "points": 300,
        "timeStamp": "2020-09-02T14:00:00Z"
    }
    

#### 5.2 Spend Points request 
   Points are spent following the two rules mentioned in the project description and response is a list of { "payer": <string>, "points": <integer> }
   
    request_url:  http://localhost:8080/api/v1/fetchrewards/spendPoints
    request_type : POST
    sample request body (JSON): 
    
    {
        "points": 400
    }
    
    Sample Response:
    
    [{"payer":"MILLER COORS","points":-200},{"payer":"DANNON","points":-200}]
    

#### 5.3 Get balance of all the payers 
   This is a GET Web request and below is the request Url.
   
    request_url: http://localhost:8080/api/v1/fetchrewards/getPayerPoints
    request_type : GET
   
    Sample Response:
   
    [{"payer":"UNILEVER","points":0},{"payer":"MILLER COORS","points":100},{"payer":"DANNON","points":0}]
