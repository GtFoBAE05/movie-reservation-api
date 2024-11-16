package org.imannuel.moviereservationapi.service.impl;

import jakarta.annotation.PostConstruct;
import org.imannuel.moviereservationapi.constant.PaymentStatusEnum;
import org.imannuel.moviereservationapi.entity.PaymentStatus;
import org.imannuel.moviereservationapi.repository.PaymentStatusRepository;
import org.imannuel.moviereservationapi.service.PaymentStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final PaymentStatusRepository paymentStatusRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void init(){
        Arrays.stream(PaymentStatusEnum.values())
                .map(Enum::name)
                .filter(name -> !checkIsPaymentStatusExists(name))
                .forEach(this::createPaymentStatus);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPaymentStatus(String name) {
        if(checkIsPaymentStatusExists(name)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment status already exists");
        }
        paymentStatusRepository.insertPaymentStatus(name);
    }

    @Override
    public PaymentStatus findPaymentStatusByName(String name) {
        return paymentStatusRepository.findPaymentStatusByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment status not found"));
    }

    @Override
    public boolean checkIsPaymentStatusExists(String name) {
        return paymentStatusRepository.existsPaymentStatusByName(name);
    }
}
