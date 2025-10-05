package com.frank.messaging.service;


import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frank.messaging.dao.MessageDAO;
import com.frank.messaging.dto.MessageDTO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.enumeration.MessageType;
import com.frank.messaging.response.UserResponse;
import com.frank.messaging.types.MessageNotification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@Log4j2
public class MessageService {

    private static final String bucketName = "jianjin-messaging-user-file";
    @Autowired private S3Presigner s3Presigner;
    @Autowired private MessageDAO messageDAO;
    @Autowired private ApiGatewayManagementApiClient apiGatewayManagementApiClient;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DynamoDbClient dynamoDbClient;

    public String sendMessage(UserDTO sender, Integer receiverUserId, Integer groupChatId, MessageType messageType)
            throws Exception {

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSendTime(new Date());
        messageDTO.setGroupChatId(groupChatId);
        messageDTO.setReceiverUserId(receiverUserId);
        messageDTO.setSenderUserId(sender.getId());
        messageDTO.setMessageType(messageType);
        this.messageDAO.insert(messageDTO);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("senderUserId", sender.getId().toString());
        metadata.put("senderNickname", sender.getNickname());
        metadata.put("senderUsername", sender.getUsername());
        metadata.put("messageId", String.valueOf(messageDTO.getId()));
        metadata.put("sendTime", String.valueOf(messageDTO.getSendTime().getTime()));
        metadata.put("messageType", messageType.name());
        if (groupChatId != null) {
            metadata.put("groupChatUserIds", "");
        }
        if (receiverUserId != null) {
            metadata.put("receiverUserId", receiverUserId.toString());
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .metadata(metadata)
                .key(messageDTO.getId() % 10000 + "/" + messageDTO.getId())
                .build();

        PresignedPutObjectRequest presignedRequest =
                this.s3Presigner.presignPutObject(r -> r.putObjectRequest(putObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));


        URL presignedUrl = presignedRequest.url();

        this.sendNotification(messageDTO.getReceiverUserId(),
                              messageDTO.getGroupChatId(),
                              sender,
                              messageDTO.getId(),
                              messageDTO.getSendTime(),
                              messageDTO.getMessageType());
        return presignedUrl.toString();
    }

    private void sendNotification(Integer receiverUserId, Integer groupChatId, UserDTO sender, Integer messageId,
                                  Date sendTime, MessageType messageType) throws Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(messageId % 10000 + "/" + messageId)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest =
                this.s3Presigner.presignGetObject(r -> r.getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));
        String presignedUrl = presignedGetObjectRequest.url().toString();

        UserResponse userResponse = UserResponse.builder()
                .id(sender.getId())
                .username(sender.getUsername())
                .nickname(sender.getNickname())
                .build();
        MessageNotification messageNotification = MessageNotification.builder()
                .sender(userResponse)
                .messageId(messageId)
                .messagePresignedUrl(presignedUrl)
                .sendTime(sendTime)
                .messageType(messageType)
                .build();

        QueryRequest queryRequest = null;
        if (receiverUserId != null) {
            queryRequest = QueryRequest.builder()
                    .tableName("user-connections-frank")
                    .keyConditions(Map.of(
                            "UserId", Condition.builder()
                                    .comparisonOperator(ComparisonOperator.EQ)
                                    .attributeValueList(AttributeValue.builder()
                                                                .s(receiverUserId.toString())
                                                                .build())
                                    .build()
                    ))
                    .build();
            this.dynamoDbClient.query(queryRequest);

        } else {
            // TODO: construct a queryRequest with all userIds in the groupChat as key conditions

        }
        QueryResponse queryResponse = this.dynamoDbClient.query(queryRequest);
        for (Map<String, AttributeValue> item : queryResponse.items()) {
            String connectionId = item.get("ConnectionId").s();
            String userId = item.get("UserId").s();
            try {
                PostToConnectionRequest postToConnectionRequest = PostToConnectionRequest.builder()
                        .connectionId(connectionId)
                        .data(SdkBytes.fromUtf8String(this.objectMapper.writeValueAsString(messageNotification)))
                        .build();
                this.apiGatewayManagementApiClient.postToConnection(postToConnectionRequest);
            } catch (GoneException goneException) {
                log.info("Connection {} for user {} is gone", connectionId, userId);
                DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                        .tableName("user-connections-frank")
                        .key(Map.of(
                                "UserId", AttributeValue.builder().s(userId).build(),
                                "ConnectionId", AttributeValue.builder().s(connectionId).build()
                        ))
                        .build();
                this.dynamoDbClient.deleteItem(deleteItemRequest);
            }
        }
    }
}
