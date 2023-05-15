package ru.yandex.yandexlavka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.yandexlavka.configuration.ApplicationConfig;
import ru.yandex.yandexlavka.dto.CourierDto;
import ru.yandex.yandexlavka.dto.CourierTypeEnum;
import ru.yandex.yandexlavka.dto.CreateCourierDto;
import ru.yandex.yandexlavka.dto.mapper.DtoMapper;
import ru.yandex.yandexlavka.dto.response.GetCourierMetaInfoResponse;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponse;
import ru.yandex.yandexlavka.model.Courier;
import ru.yandex.yandexlavka.model.mapper.CourierMapper;
import ru.yandex.yandexlavka.repository.AssignmentsRepository;
import ru.yandex.yandexlavka.repository.CourierRepository;
import ru.yandex.yandexlavka.repository.requests.OffsetBasedPageRequest;
import ru.yandex.yandexlavka.service.exceptions.NotFoundException;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouriersService {
    private final CourierRepository courierRepository;
    private final AssignmentsRepository assignmentsRepository;
    private final CourierMapper courierMapper;
    private final DtoMapper dtoMapper;
    private final ApplicationConfig config;


    @Transactional
    public List<CourierDto> createCouriers(List<CreateCourierDto> couriers) {
        List<Courier> courierList = courierRepository.saveAll(courierMapper.toListCourier(couriers));
        return dtoMapper.toCourierDtoList(courierList);
    }

    @Transactional(readOnly = true)
    public CourierDto getCourierById(Long id) {
        return dtoMapper.toCourierDto(
                courierRepository.findById(id)
                        .orElseThrow(NotFoundException::new));
    }

    @Transactional(readOnly = true)
    public List<CourierDto> getCouriers(Integer limit, Integer offset) {
        return dtoMapper.toCourierDtoList(
                courierRepository.findAll(new OffsetBasedPageRequest(offset, limit, Sort.by("id"))).getContent());
    }


    @Transactional(readOnly = true)
    public GetCourierMetaInfoResponse getCourierMetaInfo(Long courierId, LocalDate startDate, LocalDate endDate) {
        CourierDto courier = dtoMapper.toCourierDto(courierRepository.findById(courierId).orElseThrow(NotFoundException::new));

        List<Integer> earnings = courierRepository
                .getEarnedMoney(courierId,
                        startDate.atTime(0,0).atOffset(ZoneOffset.UTC),
                        endDate.atTime(0,0).atOffset(ZoneOffset.UTC));
        Integer sumEarnings = null, rating = null;

        if (earnings.size() != 0) {
            sumEarnings = earnings.stream().mapToInt(i -> i).sum() * config.earning().get(courier.courierType());
            rating = (earnings.size() / (Period.between(startDate, endDate).getDays() * 24)) * config.rating().get(courier.courierType());
        }

        return new GetCourierMetaInfoResponse(courierId,
                courier.courierType(),
                courier.regions(),
                courier.workingHours(),
                rating,
                sumEarnings);
    }

    @Transactional(readOnly = true)
    public OrderAssignResponse getOrderAssignmentsForCourier(Long courierId) {
        return dtoMapper.toOrderAssignResponse(assignmentsRepository.findAllByCourierId(courierId));
    }

    @Transactional(readOnly = true)
    public OrderAssignResponse getOrderAssignmentsAll(){
        return dtoMapper.toOrderAssignResponse(assignmentsRepository.findAll());
    }
}
