package com.example.jpatest.jpatest.api;

import com.example.jpatest.jpatest.domain.Address;
import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> MembersV(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public ResultMember MembersV2(){
        List <Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        return new ResultMember(collect, collect.size());
    }

    @Data
    @AllArgsConstructor
    static class ResultMember<E> {
        private E data;
        private int count;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String name;
    }
    /**
     * 엔티티를 직접 api 호출용으로 받지 말것!
     * @param member
     * @return
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    /**
     * 등록 api (POST)
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id
            , @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOneMember(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    @Data
    static class CreateMemberRequest{
        @NotEmpty
        String name;
    }

    @Data
    static class CreateMemberResponse{
        Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }
}

