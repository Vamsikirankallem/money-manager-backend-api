package com.vamsi.MoneyManagerApp.service;

import com.vamsi.MoneyManagerApp.dto.AuthDTO;
import com.vamsi.MoneyManagerApp.dto.ProfileDTO;
import com.vamsi.MoneyManagerApp.entity.ProfileEntity;
import com.vamsi.MoneyManagerApp.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${app.activation.url}")
    private String activationURL;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){



        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        profileRepository.save(newProfile);

        //send activation email
        String activationLink = activationURL+"/api/v1.0/activate?token="+newProfile.getActivationToken();
        String subject = "Activate your Money Manager Account";
        String body = "Click on the following link to activate your account: " + activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject,body);

        return toDTO(newProfile);
    }

    public List<ProfileDTO> getProfiles() {
        List<ProfileEntity> profiles = profileRepository.findAll();
        List<ProfileDTO> profileDTOS = new ArrayList<>();
        for(ProfileEntity profileEntity : profiles){
            profileDTOS.add(toDTO(profileEntity));
        }
        return profileDTOS;
    }

    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
                .map(profile->{
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("Profile not found"));
    }

    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentProfile = null;
        if(email==null){
            currentProfile = getCurrentProfile();
        }
        else{
            currentProfile = profileRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Profile not found with this email "+email));
        }

        return toDTO(currentProfile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .imageUrl(profileDTO.getImageUrl())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .createAt(profileDTO.getCreateAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity){
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .imageUrl(profileEntity.getImageUrl())
                .createAt(profileEntity.getCreateAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }


    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));

            String jwtToken = jwtService.generateToken(authDTO.getEmail());
                 return Map.of("token",jwtToken,"user",getPublicProfile(authDTO.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
