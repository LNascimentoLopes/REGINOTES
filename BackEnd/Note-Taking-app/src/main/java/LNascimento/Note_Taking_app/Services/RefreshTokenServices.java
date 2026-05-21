package LNascimento.Note_Taking_app.Services;

import LNascimento.Note_Taking_app.DTOs.AuthDTOs.*;
import LNascimento.Note_Taking_app.Models.RefreshToken;
import LNascimento.Note_Taking_app.Models.Users;
import LNascimento.Note_Taking_app.Repositories.RefreshTokenRepository;
import LNascimento.Note_Taking_app.Security.CustomUserDetails;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenServices {


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private jwtService jwtService; // Seu serviço que gera o Access Token (JWT)

    // 1. Método para criar um Refresh Token (Usado no Login e no próprio Refresh)
    @Transactional
    public RefreshToken createRefreshToken(CustomUserDetails user) {
        // Limpa tokens antigos para não encher o banco de lixo
        refreshTokenRepository.deleteByUser(user.getUser());

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setUser(user.getUser());

        return refreshTokenRepository.save(refreshToken);
    }

    // 2. Método que processa a rota de Refresh
    @Transactional
    public loginResponse generateNewTokens(String tokenAlvo, Users user) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenAlvo)
                .orElseThrow(() -> new JwtException("Refresh Token inválido ou não encontrado no sistema."));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new JwtException("O seu Refresh Token expirou. Por favor, faça login novamente.");
        }
        CustomUserDetails us = new CustomUserDetails(refreshToken.getUser());
        String newAccessToken = jwtService.generateToken(us);

        RefreshToken newRefreshToken = createRefreshToken(us);

        return new loginResponse(newAccessToken, newRefreshToken.getToken());
    }

    // Adicione isto no seu RefreshTokenService
    @Transactional
    public void deleteRefreshToken(String tokenStr) {
        refreshTokenRepository.findByToken(tokenStr)
                .ifPresent(token -> refreshTokenRepository.delete(token));

    }


}
