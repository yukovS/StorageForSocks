package ru.skypro.storageforsocks.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.storageforsocks.model.Socks;
import ru.skypro.storageforsocks.record.OperateSocksRecord;
import ru.skypro.storageforsocks.record.SocksRecord;


@Mapper
public interface SocksMapper {
    @Mapping(target = "id", ignore = true)
    Socks toEntity(OperateSocksRecord operateSocksRecord);

    SocksRecord toRecord(Socks socks);
}
