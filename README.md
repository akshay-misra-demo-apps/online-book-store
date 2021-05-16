# Online Shopping Store

A simple online shopping store application written in Java, Spring boot and Mongo Atlas.

The application exposes rest apis for managing 'Customer Account' and related entities like 'Contact Medium', 'Customer Address', 'Billing Account' and 'Payment Method'.

The application also exposes rest apis for managing 'Product Order' like placing order, listing all orders for a customer and cancelling order.

Supported Use Cases:

 * Rest APIs for customer management.
 *
 * i.   Creating a customer.
 * ii.  Getting customer’s info by ID.
 * iii. Updating customer’s info using the ID.
 * iv.  Listing all customers.

 * Rest APIs for product order management.
 *
 * Assumption: The customer is already logged in and trying to place an order.
 * 
 * i. Creating an order
 * ii. Canceling/Deleting an order
 * iii. List all orders using customer ID


 Steps to launch the application:
 
 Pre-requisite: This is a maven based Java-8 Spring Boot application, so 'Maven' and 'Java 8' should be installed and environment variables should be set before.  
 
 1. Clone git repo to local machine.

 Option 1 to start application:
 
   a. Open application source code in any development IDE, example in 'Intelij Idea Utimate' version. Setup Java 8 JDK in the IDE.
   b. Run Maven commands in 'Intelij Idea' from parent maven module named as  "online-store-parent". First run 'clean' and then 'install' Lifecycle commands.
   c. This will start building the application, download required maven dependencies and build a runnable jar with name "online-store-0.0.1.jar" in location "/online-store/target".
   d. After this either we can run the application from 'Intelij Idea' or any other IDE. Open bootstrap class "com.tt.shopping.Application" present under "online-store" maven module and run "com.tt.shopping.Application" from IDE.
  
   
 Option 2 to start application:
 
   a. If 'Maven' and 'Java 8' is installed and environment variables are set then after cloning the git repository run following command from the root.
       mvn clean install
   b. This will start building the application, download required maven dependencies and build a runnable jar with name "online-store-0.0.1.jar" in location "/online-store/target".  
   c. While building project, Rest APIs Tests will run testing all the exposed apis.
   d. To run the jar created in step b. go to command line tool and run following command to start the application.
      java -jar ./online-store/target/online-store-0.0.1.jar
      
      This will start the application in port 9090 and the application can be accessed using http://127.0.0.1:9090/ 
      
 
 How to know which Rest APIs are currently supported by the application, what is the URI and what input is expected by each rest api?
 --> The application uses 'Swagger' for rest API documentation which will help to find answers to above questions.
 
     To make it more user friendly the appliation has a dependency to 'Swagger UI' as well.
     
     Swagger ui can be accessed through following url: http://127.0.0.1:9090/swagger-ui.html 
     
     This shows all the available rest end points currently exposed by the application. Rest APIs can be found under each section as follow:
     
     1. customer-controller

      Customer Management REST Endpoints.


      GET
      /tmf-api/customerManagement/v1/customer
      Listing all customers.

      POST
      /tmf-api/customerManagement/v1/customer
      Creating a customer.

      GET
      /tmf-api/customerManagement/v1/customer/{id}
      Getting customer’s info using an ID.

      PATCH
      /tmf-api/customerManagement/v1/customer/{id}
      Updating customer’s info using the ID.
      
      
     2. product-offering-controller
      GET
      /tmf-api/productOfferingManagement/v1/productOffering
      List all available products.
      
     3. product-order-controller
      Product Order Management REST Endpoints.


      POST
      /tmf-api/productOrderingManagement/v1/productOrder
      Creating an order.
      GET
      /tmf-api/productOrderingManagement/v1/productOrder/{customerId}
      List all orders using customer ID.
      POST
      /tmf-api/productOrderingManagement/v1/productOrder/cancel
      Creating an order.

   ====> To try any rest api, expand that api and click on "Try it out"
         Under "request" section there are 2 tabs/buttons. 'Edit Value': where we can add a request body(for POST/PATCH operation) and 'Model' where we can find the request model details required by the rest api. Under Model tab we can check each JSON field and its data type by expanding it. If clearly shows whether a field is of 'string', 'boolean' or Enum(we can find available options expected by the api) 
         
   ====> After filling all required details click on "Execute" button to trigger the request and the response can be found below in "Responses" section.
   
   
  
                     
