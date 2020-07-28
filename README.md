In this tutorial, learn how to use Java microservices to listen for events from a distributed blockchain network using Open Liberty. Submit transactions and listen to events using Hyperledger Java SDK and Open Liberty.


<sidebar>"[Integrate Java microservices with blockchain](/tutorials/integrate-java-microservices-with-blockchain-using-hyperledger-fabric-and-open-liberty/)" (IBM Developer, May 2020): Learn about the fundamentals of blockchain and Open Liberty. Start a 1 Org blockchain network, and use Open Liberty to execute transactions to blockchain.</sidebar>

Learn specifically how to:

* Use the IBM Blockchain Platform extension to create a distributed 2 Org local blockchain network and deploy a sample smart contract called "FabCar"
* Use the Open Liberty development tools to start three Java microservices
* Transact on the blockchain network from  Org1 Java microservice
* Listen for events emitted out of Hyperledger Fabric on Org2 Java microservice
* Update the owner of a car and view the  event
* Query cars by car ID and whole ledger state

Using blockchain provides supply chains a permanent record of transactions, which are grouped in blocks that cannot be altered, creating an alternative to traditional paper tracking and manual inspection systems that can leave supply chains vulnerable to inaccuracies and fraud.

This tutorial demonstrates the scenario of a distributed blockchain network using a seller (Organization 1) and a buyer (Organization 2) in a supply chain for car sales.

The seller will add a car to the blockchain network and the buyer will be notified when a new vehicle has been added to the blockchain through an emitted event.

The buyer can listen to events in two ways:

* Querying the last event, or
* Listening to events through a web interface that automatically updates with new transaction data

## Architecture flow

<img src="images/ArchitectDiagram.png" alt="drawing">

1. The developer develops a smart contract using Java (a preconfigured "FabCar" sample).
1. Use the IBM Blockchain Platform extension for VS Code to package the decentralized "FabCar" smart contract.
1. Set up and launch the IBM Blockchain Platform 2.0 service.
1. The IBM Blockchain Platform 2.0 service enables the creation of a network in Docker containers, locally enabling installation and instantiation of the "FabCar" smart contract on the network for two organizations.
1. The seller adds a new car to the ledger using Open Liberty Organization 1.
1. The Buyer uses Open Liberty to listen for transactions, subsequently interacting with the deployed network on IBM Blockchain Platform 2.0.
1. The added car on the ledger automatically emits a transaction event, and the buyer receives said transaction.
1. The buyer can query the last event or receive automatic updates.  

## Prerequisites:

* Java
* Git
* Maven
* Docker
* VS Code
* Linux or Mac OS

## Steps

* Get the development tools. 
* Import the Open Liberty projects into VS Code.
* Import the FabCar sample smart contract project into VS Code.
* Start a 2 Org blockchain network and deploy the contract to both organizations.
* Export credentials for Org1 and Org2 to communicate with the blockchain network.
* Start Org1 and Org2 microservices.
* Add a car to the ledger as a seller from Org1.
* Query all ledger state as a buyer from Org2.
* View events from Hyperledger Fabric.
* Listen to events through a web interface.
* Update the owner of a car on the ledger from Org2.
* Query a specific car on the ledger
* Stop the Open Liberty microservices.
* Stop the Blockchain Network.

## 1 Get the development tools

