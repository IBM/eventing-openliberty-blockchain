/**
* @author  Thomas Jennings
* @since   2020-06-10
*/

package sample.hyperledger.blockchain.communication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.protos.common.Common.Envelope;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.ledger.rwset.Rwset;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.protos.peer.FabricProposal;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.protos.peer.FabricTransaction;
import org.hyperledger.fabric.protos.peer.FabricTransaction.ProcessedTransaction;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.TransactionInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

import com.google.protobuf.InvalidProtocolBufferException;

@WebServlet(urlPatterns="/servlet")
public class EventNotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
    	//navigation to http://localhost:9081/org2-ol-blockchain/servlet
        response.getWriter().append(getHTMLResponseString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
    
    private String getHTMLResponseString()
    {	
    	StringBuilder sb = new StringBuilder("<!DOCTYPE html>");
    	
    	sb.append("<html>");
    	sb.append("<body>");
    	sb.append("<h2>Notification of new cars on the Ledger</h2>");
    	sb.append("<p id=\"content\">No data</p>");
    	sb.append("<p>This screen is refreshed every 5 seconds</p>");
    	
    	sb.append("<script>");
    	sb.append("window.onload = function() {");
    	sb.append("setInterval(function () {");
    	sb.append("httpGet()");
    	sb.append("}, 5000);");
    	sb.append("};");
    	
    	sb.append("function httpGet()");
    	sb.append("{");
    	sb.append("var xmlHttp = new XMLHttpRequest();");
    	sb.append("xmlHttp.open( \"GET\", \"http://localhost:9081/org-2-ol-blockchain/System/Resources/TransactionId\", false );");
    	sb.append("xmlHttp.send( null );");
    	sb.append("document.getElementById(\"content\").innerHTML = xmlHttp.responseText;");
    	sb.append("return xmlHttp.responseText;");
    	sb.append(" }");
    	sb.append("</script> ");	
    	sb.append("</body>");
    	sb.append("</html>");
    	
    	return sb.toString();
    }
}
