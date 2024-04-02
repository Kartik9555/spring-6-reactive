package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.dto.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.example.practice.spring6reactive.controller.CustomerController.CUSTOMER_PATH;
import static com.example.practice.spring6reactive.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(10)
    void findAll() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(CUSTOMER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(20)
    void findById() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(25)
    void findByIdNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(CUSTOMER_PATH_ID, 100)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(30)
    void save() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .post()
            .uri(CUSTOMER_PATH)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(35)
    void saveBadRequest() {
        CustomerDTO customerDTO = getCustomer();
        customerDTO.setName("");
        webTestClient
            .mutateWith(mockOAuth2Login())
            .post()
            .uri(CUSTOMER_PATH)
            .body(Mono.just(customerDTO), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void update() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(getCustomer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(44)
    void updateBadRequest() {
        CustomerDTO customerDTO = getCustomer();
        customerDTO.setName("");
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(customerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void updateNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(CUSTOMER_PATH_ID, 100)
            .body(Mono.just(getCustomer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(50)
    void patch() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .patch()
            .uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(54)
    void patchNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .patch()
            .uri(CUSTOMER_PATH_ID, 100)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(9999)
    void deleteById() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .delete()
            .uri(CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(99999)
    void deleteByIdNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .delete()
            .uri(CUSTOMER_PATH_ID, 100)
            .exchange()
            .expectStatus().isNotFound();
    }

    CustomerDTO getCustomer(){
        return CustomerDTO.builder()
            .name("Jim")
            .build();
    }
}