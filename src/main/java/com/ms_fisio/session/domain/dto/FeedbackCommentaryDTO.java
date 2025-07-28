package com.ms_fisio.session.domain.dto;

import java.time.LocalDateTime;

public interface FeedbackCommentaryDTO {
    String getType(); // debe devolver "commentary"
    String getSenderName(); // nombre del paciente
    String getMessage(); // feedback
    LocalDateTime getSentAt(); // fecha de la sesión
}
