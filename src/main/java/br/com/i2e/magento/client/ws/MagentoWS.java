package br.com.i2e.magento.client.ws;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import br.com.i2e.magento.common.ws.model.CatalogProductCreateEntity;
import br.com.i2e.magento.common.ws.model.CatalogProductCreateRequestParam;
import br.com.i2e.magento.common.ws.model.CatalogProductCreateResponseParam;
import br.com.i2e.magento.common.ws.model.CatalogProductEntity;
import br.com.i2e.magento.common.ws.model.CatalogProductListRequestParam;
import br.com.i2e.magento.common.ws.model.CatalogProductListResponseParam;
import br.com.i2e.magento.common.ws.model.LoginParam;
import br.com.i2e.magento.common.ws.model.LoginResponseParam;
import br.com.i2e.magento.common.ws.model.StartSessionResponseParam;

public class MagentoWS extends WebServiceGatewaySupport  {
	private static final Logger logger = LoggerFactory.getLogger( MagentoWS.class ); 
	private String magentoUser;
	private String magentoApiKey;
	private String currentStore;
	
	private String currentSession;
	
	public MagentoWS( String magentoUser, String magentoApiKey, String currentStore ) {
		logger.info( "Creating magento client: {} - {}", magentoUser, magentoApiKey );
		this.magentoUser = magentoUser;
		this.magentoApiKey = magentoApiKey;
		this.currentStore = currentStore;
	}

	public String getSession() {
		
		if ( currentSession == null ) {
			
			LoginParam login = new LoginParam();
			login.setApiKey( magentoApiKey );//"2ad635ec127b" );
			login.setUsername( magentoUser );//"erp" );
			
			LoginResponseParam response = (LoginResponseParam) getWebServiceTemplate().marshalSendAndReceive( login );
			currentSession = response.getResult();
			System.out.println( "current session: " + currentSession );
		}
		
		return currentSession;
	}
	
	public List<CatalogProductEntity> getProuctList( ) {
		var request =  new CatalogProductListRequestParam();
		request.setStore( currentStore );
		request.setSessionId( getSession() );
		CatalogProductListResponseParam response = (CatalogProductListResponseParam) getWebServiceTemplate()
				.marshalSendAndReceive( request );
		
		return response.getResult().getComplexObjectArray();
	}
	
	public void createProduct( CatalogProductCreateRequestParam request) {
		request.setStore( currentStore );
		request.setSessionId( getSession() );
		logger.info( "Produto enviado para o Magento: {} session:{} nome:{} set:{} ", request, request.getSessionId(), request.getProductData().getName(), request.getSet() );
		var response = (CatalogProductCreateResponseParam) getWebServiceTemplate().
				marshalSendAndReceive( request, new WebServiceMessageCallback() {
					
					public void doWithMessage( WebServiceMessage message ) throws IOException, TransformerException {
						
						var m =(javax.xml.transform.dom.DOMSource) message.getPayloadSource();
						StringWriter writer = new StringWriter();
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.transform(new DOMSource(m.getNode()), new StreamResult(writer));
						String xml = writer.toString();
						logger.info( "Response:: {} ",  xml);
					}
				} );
		
		logger.info( "Produto enviado para o Magento: {}  == {} ", request, response.getResult() );
	}
}
