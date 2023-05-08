package ru.yandex.yandexlavka.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.CompleteOrderRequestDto;
import ru.yandex.yandexlavka.dto.request.CreateOrderRequest;
import ru.yandex.yandexlavka.dto.response.BadRequestResponse;
import ru.yandex.yandexlavka.dto.response.NotFoundResponse;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.dto.OrderDto;

import java.time.LocalDate;
import java.util.List;

@Validated
public interface OrdersApi {

    @Operation(summary = "", description = "", tags = {"order-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @PostMapping(value = "/orders/complete",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<OrderDto>> completeOrder(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody CompleteOrderRequestDto body);


    @Operation(summary = "", description = "", tags = {"order-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),

            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @PostMapping(value = "/orders",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<OrderDto>> createOrder(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody CreateOrderRequest body);


    @Operation(summary = "", description = "", tags = {"order-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class))),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotFoundResponse.class)))})
    @GetMapping(value = "/orders/{order_id}",
            produces = {"application/json"})
    ResponseEntity<OrderDto> getOrder(@Parameter(in = ParameterIn.PATH, description = "Order identifier", required = true, schema = @Schema()) @PathVariable("order_id") Long orderId);


    @Operation(summary = "", description = "", tags = {"order-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))),

            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @GetMapping(value = "/orders",
            produces = {"application/json"})
    ResponseEntity<List<OrderDto>> getOrders(@Parameter(in = ParameterIn.QUERY, description = "Максимальное количество заказов в выдаче. Если параметр не передан, то значение по умолчанию равно 1.", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "Количество заказов, которое нужно пропустить для отображения текущей страницы. Если параметр не передан, то значение по умолчанию равно 0.", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset);


    @Operation(summary = "Распределение заказов по курьерам", description = "Для распределения заказов между курьерами учитываются следующие параметры: <ul><li>вес заказа</li><li>регион доставки</li><li>стоимость доставки</li></ul>", tags = {"order-controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ok", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderAssignResponse.class)))),
            @ApiResponse(responseCode = "400", description = "bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadRequestResponse.class)))})
    @PostMapping(value = "/orders/assign",
            produces = {"application/json"})
    ResponseEntity<List<OrderAssignResponse>> ordersAssign(@Parameter(in = ParameterIn.QUERY, description = "Дата распределения заказов. Если не указана, то используется текущий день", schema = @Schema()) @Valid @RequestParam(value = "date", required = false) LocalDate date);

}
