package com.newdev.inservice.requestDtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ImagesDto {

    @NotNull(message = "Main Picture is required")
    private MultipartFile mainPicture;

    @NotNull(message = "At least 1 picture is required")
    private List<MultipartFile> pictures;

}
