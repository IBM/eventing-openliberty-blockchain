/**
* @author  Thomas Jennings
* @since   2020-03-25
*/

package sample.hyperledger.blockchain.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.protos.common.Common.Header;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.ledger.rwset.Rwset;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.protos.peer.FabricProposal;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.protos.peer.FabricTransaction;
import org.hyperledger.fabric.protos.peer.FabricTransaction.ProcessedTransaction;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;

import sample.hyperledger.blockchain.model.*;

@javax.ws.rs.Path("Resources")


@ApplicationScoped
public class Resources {
	
	//set this for the location of the wallet directory and the connection json file
	static String pathRoot = "/Users/Shared/FabConnection/";
	
	 @EJB
	 private EventListener el;
	 
	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}
	
	@Timed(name = "addCarProcessingTime",
	         tags = {"method=post"},
	         absolute = true,
	         description = "Time needed to add car to the inventory")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Path("Car")
	@Operation(
			summary = "Add a car to the ledger",
			description = "Requires a unique key starting with CAR to be successfull")
	public Car addCar(
			Car aCar
			)
	{
		try {
			Path walletPath = Paths.get(pathRoot + "org-2-wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "2-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org2Admin").networkConfig(networkConfigPath).discovery(true);
			try (Gateway gateway = builder.connect()) {
				
				//get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("fabcar");
				contract.submitTransaction("createCar", aCar.getKey(), aCar.getMake(), aCar.getModel(), aCar.getColour(), aCar.getOwner());
				return new Car(aCar.getMake(), aCar.getModel(), aCar.getColour(), aCar.getOwner(), aCar.getKey());
			}
			catch (Exception e){
				System.out.println("Unable to get network/contract and execute query"); 
				throw new javax.ws.rs.ServiceUnavailableException();
			}
		} 
		catch (Exception e2) 
		{
			String current;
			try {
				current = new java.io.File( "." ).getCanonicalPath();
				System.out.println("Current working dir: "+current);
			} catch (IOException e) {
				throw new javax.ws.rs.ServiceUnavailableException();
			}
			System.out.println("Unable to find config or wallet - please check the wallet directory and connection json"); 
			throw new javax.ws.rs.ServiceUnavailableException();
		}	
	}
	
	@Timed(name = "UpdateCarProcessingTime",
	         tags = {"method=put"},
	         absolute = true,
	         description = "Time needed to update car in the inventory")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@javax.ws.rs.Path("Car")
	@Operation(
			summary = "Update owner of a car in the ledger",
			description = "Requires the key to the car and a new owner name")
	public Car updateCar(
			Car aCar
			)
	{
		try {
			Path walletPath = Paths.get(pathRoot + "org-2-wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "2-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org2Admin").networkConfig(networkConfigPath).discovery(true);
			try (Gateway gateway = builder.connect()) {
				
				//get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("fabcar");
				contract.submitTransaction("changeCarOwner", aCar.getKey(), aCar.getOwner());
				return new Car(aCar.getMake(), aCar.getModel(), aCar.getColour(), aCar.getOwner(), aCar.getKey());
			}
			catch (Exception e){
				System.out.println("Unable to get network/contract and execute query"); 
				throw new javax.ws.rs.ServiceUnavailableException();
			}
		} 
		catch (Exception e2) 
		{
			String current;
			try {
				current = new java.io.File( "." ).getCanonicalPath();
				System.out.println("Current working dir: "+current);
			} catch (IOException e) {
				throw new javax.ws.rs.ServiceUnavailableException();
			}
			System.out.println("Unable to find config or wallet - please check the wallet directory and connection json"); 
			throw new javax.ws.rs.ServiceUnavailableException();
		}	
	}
	
	
	@Timed(name = "QueryACarProcessingTime",
	         tags = {"method=GET"},
	         absolute = true,
	         description = "Time needed to query a car")
	@GET
	@javax.ws.rs.Path("Car")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary = "Returns an individual car by key",
			description = "Requires the key to be provided")
	public String Querycar(@QueryParam("Key")String Key) 
	{
	
		byte[] result = null;
		String outputString = "";
		String passedOutput = "";
		
		try {
			Path walletPath = Paths.get(pathRoot + "org-2-wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "2-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org2Admin").networkConfig(networkConfigPath).discovery(true);
			try (Gateway gateway = builder.connect()) {
				
				//get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("fabcar");
				result = contract.evaluateTransaction("queryCar", Key);
				outputString = new String(result);
				passedOutput = "Queried car Successfully. \nKey = " + Key + "\nDetails = " + outputString;
				return passedOutput;
			}
			catch (Exception e){
				System.out.println("Unable to get network/contract and execute query"); 
				throw new javax.ws.rs.ServiceUnavailableException();
			}
		} 
		catch (Exception e2) 
		{
			String current;
			try {
				current = new java.io.File( "." ).getCanonicalPath();
				System.out.println("Current working dir: "+current);
			} catch (IOException e) {
				throw new javax.ws.rs.ServiceUnavailableException();
			}
			System.out.println("Unable to find config or wallet - please check the wallet directory and connection json"); 
			throw new javax.ws.rs.ServiceUnavailableException();
		}
	}
	
	@Timed(name = "QueryCarsProcessingTime",
	         tags = {"method=GET"},
	         absolute = true,
	         description = "Time needed to query all cars")
	@GET
	@javax.ws.rs.Path("Cars")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary = "Returns all cars",
			description = "No input required")
	public String Querycar() {
		
		byte[] result = null;
		String outputString = "";
		String passedOutput = "";
		
		try {
			Path walletPath = Paths.get(pathRoot + "org-2-wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "2-Org-Local-Fabric-Org1_connection.json");
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org2Admin").networkConfig(networkConfigPath).discovery(true);
			
			try (Gateway gateway = builder.connect()) {
				
				// get the network and contract
				Network network = gateway.getNetwork("mychannel");
				Contract contract = network.getContract("fabcar");
				result = contract.evaluateTransaction("queryAllCars");
				outputString = new String(result);
				passedOutput = "Queried all Cars Successfully. \nCars are:\n " + outputString;	
				return passedOutput;
			}
			catch (Exception e){
				System.out.println("Unable to get network/contract and execute query"); 
				throw new javax.ws.rs.ServiceUnavailableException();
			}
		} 
		catch (Exception e2) 
		{
			String current;
			try {
				current = new java.io.File( "." ).getCanonicalPath();
				System.out.println("Current working dir: "+current);
			} catch (IOException e) {
				throw new javax.ws.rs.ServiceUnavailableException();
			}
			System.out.println("Unable to find config or wallet - please check the wallet directory and connection json"); 
			throw new javax.ws.rs.ServiceUnavailableException();
		}
	}
	
	@Timed(name = "QueryTransactionIdTime",
	         tags = {"method=GET"},
	         absolute = true,
	         description = "Time needed to query transactionId")
	@GET
	@javax.ws.rs.Path("TransactionId")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary = "Returns transactionId data",
			description = "TransactionId")
	public String QueryLastTransactionId() {
		return QueryLastTransactionIdResult();
	}
	
	private String QueryLastTransactionIdResult() {
		String passedOutput = "";
		
		try {
			Path walletPath = Paths.get(pathRoot + "org-2-wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "2-Org-Local-Fabric-Org1_connection.json");
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org2Admin").networkConfig(networkConfigPath).discovery(true);
			
			try (Gateway gateway = builder.connect()) {
				
				// get the network and contract
				Network network = gateway.getNetwork("mychannel");
			    Channel ch = network.getChannel();
			    TransactionInfo ti = ch.queryTransactionByID(GetLastEventId());
			    System.out.println("TransactionInfo : "+ ti.toString());
			    
			    ProcessedTransaction pt = ti.getProcessedTransaction();
			    Envelope ev = pt.getTransactionEnvelope();
			    System.out.println("Envelope created - next get payload");
			    
			    
			    try {
			        Payload payload = Payload.parseFrom(ev.getPayload());
			        FabricTransaction.Transaction transaction = FabricTransaction.Transaction.parseFrom(payload.getData());
				    FabricTransaction.TransactionAction action = transaction.getActionsList().get(0); // 0 is a index
				    FabricTransaction.ChaincodeActionPayload chaincodeActionPayload = FabricTransaction.ChaincodeActionPayload.parseFrom(action.getPayload());
				    
				    FabricProposalResponse.ProposalResponsePayload prp = FabricProposalResponse.ProposalResponsePayload.parseFrom(chaincodeActionPayload.getAction().getProposalResponsePayload());
				    //ChaincodeAction; the actions the events generated by the Chaincode
				    FabricProposal.ChaincodeAction ca = FabricProposal.ChaincodeAction.parseFrom(prp.getExtension());
				    
				    //in proposal_response_payload, the results are the Read Write Set (RWSet)
				    Rwset.TxReadWriteSet txnRWS = Rwset.TxReadWriteSet.parseFrom(ca.getResults());
				    TxReadWriteSetInfo txrwsInfo = new TxReadWriteSetInfo(txnRWS);
				    
				    //Keyvalue datamodel
				    KvRwset.KVRWSet kvRWSet = txrwsInfo.getNsRwsetInfo(0).getRwset();
				    
				    //KVWrite captures a write (update/delete) operation 
				    KvRwset.KVWrite kvWrite = kvRWSet.getWrites(0);
				    passedOutput = kvWrite.getValue().toStringUtf8();
				    
				    System.out.println(passedOutput);
				    System.out.println("There it is ........");
			      } catch (InvalidProtocolBufferException e) {
			        throw new Exception("Error creating object from ByteString", e);
			      }	
				return passedOutput;
			}
			catch (Exception e){
				System.out.println("Unable to get network/channel and retrieve the transaction data"); 
				throw new javax.ws.rs.ServiceUnavailableException();
			}
		} 
		catch (Exception e2) 
		{
			System.out.println("Unable to find config or wallet - please check the wallet directory and connection json"); 
			throw new javax.ws.rs.ServiceUnavailableException();
		}
	}
	
	@GET
	@javax.ws.rs.Path("Events")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(
			summary = "Returns events",
			description = "No input required")
	
	public String GetLastEventId() {
		
		//EventListener el = new EventListener();
		String lastEvent = el.getLastTransactionId(); 
		
		//if (lastEvent.length() == 0) {lastEvent = "None";}
		return lastEvent;
	}
	
}
