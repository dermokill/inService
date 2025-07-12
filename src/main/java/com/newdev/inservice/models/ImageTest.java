package com.newdev.inservice.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "image")
public class ImageTest {

    private String id =  UUID.randomUUID().toString();

    private String imagePath;

    public ImageTest(String imagePath) {
        this.imagePath = imagePath;
    }
}
