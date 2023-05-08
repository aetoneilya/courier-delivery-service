package ru.yandex.yandexlavka.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.request.CreateCourierRequest;
import ru.yandex.yandexlavka.dto.response.*;

import java.time.LocalDate;

@Validated
public interface CouriersApi {
    @Operation(summary = "Список распределенных заказов", description = "", tags = {"courier-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderAssignResponse.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @GetMapping(value = "/couriers/assignments", produces = {"application/json"})
    ResponseEntity<OrderAssignResponse> couriersAssignments(@Parameter(in = ParameterIn.QUERY, description = "Дата распределения заказов. Если не указана, то используется текущий день", schema = @Schema()) @Valid @RequestParam(value = "date", required = false) LocalDate date, @Parameter(in = ParameterIn.QUERY, description = "Идентификатор курьера для получения списка распредленных заказов. Если не указан, возвращаются данные по всем курьерам.", schema = @Schema()) @Valid @RequestParam(value = "courier_id", required = false) Integer courierId);


    @Operation(summary = "", description = "", tags = {"courier-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouriersResponse.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @PostMapping(value = "/couriers", produces = {"application/json"}, consumes = {"application/json"})
    ResponseEntity<CreateCouriersResponse> createCourier(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody CreateCourierRequest body);


    @Operation(summary = "", description = "", tags = {"courier-controller"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourierDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundResponse.class)))})
    @GetMapping(value = "/couriers/{courier_id}", produces = {"application/json"})
    ResponseEntity<CourierDto> getCourierById(@Parameter(in = ParameterIn.PATH, description = "Courier identifier", required = true, schema = @Schema()) @PathVariable("courier_id") Long courierId);


    @Operation(summary = "", description = "", tags = {"courier-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCourierMetaInfoResponse.class)))})
    @GetMapping(value = "/couriers/meta-info/{courier_id}", produces = {"application/json"})
    ResponseEntity<GetCourierMetaInfoResponse> getCourierMetaInfo(@Parameter(in = ParameterIn.PATH, description = "Courier identifier", required = true, schema = @Schema()) @PathVariable("courier_id") Long courierId, @NotNull @Parameter(in = ParameterIn.QUERY, description = "Rating calculation start date", required = true, schema = @Schema()) @Valid @RequestParam(value = "startDate", required = true) LocalDate startDate, @NotNull @Parameter(in = ParameterIn.QUERY, description = "Rating calculation end date", required = true, schema = @Schema()) @Valid @RequestParam(value = "endDate", required = true) LocalDate endDate);


    @Operation(summary = "", description = "", tags = {"courier-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetCouriersResponse.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @GetMapping(value = "/couriers", produces = {"application/json"})
    ResponseEntity<GetCouriersResponse> getCouriers(@Parameter(in = ParameterIn.QUERY, description = "Максимальное количество курьеров в выдаче. Если параметр не передан, то значение по умолчанию равно 1.", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "Количество курьеров, которое нужно пропустить для отображения текущей страницы. Если параметр не передан, то значение по умолчанию равно 0.", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset);

}
