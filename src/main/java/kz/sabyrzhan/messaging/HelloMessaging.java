package kz.sabyrzhan.messaging;

import io.smallrye.reactive.messaging.MutinyEmitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class HelloMessaging {
    @Channel("userChannel")
    private MutinyEmitter<Long> userChannel;

//    @Channel("userChannel")
//    private Multi<Long> userChannelConsumer;

    public void sendToUserChannel(Long message) {
        userChannel.send(
                Message.of(message,
                    () -> CompletableFuture.completedFuture(null),
                    t -> CompletableFuture.completedFuture(null))
        );
    }

//    public void init(@Observes StartupEvent event) {
//        userChannelConsumer.subscribe().with(s -> {
//            System.out.println(s);
//        });
//    }

    @Incoming("userChannel")
    @Outgoing("hello")
    public Message<String> userChannelConsume(Message<Long> data) {
        return data.withPayload("Hello chained - " + data.getPayload());
    }

    @Incoming("hello")
    public void print(String msg) {
        System.out.println(msg);
    }
}
