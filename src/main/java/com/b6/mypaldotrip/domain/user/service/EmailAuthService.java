package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.store.entity.EmailAuth;
import com.b6.mypaldotrip.domain.user.store.repository.EmailAuthRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
    private final EmailAuthRepository emailAuthRepository;

    public boolean hasMail(String email) {
        return emailAuthRepository.existsById(email);
    }

    public void delete(String email) {
        emailAuthRepository.deleteById(email);
    }

    public EmailAuth save(EmailAuth emailAuth) {
        return emailAuthRepository.save(emailAuth);
    }

    public Optional<EmailAuth> findById(String email) {
        return emailAuthRepository.findById(email);
    }
}
