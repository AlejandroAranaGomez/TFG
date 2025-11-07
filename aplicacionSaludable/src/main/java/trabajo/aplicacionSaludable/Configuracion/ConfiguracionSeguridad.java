package trabajo.aplicacionSaludable.Configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Bean
    public PasswordEncoder encriptaContrasenha() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filtroParaRegistroEInicio(HttpSecurity http) throws Exception {

        //Permito que usuarios anonimos puedan registrarse e iniciar sesion.

        http
                //No creo sesiones para usar la api.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/autentification/registrar").permitAll()
                        .requestMatchers("/api/autentification/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

}
