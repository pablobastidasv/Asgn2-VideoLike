package org.magnum.mobilecloud.video.oauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * Created by pbastidas on 9/14/14.
 */
public class ClientAndUserDetailsService  implements UserDetailsService,
        ClientDetailsService {

    private final ClientDetailsService clients_;
    private final UserDetailsService users_;
    private final ClientDetailsUserDetailsService clientDetailsWrapper_;

    public ClientAndUserDetailsService(ClientDetailsService clients, UserDetailsService users) {
        this.clients_ = clients;
        this.users_ = users;
        this.clientDetailsWrapper_ = new ClientDetailsUserDetailsService(clients);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clients_.loadClientByClientId(clientId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user;

        try{
            user = users_.loadUserByUsername(username);
        }catch(UsernameNotFoundException e){
            user = clientDetailsWrapper_.loadUserByUsername(username);
        }

        return user;
    }
}
