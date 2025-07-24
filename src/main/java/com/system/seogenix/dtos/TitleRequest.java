package com.system.seogenix.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TitleRequest (

        @NotBlank(message = "title must not be blank")
        @Size(min = 5,max = 300,message = "title must between 5 and 300")
        String title
){}
