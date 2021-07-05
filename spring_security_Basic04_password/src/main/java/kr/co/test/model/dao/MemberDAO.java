package kr.co.test.model.dao;

import java.util.List;

import kr.co.test.model.dto.Member;
import kr.co.test.model.dto.Notice;

public interface MemberDAO {

	//회원가입
	public int insertMember(Member member);
	
	//회원id중복검사(비동기처리)
	public int idCheck(String userid);
	
	//로그인
	public int loginCheck(String username, String password);
	
	//회원정보얻기
	public Member getMember(String userid);
	
	//회원수정
	public int updateMember(Member member);

}
