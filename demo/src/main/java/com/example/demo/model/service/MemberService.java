package com.example.demo.model.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Member;
import com.example.demo.model.dto.MemberForm;
import com.example.demo.model.repository.MemberRepository;
import org.springframework.security.core.userdetails.*;
@Service
public class MemberService implements UserDetailsService{
    @Autowired
    private MemberRepository memberRepository;
    @Autowired // SecurityConfig 에 등록한 BCryptPasswordEncoder 주입
    private PasswordEncoder passwordEncoder;
    public Member signup(MemberForm form) { // 회원가입
        if (form.getPassword() == null || !form.getPassword().equals(form.getPasswordConfirm())) {
            // 예외 발생 시 하단의 DB 저장(save) 코드로 이동하지 않음 (DB 저장 안 됨)
            // 컨트롤러의 catch (IllegalArgumentException e)로 메시지 전달됨
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (memberRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
            
    }
    
    Member member = new Member();
    member.setUsername(form.getUsername());
    member.setPassword(passwordEncoder.encode(form.getPassword())); // ★ 암호화
    member.setName(form.getName());
    member.setRole("USER"); // 기본 권한
    return memberRepository.save(member); // INSERT
    }
    @Override // 로그인 시 시큐리티가 자동 호출
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("회원 없음 : " + username));
        return User.builder() // 시큐리티용 사용자 객체로 변환
                .username(member.getUsername())
                .password(member.getPassword()) // 암호화 값 → matches()로 비교
                .roles(member.getRole()) // USER → ROLE_USER
                .build();
    }
}