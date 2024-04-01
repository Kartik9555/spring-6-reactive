package com.example.practice.spring6reactive.service;

import com.example.practice.spring6reactive.dto.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> findAll(String beerStyle);

    Mono<BeerDTO> findById(Integer beerId);

    Mono<BeerDTO> save(BeerDTO beerDTO);

    Mono<BeerDTO> update(Integer beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patch(Integer beerId, BeerDTO beerDTO);

    Mono<Void> deleteById(Integer beerId);
}
