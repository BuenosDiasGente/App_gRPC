package org.example.server;

import com.example.grpc.NumberGeneratorGrpc;
import com.example.grpc.NumberGeneratorOuterClass;
import io.grpc.stub.StreamObserver;

public class NumberGenerationService extends NumberGeneratorGrpc.NumberGeneratorImplBase {

    /**
     * Метод получает запрос от клиента ввиде 2-х чисел (firstValue и lastValue)
     * и раз в 2 сек. отправляет клиенту сгенерированное новое значение (currentValue).
     *
     * @param numberRequest
     * @param responseObserver
     */
    @Override
    public void generateNumbers(NumberGeneratorOuterClass.NumberRequest numberRequest,
                                StreamObserver<NumberGeneratorOuterClass.NumberResponse> responseObserver) {

        System.out.println(numberRequest);

        int firstValue = numberRequest.getFirstValue();
        int lastValue = numberRequest.getLastValue();

        int currentValue = firstValue;

        try {
            while (currentValue <= lastValue) {
                NumberGeneratorOuterClass.NumberResponse response = NumberGeneratorOuterClass.NumberResponse.newBuilder()
                        .setValue(currentValue)
                        .build();
                responseObserver.onNext(response);
                Thread.sleep(2000);
                currentValue++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }
}
