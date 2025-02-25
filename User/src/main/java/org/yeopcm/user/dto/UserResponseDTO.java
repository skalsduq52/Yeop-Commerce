package org.yeopcm.user.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserResponseDTO {
    private String email;
    private String name;
    private String phone;
    private String address;
    private String accessToken;
}
