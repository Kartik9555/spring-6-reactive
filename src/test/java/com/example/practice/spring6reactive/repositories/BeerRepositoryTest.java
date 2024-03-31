package com.example.practice.spring6reactive.repositories;

import com.example.practice.spring6reactive.domain.Beer;
import com.example.practice.spring6reactive.config.DatabaseConfig;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer(){
        beerRepository.save(getBeer())
            .subscribe(beer -> {
                System.out.println(beer);
            });
    }

    Beer getBeer(){
        return Beer.builder()
            .beerName("Monster Feel")
            .beerStyle("IPA")
            .upc("1223345")
            .price(BigDecimal.TEN)
            .quantityOnHand(10)
            .build();
    }
}