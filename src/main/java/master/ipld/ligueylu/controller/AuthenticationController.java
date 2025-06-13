package master.ipld.ligueylu.controller;

import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.model.abstracts.Utilisateur;
import master.ipld.ligueylu.repository.utilisateur.UtilisateurRepository;
import master.ipld.ligueylu.request.AuthenticationRequest;
import master.ipld.ligueylu.request.RegisterRequest;
import master.ipld.ligueylu.response.ApiResponse;
import master.ipld.ligueylu.response.AuthenticationResponse;
import master.ipld.ligueylu.service.authentication.AuthenticationService;
import master.ipld.ligueylu.service.security.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
            request.getEmail(), request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(creds);
        String token = jwtService.getToken(authentication.getName());
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