====================  Rest API Specifications  =====================


1. Customer Account Management Module:

1.1 Create Customer: 

 POST:  /tmf-api/customerManagement/v1/customer

Currently the application supports POST operation for 'Customer' creation along with an option to create related entities like  Address/Billing Account.
or
Initially we can just create a customer without Address and Billing Account and later via Customer PATCH operation we can create Addresses and Billing Accounts along with Payment Methods within a Billing Account.
 
Below is a request to create a customer entity along with 'contactMedium' 'address' and 'billingAccount' together.

Request Body:

{
"firstName": "Akshay",
 "lastName": "Mishra",
 "primaryEmail": "akshaymishra2488@gmail.com",
 "secondaryEmail": "akshaymishra2490@gmail.com",
 "customerCategory": "RESIDENTIAL",
 "characteristic": [
   {
     "name": "membership",
     "value": "Premium"
   }
 ],
  "contactMedium": [
   {
      "type": "EMAIL",
     "preferred": true,
     "characteristic": [
       {
         "name": "email",
         "value": "akshaymishra2488@gmail.com"
       }
      ]
   }
 ],
 "address": [
   {
     "addressType": "HOME",
     "street1": "15th A Main",
     "street2": "14th Cross",
     "city": "Bangalore",
     "stateOrProvince": "Karnataka",
     "country": "India",
     "postCode": "560102",
     "phoneNumber": "9789090909"         
   }
 ],
 "billingAccount": [
   {
     "currency": "INR",
     "defaultBillingAccount": true,
     "paymentMethod": [
       {
         "characteristic": [
           {
             "name": "cardNumber",
             "value": "4145676700091200"
           }
         ],
         "defaultMethod": true,
         "paymentMethodType": "DEBIT_CARD"
       }
     ]
   },
   {
     "currency": "EUR",
     "defaultBillingAccount": false,
     "paymentMethod": [
       {
         "characteristic": [
           {
             "name": "accountNumber",
             "value": "700091200"
           }
         ],
         "defaultMethod": true,
         "paymentMethodType": "PAYPAL"
       }
     ]
   }
 ]
}



Data created in mongodb 'customers', 'addresses' and 'billingAccounts' collections:

1.2 Getting customer’s info using an ID:

We can get complete customer data along with optional related collections like 'addresses' and 'billingAccounts' together using GET RestAPIs by passing optional 'fields' request params like below:

GET: /tmf-api/customerManagement/v1/customer/{id}?fields=billingAccount,address



Example:
http://127.0.0.1:9090/tmf-api/customerManagement/v1/customer/609d28289cc78b7ff21fce70?fields=billingAccount,address

Response:

{
   "id": "60a13f5cdd4cbf0d9898aa52",
   "href": null,
   "name": "Akshay Mishra",
   "firstName": "Akshay",
   "lastName": "Mishra",
   "primaryEmail": "akshaymishra2488@gmail.com",
   "secondaryEmail": "akshaymishra2490@gmail.com",
   "accountNumber": "04552793591374",
   "customerCategory": "RESIDENTIAL",
   "status": "Approved",
   "statusReason": "Customer created and validated.",
   "characteristic": [
       {
           "name": "membership",
           "value": "Premium"
       }
   ],
   "contactMedium": [
       {
           "id": "60a13f5cdd4cbf0d9898aa4d",
           "href": null,
           "preferred": true,
           "type": "EMAIL",
           "characteristic": [
               {
                   "name": "email",
                   "value": "akshaymishra2488@gmail.com"
               }
           ]
       }
   ],
   "address": [
       {
           "id": "60a13f5edd4cbf0d9898aa53",
           "href": null,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "country": "India",
           "stateOrProvince": "Karnataka",
           "city": "Bangalore",
           "street1": "15th A Main",
           "street2": "14th Cross",
           "postCode": "560102",
           "phoneNumber": "9789090909",
           "addressType": "HOME"
       }
   ],
   "billingAccount": [
       {
           "id": "60a13f5cdd4cbf0d9898aa4e",
           "href": null,
           "name": "BA - 80906195559569",
           "defaultBillingAccount": true,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "accountNumber": "80906195559569",
           "currency": "INR",
           "actualStartDate": "2021-05-16T21:20:52.339",
           "paymentMethod": [
               {
                   "id": "60a13f5cdd4cbf0d9898aa4f",
                   "href": null,
                   "name": "Payment Method - DEBIT_CARD",
                   "paymentMethodType": "DEBIT_CARD",
                   "defaultMethod": true,
                   "characteristic": [
                       {
                           "name": "cardNumber",
                           "value": "4145676700091200"
                       }
                   ]
               }
           ]
       },
       {
           "id": "60a13f5cdd4cbf0d9898aa50",
           "href": null,
           "name": "BA - 84341103635088",
           "defaultBillingAccount": false,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "accountNumber": "84341103635088",
           "currency": "EUR",
           "actualStartDate": "2021-05-16T21:20:52.339",
           "paymentMethod": [
               {
                   "id": "60a13f5cdd4cbf0d9898aa51",
                   "href": null,
                   "name": "Payment Method - PAYPAL",
                   "paymentMethodType": "PAYPAL",
                   "defaultMethod": true,
                   "characteristic": [
                       {
                           "name": "accountNumber",
                           "value": "700091200"
                       }
                   ]
               }
           ]
       }
   ]
}
 
 
1.3 Updating customer’s info using ID:
 
