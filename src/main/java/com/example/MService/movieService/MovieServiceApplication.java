package com.example.MService.movieService;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
public class MovieServiceApplication {
	
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};

//		tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
		
		return tomcat;
	}

	private Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(9000);
		connector.setSecure(false);
		connector.setRedirectPort(9443);

		return connector;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}
}


@EnableResourceServer
@EnableWebSecurity
class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resourceService1";

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.requestMatchers().antMatchers("/**")
				.and()
				.formLogin().disable()
				.authorizeRequests()
				.antMatchers("/movieList/**").access("#oauth2.isClient() or hasRole('END_USER')")
				.anyRequest().authenticated();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources
//				.resourceId(RESOURCE_ID)
				.tokenStore(tokenStore());
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	/**
	 * This bean generates an token enhancer, which manages the exchange between JWT acces tokens and Authentication
	 * in both directions.
	 *
	 * @return an access token converter configured with the authorization server's public/private keys
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

		Resource resource = new ClassPathResource("public.txt");
		String publicKey = null;
		try {
//			publicKey = String.valueOf(IOUtils.readFully(resource.getFile()));
			publicKey = IOUtils.toString(resource.getInputStream());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		converter.setVerifierKey(publicKey);
		return converter;
	}

}

