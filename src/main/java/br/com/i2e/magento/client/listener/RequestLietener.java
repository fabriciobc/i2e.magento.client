package br.com.i2e.magento.client.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.i2e.magento.client.ws.MagentoWS;
import br.com.i2e.magento.common.util.JsonUtils;
import br.com.i2e.magento.common.ws.model.CatalogProductCreateRequestParam;


@Component
public class RequestLietener {
	public final String MAGENTO_REQUEST_QUEUE = "magento.request.queue";
	@Autowired
	MagentoWS magentoWS;
	
	@RabbitListener( queues = MAGENTO_REQUEST_QUEUE )
	public void onMessage( @Payload String jsonMessage ) {
		magentoWS.createProduct( JsonUtils.fromJson( jsonMessage, CatalogProductCreateRequestParam.class ) );
	}
}
