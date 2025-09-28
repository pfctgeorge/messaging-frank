package com.frank.messaging.service;


import java.net.URL;
import java.time.Duration;

import com.frank.messaging.dao.MessageDAO;
import com.frank.messaging.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
public class MessageService {

    private static final String bucketName = "jianjin-messaging-user-file";
    @Autowired S3Presigner s3Presigner;
    @Autowired MessageDAO messageDAO;

    public String sendMessage() {

        MessageDTO messageDTO = new MessageDTO();
        this.messageDAO.insert(messageDTO);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(messageDTO.getId() % 10000 + "/" + messageDTO.getId())
                .build();

        PresignedPutObjectRequest presignedRequest =
                this.s3Presigner.presignPutObject(r -> r.putObjectRequest(putObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));


        URL presignedUrl = presignedRequest.url();

        this.sendNotification(messageDTO.getReceiverUserId(),
                              messageDTO.getGroupChatId(),
                              messageDTO.getId());
        return presignedUrl.toString();
    }

    private void sendNotification(Integer receiverUserId, Integer groupChatId, Integer messageId) {
        

    }
}