1. If you have not already, download and [Install version 1.38 Visual Studio Code]((https://code.visualstudio.com/download).

1. Install the [IBM Blockchain Platform extension for VS Code](https://marketplace.visualstudio.com/items?itemName=IBMBlockchain.ibm-blockchain-platform).

   After installation, if any additional prerequisites are needed, the extension will guide you through installing them. Make sure you pick up the Docker prerequisites, as they will be used to create your Fabric network.

1. Install the [Open Liberty Tools for VS Code](https://marketplace.visualstudio.com/items?itemName=Open-Liberty.liberty-dev-vscode-ext).

## 2 Import the Open Liberty projects into VS Code

As we are demonstrating an event-driven microservice-architecture there are three Open Liberty Java microservices starting up. Org1-Functions will transact with the blockchain network for the seller, Org2-Functions will transact with the blockchain network for the buyer, and Org2-Events will listen to events for the buyer. 

1. Open a terminal window and clone the [sample project](https://github.com/IBM/eventing-openliberty-blockchain) in GitHub:
<!-- SFD: update GH URLs when moved to IBM instance -->

    `git clone https://github.com/IBM/eventing-openliberty-blockchain.git`


1. To add Org1-Functions project to VS Code, select **File** > **Open** > **eventing-openliberty-blockchain/microservices/org1-microservice/org1-OL-Blockchain-Functions**, and then click **Open**.

  This will add the Org1 project to the workspace and will automatically add `Liberty Dev Dashboard` into the VS Code extension. To display `org1-ol-blockchain-functions`, you can click `Liberty Dev Dashboard`.

1. Select **File** > **New Window** to open a new VS Code window to add the Org2-Functions project.

1. To import the Org2 project, select **File** > **Open** > **eventing-openliberty-blockchain/microservices/org2-microservices/org2-OL-Blockchain-Functions**, and then click **Open**.

   Open the `Liberty Dev Dashboard` to view the `org2-ol-blockchain-functions`.
   
1. Select **File** > **New Window** to open a new VS Code window to add the Org2-Events project.

1. To import the Org2-Events project, select **File** > **Open** > **eventing-openliberty-blockchain/microservices/org2-microservices/org2-OL-Blockchain-Events**, and then click **Open**.

   To display `org2-ol-blockchain-events`, you can click `Liberty Dev Dashboard`. 

## 3 Import the FabCar sample smart contract project into VS Code

1. Click the IBM Blockchain Platform icon in the top right corner (looks like a square).

   <img src="images/blockchainlogo.png" alt="drawing">

    It may take a moment. In the purple bar at the bottom, it will say, "Activating extension."

1. Select **Sample Code: FabCar** from the "Other Resources" section.

1. Click the **Clone** button to git clone the sample code for the FabCar sample, and choose a convenient location to clone the fabric sample.

1. Select **Clone**.

   <img src="images/Fabcarsample-repo.png" alt="drawing">

1.  From the list of options choose, **FabCar v1.0.0 Java**.

1. Click **Open Locally**.

   <img src="images/openlocally.png" alt="drawing">

1. In the Command Palette, click **Add to workspace**.

1. *Optional*: Click the **File explorer** button in the top left, and you will see `fabcar-contract-java`, which is the smart contract project.

1. Click the IBM Blockchain Platform icon on the left side to navigate back to the IBM Blockchain Platform extension for VS Code.

## 4 Start a 2 Org blockchain network and deploy the contract

1. Within **FABRIC ENVIRONMENTS**, select the add (+) icon to create a customized blockchain network.

1. Select **Create new from template**, using a template blockchain network structure.

1. As we are creating a 2 Org network, select **2 Org template (2CAs, 2 peers, 1 channel)**.

   <img src="images/2OrgTemplate.png" alt="drawing">

1. Name the environment `2 Org Local Fabric`.

   It will take 2 - 5 minutes to start a new local environment. Once successful, it will display "Successfully added a new environment."

1. Click on **2 Org Local Fabric** to connect to the local fabric runtime. Once you're connected to the "2 Org Local Fabric" environment, select **Smart Contracts** > **Instantiated**, **+ Instantiate**.

1. Choose **fabcar-contract-java Open Project** (at the Command Palette prompt).

1. When prompted to "Enter a name for your Java Package," enter `fabcar`, and press **Enter**.

1. When prompted to "Enter a version for your Java package," enter `2.0.0`.

1. Select all the peers to install the smart contract onto, and press **OK**

   In a supply chain environment, the smart contract is a self-enforcing agreement between parties. If all parties agree who are bound into that smart contract, data can be added to the blockchain.

   In our example, the buyer and the seller are both agreeing to the terms and conditions of the smart contract.

   <img src="images/all-peer-installation.png" alt="drawing">

1. When "Optional functions" appears, enter `initLedger`. This initializes the ledger with unique cars. Not entering the function will result in the blockchain network being empty.

   <img src="images/initLedger-function.png" alt="drawing">

1. For all other "Optional functions," press **Enter** to skip.

1. Select **No** when asked, "Do you want to provide a private data collection configuration file?" You do not need any private data configuration files.

   The notification window at the bottom left will say, "IBM Blockchain Platform extension: Instantiating Smart Contract." It will take approximately 2 - 5 minutes to instantiate the smart contract.

## 5 Export credentials to communicate with the blockchain network

For Open Liberty to communicate to the blockchain network, Hyperledger Fabric has security features that stop applications attempting to make transactions unless you have the specific profiles and certificate authorities.

1. Export the Local Fabric Gateways:

   1. In the "FABRIC GATEWAYS" panel, select **2 Org Local Fabric**.

      As there are multiple organizations, we need to connect as admin to both `Org1` and `Org2` to export the connection profiles.  

   1. Select **Org1**.

   1. "Choose an identity to connect with" will appear from the command palette. Select **admin**.

      <img src="images/identity-admin.png">

   1. Hover over the **FABRIC GATEWAYS** heading, and click **...** > **Export connection profile**.

      <img src="images/export-fab-gateway.png" alt="drawing">

   1. The `finder` window will open.

   1. Navigate to `Users/Shared/`.

   1. Create a new folder `FabConnection`.

      The full path directory should be `Users/Shared/FabConnection`.

   1. Save the `.json` file as `2-Org-Local-Fabric-Org1_connection.json`.

   1. Disconnect from Org1 Fabric gateway.

      <img src="images/exit-door.png" alt="drawing">

   1. Select **Org2**.

   1. "Choose an identity to connect with" will appear from the command palette. Select **admin**.

   1. Hover over the **FABRIC GATEWAYS** heading, and click **...** > **Export connection profile**.

   1. The `finder` window will open.

   1. Navigate to `Users/Shared/FabConnection`.

   1.  Save the `.json` file as `2-Org-Local-Fabric-Org2_connection.json`.

1. Export the Fabric Wallets:

   1. In the "FABRIC WALLETS" panel, select **2 Org Local Fabric**, then right-click **Org1**, and select **Export Wallet**.

      <img src="images/export-org1.png" alt="drawing">

   1. Save the folder as `org-1-wallet` in the `/Users/Shared/FabConnection/` directory.

   1. To export the Org2 wallet, select **Org2**, then right-click, and select **Export Wallet**.

   1. Save the folder as `org-2-wallet` in the `/Users/Shared/FabConnection/` directory.

## 6 Start Org 1 and Org 2 microservices

1. You will have three VS Code windows open. Navigate back to the VS Code window with Org1's Java microservice. As we installed the `Dev Tool` for Open Liberty, click the Liberty Dev Dashboard icon, and the extension will display the project: `org-1-ol-blockchain-functions`.

1. Right-click **org1-ol-blockchain-functions**, and select **Start**.

   This will quickly start up the microservice within 2 – 5 seconds.

   org-1-ol-blockchain-functions is now running on port 9080.

1. Navigate to the other VS Code windows and click the Liberty Dev Dashboard icon, and the extension will display the project. 

    Right-click **org2-ol-blockchain-events**, and select **Start**.

    org2-ol-blockchain-events is now running on port 9081.

    Right-click **org2-ol-blockchain-functions**, and select **Start**.
    
    org2-ol-blockchain-functions is now running on port 9082.

## 7 Add a car to the ledger as a seller

As there are two organizations, we are going to test submitting a transaction from the seller and then view the updated ledger as the buyer.

1. Navigate to the seller's Java microservice on port 9080:

   `http://localhost:9080/openapi/ui/`

1. Navigate to **POST /System/Resources/Car Add a car to the ledger**.

1. Click **Try it out**.

1. Fill in the example schema with the following values:

   ```
   {
     "make": "VW",
     "model": "Golf",
     "colour": "White",
     "owner": "Tom J",
     "key": "CAR20"
   }
   ```

## 8 Query all ledger state as a buyer

A buyer may be interested in purchasing a used car and will query all cars on the blockchain.

1. Navigate to the buyer's Java functions-microservice on port 9082:

   `http://localhost:9082/openapi/ui/`

1. Open a new tab and navigate to **GET /System/Resources/Cars Returns all cars** > **Try it out** > **Execute.**

   This will send a request to the ledger and return all the cars.

   A successful response should look like:

   ```
   [
    {
      "owner": "Tomoko",
      "color": "blue",
      "model": "Prius",
      "make": "Toyota"
    },
    {
      "owner": "Brad",
      "color": "red",
      "model": "Mustang",
      "make": "Ford"
    },
    ...
    {
     "owner": "Tom",
     "color": "White",
     "model": "VW",
     "make": "Golf"
    }
   ]
   ```

## 9 View events from Hyperledger Fabric

The following diagram illustrates how Open Liberty is listening to events from Hyperledger Fabric locally.

<img src="images/events-diagram.png" alt="Event listening diagram">

1. The seller submits a transaction, and the Java microservice processes the car and adds it to the blockchain network.

   The vehicle can be queried by any organization, as demonstrated in [Step 8](#8-query-all-ledger-state-as-a-buyer) of the tutorial instructions.

1. When the buyer or seller submits a transaction to the Ledger, an event is emitted from Hyperledger Fabric. Organization 1 does not have the configuration to listen for the said event as the seller is not interested in being notified of the event.

1. Organization 2 has the configuration to listen to the events, resulting in the organization receiving the event triggered by Org1.

1. This is an event-driven architecture between Organization 1 and Organization 2. Organization 2 automatically receives events emitted out of Hyperledger Fabric and displays these through a servlet (5).

### Submit a new transaction

1. Navigate to the seller's Java microservice on port 9080:

   `http://localhost:9080/openapi/ui`

1. Navigate to **POST /System/Resources/Car Add a car to the ledger**.

1. Fill in the example schema with the following values:

    ```
    {
     "make": "Audi",
     "model": "A6",
     "colour": "Black",
     "owner": "David J",
      "key": "CAR21"
    }
    ```

1. Org2 automatically receives the event data from listening for the event.

1.  Navigate to the buyer's Java microservice on port 9081:

   `http://localhost:9081/openapi/ui`

1. Navigate to **GET /System/Resources/TransactionId Returns transactionId data**.

1. View the contents of the event that has been collected.

   ![](images/viewtransactiondata.gif)

1. Optional: Fill out the example schema with your own cars and see the recent event of your own cars.

1. Navigate to **GET /System/Resources/Events Returns events**.

   ![](images/transactionid.gif)

   Every transaction submitted to Hyperledger Fabric has a unique transaction id. Selecting **Execute** returns the unique transactionid data.

### Listen to events through a web interface

Open another tab in the web browser of your choice and experience the event-based driven architecture of listening to events.

1. Navigate to:

   `http://localhost:9081/org-2-ol-blockchain-events/servlet`

   The Servlet on Org2 is automatically listening to events out of Hyperledger Fabric using a [servlet](https://openliberty.io/guides/maven-intro.html#creating-the-project-pom-file).

   ![](images/transaction-event.gif)

1. Navigate to Org1 and add a car to the ledger: **Post /System/Resources/Car Add a car to the ledger**.

1. Click **Try it out**.

1. Fill in the example schema with the following values, as illustrated in the above figure:

   ```
   {
     "make": "Ford",
     "model": "Fiesta",
     "colour": "Blue",
     "owner": "Hannah J",
     "key": "CAR22"
   }
   ```

   The event appears successfully on the buyers window, showing the latest transaction that has been added to the ledger. 
   The window refreshes every five seconds that is listening for a new event, however the event may take a couple of seconds longer to appear.

Try it out by adding more cars to the ledger and viewing the events out of Hyperledger fabric.

## 10. Update the owner of the car

The buyer on Organization 2 may decide that they want to buy the car from the seller.

1. Navigate to Org2:

  `http://localhost:9082/openapi/ui/`
  
Update the owner of a car: **PUT /System/Resources/Car Update the owner of a car in the ledger**.

1. Click **Try it out**.

1. Fill in the example schema with the following values:

   ```
   {
     "make": "string",
     "model": "string",
     "colour": "string",
     "owner": "Yasmin A",
     "key": "CAR22"
   }
   ```

## 11. Query a specific car on the ledger

As the blockchain is distributed, you can query the specific car from any organization. However, as the buyer has agreed to buy the car, query it from Org2:

   `http://localhost:9082/openapi/ui/`

As well as a unique transaction id, there is a unique key for every car. The difference between a transaction id and the key is that every time a transaction is made, even with the same key, the transaction id changes; they key does not.

1. In the OpenAPI UI, select **GET /System/Resources/Car Returns an individual car by key**.

1. Re-query CAR22 to see the updated owner.

   Query the recently added car by inserting the ID **CAR22** and clicking Execute.

   The successful response should look like:

   ```
   {
     "owner": "Yasmin A",
     "color": "Blue",
     "model": "Fiesta",
     "make": "Ford"
   }
   ```

## 12. Stop the Open Liberty microservices

Once you have finished, for both organizations go to VS Code > Liberty Dev Dashboard, and press **Stop**. This will stop the Open Liberty server.

Now, the server is off and the application is not running anymore. If you tried to hit one of the endpoints, it would not find it.

## 13. Stop the blockchain network

*Optional*: You can stop the blockchain network, and save the state on the ledger if you decide to come back to it later. Click on the IBM Blockchain Platform icon on the left side. On Fabric Environments, click **...** > **Stop Fabric Environment**.

1. You can easily start it again by clicking **2 Org Local Fabric**.

To remove the Docker images where it is running, on Fabric Environments, click **...** > **Teardown Fabric Environment**.

## Conclusion

Well done. You have created a 2 Org network, where different organizations submit transactions to the blockchain: adding, updating, and querying, as well as listening to events from a blockchain using the Hyperledger Java SDK and Open Liberty.
