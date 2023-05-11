package hello.itemservice.config;

import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQueryRepository;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class V2Config {

    private final EntityManager em;
    private final ItemRepositoryV2 itemRepository;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemRepository, itemQueryRepository());
    }

    @Bean
    public ItemQueryRepository itemQueryRepository() {
        return new ItemQueryRepository(em);
    }

    // 테스트에서 사용하므로 넣어놨다
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
