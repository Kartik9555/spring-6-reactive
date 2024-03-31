package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BASE_PATH = "http://localhost:8080";
    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;


    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeer() {
        return beerService.findAll();
    }

    @GetMapping(BEER_PATH_ID)
    public Mono<BeerDTO> findById(@PathVariable("beerId") Integer beerId) {
        return beerService.findById(beerId);
    }

    @PostMapping(BEER_PATH)
    public Mono<ResponseEntity<Void>> saveBeer(@Validated @RequestBody BeerDTO beerDTO) {
        return beerService.save(beerDTO)
            .map(beer -> ResponseEntity.created(
                UriComponentsBuilder.fromHttpUrl(
                        BASE_PATH + BEER_PATH + "/" + beer.getId()
                    ).build()
                    .toUri()
            ).build());
    }

    @PutMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO){
        return beerService.update(beerId, beerDTO)
            .map(beer -> ResponseEntity.noContent().build());
    }

    @PatchMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO){
        return beerService.patch(beerId, beerDTO)
            .map(beer -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("beerId") Integer beerId){
        return beerService.deleteById(beerId)
            .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
