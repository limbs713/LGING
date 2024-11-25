package com.lge.connected.global.config.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lge.connected.domain.user.dto.LoginResponseDTO;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.global.util.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파서를 위한 ObjectMapper


    // loginId를 요청에서 추출하는 메서드 추가
    protected String obtainLoginId(HttpServletRequest request) {
        return request.getParameter("loginId"); // request 파라미터에서 loginId를 추출
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String loginId;
        String password;

        // Content-Type이 application/json인지 확인
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            // JSON 데이터 읽기
            try (InputStream inputStream = request.getInputStream()) {
                Map<String, String> jsonRequest = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
                loginId = jsonRequest.get("loginId");
                password = jsonRequest.get("password");
            } catch (IOException e) {
                throw new AuthenticationServiceException("Failed to read loginId and password from JSON request", e);
            }
        } else {
            // 폼 데이터에서 추출
            loginId = obtainLoginId(request);
            password = obtainPassword(request);
        }

        //spring security에서 loginId와 password 검증하려면 token(DTO)에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password,
                null);

        //authToken 검증을 위해 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }


    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    @Transactional(readOnly = true)
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByLoginId(customUserDetails.getLoginId()).orElseThrow(()->new CustomException(UserErrorCode.USER_NOT_EXIST));

        String loginId = customUserDetails.getLoginId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(loginId, role, 100*60*60L);

        response.addHeader("Authorization", "Bearer " + token);
        // 필요한 회원 정보만 포함하도록 LoginResponseDTO 객체 생성
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
                customUserDetails.getLoginId(),
                customUserDetails.getUsername(),
                token
        );

        // 응답 본문에 회원 정보 추가
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), loginResponseDTO);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user info to response", e);
        }
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }
}
