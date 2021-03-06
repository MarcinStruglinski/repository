package pl.sda.poznan.spring.petclinic.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import pl.sda.poznan.spring.petclinic.service.ApplicationUserDetailsService;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final ApplicationUserDetailsService applicationUserDetailsService;
  private final RestAuthenticationEntryPoint entryPoint;
  private final RestLoginSuccessHandler successHandler;
  private final RestLoginFailureHandler failureHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(applicationUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/h2-console/**")
        .permitAll()
        .antMatchers("/api/v1/register")
        .permitAll()
        .antMatchers("/api/v1/activate/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(entryPoint)
        .and()
        .formLogin()
        .usernameParameter("email")
        .passwordParameter("password")
        .loginProcessingUrl("/api/v1/authenticate")
        .successHandler(successHandler)
        .failureHandler(failureHandler)
        .and()
        .logout()
        .logoutUrl("/api/v1/logout")
        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(200))
        .and()
        .headers()
        .frameOptions()
        .disable()
        .and()
        .csrf()
        .disable()
        .addFilterBefore(corsFilter(), CsrfFilter.class);
  }

  @Bean
  public CorsFilter corsFilter() {
    String allowedOrigin =
        "http://sda-javapoz6-angular-test-bucket.s3-website.eu-central-1.amazonaws.com";
    String localOrigin = "http://localhost:4200";
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin(allowedOrigin);
    config.addAllowedOrigin(localOrigin);
    config.addAllowedMethod(CorsConfiguration.ALL);
    config.addAllowedHeader(CorsConfiguration.ALL);
    config.setAllowCredentials(true);
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