Currently the application supports PATCH/Partial update operation for 'Customer' document and Address/Billing Account partial updation and creation. 
We need to specify only those fields that we want to update on Customer or related entities as part of PATCH operation.
If a JSON object for Address/Billing Account is present with 'id' in the request, then it is considered as a PATCH operation where data is merged between json received from Rest API and entity instantiated from mongo db. 
If a JSON object  for Address/Billing Account without 'id' is received, then it is considered as a new document creation operation and new objects are inserted into corresponding collections.
Updation of 'Payment Method' under a Billing Account is not supported, only new Payment Method addition under an existing Billing Account is allowed.
Partial updation and creation of new Billing Account along with Payment Method is supported.
 
 
PATCH:   /tmf-api/customerManagement/v1/customer/{id}
 
Sample Request to update first name on customer level, updating existing characteristic of customer, adding a new characteristic, adding a new contact medium, updating existing address type, adding a new address, adding a new payment method under existing billing account, adding a new billing account along with default payment method.
 
 



Request Body:
 
{
"firstName": "Rocky",
"characteristic": [
  {
    "name": "membership",
    "value": "VIP"
  },
  {
    "name": "test",
    "value": "testing"
  }
],
"contactMedium": [
  {
    "preferred": false,
    "type": "PHONE",
    "characteristic": [
      {
        "name": "number",
        "value": "7090899067"
      }
    ]
  }
],
"address": [
  {
    "id": "60a13f5edd4cbf0d9898aa53",
    "addressType": "OFFICE"
  },
  {
    "country": "India",
    "stateOrProvince": "Uttarakhand",
    "city": "Haldwani",
    "street1": "10th Main Road",
    "street2": "ABC street",
    "postCode": "263138",
    "phoneNumber": "9765889902",
    "addressType": "HOME"
  }
],
"billingAccount": [
  {
    "id": "60a13f5cdd4cbf0d9898aa50",
    "paymentMethod": [
      {
        "paymentMethodType": "CREDIT_CARD",
        "defaultMethod": false,
        "characteristic": [
          {
            "name": "cardNumber",
            "value": "7145676705091002"
          }
        ]
      }
    ]
  },
  {
    "defaultBillingAccount": false,
    "currency": "EUR",
    "paymentMethod": [
      {
        "paymentMethodType": "UPI",
        "defaultMethod": true,
        "characteristic": [
          {
            "name": "upi",
            "value": "rocky@citibank"
          }
        ]
      }
    ]
  }
]
}
 
Response after PATCH operation:

http://127.0.0.1:9090/tmf-api/customerManagement/v1/customer/60a13f5cdd4cbf0d9898aa52
?fields=billingAccount,address

Response:


