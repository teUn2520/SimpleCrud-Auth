package com.example.universitycrud.sevice;

import com.example.universitycrud.model.User;
import com.example.universitycrud.repository.MyUserRepository;
import com.example.universitycrud.sevice.impl.MyUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserService implements UserDetailsService {
    public MyUserRepository myUserRepository;

    @Autowired
    public void setMyUserRepository(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = myUserRepository.findUsersByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' is not found", username)
        ));

        return MyUserDetailsImpl.build(user);
    }
}
