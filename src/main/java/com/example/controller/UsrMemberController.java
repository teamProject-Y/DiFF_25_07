package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.interceptor.BeforeActionInterceptor;
import com.example.service.MemberService;
import com.example.vo.Member;
import com.example.vo.ResultData;
import com.example.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.util.Ut;

@Controller
public class UsrMemberController {

	private final BeforeActionInterceptor beforeActionInterceptor;

	@Autowired
	private Rq rq;

	@Autowired
	private MemberService memberService;

	public UsrMemberController(HttpSession session, BeforeActionInterceptor beforeActionInterceptor) {
		this.beforeActionInterceptor = beforeActionInterceptor;

	}

	@RequestMapping("/usr/member/join")
	public String join() {

		return "/usr/member/join";
	}

	// 액션메서드
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String checkLoginPw, String name, String nickName, String cellPhone, String email) {

		if(Ut.isEmpty(loginId)) return Ut.jsHistoryBack("F-1", "아이디를 쓰시오");
		if(Ut.isEmpty(loginPw)) return Ut.jsHistoryBack("F-2", "비밀번호를 쓰시오");
		if(Ut.isEmpty(name)) return Ut.jsHistoryBack("F-3", "이름을 쓰시오");
		if(Ut.isEmpty(nickName)) return Ut.jsHistoryBack("F-4", "닉네임을 쓰시오");
		if(Ut.isEmpty(cellPhone)) return Ut.jsHistoryBack("F-5", "전화번호 좀 쓰시오");
		if(Ut.isEmpty(email) || !email.contains("@")) return Ut.jsHistoryBack("F-6", "이메일 정확히 쓰시오");
		if(!loginPw.equals(checkLoginPw)) return Ut.jsHistoryBack("F-7", "비밀번호가 일치하지 않소");

		int id = memberService.doJoin(loginId, loginPw, name, nickName, cellPhone, email);

		if(id == -1) return Ut.jsHistoryBack("F-8", Ut.f("%s는 이미 사용 중인 아이디입니다.", loginId));
		if(id == -2) return Ut.jsHistoryBack("F-9", Ut.f("이름 %s과 이메일 %s은(는) 이미 사용 중입니다.", loginId, email));

		Member member = memberService.getMemberById(id);

		return Ut.jsReplace("S-1", Ut.f("%s 님 회원가입을 축하", nickName), "/");
	}

	@RequestMapping("/usr/member/login")
	public String login() {

		return "/usr/member/login";
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(HttpServletRequest req, String loginId, String loginPw) {

		Rq rq = (Rq) req.getAttribute("rq");

		if(Ut.isEmpty(loginId)) return Ut.jsHistoryBack("F-1", "아이디 입력해주세요");
		if(Ut.isEmpty(loginPw)) return Ut.jsHistoryBack("F-2", "비밀번호 입력햇주세요");

		Member member = memberService.getMemberByLoginId(loginId);

		if(member == null) return Ut.jsHistoryBack("F-3", "존재하지 않는 아이디에요");
		if(!member.getLoginPw().equals(loginPw)) return Ut.jsHistoryBack("F-A", "올바르지 않은 비밀번호에요");

		rq.login(member);

		return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickName()), "/");
	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(HttpServletRequest req) {

		Rq rq = (Rq) req.getAttribute("rq");

		rq.logout();

		return Ut.jsReplace("S-1", "로그아웃 되었습니다", "/");

	}

	@RequestMapping("/usr/member/myInfo")
	public String myInfo(Model model, HttpServletRequest req) {

		Rq rq = (Rq) req.getAttribute("rq");
		Member member = memberService.getMemberById(rq.getLoginedMemberId());

		model.addAttribute("member", member);

		return "/usr/member/myInfo";
	}

	@RequestMapping("/usr/member/modify")
	public String modify(Model model, HttpServletRequest req) {

		Rq rq = (Rq) req.getAttribute("rq");
		Member member = memberService.getMemberById(rq.getLoginedMemberId());

		model.addAttribute("member", member);

		return "/usr/member/modify";
	}

	@RequestMapping("/usr/member/checkPw")
	@ResponseBody
	public ResultData checkPw(HttpServletRequest req, String pw) {

		Rq rq = (Rq) req.getAttribute("rq");
		Member member = memberService.getMemberById(rq.getLoginedMemberId());

		if(!member.getLoginPw().equals(pw)) {
			return ResultData.from("F-1", "비밀번호 불일치");
		}

		return ResultData.from("S-1", "비밀번호 일치 성공");
	}

	// 로그인 체크 -> 유무 체크 -> 권한 체크
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, String loginId, String loginPw, String name, String nickName, String cellPhone, String email) {

		Rq rq = (Rq) req.getAttribute("rq");
		int loginedMemberId = rq.getLoginedMemberId();

//		if(Ut.isEmpty(loginId)) return Ut.jsHistoryBack("F-1", "아이디를 쓰시오");
//		if(memberService.isUsableLoginId(loginId)) return Ut.jsHistoryBack("F-7", "사용 중인 아이디입니다.");
		if(Ut.isEmpty(loginPw)) return Ut.jsHistoryBack("F-2", "비밀번호를 쓰시오");
		if(Ut.isEmpty(name)) return Ut.jsHistoryBack("F-3", "이름을 쓰시오");
		if(Ut.isEmpty(nickName)) return Ut.jsHistoryBack("F-4", "닉네임을 쓰시오");
		if(Ut.isEmpty(cellPhone)) return Ut.jsHistoryBack("F-5", "전화번호 좀 쓰시오");
		if(Ut.isEmpty(email) || !email.contains("@")) return Ut.jsHistoryBack("F-6", "이메일 정확히 쓰시오");

		int memberUpdate = memberService.modifyMember(loginedMemberId, loginId, loginPw, name, nickName, cellPhone, email);

		return Ut.jsReplace("S-1", Ut.f("%s 회원님 정보 수정 완료", nickName), "../member/myInfo");
	}
}