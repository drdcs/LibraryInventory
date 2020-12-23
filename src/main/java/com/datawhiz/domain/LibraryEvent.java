package com.datawhiz.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // actual getter and setters
@Builder // fluent api style of building this library event domain

public class LibraryEvent {

    private Integer LibraryEventId;
    private Book book;

}
