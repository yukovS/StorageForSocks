package ru.skypro.storageforsocks.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.storageforsocks.exception.NeedDeleteMoreSocksThanExistException;
import ru.skypro.storageforsocks.exception.SocksNotFoundException;
import ru.skypro.storageforsocks.mapper.SocksMapper;
import ru.skypro.storageforsocks.model.Operation;
import ru.skypro.storageforsocks.model.Socks;
import ru.skypro.storageforsocks.record.OperateSocksRecord;
import ru.skypro.storageforsocks.record.SocksRecord;
import ru.skypro.storageforsocks.repository.SocksRepository;
import ru.skypro.storageforsocks.service.SocksService;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    @Override
    public String getQuantityOfSocks(String color, Operation operation, Integer cottonPart) {
        List<Socks> socksList = new ArrayList<>();
        switch (operation) {
            case EQUAL -> {
                log.info("Get socks with cotton part equal {}", cottonPart);
                socksList = socksRepository.findAllByColorAndCottonPartEquals(color, cottonPart);
            }
            case LESSTHAN -> {
                log.info("Get socks with cotton part less than {}", cottonPart);
                socksList = socksRepository.findAllByColorAndCottonPartLessThan(color, cottonPart);
            }
            case MORETHAN -> {
                log.info("Get socks with cotton part more than {}", cottonPart);
                socksList = socksRepository.findAllByColorAndCottonPartGreaterThan(color, cottonPart);
            }
        }
        log.info("Gotten quantity of socks successfully");
        return String.valueOf(
                socksList.stream()
                        .mapToInt(Socks::getQuantity)
                        .sum());
    }

    @Override
    public SocksRecord addSocks(OperateSocksRecord incomeSocksRecord) {
        Socks foundedSocks = socksRepository.findSocksByColorAndCottonPart(incomeSocksRecord.getColor(), incomeSocksRecord.getCottonPart());
        if (foundedSocks == null) {
            log.info("New socks are successfully added");
            return socksMapper.toRecord(socksRepository.save(socksMapper.toEntity(incomeSocksRecord)));
        }
        foundedSocks.setQuantity(foundedSocks.getQuantity() + incomeSocksRecord.getQuantity());
        log.info("Founded socks are successfully updated(added)");
        return socksMapper.toRecord(socksRepository.save(foundedSocks));
    }

    @Override
    public SocksRecord deleteSocks(OperateSocksRecord outcomeSocksRecord) {
        Socks foundedSocks = socksRepository.findSocksByColorAndCottonPart(outcomeSocksRecord.getColor(), outcomeSocksRecord.getCottonPart());
        if (foundedSocks == null) {
            log.warn("Socks not found");
            throw new SocksNotFoundException();
        }
        checkQuantityBeforeDeleteSocks(foundedSocks, outcomeSocksRecord);
        foundedSocks.setQuantity(foundedSocks.getQuantity() - outcomeSocksRecord.getQuantity());
        log.info("Founded socks are successfully updated(deleted)");
        return socksMapper.toRecord(socksRepository.save(foundedSocks));
    }

    /**
     * Check quantity of socks
     *
     * @param existingSocks   socks which existed in storage
     * @param outcomeSocksRecord socks which need decrease in storage
     */
    private void checkQuantityBeforeDeleteSocks(Socks existingSocks, OperateSocksRecord outcomeSocksRecord) {
        if (existingSocks.getQuantity() < outcomeSocksRecord.getQuantity()) {
            log.warn("Not correct quantity");
            throw new NeedDeleteMoreSocksThanExistException();
        }
    }
}
