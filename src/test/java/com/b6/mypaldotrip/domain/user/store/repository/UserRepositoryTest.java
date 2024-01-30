package com.b6.mypaldotrip.domain.user.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.UserCustomRepositoryTestConfig;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(UserCustomRepositoryTestConfig.class)
@ActiveProfiles("test")
class UserRepositoryTest implements CommonTest {

    @Autowired private UserRepository userRepository;
    private UserEntity saveUser;
    private UserEntity saveAnotherUser;

    @BeforeEach
    void setUp() {
        saveUser = userRepository.save(TEST_USER);
        saveAnotherUser = userRepository.save(TEST_ANOTHER_USER);
    }

    @Test
    @DisplayName("이메일로 찾기")
    void 이메일로찾기() {
        // given
        String email = saveUser.getEmail();

        // when
        Optional<UserEntity> user = userRepository.findByEmail(email);

        // then
        assertThat(user.get()).isEqualTo(TEST_USER);
    }

    @Test
    @DisplayName("유저아이디로 유저네임 찾기")
    void 이름구하기() {
        // given
        Long userId = saveUser.getUserId();

        // when
        String username = userRepository.findUsernameByUserId(userId);

        // then
        assertThat(username).isEqualTo(TEST_USERNAME);
    }
}
