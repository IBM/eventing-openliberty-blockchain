# Listen to events out of a distributed blockchain network using Open Liberty


> 2 Org Hyperledger Fabric sample using Open Liberty to execute transactions and listen to events with IBM Blockchain Platform


In this tutorial, you will learn to:

* Use the IBM Blockchain Platform extension to create a fully distributed 2 Org local blockchain network and deploy a sample smart contract (based on cars)
* Use the [Open Liberty](https://openliberty.io) extension to start two web servers that can communicate with your blockchain network
* Transact on the blockchain network from one web server via REST APIs to sell cars and listen for events emitted out of Hyperledger fabric on the buyer organization. 

Learn about the fundamentals of blockchain and Open Liberty by following the [Integrate Java microservices with blockchain using Hyperledger Fabric and Open Liberty](https://developer.ibm.com/tutorials/integrate-java-microservices-with-blockchain-using-hyperledger-fabric-and-open-liberty/) to experience starting a 1 Org blockchain network and an Open Liberty server to execute transactions to blockchain. Also, follow this to follow the instructions on installing the IBM Blockchain platform VS Code extension.

You will use a fully distributed 2 Org blockchain network. Leveraging a distributed network and Open Liberty to submit transactions and listen to events.

Using blockchain provides supply chains a permanent record of transactions which are grouped in blocks that cannot be altered, creating an alternative to traditional paper tracking and manual inspection systems, which can leave supply chains vulnerable to inaccuracies and fraud.

In our example we are creating a distributed blockchain network using a buyer and a seller in a supply chain for car sales, based on the fabcar sample.

The seller (Org 1) can add a car to the blockchain network, which will emit an event notifying other organisations an event has occured on the blockchain.
The buyer (Org 2) becomes notified that a new car has been added to the blockchain network. The buyer will be notified through Open Liberty and see the new car which is up for sale, with a transaction ID. If the buyer is looking for a new car,they can query the car which has been added to the blockchain network. 

## Architecture flow

 <img src="images/ArchitectDiagram.jpg" alt="drawing">

## Prerequisites:

* Java
* Git
* Maven
* Docker
* VS Code

## Steps

* Import the Open Liberty projects into VS Code

* Import the "fabcar" sample smart contract project into VS Code

* Start a 2 Org blockchain network and deploy the contract to both organisations

* Export credentials for Org1 and Org2 to communicate with the blockchain network

* Startup the buyer and seller Open Liberty servers.

* Add a car to the ledger from Org1.

* Look at the events out of hyperledger fabric.

* Update the owner of a Car on the Ledger

* Optional - View Open Liberty Metrics

* Stop the Open Liberty server

* Stop the Blockchain Network

* Finished


## 1. Get the development tools

1. If you have not already, download and [Install Visual Studio Code.]((https://code.visualstudio.com/download) )

1.  Install the [IBM Blockchain Platform extension for VS Code.](https://marketplace.visualstudio.com/items?itemName=IBMBlockchain.ibm-blockchain-platform)

    After installation, if any additional prerequisites are needed, the extension will guide you through installing them. Make sure you pick up the Docker prerequisites, as they will be used to create your Fabric network.

1. Install the [Open Liberty Tools for VS Code.](https://marketplace.visualstudio.com/items?itemName=Open-Liberty.liberty-dev-vscode-ext)

## 2. Import the Open Liberty projects into VS Code

1. Add the Org1 project to VS Code, select **File** > **Open** > **eventing/org1**, and then click **Open**.

    This will add the Org1 project to the workspace and will automatically add `Liberty Dev Dashboard` into the VS Code extension. Clicking on the `Liberty Dev Dashboard` will display `org1-ol-blockchain`

1. Open a new VS Code window to add the Org2 project **File** > **New Window**. Import the Org2 project **File** > **Open** > **eventing/org2**, and then click **Open**.

    Adding the Org2 project is what makes the blockchain network dectralized as it will add multiple organizations. Open the `Liberty Dev Dashboard` to view the `org2-ol-blockchain`.

## 3. Import the FabCar sample smart contract project into VS Code

1. Click the IBM Blockchain Platform icon in the top right corner (looks like a square).

   <img src="images/blockchainlogo.png" alt="drawing">

    It may take a moment. In the purple bar at the bottom, it will say, "Activating extension."

1. Select **FabCar** from the "Explore sample code" section.

1. Click the **Clone** button to git clone the sample code for the FabCar sample, and choose a convenient location to clone the fabric sample.

1. Select **Clone**. 

   <img src="images/Fabcarsample-repo.png" alt="drawing">

1.  From the list of options choose, **FabCar v1.0.0 Java**.

1. Click **Open Locally**.

   <img src="images/openlocally.png" alt="drawing">

1. In the Command Palette, click **Add to workspace**.

1. *Optional*: Click the **File explorer** button in the top left, and you will see `fabcar-contract-java`, which is the project to create the blockchain network.

1. Click the IBM Blockchain Platform icon on the left side to navigate back to the IBM Blockchain Platform extension for VS Code.

## 4. Start a 2 Org blockchain network and deploy the contract

1. Within **FABRIC ENVIRONMENTS**, select the ` + ` icon to create a customized blockchain network.

1. Select `Create  new from template`, using a template blockchain network structure created by IBM Blockchain platform team

1. As we are creating a **2 Org network** select **2 Org template (2CAs, 2 peers, 1 channel)**

    <img src="images/2OrgTemplate.png" alt="drawing">

1. Name the Environment `2 Org Local Fabric`

1. Once you're connected to the "2 Org Local Fabric" environment (this happens automatically after it has started), under **Smart Contracts** > **Instantiated**, click **+Instantiate**.`

1. Choose **fabcar-contract-java Open Project** (at the Command Palette prompt).

1. When prompted to `Enter a name for your Java Package`, enter `fabcar`, and press **Enter**.

1. When prompted to `Enter a version for your Java package`, enter `2.0.0`.

1. Select all the Peers to install the smart contract onto and press **OK**
    
    In a supply chain environment the smart contract is a self-enforcing agreement between parties. If all parties agree who are bound into that smart contract data can be added to the blockchain.

    In our example the buyer and the seller are both agreeing to the terms and conditions of the smart contract. 

  <img src="images/all-peer-installation.png" alt="drawing">

  1. When "Optional functions" appears, enter `initLedger`. This initializes the ledger with unique cars. Not entering the function will result in the blockchain network being empty.

   <img src="images/initLedger-function.png" alt="drawing">

1. For all other "Optional functions", press **Enter** to skip.

1. When asked, `Do you want to provide a private data collection configuration file?`, select `No`, as you do not need any private data configuration files.

The notification window at the bottom left will say, `IBM Blockchain Platform extension: Instantiating Smart Contract`. It will take approximately 2 - 5 minutes to instantiate the smart contract.

## 5. Export credentials to communicate with the blockchain network

For Open Liberty to communicate to the blockchain network, Hyperledger Fabric has security features that stop applications attempting to make transactions unless you have the specific profiles and certificate authorities. 

1. Export the Local Fabric Gateways:

   1. In the "FABRIC GATEWAYS" panel, select `2 Org Local Fabric`.

        As there are multiple organizations, we need to connect to both `Org1` and `Org2` as admin to export the connection profiles.  

   1. Select `Org1` and "Choose an identity to connect with" will appear from the command palette. Select **admin**.

      <img src="images/identity-admin.png" alt="drawing" width="500" height="180">

   1. Hover over the **FABRIC GATEWAYS** heading, click **...** > **Export connection profile**.

      <img src="images/export-fab-gateway.png" alt="drawing">

   1. The `finder` window will open.

   1. Navigate to `Users/Shared/`.

   1. Create a new folder `FabConnection`.

      The full path directory should be `Users/Shared/FabConnection`.

   1. Save the `.json` file as `2-Org-Local-Fabric-Org1_connection.json`.

   1. To disconnect from Org1 Fabric gateway press the "door" icon 

    <img src="images/exit-door.png" alt="drawing">

  1. 


1. Export the Fabric Wallets:

   1. In the "FABRIC WALLETS" panel, select **1 Org Local Fabric**, then right-click **Org1**, and select **Export Wallet**.

      <img src="images/export-org1.png" alt="drawing">

   1. Save the folder as `wallet` in the `/Users/Shared/FabConnection/` directory.


## 6. Start Org 1 and Org 2 Open Liberty Servers

1. You will have two VS Code widows open. As we installed the Dev Tool for Open Liberty, click the Liberty Dev Dashboard icon, and the extension will display the project: org-1-ol-blockchain.

1. Right-click org1-ol-blockchain, and select Start.

    This will quickly start up the application server up — usually within 2 – 5 seconds.

    Org1 is now running on port 9080.

1. Navigate to the other VS Code window,click the Liberty Dev Dashboard icon, and the extension will display the project: org-2-ol-blockchain.

    Right-click org2-ol-blockchain, and select Start.

    Org2 is now running on port 9081.

## 7. Add a car to the ledger

As there are two organisations, we are going to test create a transaction from the seller and view the updated ledger for the buyer

From a seller persepective, add a car to the ledger. 

1. Navigate to `http://localhost:9080/openapi/ui`


1. Navigate to **POST /System/Resources/Car Add a car to the ledger**.

1. Click **Try it out**.

1. Fill in the example schema with the following values, as illustrated in the figure:

   ```
   {
     "make": "VW",
     "model": "Golf",
     "colour": "White",
     "owner": "Tom Jennings"
     "key": "CAR20"
   }
   ```

## 8. Query all ledger state as a buyer

As an interested party who is on the blockchain, such as national car dealers, may be interested in buying a used car. For example a 21 year old boy may want to buy a VW Golf.

1. Navigate to the buyer on `http://localhost:9081/openapi/ui`

    In reality, the blockchain network will be deployed on the IBM Cloud Blockchain Platform, with the Java microservices deployed to Kubernetes, whereby multiple clients will be on different systems nationally. 

1. Navigate to **GET /System/Resources/Cars Returns all cars > Try it out > Execute**.

It will send a request to the Ledger and return back all cars.

The successful response should look like

```
Queried all Cars Successfully.
Cars:
[{"make":"VW","model":"Golf","colour":"White","owner":"Tom"}}]
```

## 9. Events out of Hyperledger Fabric

Hyperledger Fabric has the event logic preconfigured. When a transaction is submitted the event logic is triggered, however the client is not always listening to events. 

The diagram illustrates how Open Liberty is listening to events from Hyperledger Fabric locally. 

 <img src="images/events-diagram.png" alt="drawing">

1. The buyer submits a transaction through as a REST request. The car is processed by the Java microserivce, and adds it to the blockchian network. The car can be queried by any organsiation, as demonstrated in step 7.

2. When the transaction is processed, an event is emitted out of Hyperledger Fabric. Organisation One however is not configured to listen to the events as the Seller is not interested in listening to events of the car that has just been added. 

3. For Organisation two, the same event is emmited, however the Open Liberty is configured to listen to the events, resulting in the event apprearing.

4. Organisation two can manually ask for the recent events that are emitted out of Hyperledger Fabric, through the OpenAPI User Interface. This manually executes the methods to retrieve the events of the cars being added.

5. Organisation two however can automatically get updates of the events emitted out of Hyperledger Fabric. The methods surrounding eventing are automatically executing, resulting in Open Liberty constanstly listening for new events out of Hyperledger Fabric. The web browser is refreshing every three seconds so the the buyer is always recieving automatic updates of cars that are added to the Ledger.

1.  Navigate to `http://localhost:9080/openapi/ui` 

    To add a car to the ledger

    ![](images/gifs/cargif.gif)

1. Navigate to **POST /System/Resources/Car Add a car to the ledger**.

    Fill in the example Schema

    ```
    {
     "make": "Range Rover",
     "model": "Sport",
     "colour": "Gunmetal Grey",
     "owner": "David J",
      "key": "CAR20"
    }
    ```



1.  Navigate to `http://localhost:9081/openapi/ui` 

    An event has been triggered out of Hyperledger Fabric


1. Navigate to **GET /System/Resources/TransactionId Returns transactionId data**.

    View the contents of the recently added car. This is the manual way of listening to the events, as shown in step 4 of the diagram. 

    ![](images/gifs/viewtransactiondata.gif)

    Fill out the example schema with your own cars and see the event.
 
1. Navigate to **GET /System/Resources/Events Returns events**.

    ![](images/gifs/transactionid.gif)

    Every transaction submitted to Hyperledger Fabric has a unique transaction id. When pressing execute, it returns the unique transactionid data.

    The transactionid is unique every time an event is emitted.

### Listen to Events automatically from Hyperledger Fabric

Open another tab on the web browser of your choice

1. Navigate to **http://localhost:9081/ol-blockchain/servlet**

    The Servlet is on Org2 listening to events automatically out of Hyperledger Fabric. This is done through a [servlet.](https://openliberty.io/guides/maven-intro.html#creating-the-project-pom-file)  

    ![](images/gifs/transaction-event.gif)

1. Navigate to Org1 and add a car to the ledger **Post /System/Resources/Car Add a car to the ledger.**

1. Click **Try it out.**

1. Fill in the example schema with the following values, as illustrated in the figure:

```
 {
  "make": "Ford",
  "model": "Fiesta",
  "colour": "Blue",
  "owner": "Hannah J",
  "key": "CAR23"
}
```

The event appears successfully on the buyers window, showing the latest transaction that has been added to the ledger.

Try it out by adding more cars to the ledger. 

## 10. Update the owner of the car

The buyer on Org2 may decide they want to buy the car from the seller. 

In bitcoin, there is the concept of 'bitcoin miners'. The miners do three main jobs; issuance of new bitcoins, security and confirming transactions. A transaction can only be added to the blockchain once it has been verified by the miners. The more miners that agree that the transaction is legitamate, the better it is for larger payments. In return miners get paid.

This is a classic example of all parties agreeing to adding a block to the chain, moving the virtual money to the ownership of someone else.

It is the same in supply chain networks, all parties agree of adding a block to the chain. In hyperledger fabric these are called Smart Contracts. 

1. Navigate to Org2 to update the owner of a car **PUT /System/Resources/Car Update the owner of a car in the ledger**

1. Click **Try it out**

1. Fill in the example Schema


```
{
  "make": "string",
  "model": "string",
  "colour": "string",
  "owner": "Yasmin A",
  "key": "CAR23"
}
```

1. Re-query CAR23 to see the updated owner.

## 11. Stop the Open Liberty servers

Once you have finished, for both organsiations go to VS Code > Liberty Dev Dashboard, and press **Stop**. This will stop the Open Liberty server. 

Now, the servers is off and the application is not running anymore. If you tried to hit one of the endpoints, it would not find it.

## 12. Tear down the blockchain network

*Optional*: You can stop the blockchain network, and save the state on the ledger if you decide to come back to it later. Click on the IBM Blockchain Platform icon on the left side. On Fabric Environments, click **...** > **Stop Fabric Environment**. 

1. You can easily start it again by clicking `1 Org Local Fabric`. 

To remove the Docker images where it is running, on Fabric Environments click **...** > **Teardown Fabric Environment**.

## Conclusion

Well done. You have created a fully distributed 2 Org network, submitted transactions to the blockchain and listened to events from the distributed network. As well as updating the owner of a car based off events. 


