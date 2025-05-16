package com.example.BusTicketBookingApp.security;
import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("nikitanikki".equals(username)) {
            return new User("nikitanikki", "$2a$08$WsqOm1AOUsVQqvzI7TCxU.8pQS53wR89tj0A1oHFYd0eCijtBCgdW",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException(username + " is not found in the records");
        }
    }
}