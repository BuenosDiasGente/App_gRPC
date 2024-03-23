package org.example;

import org.example.client.NumberGeneratorClient;

public class ClientApp {
    public static void main(String[] args){
        NumberGeneratorClient client = new NumberGeneratorClient("localhost", 8084);
                client.generateNumbers(0, 30);
    }

}
