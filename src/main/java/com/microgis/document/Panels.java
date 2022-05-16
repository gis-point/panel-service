package com.microgis.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "panel")
public class Panels {

    @Id
    private Long id;

    private String stopName;

    private int number;

    private String phoneNumber;

    private List<Routes> routes;

}