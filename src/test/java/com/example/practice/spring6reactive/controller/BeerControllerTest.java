package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.domain.Beer;
import com.example.practice.spring6reactive.dto.BeerDTO;
import java.math.BigDecimal;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.example.practice.spring6reactive.controller.BeerController.BEER_PATH;
import static com.example.practice.spring6reactive.controller.BeerController.BEER_PATH_ID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(10)
    void listBeer() {
        webTestClient.get().uri(BEER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(20)
    void findById() {
        webTestClient.get().uri(BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody(BeerDTO.class);
    }

    @Test
    @Order(25)
    void findByIdNotFound() {
        webTestClient.get().uri(BEER_PATH_ID, 9999)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(30)
    void saveBeer() {
        webTestClient.post().uri(BEER_PATH)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(30)
    void saveBeerBadRequest() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient.post().uri(BEER_PATH)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void updateBeer() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient.put().uri(BEER_PATH_ID, 1)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(44)
    void updateBeerBadRequest() {
        webTestClient.put().uri(BEER_PATH_ID, 1)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(48)
    void updateBeerNotFound() {
        webTestClient.put().uri(BEER_PATH_ID, 100)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(50)
    void patchBeer() {
        webTestClient.patch().uri(BEER_PATH_ID, 1)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(54)
    void patchBeerBadRequest() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient.patch().uri(BEER_PATH_ID, 1)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(58)
    void patchBeerNotFound() {
        webTestClient.patch().uri(BEER_PATH_ID, 100)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(9999)
    void deleteById() {
        webTestClient.delete().uri(BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(99999)
    void deleteByIdNotFound() {
        webTestClient.delete().uri(BEER_PATH_ID, 1000)
            .exchange()
            .expectStatus().isNotFound();
    }

    BeerDTO getBeer(){
        return BeerDTO.builder()
            .beerName("Monster Feel")
            .beerStyle("IPA")
            .upc("1223345")
            .price(BigDecimal.TEN)
            .quantityOnHand(10)
            .build();
    }
}