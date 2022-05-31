package br.com.i2e.magento.client.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

@Configuration
public class MagentoClientConfig {
	@Value( "${magento.ws.user}" )
	private String magentoUser;
	@Value( "${magento.ws.api.key}" )
	private String magentoApiKey;
	@Value( "${magento.ws.url}" )
	private String magentoURL;
	@Value( "${magento.ws.current.store}" )
	private String magentoCurrentStore;
	

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath( "br.com.i2e.magento.common.ws.model" );
		return marshaller;
	}

	@Bean
	public MagentoWS magentoWS( Jaxb2Marshaller marshaller ) {
		MagentoWS client = new MagentoWS( magentoUser, magentoApiKey, magentoCurrentStore );
		client.setDefaultUri( magentoURL );
		client.setMarshaller( marshaller );
		client.setUnmarshaller( marshaller );
		client.setInterceptors( new ClientInterceptor[] { new ClientInterceptor() {

			@Override
		    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		        return true;
		    }

		    @Override
		    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		        return true;
		    }

		    @Override
		    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		        return true;
		    }

			@Override
			public void afterCompletion( MessageContext messageContext, Exception ex )
					throws WebServiceClientException {
				
			}
		}}  );
		return client;
	}
}