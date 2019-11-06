package springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.request.LoginForm;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.request.SignUpForm;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.response.JwtResponse;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.RoleRepository;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.UserRepository;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.security.jwt.JwtProvider;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        return this.userService.createUser(signUpRequest);
    }

}