package com.github.sangueamigo.modules.conta.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String role
) {
}
