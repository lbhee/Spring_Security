package kr.co.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import kr.co.test.model.dto.Member;
import kr.co.test.service.JoinService;

@Controller
@RequestMapping("/join/")
public class JoinController {

	@Autowired
	private View jsonview; 
	//JSON으로 변환 <bean name="jsonview" class="org.springframework.web.servlet.view.json.MappingJackson2JsonView"/>

	@Autowired
	private JoinService service;

	////////////////////////////////////////////////////////암호화
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	//<bean id="bCryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder">
    /////////////////////////////////////////////////////// 
	
	@RequestMapping(value = "join.htm", method = RequestMethod.GET)
	public String join() {
		return "join.join";
	}

	@RequestMapping(value = "join.htm", method = RequestMethod.POST)
	public String join(Member member) {
		int result = 0;
		String viewpage = "";
		
		//회원가입할때 입력한 비밀번호를 암호화시킨다.////////////////////////////////////
		member.setPwd(this.bCryptPasswordEncoder.encode(member.getPwd()));
		////////////////////////////////////////////////////////////////////
		
		result = service.insertMember(member);
		if (result > 0) {
			System.out.println("가입 성공");
			viewpage = "redirect:/index.htm";
		} else {
			System.out.println("가입 실패");
			viewpage = "join.htm";
		}
		return viewpage;
	}

	
	@RequestMapping(value = "login.htm", method = RequestMethod.GET)
	public String login() {
		return "join.login";
	}
	
	
	//아이디중복체크(비동기) - 현재예제에서는 쓰지않음.
	@RequestMapping(value = "idcheck.htm", method = RequestMethod.POST)
	public View idCheck(@RequestParam("userid") String userid, Model model) {
		int result = service.idCheck(userid);
		if (result > 0) {
			System.out.println("아이디 중복");
			model.addAttribute("result", "fail");
		} else {
			System.out.println("삽입 실패");
			model.addAttribute("result", "success");
		}
		return jsonview;
	}
}
