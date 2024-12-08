package marketing.management.system.service.implementations;

import marketing.management.system.service.interfaces.RecaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaToken) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(RECAPTCHA_VERIFY_URL)
                .queryParam("secret", recaptchaSecret)
                .queryParam("response", recaptchaToken);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(builder.toUriString(), null, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        return responseBody != null && (Boolean) responseBody.get("success");
    }
}

