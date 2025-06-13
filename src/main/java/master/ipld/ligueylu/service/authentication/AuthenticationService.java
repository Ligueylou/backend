package master.ipld.ligueylu.service.authentication;

import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.exception.ResourceAlreadyExistException;
import master.ipld.ligueylu.model.Administrateur;
import master.ipld.ligueylu.model.Client;
import master.ipld.ligueylu.model.Prestataire;
import master.ipld.ligueylu.model.abstracts.Utilisateur;
import master.ipld.ligueylu.model.enums.Role;
import master.ipld.ligueylu.repository.utilisateur.UtilisateurRepository;
import master.ipld.ligueylu.request.AuthenticationRequest;
import master.ipld.ligueylu.request.RegisterRequest;
import master.ipld.ligueylu.response.AuthenticationResponse;
import master.ipld.ligueylu.service.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request)
    {
        Role role = request.getRole() != null ? request.getRole() : Role.CLIENT;
        Utilisateur user = switch (role) {
            case ADMIN -> Administrateur.builder()
                    .nomComplet(request.getNomComplet())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .telephone(request.getTelephone())
                    .role(Role.ADMIN)
                    .actif(true)
                    .build();
            case PRESTATAIRE -> Prestataire.builder()
                    .nomComplet(request.getNomComplet())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .telephone(request.getTelephone())
                    .role(Role.PRESTATAIRE)
                    .actif(false)
                    .score(0.0)
                    .build();
            default -> Client.builder()
                    .nomComplet(request.getNomComplet())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .telephone(request.getTelephone())
                    .role(Role.CLIENT)
                    .actif(false)
                    .build();
        };

        var existingEmail = utilisateurRepository.findByEmail(request.getEmail());
        if(existingEmail.isPresent()){
            throw new ResourceAlreadyExistException("Un utilisateur avec cet email existe déjà.");
        }

        utilisateurRepository.save(user);
        var jwtToken = jwtService.getToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user  = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.getToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