{
   "id": "60a13f5cdd4cbf0d9898aa52",
   "href": null,
   "name": "Rocky Mishra",
   "firstName": "Rocky",
   "lastName": "Mishra",
   "primaryEmail": "akshaymishra2488@gmail.com",
   "secondaryEmail": "akshaymishra2490@gmail.com",
   "accountNumber": "04552793591374",
   "customerCategory": "RESIDENTIAL",
   "status": "Approved",
   "statusReason": "Customer created and validated.",
   "characteristic": [
       {
           "name": "membership",
           "value": "VIP"
       },
       {
           "name": "test",
           "value": "testing"
       }
   ],
   "contactMedium": [
       {
           "id": "60a13f5cdd4cbf0d9898aa4d",
           "href": null,
           "preferred": true,
           "type": "EMAIL",
           "characteristic": [
               {
                   "name": "email",
                   "value": "akshaymishra2488@gmail.com"
               }
           ]
       }
   ],
   "address": [
       {
           "id": "60a13f5edd4cbf0d9898aa53",
           "href": null,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "country": "India",
           "stateOrProvince": "Karnataka",
           "city": "Bangalore",
           "street1": "15th A Main",
           "street2": "14th Cross",
           "postCode": "560102",
           "phoneNumber": "9789090909",
           "addressType": "OFFICE"
       },
       {
           "id": "60a143e37bde551330ae3d88",
           "href": null,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "country": "India",
           "stateOrProvince": "Uttarakhand",
           "city": "Haldwani",
           "street1": "10th Main Road",
           "street2": "ABC street",
           "postCode": "263138",
           "phoneNumber": "9765889902",
           "addressType": "HOME"
       }
   ],
   "billingAccount": [
       {
           "id": "60a13f5cdd4cbf0d9898aa4e",
           "href": null,
           "name": "BA - 80906195559569",
           "defaultBillingAccount": true,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "accountNumber": "80906195559569",
           "currency": "INR",
           "actualStartDate": "2021-05-16T21:20:52.339",
           "paymentMethod": [
               {
                   "id": "60a13f5cdd4cbf0d9898aa4f",
                   "href": null,
                   "name": "Payment Method - DEBIT_CARD",
                   "paymentMethodType": "DEBIT_CARD",
                   "defaultMethod": true,
                   "characteristic": [
                       {
                           "name": "cardNumber",
                           "value": "4145676700091200"
                       }
                   ]
               }
           ]
       },
       {
           "id": "60a13f5cdd4cbf0d9898aa50",
           "href": null,
           "name": "BA - 84341103635088",
           "defaultBillingAccount": false,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "accountNumber": "84341103635088",
           "currency": "EUR",
           "actualStartDate": "2021-05-16T21:20:52.339",
           "paymentMethod": [
               {
                   "id": "60a13f5cdd4cbf0d9898aa51",
                   "href": null,
                   "name": "Payment Method - PAYPAL",
                   "paymentMethodType": "PAYPAL",
                   "defaultMethod": true,
                   "characteristic": [
                       {
                           "name": "accountNumber",
                           "value": "700091200"
                       }
                   ]
               }
           ]
       },
       {
           "id": "60a143e27bde551330ae3d86",
           "href": null,
           "name": "BA - 43807018039205",
           "defaultBillingAccount": false,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "accountNumber": "43807018039205",
           "currency": "EUR",
           "actualStartDate": "2021-05-16T21:40:10.845",
           "paymentMethod": [
               {
                   "id": "60a143e27bde551330ae3d87",
                   "href": null,
                   "name": "Payment Method - UPI",
                   "paymentMethodType": "UPI",
                   "defaultMethod": true,
                   "characteristic": [
                       {
                           "name": "upi",
                           "value": "rocky@citibank"
                       }
                   ]
               }
           ]
       }
   ]
}

 

1.4 Listing all customers:

We can get a list of all customers along with optional related collections like 'addresses' and 'billingAccounts' together using GET RestAPIs by passing optional 'fields' request params like below:

GET:  /tmf-api/customerManagement/v1/customer?fields=billingAccount,address

Or 

We can just get customer details without fields=billingAccount,address

Example:  http://127.0.0.1:9090/tmf-api/customerManagement/v1/customer?fields=billingAccount,address  

Response:

