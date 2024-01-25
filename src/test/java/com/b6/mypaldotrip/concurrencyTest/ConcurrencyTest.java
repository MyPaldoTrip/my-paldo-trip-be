package com.b6.mypaldotrip.concurrencyTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.b6.mypaldotrip.domain.like.service.LikeService;
import com.b6.mypaldotrip.domain.like.store.repository.LikeRepository;
import com.b6.mypaldotrip.domain.user.service.UserService;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ConcurrencyTest {

    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserService userService;
    @Test
    public void testLikeInMultiThread() throws InterruptedException {
        // given
        int threadCount = 100;
        Long courseId = 11L;
        int amount = likeRepository.findAll().size();

        // when
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (long i = 0L; i < threadCount; i++) {
            UserEntity user = userService.findUser(i+9);
            executor.execute(() -> likeService.toggleLike(courseId, user));
        }

        // 모든 작업이 끝날 때까지 대기
        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        // then
        assertEquals(threadCount, likeRepository.findAll().size() - amount);

    }
}
