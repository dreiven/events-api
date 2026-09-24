//package com.fever.eventsapi.entity;
//
//import com.fever.eventsapi.dto.EventDto;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface EventEntityStructMapper {
//
//    @Mapping(target = "startDateTime",
//            expression = "java(java.time.LocalDateTime.of(dto.getStartDate(), dto.getStartTime()))")
//    @Mapping(target = "endDateTime",
//            expression = "java(dto.getEndDate() != null && dto.getEndTime() != null "
//                    + "? java.time.LocalDateTime.of(dto.getEndDate(), dto.getEndTime()) : null)")
//    EventEntity toEntity(EventDto dto);
//
//    EventDto toDto(EventEntity entity);
//}
