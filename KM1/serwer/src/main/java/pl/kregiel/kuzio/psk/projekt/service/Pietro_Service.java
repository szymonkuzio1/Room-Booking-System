package pl.kregiel.kuzio.psk.projekt.service;

import java.util.List;
import java.nio.file.Path;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class Pietro_Service {
    private ObjectMapper objectMapper;

    public Pietro_Service(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Integer> sale(int pietro) throws Exception {
        Path pietro_path = Path.of("pietra", "pietro_" + pietro + ".json");
        return objectMapper.readValue(
                pietro_path.toFile(),
                new TypeReference<List<Integer>>() {}
        );
    }
}