[
   {
       "id": "60a13f5cdd4cbf0d9898aa52",
       "href": null,
       "name": "Rocky Mishra",
       "firstName": "Rocky",
       "lastName": "Mishra",
       "primaryEmail": "akshaymishra2488@gmail.com",
       "secondaryEmail": "akshaymishra2490@gmail.com",
       "accountNumber": "04552793591374",
       "customerCategory": "RESIDENTIAL",
       "status": "Approved",
       "statusReason": "Customer created and validated.",
       "characteristic": [
           {
               "name": "membership",
               "value": "VIP"
           },
           {
               "name": "test",
               "value": "testing"
           }
       ],
       "contactMedium": [
           {
               "id": "60a13f5cdd4cbf0d9898aa4d",
               "href": null,
               "preferred": true,
               "type": "EMAIL",
               "characteristic": [
                   {
                       "name": "email",
                       "value": "akshaymishra2488@gmail.com"
                   }
               ]
           }
       ],
       "address": [
           {
               "id": "60a13f5edd4cbf0d9898aa53",
               "href": null,
               "customerId": "60a13f5cdd4cbf0d9898aa52",
               "country": "India",
               "stateOrProvince": "Karnataka",
               "city": "Bangalore",
               "street1": "15th A Main",
               "street2": "14th Cross",
               "postCode": "560102",
               "phoneNumber": "9789090909",
               "addressType": "OFFICE"
           },
           {
               "id": "60a143e37bde551330ae3d88",
               "href": null,
               "customerId": "60a13f5cdd4cbf0d9898aa52",
               "country": "India",
               "stateOrProvince": "Uttarakhand",
               "city": "Haldwani",
               "street1": "10th Main Road",
               "street2": "ABC street",
               "postCode": "263138",
               "phoneNumber": "9765889902",
               "addressType": "HOME"
           }
       ],
       "billingAccount": [
           {
               "id": "60a13f5cdd4cbf0d9898aa4e",
               "href": null,
               "name": "BA - 80906195559569",
               "defaultBillingAccount": true,
               "customerId": "60a13f5cdd4cbf0d9898aa52",
               "accountNumber": "80906195559569",
               "currency": "INR",
               "actualStartDate": "2021-05-16T21:20:52.339",
               "paymentMethod": [
                   {
                       "id": "60a13f5cdd4cbf0d9898aa4f",
                       "href": null,
                       "name": "Payment Method - DEBIT_CARD",
                       "paymentMethodType": "DEBIT_CARD",
                       "defaultMethod": true,
                       "characteristic": [
                           {
                               "name": "cardNumber",
                               "value": "4145676700091200"
                           }
                       ]
                   }
               ]
           },
           {
               "id": "60a13f5cdd4cbf0d9898aa50",
               "href": null,
               "name": "BA - 84341103635088",
               "defaultBillingAccount": false,
               "customerId": "60a13f5cdd4cbf0d9898aa52",
               "accountNumber": "84341103635088",
               "currency": "EUR",
               "actualStartDate": "2021-05-16T21:20:52.339",
               "paymentMethod": [
                   {
                       "id": "60a13f5cdd4cbf0d9898aa51",
                       "href": null,
                       "name": "Payment Method - PAYPAL",
                       "paymentMethodType": "PAYPAL",
                       "defaultMethod": true,
                       "characteristic": [
                           {
                               "name": "accountNumber",
                               "value": "700091200"
                           }
                       ]
                   }
               ]
           },
           {
               "id": "60a143e27bde551330ae3d86",
               "href": null,
               "name": "BA - 43807018039205",
               "defaultBillingAccount": false,
               "customerId": "60a13f5cdd4cbf0d9898aa52",
               "accountNumber": "43807018039205",
               "currency": "EUR",
               "actualStartDate": "2021-05-16T21:40:10.845",
               "paymentMethod": [
                   {
                       "id": "60a143e27bde551330ae3d87",
                       "href": null,
                       "name": "Payment Method - UPI",
                       "paymentMethodType": "UPI",
                       "defaultMethod": true,
                       "characteristic": [
                           {
                               "name": "upi",
                               "value": "rocky@citibank"
                           }
                       ]
                   }
               ]
           }
       ]
   }
]
 

 
1.5 Product Offering Management Module:

For placing order we need sellable products and this module provides sample products that are inserted into mongo db while application startup. 
This module exposes only a single Rest API to list all available products.

GET:   /tmf-api/productOfferingManagement/v1/productOffering

