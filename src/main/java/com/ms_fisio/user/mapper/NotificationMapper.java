package com.ms_fisio.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import com.ms_fisio.user.domain.dto.NotificationDTO;
import com.ms_fisio.user.domain.model.NotificationModel;

@Mapper(componentModel = ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    
    NotificationModel toModel(NotificationDTO notificationDTO);

    NotificationDTO toDTO(NotificationModel notificationModel);

    List<NotificationModel> toModelList(List<NotificationDTO> notificationDTOs);

    List<NotificationDTO> toDTOList(List<NotificationModel> notificationModels);
}
