package com.nhnent.edu.security;

import com.nhnent.edu.dao.MemberDao;
import com.nhnent.edu.model.CustomUserDetails;
import com.nhnent.edu.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    MemberDao memberDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberDao.exists(username);
        if (member == null) {
            throw new UsernameNotFoundException("username not found");
        }

        String authority = memberDao.getAuthority(username);
        if (authority == null) {
            throw new UsernameNotFoundException("username not found");
        }

        // TODO : #5 UserDetails의 custom 구현체를 반환하세요.
        return new CustomUserDetails(username, member.getPassword(), authority);
    }

}
