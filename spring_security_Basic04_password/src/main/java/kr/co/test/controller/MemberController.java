package kr.co.test.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.test.model.dto.Member;
import kr.co.test.service.MemberService;

@Controller
@RequestMapping("/join/")
public class MemberController {

	@Autowired
	private MemberService service;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//수정하기 화면
	@RequestMapping(value="memberconfirm.htm",method=RequestMethod.GET)
	public String memberConfirm(){
		return "join.memberConfirm";
	}
	
	//수정하기 처리
	@RequestMapping(value="memberconfirm.htm",method=RequestMethod.POST)
	public String memberConfirm(@RequestParam("password") String rawPassword,	Principal principal){
		String viewpage="";     //화면에 입력한 비밀번호
		
		//회원정보
		Member member = service.getMember(principal.getName());
		
		
		//DB에서 가져온 암호화된 문자열
		String encodedPassword = member.getPwd();
		
		System.out.println("rawPassword : "+rawPassword );
		System.out.println("encodepassword : " + encodedPassword);
		
		boolean result = bCryptPasswordEncoder.matches(rawPassword, encodedPassword); //비밀번호 일치확인(true)
		
		if(result){ //true면
			viewpage="redirect:memberupdate.htm"; //수정하기 업데이트 화면으로
		}else{
			viewpage="redirect:memberconfirm.htm";
		}
		
		return viewpage;
	}
	
	//수정하기 업데이트 화면
	@RequestMapping(value="memberupdate.htm", method=RequestMethod.GET)
	public String memberUpdate(Model model, Principal principal){
		Member member = service.getMember(principal.getName());
		model.addAttribute("member", member);
		return "join.memberUpdate";
	}
	
	//수정하기 업데이트 처리
	@RequestMapping(value="memberupdate.htm", method=RequestMethod.POST)
	public String memberUpdate(Model model, Member member, Principal principal){
		
		//회원정보
		Member updatemember = service.getMember(principal.getName());
		
		//수정
		updatemember.setName(member.getName());
		updatemember.setCphone(member.getCphone());
		updatemember.setEmail(member.getEmail());
		updatemember.setPwd(bCryptPasswordEncoder.encode(member.getPwd())); //비밀번호 다시 암호화
		
		//업데이트문
		service.updateMember(updatemember);
		
		return "redirect:/index.htm";
	}
}
