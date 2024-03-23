package org.example.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Создание Server для работы с сервисом NumberGenerationService
 */
public class NumberGeneratorServer {
    private Server server;

    /**
     * Метод обеспечивает запуск сервера gRPC на указанном порту
     * и корректную его остановку при завершении программы или прерывании JVM.
     *
     * Добавляется хук Runtime.getRuntime().addShutdownHook(),
     * который будет выполнен при завершении программы или прерывании JVM.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        int port = 8084;
        server = ServerBuilder.forPort(port)
                .addService(new NumberGenerationService())
                .build()
                .start();
        System.out.println("Server started on port: " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server");
            NumberGeneratorServer.this.stop();
        }));
    }

    /**
     * Метод проверяет, существует ли сервер (server),
     * и если да, вызывает метод shutdown(),
     * чтобы корректно остановить сервер.
     */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Метод используется для блокировки основного потока сервера
     * и ожидания его завершения.
     * Если сервер существует (server),
     * вызывается метод awaitTermination(),
     * который блокирует поток до тех пор,
     * пока сервер не завершит свою работу.
     *
     * @throws InterruptedException
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
