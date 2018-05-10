package psychic.guide.api.services.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import psychic.guide.api.SearchProperties;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().disable()
				.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
				.anyRequest().authenticated()
				.and().httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser(SearchProperties.getInstance().get("authentication.username"))
				.password(SearchProperties.getInstance().get("authentication.password"))
				.roles(SearchProperties.getInstance().get("authentication.roles"));
	}
}
