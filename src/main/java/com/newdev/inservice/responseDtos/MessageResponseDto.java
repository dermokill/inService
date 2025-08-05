package com.newdev.inservice.responseDtos;

import java.time.LocalDateTime;

public record MessageResponseDto(String id,
                                 SenderDto senderDto,
                                 String content,
                                 LocalDateTime sentAt) {
}
