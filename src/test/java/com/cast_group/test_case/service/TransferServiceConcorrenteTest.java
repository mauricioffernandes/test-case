package com.cast_group.test_case.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class TransferServiceConcorrenteTest {

    @Autowired
    private TransferService transferService;

    private final AtomicInteger successCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);

    @Test
    public void testTransferenciaConcorrente() throws InterruptedException {
        int threadCount = 3;
        double amount = 5.0;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.err.println("Iniciando teste de transferência concorrente com " + threadCount + " threads...");

        for (int i = 0; i < threadCount; i++) {
            int threadId = i+1;
            executor.submit(() -> {
                try {
                    System.err.println("Thread " + threadId + " iniciando transferência...");
                    transferService.transferWithRetry(5L, 7L, amount);

                    System.err.println("Thread " + threadId + " completou a transferência com sucesso.");
                    successCounter.incrementAndGet();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.err.println("Thread " + threadId + " falhou: " + e.getMessage());
                    failureCounter.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();  // Espera todas as threads concluírem
        executor.shutdown();

        System.err.println("Teste concluído. Sucessos: " + successCounter.get() + ", Falhas: " + failureCounter.get());
    }
}

