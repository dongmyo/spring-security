package com.nhnent.edu.dao;

import com.nhnent.edu.model.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO : #6 Member DAO
@Repository
public class MemberDao {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;


    public Member exists(String name, String password) {
        Member member = new Member();
        member.setName(name);
        member.setPassword(password);

        return sqlSessionTemplate.selectOne("memberDao.existsMember", member);
    }

    public String getAuthority(String name) {
        return sqlSessionTemplate.selectOne("memberDao.getAuthority", name);
    }

}
