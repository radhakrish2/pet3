package com.pet.auth;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pet.entity.User;
import com.pet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

  

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from database by username (or email, or other unique field)
    	
    		 User user = userRepository.findByEmail(username);
    	if(user==null)
    	{
        	throw new UsernameNotFoundException("User not found with username: " + username);
    	}
               
        
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), 
        		List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}