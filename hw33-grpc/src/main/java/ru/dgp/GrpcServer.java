package ru.dgp;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.service.RemoteDataService;

public class GrpcServer {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var service = new RemoteDataService();

        var server = ServerBuilder.forPort(SERVER_PORT).addService(service).build();
        server.start();

        logger.atInfo().setMessage("server waiting for client connections...").log();

        server.awaitTermination();
    }
}
