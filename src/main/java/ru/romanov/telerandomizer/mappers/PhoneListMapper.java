package ru.romanov.telerandomizer.mappers;

import org.mapstruct.Mapper;
import ru.romanov.telerandomizer.dto.PhoneDTO;
import ru.romanov.telerandomizer.entity.Phone;

import java.util.List;


@Mapper(componentModel = "spring", uses = PhoneMapper.class)
public interface PhoneListMapper {

    List<PhoneDTO> toDTOList(List<Phone> phones);
}
