package demo;

import org.apache.camel.Exchange;
import org.fusesource.camel.component.sap.SAPEndpoint;
import org.fusesource.camel.component.sap.model.rfc.Structure;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

public class CreateBapiFlcustGetlistRequest implements InitializingBean {
	private String sapEndpoint;

	public String getSapEndpoint() {
		return sapEndpoint;
	}

	public void setSapEndpoint(String sapEndpoint) {
		this.sapEndpoint = sapEndpoint;
	}

	public void create(Exchange exchange) throws Exception {
		String search = exchange.getIn().getHeader("search", String.class);
		// Get SAP Endpoint to be called from context.
		SAPEndpoint endpoint = exchange.getContext().getEndpoint(getSapEndpoint(), SAPEndpoint.class);
		// Create SAP Request object from target endpoint.
		Structure request = endpoint.getRequest();
		// filter for customers whose name starts with S
		request.put("CUSTOMER_NAME", (search == null) ? "" : search);
		request.put("MAX_ROWS", 100);
		// Put request object into body of exchange message.
		exchange.getIn().setBody(request);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (sapEndpoint == null || "".equals(sapEndpoint))
			throw new BeanInitializationException("no endpoint set!");
	}
}
