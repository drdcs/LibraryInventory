package com.datawhiz.controller;


import com.datawhiz.domain.LibraryEvent;
import com.datawhiz.domain.LibraryEventType;
import com.datawhiz.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")

    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent ) throws JsonProcessingException, ExecutionException, InterruptedException {

        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        // libraryEventProducer.sendLibraryEvent(libraryEvent);
        // log.info("Before sending the message.. ");
        // SendResult<Integer, String > sendResult = libraryEventProducer.sendLibraryEventSynchronous(libraryEvent);
         libraryEventProducer.sendLibraryEventAsyncWithSend(libraryEvent);

        //log.info("SendResult is {} ", sendResult.toString());
        // log.info("After sending the message..");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);

    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent ) throws JsonProcessingException {

        if(libraryEvent.getLibraryEventId()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please pass Library Event ID");
        }
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        libraryEventProducer.sendLibraryEventAsyncWithSend(libraryEvent);
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);

    }


}
