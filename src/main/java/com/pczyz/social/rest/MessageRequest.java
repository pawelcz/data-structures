package com.pczyz.social.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class MessageRequest {

    @NotEmpty
    private String userEmail;

    @Size(min = 1, max = 140)
    private String messageContent;
}
