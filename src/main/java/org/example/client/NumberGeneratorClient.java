package org.example.client;


import com.example.grpc.NumberGeneratorGrpc;
import com.example.grpc.NumberGeneratorOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


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
     * Метод формирования запроса к серверу
     *
     * @param firstValue
     * @param lastValue
     */

    public void generateNumbers(int firstValue, int lastValue) {
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
            }


            /**
             * В методе выполняется цикл, который выводит
             * в консоль значения currentValue каждую секунду.
             * Значение currentValue обновляется с учетом последнего числа от сервера (lastNumber).
             */
            @Override
            public void onCompleted() {
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
            }
        });
    }
}
