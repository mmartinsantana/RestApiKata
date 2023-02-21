package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.message.Sender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(MessageController.PATH)
public class MessageController {

    public static final String PATH="/api/msg";

    public static final String SUB_PATH_SEND = "/send";

    private Sender sender;

    public MessageController(Sender sender) {
        this.sender = sender;
    }

    @PostMapping(value = SUB_PATH_SEND)
    public ResponseEntity<String> send(@RequestBody String message) {
        try {
            sender.send(message);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok().body(message);
    }

}

