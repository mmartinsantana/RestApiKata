package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.message.Sender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(MessageController.PATH)
public class MessageController {

    public static final String PATH="/api/msg";

    public static final String SUB_PATH_SEND = "/send";

    public static final String SUB_PATH_SEND_EPHEMERAL = "/send_ephemeral";

    public static final String SUB_PATH_RECOVER = "/recover";

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

    @PostMapping(value = SUB_PATH_SEND_EPHEMERAL)
    public ResponseEntity<String> sendEphemeral(@RequestBody String message) {
        try {
            sender.sendEphemeral(message);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok().body(message);
    }


    @GetMapping(value = SUB_PATH_RECOVER)
    public ResponseEntity<String> recover() {
        String recoveredMessage = null;
        try {
            recoveredMessage = sender.recover();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok().body(recoveredMessage);
    }

}

