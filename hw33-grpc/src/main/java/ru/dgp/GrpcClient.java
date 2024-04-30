package ru.dgp;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.protobuf.BoundsMessage;
import ru.dgp.protobuf.DataMessage;
import ru.dgp.protobuf.RemoteDataServiceGrpc;

public class GrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteDataServiceGrpc.newStub(channel);

        var exchange = new AtomicLong(0);

        stub.getData(
                BoundsMessage.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
                    @Override
                    public void onNext(DataMessage value) {
                        exchange.set(value.getValue());

                        logger.atInfo()
                                .setMessage("++ Value from server is {}")
                                .addArgument(value.getValue())
                                .log();
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.atError()
                                .setMessage("Something wrong has happened.")
                                .log();
                    }

                    @Override
                    public void onCompleted() {
                        logger.atInfo().setMessage("Server: The End.").log();
                    }
                });

        var value = 0L;
        long numberFromServer;
        var index = 0;

        while (index <= 50) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            numberFromServer = exchange.getAndSet(0);

            value = value + numberFromServer + 1;

            logger.atInfo()
                    .setMessage("-- Current value is {}")
                    .addArgument(value)
                    .log();

            index++;
        }

        logger.atInfo().setMessage("Client: The End.").log();

        channel.shutdown();
    }
}
