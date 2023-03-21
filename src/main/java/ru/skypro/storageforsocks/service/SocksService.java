package ru.skypro.storageforsocks.service;


import ru.skypro.storageforsocks.model.Operation;
import ru.skypro.storageforsocks.record.OperateSocksRecord;
import ru.skypro.storageforsocks.record.SocksRecord;

public interface SocksService {
    /**
     *Get quantity of socks from storage filtered by cotton part
     * @param color of socks which need
     * @param operation which need(equal, more than or less than)
     * @param cottonPart % of cotton which need
     * @return quantity of needed socks in storage
     */
    String getQuantityOfSocks(String color, Operation operation, Integer cottonPart);

    /**
     * Increase quantity of existed socks in storage or add new socks to storage if socks are not existed
     * @param incomeSocksRecord socks which need increase quantity or create
     * @return Updated socks
     */
    SocksRecord addSocks(OperateSocksRecord incomeSocksRecord);

    /**
     * Decrease quantity of existed socks in storage if socks are existed
     * @param outcomeSocksRecord socks which need decrease quantity
     * @return Updated socks
     */
    SocksRecord deleteSocks(OperateSocksRecord outcomeSocksRecord);
}
