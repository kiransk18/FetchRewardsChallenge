# FetchRewardsChallenge
 This is a payment rewards management system
 # Project description: https://fetch-hiring.s3.us-east-1.amazonaws.com/points.pdf

# ** Make sure you have Maven and JDK Version >= 1.8 **

# clone this repository
git clone https://github.com/kiransk18/FetchRewardsChallenge

# change directory to this repo
cd FetchRewardsChallenge

# install the repo with mvn
mvn clean install

# start the application
mvn spring-boot:run

# Note: port 8080 must be available


# Note: Download and Install Postman to make requests.

The PaymentRewardsService handles three types of requests.

# 1. Add Transaction request
   The Add Transaction request adds the transaction to the system and updates points. The Service will throw an exception if the total balance or payer's balance is    insufficient. This is a POST Web request and below is the request Url. Request body is a JSON object. Response contains transaction success message or the error    message.
   
    request_url:  http://localhost:8080/api/v1/fetchrewards/addTransaction
    sample request body (JSON): 
    
    {
        "payer": "DANNON",
        "points": 300,
        "timeStamp": "2020-09-02T14:00:00Z"
    }
    

# 2. Spend Points request 
   Points are spent following the two rules  and response is a list of { "payer": <string>, "points": <integer> } This is a POST Web request and below is the request Url. Request body is a JSON object.
   
    request_url:  http://localhost:8080/api/v1/fetchrewards/spendPoints
    sample request body (JSON): 
    
    {
        "points": 400
    }
    
    Sample Response:
    
    [{"payer":"MILLER COORS","points":-200},{"payer":"DANNON","points":-200}]
    

# 3. Get balance of all the payers 
   This is a GET Web request and below is the request Url.
   
    request_url: http://localhost:8080/api/v1/fetchrewards/getPayerPoints
   
    Sample Response:
   
    [{"payer":"UNILEVER","points":0},{"payer":"MILLER COORS","points":100},{"payer":"DANNON","points":0}]
