package catinder.website.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import catinder.website.model.Cat;

@Service
public class CatService {

    private static final String BASE_URL = "http://localhost:8081/api/cats";

    @Autowired
    private RestTemplate restTemplate;

    public Cat[] getAllCats() {
        ResponseEntity<Cat[]> response = restTemplate.getForEntity(BASE_URL, Cat[].class);
        return response.getBody();
    }

    public Cat createCat(Cat cat) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Cat> requestEntity = new HttpEntity<>(cat, headers);

        ResponseEntity<Cat> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, Cat.class);
        return response.getBody();
    }
}
