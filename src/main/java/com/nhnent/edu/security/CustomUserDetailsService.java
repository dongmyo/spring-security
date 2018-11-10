package com.nhnent.edu.security;

import com.nhnent.edu.dao.MemberDao;
import com.nhnent.edu.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    MemberDao memberDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = null;
        List<GrantedAuthority> authorities = new ArrayList<>();

        Member member = memberDao.getMember(username);
        if (member == null) {
            throw new UsernameNotFoundException("Not found username : " + username);
        }
        password = member.getPassword();
        String authority = memberDao.getAuthority(username);
        if (authority == null) {
            throw new UsernameNotFoundException("No authority username : " + username);
        }
        authorities.add(new SimpleGrantedAuthority(authority));

        return User.withUsername(username).password(password).authorities(authorities).build();
    }

}
