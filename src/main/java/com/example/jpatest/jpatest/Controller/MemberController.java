package com.example.jpatest.jpatest.Controller;

import com.example.jpatest.jpatest.domain.Address;
import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());

        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String createMember(@Valid MemberForm memberForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()) return "members/createMemberForm";

        Address adress = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(adress);

        memberService.join(member);

        return "redirect:/";
    }
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        List <MemberForm> memberForms = new ArrayList<>();

        for(Member m : members){
            MemberForm tempMemberForm  = new MemberForm();

            tempMemberForm.setId(m.getId());
            tempMemberForm.setName(m.getName());
            tempMemberForm.setCity(m.getAddress().getCity());
            tempMemberForm.setStreet(m.getAddress().getStreet());
            tempMemberForm.setZipcode(m.getAddress().getZipcode());
            memberForms.add(tempMemberForm);
        }

        model.addAttribute("members", memberForms);
        return "members/memberList";
    }
}
