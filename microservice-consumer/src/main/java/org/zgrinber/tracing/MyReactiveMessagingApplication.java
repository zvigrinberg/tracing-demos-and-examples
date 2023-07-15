package org.zgrinber.tracing;

import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.stream.Stream;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    private Logger LOG = Logger.getLogger(MyReactiveMessagingApplication.class);

    /**
     * Sends message to the "words-out" channel, can be used from a JAX-RS resource or any bean of your application.
     * Messages are sent to the broker.
     **/
    void onStart(@Observes StartupEvent ev) {
//        Stream.of("Hello", "with", "SmallRye", "reactive", "message").forEach(string -> emitter.send(string));
    }

    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("vehicles")
    @Outgoing("uppercase")
    public String toUpperCase(ConsumerRecord<String,String> rec) {
        LOG.infof("Got message from Vehicles channel - event key ={%s}, event message={%s}, event timestamp={%s}, topic name={%s}, event offset in topic={%s}", rec.key(), rec.value(), rec.timestamp(),rec.topic(), rec.offset());
        return rec.value().toUpperCase();
    }

    /**
     * Consume the uppercase channel (in-memory) and print the messages.
     **/
    @Incoming("uppercase")
    public void sink(String word) {
        LOG.info("Converted to Uppercase through the uppercase Channel - " + word);
    }
}
