/**
* @author  Thomas Jennings
* @since   2020-06-08
*/
package sample.hyperledger.blockchain.communication;

import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;

public class CommitEventListener {
		  private final String handle;
		  private final BlockEvent blockEvent;
		  private final ChaincodeEvent chaincodeEvent;
		  
		  public CommitEventListener(String handle, BlockEvent blockEvent,
		      ChaincodeEvent chaincodeEvent) {
		    this.handle = handle;
		    this.blockEvent = blockEvent;
		    this.chaincodeEvent = chaincodeEvent;
		  }

		  /**
		   * @return the handle
		   */
		  public String getHandle() {
		    return handle;
		  }

		  /**
		   * @return the blockEvent
		   */
		  public BlockEvent getBlockEvent() {
		    return blockEvent;
		  }

		  /**
		   * @return the chaincodeEvent
		   */
		  public ChaincodeEvent getChaincodeEvent() {
		    return chaincodeEvent;
		  }
}
