package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    public Flux<BeerDTO> listBeer(
        @RequestParam(value = "beerStyle", required = false) String beerStyle
    ) {
        return beerService.findAll(beerStyle);
    }

    @GetMapping(BEER_PATH_ID)
    public Mono<BeerDTO> findById(@PathVariable("beerId") Integer beerId) {
        return beerService.findById(beerId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
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
    public Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.update(beerId, beerDTO)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(beer -> ResponseEntity.noContent().build());
    }

    @PatchMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> patchBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.patch(beerId, beerDTO)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(beer -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("beerId") Integer beerId) {
        return beerService.findById(beerId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(beerDTO -> beerService.deleteById(beerDTO.getId()))
            .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
