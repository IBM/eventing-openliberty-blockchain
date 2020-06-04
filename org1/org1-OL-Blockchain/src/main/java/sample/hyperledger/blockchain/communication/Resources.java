/**
* @author  Thomas Jennings
* @since   2020-03-25
*/

package sample.hyperledger.blockchain.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;

import sample.hyperledger.blockchain.model.*;

@javax.ws.rs.Path("Resources")


@ApplicationScoped
public class Resources {
	
	//set this for the location of the wallet directory and the connection json file
	static String pathRoot = "/Users/Shared/FabConnection/";
	
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
			Path walletPath = Paths.get(pathRoot + "wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "1-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org1Admin").networkConfig(networkConfigPath).discovery(true);
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
			Path walletPath = Paths.get(pathRoot + "wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "1-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org1Admin").networkConfig(networkConfigPath).discovery(true);
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
			Path walletPath = Paths.get(pathRoot + "wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "1-Org-Local-Fabric-Org1_connection.json");
			
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org1Admin").networkConfig(networkConfigPath).discovery(true);
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
			Path walletPath = Paths.get(pathRoot + "wallet");
			Wallet wallet = Wallet.createFileSystemWallet(walletPath);
			
			//load a CCP
			//expecting the connect profile json file; export the Connection Profile from the
			//fabric gateway and add to the default server location 
			Path networkConfigPath = Paths.get(pathRoot + "1-Org-Local-Fabric-Org1_connection.json");
			Gateway.Builder builder = Gateway.createBuilder();
			
			//expecting wallet directory within the default server location
			//wallet exported from Fabric wallets Org 1
			builder.identity(wallet, "org1Admin").networkConfig(networkConfigPath).discovery(true);
			
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
}
