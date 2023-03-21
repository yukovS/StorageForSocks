package ru.skypro.storageforsocks.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skypro.storageforsocks.model.Operation;
import ru.skypro.storageforsocks.record.OperateSocksRecord;
import ru.skypro.storageforsocks.record.SocksRecord;
import ru.skypro.storageforsocks.service.SocksService;


@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
@Validated
public class SocksController {
    private final SocksService socksService;

    /**
     * Get quantity of socks from storage filtered by cotton part(can use 3 operation)
     * Use method of service {@link SocksService#getQuantityOfSocks(String, Operation, Integer)}
     */
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Got quantity of socks"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )})
    @GetMapping
    public ResponseEntity<String> getQuantityOfSocks(@RequestParam @NotBlank(message = "color can't be empty") @Size(min = 3, max = 50, message = "color must be more 2 characters and less 51 characters") String color,
                                                     @RequestParam Operation operation,
                                                     @RequestParam @Min(value = 0,message = "cotton part must be >=0")
                                                         @Max(value = 100, message = "cotton part must be <=100") Integer cottonPart) {
        return new ResponseEntity<>(socksService.getQuantityOfSocks(color, operation, cottonPart), HttpStatus.OK);
    }

    /**
     * Increase quantity of existed socks in storage or add new socks to storage if socks are not existed
     * Use method of service {@link SocksService#addSocks(OperateSocksRecord)}
     */
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Income of socks was successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SocksRecord.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )})
    @PostMapping("/income")
    public ResponseEntity<SocksRecord> addSocks(@RequestBody @Valid OperateSocksRecord incomeSocksRecord) {
        return new ResponseEntity<>(socksService.addSocks(incomeSocksRecord), HttpStatus.OK);
    }

    /**
     * Decrease quantity of existed socks in storage if socks are existed
     * Use method of service {@link SocksService#deleteSocks(OperateSocksRecord)}
     */
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Outcome of socks was successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SocksRecord.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )})
    @PostMapping("/outcome")
        public ResponseEntity<SocksRecord> deleteSocks(@RequestBody @Valid OperateSocksRecord outcomeSocksRecord) {
        return new ResponseEntity<>(socksService.deleteSocks(outcomeSocksRecord), HttpStatus.OK);
    }
}