Response: 
[
   {
       "id": "609aeded1ca8bb53dc32a1c8",
       "href": null,
       "name": "Book Romance 2",
       "sku": "BKROMT02",
       "description": "A book based on romantic stories 2.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 2,
       "launchDate": "2021-05-11T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "BB Publications"
           },
           {
               "name": "genre",
               "value": "Romance"
           }
       ]
   },
   {
       "id": "609aeded1ca8bb53dc32a1c4",
       "href": null,
       "name": "Book Historical 1",
       "sku": "BKHIST01",
       "description": "A book based on historical events.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 2,
       "launchDate": "2021-05-11T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "ABC Publications"
           },
           {
               "name": "genre",
               "value": "History"
           }
       ]
   },
   {
       "id": "609aeded1ca8bb53dc32a1c7",
       "href": null,
       "name": "Book Historical 2",
       "sku": "BKHIST02",
       "description": "A book based on historical events 2.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 5,
       "launchDate": "2021-05-11T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "ABC Publications"
           },
           {
               "name": "genre",
               "value": "History"
           }
       ]
   },
   {
       "id": "609aeded1ca8bb53dc32a1c5",
       "href": null,
       "name": "Book Romance 1",
       "sku": "BKROMT01",
       "description": "A book based on romantic stories.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 1,
       "launchDate": "2021-05-11T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "BB Publications"
           },
           {
               "name": "genre",
               "value": "Romance"
           }
       ]
   },
   {
       "id": "609aeded1ca8bb53dc32a1c3",
       "href": null,
       "name": "Book Sci-Fi 1",
       "sku": "BKSCIFI01",
       "description": "A book based on science fiction.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 3,
       "launchDate": "2021-05-11T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "Xyz Publications"
           },
           {
               "name": "genre",
               "value": "Sci-Fi"
           }
       ]
   },
   {
       "id": "609aeded1ca8bb53dc32a1c6",
       "href": null,
       "name": "Book Sci-Fi 2",
       "sku": "BKSCIFI02",
       "description": "A book based on science fiction 2.",
       "imageUrl": null,
       "status": "ACTIVE",
       "quantityPerUser": 3,
       "launchDate": "2021-06-12T02:19:46.429",
       "price": {
           "name": "One Time Charge",
           "description": "Amount charge only once from customer.",
           "priceType": "ONE_TIME",
           "recurringChargePeriod": null,
           "basePrice": 20.22,
           "taxRate": 18.05
       },
       "characteristic": [
           {
               "name": "author",
               "value": "Akshay Misra"
           },
           {
               "name": "publication",
               "value": "Xyz Publications"
           },
           {
               "name": "genre",
               "value": "Sci-Fi"
           }
       ]
   }
]
 
 
2. Order Management Module:

2.1 Create Product Order:

POST:  /tmf-api/productOrderingManagement/productOrder

Request Body:

{
 "deliveryAddressRef": "60a13f5edd4cbf0d9898aa53",
 "customerId": "60a13f5cdd4cbf0d9898aa52",
 "description": "ordering books from online store",
 "externalId": "EXT-0002",
 "notificationContact": "akshaymishra2488@gmail.com",
 "orderItem": [
   {
     "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e",
     "quantity": 3,
     "sku": "BKSCIFI02"
   },
   {
     "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e",
     "quantity": 2,
     "sku": "BKHIST01"
   }  
 ],
 "paymentMethod": {
   "characteristic": [
     {
       "name": "accountNumber",
       "value": "1100909878"
     }
   ],
   "defaultMethod": true,
   "paymentMethodType": "PAYPAL"
 },
 "priority": "1"
}
 
Response:

{
   "id": "60a1496ec0987b78426497d6",
   "href": null,
   "externalId": "EXT-0002",
   "priority": "1",
   "description": "ordering books from online store",
   "state": "ACKNOWLEDGED",
   "cancellationReason": null,
   "cancellationDescription": null,
   "customerId": "60a13f5cdd4cbf0d9898aa52",
   "orderDate": "2021-05-16T22:03:50.431",
   "completionDate": null,
   "expectedCompletionDate": "2021-05-19T22:03:50.431",
   "orderItem": [
       {
           "id": "60a1496ec0987b78426497d4",
           "href": null,
           "name": null,
           "sku": "BKSCIFI02",
           "quantity": 3,
           "itemPrice": {
               "name": "One Time Charge",
               "description": "Amount charge only once from customer.",
               "priceType": "ONE_TIME",
               "recurringChargePeriod": null,
               "taxIncludedAmount": 23.869709999999998,
               "dutyFreeAmount": 20.22,
               "taxRate": 18.05
           },
           "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e"
       },
       {
           "id": "60a1496ec0987b78426497d5",
           "href": null,
           "name": null,
           "sku": "BKHIST01",
           "quantity": 2,
           "itemPrice": {
               "name": "One Time Charge",
               "description": "Amount charge only once from customer.",
               "priceType": "ONE_TIME",
               "recurringChargePeriod": null,
               "taxIncludedAmount": 23.869709999999998,
               "dutyFreeAmount": 20.22,
               "taxRate": 18.05
           },
           "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e"
       }
   ],
   "notificationContact": "akshaymishra2488@gmail.com",
   "orderTotalPrice": 47.739419999999996,
   "channel": "ONLINE",
   "payment": {
       "id": null,
       "href": null,
       "name": "Order Payment - PAYPAL",
       "status": "AWAITING",
       "amount": 47.739419999999996,
       "type": "PAYPAL",
       "characteristic": [
           {
               "name": "accountNumber",
               "value": "1100909878"
           }
       ]
   },
   "deliveryAddress": {
       "id": "60a13f5edd4cbf0d9898aa53",
       "href": null,
       "customerId": "60a13f5cdd4cbf0d9898aa52",
       "country": "India",
       "stateOrProvince": "Karnataka",
       "city": "Bangalore",
       "street1": "15th A Main",
       "street2": "14th Cross",
       "postCode": "560102",
       "phoneNumber": "9789090909",
       "addressType": "OFFICE"
   }
}

