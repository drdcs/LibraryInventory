package com.datawhiz.controller;


import com.datawhiz.domain.LibraryEvent;
import com.datawhiz.producer.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping("/v1/libraryevent")

    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent ) throws JsonProcessingException {

        // invoke kafka producer

        /* Serializer -> Key ser. and value Ser.
         * Partitioner -> DefaultPartitioner.
         * Record Accumulator.
         * Buffer memory (linger.ms)
         *
         * */

        libraryEventProducer.sendLibraryEvent(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);

    }


}
