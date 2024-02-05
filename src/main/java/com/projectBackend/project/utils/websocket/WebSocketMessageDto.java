package com.projectBackend.project.utils.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessageDto {
    private String type; // You can add more fields as needed
    private String message;
    // Add other fields as needed
}