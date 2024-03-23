package org.example;

import org.example.server.NumberGeneratorServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException, InterruptedException {

        NumberGeneratorServer server = new NumberGeneratorServer();
        server.start();
        server.blockUntilShutdown();
    }
}