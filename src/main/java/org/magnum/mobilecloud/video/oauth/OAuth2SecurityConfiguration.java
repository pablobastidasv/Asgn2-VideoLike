package org.magnum.mobilecloud.video.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

/**
 * Created by pbastidas on 9/14/14.
 */
@Configuration
public class OAuth2SecurityConfiguration {

    // Esta primera seccion de la configuracion solo se asegura que String Security reciba el
    // UserDetailsService que crearemos mas adelante
    @Configuration
    @EnableWebSecurity
    protected static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        protected void registerAuthentication(final AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(userDetailsService);
        }
    }

    /**
     * Este metodo es usado para configurar quien tiene permiso de acceder a que parte
     * de nuestro recurso (ej: El "/video" endpoint)
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServer extends ResourceServerConfigurerAdapter{

        // This method configures the OAuth scopes required by clients to access all of the paths in the video service.
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();

            http.authorizeRequests().antMatchers("/oauth/token").anonymous();

            // Requiere para todos los request por GET tener permiso de "read"
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/**")
                    .access("#oauth2.hasScope('read')");

            // Para todos los demas request debe tener permiso "write"
            http.authorizeRequests()
                    .antMatchers("/**")
                    .access("@oauth2.hasScope('write')");
        }
    }

    /**
     * Esta clase es usada para configurar como nuestro servidor de autorizaciones (el '/oauth/token' endpoin)
     * valida las credenciales del cliente.
     */
    @Configuration
    @EnableAuthorizationServer
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter{

        // Delega el proceso de autenticar el request a el framework
        @Autowired
        private AuthenticationManager authenticationManager;

        private ClientAndUserDetailsService combinedService_;

        /**
         * Este contructor es usado para configurar los clientes y usuario que estaran abilitado para ingresar
         * al sistema.
         * @throws Exception
         */
        public OAuth2Config() throws Exception{

            // crear un servicio que tenga las credenciales de todos los clientes
            ClientDetailsService csvc = new InMemoryClientDetailsServiceBuilder()
                    // Crear un cliente que tiene acceso de lectura y escritura
                    // a el servicio de video
                    .withClient("mobile").authorizedGrantTypes("password")
                    .authorizedGrantTypes("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                    .scopes("read", "write").resourceIds("video")
                    .and()
                    // Crea un segundo cliente que solo tenga acceso "read" a el
                    // servicio de video
                    .withClient("mobileReader").authorizedGrantTypes("password")
                    .authorities("ROLE_CLIENT")
                    .scopes("read").resourceIds("video")
                    .accessTokenValiditySeconds(3600)
                    .and().build();

            // Crear los usuarios
            UserDetailsService svc = new InMemoryUserDetailsManager(
                    Arrays.asList(
                              User.create("admin", "pass", "ADMIN", "USER")
                            , User.create("user0", "pass", "USER")
                    )
            );


            combinedService_ = new ClientAndUserDetailsService(csvc, svc);
        }

        @Bean
        public ClientDetailsService clientDetailsService() throws Exception {
            return combinedService_;
        }

        @Bean
        public UserDetailsService userDetailsService(){
            return combinedService_;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService());
        }
    }
}
