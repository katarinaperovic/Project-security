package marketing.management.system.controller;

import jakarta.validation.Valid;
import marketing.management.system.model.User;
import marketing.management.system.dto.UserDTO;
import marketing.management.system.service.interfaces.ClientService;
import marketing.management.system.service.interfaces.RecaptchaService;
import marketing.management.system.service.interfaces.UserService;
import marketing.management.system.util.TokenUtils;
import marketing.management.system.dto.JwtAuthenticationRequest;
import marketing.management.system.dto.UserTokenState;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private RecaptchaService recaptchaService;

    @PostMapping("/register")

    @CrossOrigin(origins = "https://localhost:4200")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO registrationRequest) {
        UserDTO sanitizedRequest = sanitizeUserDTO(registrationRequest);
        User user = clientService.registerClient(sanitizedRequest);
        if(user != null)
            return ResponseEntity.ok("");

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
    }

    private UserDTO sanitizeUserDTO(UserDTO userDTO) {
        UserDTO sanitizedDTO = new UserDTO();
        if(userDTO.getName()!=null){
        sanitizedDTO.setName(sanitizeInput(userDTO.getName()));}
        if(userDTO.getSurname()!=null){
            sanitizedDTO.setSurname(sanitizeInput(userDTO.getSurname()));}
        if(userDTO.getFirmName()!=null){
            sanitizedDTO.setFirmName(sanitizeInput(userDTO.getFirmName()));}
        if(userDTO.getPib()!=null){
            sanitizedDTO.setPib(sanitizeInput(userDTO.getPib()));}
        sanitizedDTO.setPhone(sanitizeInput(userDTO.getPhone()));
        sanitizedDTO.setEmail(sanitizeInput(userDTO.getEmail()));
        sanitizedDTO.setPassword(sanitizeInput(userDTO.getPassword()));
        sanitizedDTO.setRe_password(userDTO.getRe_password());
        sanitizedDTO.setPackageType(userDTO.getPackageType());
        sanitizedDTO.setAddress(userDTO.getAddress());
        return sanitizedDTO;
    }

    private String sanitizeInput(String input) {

        System.out.println("Ulazni unos: " + input);
        String sanitizedInput = Jsoup.clean(input, Whitelist.none());
        System.out.println("Izlazni unos nakon sanitizacije: " + sanitizedInput);

        return sanitizedInput;
    }


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @Valid @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        if (!recaptchaService.verifyRecaptcha(authenticationRequest.getRecaptchaToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String sanitizedUsername = sanitizeInput(authenticationRequest.getUsername());
        String sanitizedPassword = sanitizeInput(authenticationRequest.getPassword());

        String salt = userService.getSalt(sanitizedUsername);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                sanitizedUsername, sanitizedPassword.concat(salt)));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accesJwt = tokenUtils.generateAccessToken(user);
        String refreshJwt = tokenUtils.generateRefreshToken(user) ;
        int refreshTokenExpiresIn = tokenUtils.getRefreshTokenExpiresIn();
        int accessTokenExpiresIn = tokenUtils.getAccessTokenExpiresIn();

        UserTokenState userTokenState = new UserTokenState();
        userTokenState.setAccessToken(accesJwt);
        userTokenState.setRefreshToken(refreshJwt);
        userTokenState.setAccessTokenexpiresIn(accessTokenExpiresIn);
        userTokenState.setRefreshTokenexpiresIn(refreshTokenExpiresIn);

        return ResponseEntity.ok(userTokenState);
    }


}