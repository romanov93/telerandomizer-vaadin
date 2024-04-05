package ru.romanov.telerandomizer.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.romanov.telerandomizer.dto.PhoneDTO;
import ru.romanov.telerandomizer.entity.Phone;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    @Mapping(target = "phoneNumber", source = "phone.number")
    @Mapping(target = "searchDate", source = "phone.creationDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    PhoneDTO toDTO(Phone phone);

}