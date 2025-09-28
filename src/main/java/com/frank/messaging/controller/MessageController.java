package com.frank.messaging.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.frank.messaging.dto.MessageDTO;
import com.frank.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired private MessageService messageService;

    @PostMapping("/send")
    public String sendMessage() {
        return this.messageService.sendMessage();
    }

    @GetMapping("/receive")
    public DeferredResult<List<MessageDTO>> receiveMessages(@RequestParam Integer lastReceivedMessageId) {
        var deferredResult = new DeferredResult<List<MessageDTO>>();
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
                deferredResult.setResult(List.of());
                return null;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        System.out.println("deferred");
        return deferredResult;
    }
}
