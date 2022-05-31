package br.com.i2e.magento.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import br.com.i2e.magento.client.ws.MagentoWS;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {
	private static final Logger logger = LoggerFactory.getLogger( Application.class );
	@Autowired
	MagentoWS magentoWS; 

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	public CommandLineRunner testLogin() {
		return args -> {
			magentoWS.getSession();
			
		};
	}
	@Bean
	public CommandLineRunner testCatalog() {
		return args -> {
//			magentoWS.getProuctList().stream().forEach( p -> {
//				var sb = new StringBuilder();
//				
//				p.getCategoryIds().getComplexObjectArray().stream().forEach( s ->  sb.append( s ) );
//				logger.info( "Product: {} {} {} {} {}", p.getProductId(), p.getName(), p.getSku(), p.getType(), p.getSet()  );
//			});
		};
	}
}
