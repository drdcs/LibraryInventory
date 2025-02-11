package com.datawhiz.producer;

import com.datawhiz.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j

public class LibraryEventProducer {

    String topic = "library-events";
    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    public void sendLibraryEventAsyncWithSend(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {

        List<Header>  recordHeader = List.of(new RecordHeader("event_source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeader);
    }

    public SendResult<Integer, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        SendResult<Integer, String> sendResult = null;

        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get();

        } catch (ExecutionException | InterruptedException e) {

            log.error("ExecutionException/InterruptedException sending the message and the exception is {}", e.getMessage());
            throw e;

        } catch ( Exception e) {

            log.error("Exception sending the message and the exception is {}", e.getMessage());
            throw e;
        }

        return sendResult;
    }

    public void handleSuccess(Integer key, String value, SendResult<Integer, String> result){
        log.info("message send sucessfully for the key: {} and value is {}, partition is {} ", key, value, result.getRecordMetadata().partition());

    }

    public void handleFailure(Integer key, String value, Throwable ex){
        log.info("Error sending message and the exception is {}", ex.getMessage());
        try{
            throw ex;
        }catch(Throwable throwable){
            log.error("Error sending message: {} ", throwable.getMessage());
        }

    }

}
