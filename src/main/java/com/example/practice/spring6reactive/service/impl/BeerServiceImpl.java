package com.example.practice.spring6reactive.service.impl;

import com.example.practice.spring6reactive.domain.Beer;
import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.mapper.BeerMapper;
import com.example.practice.spring6reactive.repositories.BeerRepository;
import com.example.practice.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Flux<BeerDTO> findAll(String beerStyle) {
        Flux<Beer> flux;
        if(StringUtils.hasLength(beerStyle)){
            flux = beerRepository.findByBeerStyle(beerStyle);
        } else {
            flux = beerRepository.findAll();
        }
        return flux.map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> findById(Integer beerId) {
        return beerRepository.findById(beerId).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> save(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)).map(beerMapper::beerToBeerDTO);
    }


    @Override
    public Mono<BeerDTO> update(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId).map(
                beer -> {
                    beer.setBeerName(beerDTO.getBeerName());
                    beer.setBeerStyle(beerDTO.getBeerStyle());
                    beer.setPrice(beerDTO.getPrice());
                    beer.setUpc(beerDTO.getUpc());
                    beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return beer;
                }
            ).flatMap(beerRepository::save)
            .map(beerMapper::beerToBeerDTO);
    }


    @Override
    public Mono<BeerDTO> patch(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId).map(
                beer -> {
                    if(StringUtils.hasLength(beerDTO.getBeerName())){
                        beer.setBeerName(beerDTO.getBeerName());
                    }
                    if(StringUtils.hasLength(beerDTO.getBeerStyle())){
                        beer.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if(StringUtils.hasLength(beerDTO.getUpc())){
                        beer.setUpc(beerDTO.getUpc());
                    }
                    if(beerDTO.getPrice() != null) {
                        beer.setPrice(beerDTO.getPrice());
                    }
                    if(beerDTO.getQuantityOnHand() != null) {
                        beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    return beer;
                }
            ).flatMap(beerRepository::save)
            .map(beerMapper::beerToBeerDTO);
    }


    @Override
    public Mono<Void> deleteById(Integer beerId) {
        return beerRepository.deleteById(beerId);
    }
}
