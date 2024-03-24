package org.example.client;


import com.example.grpc.NumberGeneratorGrpc;
import com.example.grpc.NumberGeneratorOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class NumberGeneratorClient {
    private final ManagedChannel channel;
    private final NumberGeneratorGrpc.NumberGeneratorStub stub;
    private int lastNumber = 0;

    /**
     * Конструктор канала передачи данных.
     *
     * @param host
     * @param port
     */
    public NumberGeneratorClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = NumberGeneratorGrpc.newStub(channel);
    }


    /**
     * Метод формирует запрос к серверу.
     * Используем счетчик CountDownLatch для ожидания завершения всех ответов от сервера.
     *
     * @param firstValue
     * @param lastValue
     */
    public void generateNumbers(int firstValue, int lastValue) {

        CountDownLatch latch = new CountDownLatch(1);

        NumberGeneratorOuterClass.NumberRequest request = NumberGeneratorOuterClass.NumberRequest.newBuilder()
                .setFirstValue(firstValue)
                .setLastValue(lastValue)
                .build();
        stub.generateNumbers(request, new StreamObserver<NumberGeneratorOuterClass.NumberResponse>() {

            @Override
            public void onNext(NumberGeneratorOuterClass.NumberResponse response) {
                lastNumber = response.getValue();
                System.out.println("число от сервера: " + lastNumber);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                latch.countDown();
            }


            /**
             * В методе выполняется цикл, который выводит
             * в консоль значения currentValue каждую секунду.
             * Значение currentValue обновляется с учетом последнего числа от сервера (lastNumber).
             */
            @Override
            public void onCompleted() {
                latch.countDown(); // Уменьшаем счетчик для завершения ожидания
            }
        });
        try {
            latch.await(); // Ожидаем завершения ответов от сервера
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int currentValue = 0;
        while (currentValue <= 50) {
            System.out.println("currentValue: " + currentValue);
            currentValue = currentValue + lastNumber + 1;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        channel.shutdown();
    }

    public static class ClientApp {
        public static void main(String[] args){
            NumberGeneratorClient client = new NumberGeneratorClient("localhost", 8084);
                    client.generateNumbers(0, 30);
        }

    }
}
