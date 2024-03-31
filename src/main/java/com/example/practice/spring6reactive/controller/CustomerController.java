package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.CustomerDTO;
import com.example.practice.spring6reactive.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    public static final String BASE_PATH = "http://localhost:8080";
    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    public Flux<CustomerDTO> findAll(){
        return customerService.findAll();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public Mono<CustomerDTO> findById(@PathVariable("customerId") Integer customerId){
        return customerService.findById(customerId);
    }

    @PostMapping(CUSTOMER_PATH)
    public Mono<ResponseEntity<Void>> save(@RequestBody CustomerDTO customerDTO){
        return customerService.save(customerDTO)
            .map(customer -> ResponseEntity.created(
                UriComponentsBuilder.fromHttpUrl(BASE_PATH + CUSTOMER_PATH + "/" + customer.getId())
                    .build()
                    .toUri()
                ).build()
            );
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> update(@PathVariable("customerId") Integer customerId, @RequestBody CustomerDTO customerDTO){
        return customerService.update(customerId, customerDTO)
            .map(customer -> ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> patch(@PathVariable("customerId") Integer customerId, @RequestBody CustomerDTO customerDTO){
        return customerService.patch(customerId, customerDTO)
            .map(customer -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public Mono<ResponseEntity<Void>> delete(@PathVariable("customerId") Integer customerId){
        return customerService.deleteById(customerId)
            .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