2.2 List all orders using customer ID:

GET:   /tmf-api/productOrderingManagement/v1/productOrder/{customerId}

Example: http://127.0.0.1:9090/tmf-api/productOrderingManagement/v1/productOrder/60a13f5cdd4cbf0d9898aa52

Response:

[
   {
       "id": "60a1496ec0987b78426497d6",
       "href": null,
       "externalId": "EXT-0002",
       "priority": "1",
       "description": "ordering books from online store",
       "state": "ACKNOWLEDGED",
       "cancellationReason": null,
       "cancellationDescription": null,
       "customerId": "60a13f5cdd4cbf0d9898aa52",
       "orderDate": "2021-05-16T22:03:50.431",
       "completionDate": null,
       "expectedCompletionDate": "2021-05-19T22:03:50.431",
       "orderItem": [
           {
               "id": "60a1496ec0987b78426497d4",
               "href": null,
               "name": null,
               "sku": "BKSCIFI02",
               "quantity": 3,
               "itemPrice": {
                   "name": "One Time Charge",
                   "description": "Amount charge only once from customer.",
                   "priceType": "ONE_TIME",
                   "recurringChargePeriod": null,
                   "taxIncludedAmount": 23.869709999999998,
                   "dutyFreeAmount": 20.22,
                   "taxRate": 18.05
               },
               "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e"
           },
           {
               "id": "60a1496ec0987b78426497d5",
               "href": null,
               "name": null,
               "sku": "BKHIST01",
               "quantity": 2,
               "itemPrice": {
                   "name": "One Time Charge",
                   "description": "Amount charge only once from customer.",
                   "priceType": "ONE_TIME",
                   "recurringChargePeriod": null,
                   "taxIncludedAmount": 23.869709999999998,
                   "dutyFreeAmount": 20.22,
                   "taxRate": 18.05
               },
               "billingAccountRef": "60a13f5cdd4cbf0d9898aa4e"
           }
       ],
       "notificationContact": "akshaymishra2488@gmail.com",
       "orderTotalPrice": 47.739419999999996,
       "channel": "ONLINE",
       "payment": {
           "id": null,
           "href": null,
           "name": "Order Payment - PAYPAL",
           "status": "AWAITING",
           "amount": 47.739419999999996,
           "type": "PAYPAL",
           "characteristic": [
               {
                   "name": "accountNumber",
                   "value": "1100909878"
               }
           ]
       },
       "deliveryAddress": {
           "id": "60a13f5edd4cbf0d9898aa53",
           "href": null,
           "customerId": "60a13f5cdd4cbf0d9898aa52",
           "country": "India",
           "stateOrProvince": "Karnataka",
           "city": "Bangalore",
           "street1": "15th A Main",
           "street2": "14th Cross",
           "postCode": "560102",
           "phoneNumber": "9789090909",
           "addressType": "OFFICE"
       }
   }
]






2.3 Canceling/Deleting an order:

POST:   /tmf-api/productOrderingManagement/v1/productOrder/cancel

Request Body:

{
 "cancellationDescription": "Requested by customer",
 "cancellationReason": "CUSTOMER_INITIATED",
 "orderId": "60a1496ec0987b78426497d6"
}

Response:

Product Order with id: 60a1496ec0987b78426497d6 has been cancelled successfully.




   




     
     
     
   
