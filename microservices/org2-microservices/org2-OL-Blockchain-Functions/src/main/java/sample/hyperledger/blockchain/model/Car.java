/**
* @author  Thomas Jennings
* @since   2020-03-25
*/

package sample.hyperledger.blockchain.model;

public class Car {

		private String make;
		private String model;
		private String colour;
		private String owner;
		private String key;
		
		public Car() {
			
		}
		
		public Car(String cmake, String cmodel, String ccolour, String cowner, String ckey) {
			make = cmake;
			model = cmodel;
			colour = ccolour;
			owner = cowner;
			key = ckey;
		}
		
		public String getMake() {
			return make;
		}
		public void setMake(String make) {
			this.make = make;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getColour() {
			return colour;
		}
		public void setColour(String colour) {
			this.colour = colour;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		
}
