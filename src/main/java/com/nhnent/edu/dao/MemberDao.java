package com.nhnent.edu.dao;

import com.nhnent.edu.model.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;


    public Member exists(String name) {
        return sqlSessionTemplate.selectOne("memberDao.existsMember", name);
    }

    public String getAuthority(String name) {
        return sqlSessionTemplate.selectOne("memberDao.getAuthority", name);
    }

}
