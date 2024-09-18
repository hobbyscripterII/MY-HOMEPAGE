package com.project.homepage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(c -> c.disable())
                .formLogin(in -> in
                        .loginPage("/login")			// HTML 경로
                        .loginProcessingUrl("/login")	// URL 호출 시 Security가 낚아채므로 Controller에 구현할 필요 X
                        .usernameParameter("id")
                        .passwordParameter("pw")
                        .failureUrl("/login?error") 	// 매핑 URL, Error Param 생략 시 Login 화면에 에러 메세지 출력 X
                        .defaultSuccessUrl("/") 		// 인증 완료 후 호출되는 URL
                        .permitAll())
                .exceptionHandling(e -> e
                        .accessDeniedPage("/access-denied?error")) // 매핑 URL
                .authorizeHttpRequests(a -> a
                        // 권한없이 접근 가능
                        .requestMatchers(
                                "/", "/css/**", "/js/**", "/img/**",
                                "/login", "/logout", "/access-denied",
                                "/board/", "/board/list/**", "/board/read-md/**",
                                "/thumbnail/**"
                        ).permitAll()
                        // ADMIN만 접근 가능
                        .anyRequest().hasRole("ADMIN")
                );
        httpSecurity.logout(out -> {
                 out.logoutUrl("/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        HttpSession session = request.getSession();
                        if (session != null) {
                            session.invalidate();
                        }
                    })
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.sendRedirect("/");
                    });
        });
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
