package com.example.jpatest.jpatest.service;

import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //회원 가입
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //if error, generate Exception

        List<Member> findedMembers = memberRepository.findByName(member.getName());
        if(!findedMembers.isEmpty()) throw new IllegalStateException("Already Exist Member");
    }

    //회원 전체 조회
    @Transactional(readOnly = true)
    public List <Member> findMembers(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOneMember(Long id){
        return memberRepository.findOne(id);
    }


}
