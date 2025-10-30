package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.dto.AuthDTO;
import com.vamsi.MoneyManagerApp.dto.ProfileDTO;
import com.vamsi.MoneyManagerApp.entity.ProfileEntity;
import com.vamsi.MoneyManagerApp.service.JwtService;
import com.vamsi.MoneyManagerApp.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {

    private final AuthenticationManager authenticationManager;

    private final ProfileService profileService;

    private final JwtService jwtService;

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileDTO>> getProfiles(){
        List<ProfileDTO> profileDTOs = profileService.getProfiles();
        return new ResponseEntity<>(profileDTOs,HttpStatus.FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){

        ProfileDTO profileDTO1 = profileService.registerProfile(profileDTO);
        return new ResponseEntity<>(profileDTO1,HttpStatus.CREATED);

    }



    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
        try{
            if(!profileService.isAccountActive(authDTO.getEmail())){
                return new ResponseEntity<>(Map.of("message","Account is not active. Please activate your account first"),HttpStatus.FORBIDDEN);
            }
            Map<String,Object> response = profileService.authenticateAndGenerateToken(authDTO);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message",e.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam  String token){
        boolean isActivated = profileService.activateProfile(token);

        return isActivated ? new ResponseEntity<>("Profile Activated Successfully",HttpStatus.OK) : new ResponseEntity<>("Profile Activation Failed",HttpStatus.CONFLICT);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getPublicProfile(){
        return new ResponseEntity<>(profileService.getPublicProfile(null),HttpStatus.OK);
    }
}
