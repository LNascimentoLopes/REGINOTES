package LNascimento.Note_Taking_app.Services;

import LNascimento.Note_Taking_app.Repositories.BlackListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class DatabaseCleanerService {

    @Autowired
    private BlackListRepository blacklistedTokenRepository;

    // Cron expressão: Roda todos os dias à meia-noite (00:00:00)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanExpiredBlacklistedTokens() {
        Date now = Date.from(Instant.now());
        blacklistedTokenRepository.deleteByExpiryDateBefore(now);

    }
}
