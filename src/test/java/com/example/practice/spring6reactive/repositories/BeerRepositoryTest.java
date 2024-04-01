package com.example.practice.spring6reactive.repositories;

import com.example.practice.spring6reactive.domain.Beer;
import java.math.BigDecimal;

//@DataR2dbcTest
///@Import(DatabaseConfig.class)
class BeerRepositoryTest {

    //@Autowired
    BeerRepository beerRepository;

    //@Test
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