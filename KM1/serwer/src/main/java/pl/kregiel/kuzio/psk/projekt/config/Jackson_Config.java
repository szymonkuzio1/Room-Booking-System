package pl.kregiel.kuzio.psk.projekt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Jackson_Config {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
