package org.bidve.hotelmanagement.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityConfigureAdapter{ 
	
	@Autowired
	CustomerUsersDetailsService customerUsersDetailsService;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(customerUsersDetailsService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
			.and()
			.csrf().disable()
			.authorizedRequests()
			.antMatchers("/user/login","/user/signup","/user/forgotPassword")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and().exceptionHandling()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		
		
	}

	
}
