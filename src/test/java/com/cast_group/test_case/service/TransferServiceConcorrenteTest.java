package com.cast_group.test_case.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class TransferServiceConcorrenteTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private ApplicationContext context;

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

    @Test
    public void testarTransferenciaConcorrente() {
        // Obtém duas instâncias do TransferService para simular concorrência.
        TransferService transferService1 = context.getBean(TransferService.class);
        TransferService transferService2 = context.getBean(TransferService.class);

        // Define contas e valor de transferência
        Long fromAccountId = 8L;
        Long fromAccountId16 = 8L;
        Long fromAccountId15 = 8L;
        Long fromAccountId14 = 8L;
        Long toAccountId = 9L;
        double amount = 50.0;

        // Executa duas transferências simultâneas
        Runnable task1 = () -> transferService1.transferWithRetry(fromAccountId, toAccountId, amount);       // toAccountId, fromAccountId
        Runnable task2 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task3 = () -> transferService1.transferWithRetry(toAccountId, fromAccountId, amount);
        Runnable task4 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task5 = () -> transferService1.transferWithRetry(toAccountId, fromAccountId, amount);
        Runnable task6 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task7 = () -> transferService1.transferWithRetry(toAccountId, fromAccountId, amount);
        Runnable task8 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task9 = () -> transferService1.transferWithRetry(toAccountId, fromAccountId, amount);
        Runnable task10 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task11 = () -> transferService1.transferWithRetry(toAccountId, fromAccountId, amount);
        Runnable task12 = () -> transferService2.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task13 = () -> transferService1.transferWithRetry(fromAccountId, toAccountId, amount);
        Runnable task14 = () -> transferService2.transferWithRetry(fromAccountId14, toAccountId, amount);
        Runnable task15 = () -> transferService1.transferWithRetry(fromAccountId15, toAccountId, amount);
        Runnable task16 = () -> transferService2.transferWithRetry(fromAccountId16, toAccountId, amount);

        // Executa em paralelo e valida se ambas tiveram sucesso
        assertDoesNotThrow(() -> {
            task1.run();
            task2.run();
            task3.run();
            task4.run();
            task5.run();
            task6.run();
            task7.run();
            task8.run();
            task9.run();
            task10.run();
            task11.run();
            task12.run();
            task13.run();
            task14.run();
            task15.run();
            task16.run();
        });
    }
}

