package com.error.center.mapper;

import com.error.center.dto.EventDTO;
import com.error.center.dto.EventListDTO;
import com.error.center.dto.PageDTO;
import com.error.center.entity.Event;
import com.error.center.util.enums.Level;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mappings({
            @Mapping(source = "level", target = "level", qualifiedByName = "ToString"),
            @Mapping(source = "createdBy", target = "createdBy"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "lastModifiedBy", target = "lastModifiedBy"),
            @Mapping(source = "lastModifiedAt", target = "lastModifiedAt"),
    })
    EventDTO toDTO(Event object);

    @Mappings({
            @Mapping(source = "level", target = "level", qualifiedByName = "ToString"),
    })
    EventListDTO toListDTO(Event object);

    @Mappings({
            @Mapping(source = "level", target = "level", qualifiedByName = "ToLevel"),
            @Mapping(source = "createdBy", target = "createdBy", ignore = true),
            @Mapping(source = "createdAt", target = "createdAt", ignore = true),
            @Mapping(source = "lastModifiedBy", target = "lastModifiedBy", ignore = true),
            @Mapping(source = "lastModifiedAt", target = "lastModifiedAt", ignore = true),
    })
    Event toEntity(EventDTO entity);

    List<EventDTO> map(List<Event> entities);

    List<EventListDTO> mapList(List<Event> entities);

    default PageDTO<EventListDTO> page(Page<Event> entities) {
        if (entities == null) {
            return null;
        }

        PageDTO<EventListDTO> pageDTO = new PageDTO<>();

        pageDTO.setSize(entities.getSize());
        pageDTO.setNumber(entities.getNumber());
        pageDTO.setTotalElements(entities.getTotalElements());
        pageDTO.setTotalPages(entities.getTotalPages());
        pageDTO.setContent(mapList(entities.getContent()));

        return pageDTO;
    }

    @Named("ToString")
    default String toString(String input) {
        return input.toUpperCase();
    }

    @Named("ToLevel")
    default Level toLevel(String input) {
        return Level.valueOf(input.toUpperCase());
    }
}
