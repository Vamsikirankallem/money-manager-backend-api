package com.vamsi.MoneyManagerApp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class ProfileDTO {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String imageUrl;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

}
