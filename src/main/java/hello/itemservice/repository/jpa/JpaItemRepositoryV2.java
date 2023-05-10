package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA 적용
 * 해당 클래스는 ItemRepository 를 구현하고, SpringDataJpaItemRepository 를 사용한다
 * 그래서 해당 클래스는 ItemRepository 와 SpringDataJpaItemRepository 사이를 맞추기 위한 어댑터 처럼 사용된다
 */
@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;

    /**
     * 스프링 데이터 JPA 가 제공하는 save 를 호출한다
     */
    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    /**
     * 스프링 데이터 JPA 가 제공하는 findById 메서드를 사용해서 엔티티를 찾는다
     * 그리고 데이터 수정
     */
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    /**
     * 스프링 데이터 JPA 가 제공하는 findById 메서드를 이용해 엔티티를 찾는다
     */
    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * 데이터를 조건에 따라 4가지로 분류해서 검색한다
     *
     * 모든 데이터 조회
     * 이름 조회
     * 가격 조회
     * 이름 + 가격 조회
     */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        if (StringUtils.hasText(itemName) && maxPrice != null) {
            return repository.findItems("%" + itemName + "%", maxPrice);
        } else if (StringUtils.hasText(itemName)) {
            return repository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) {
            return repository.findByPriceLessThanEqual(maxPrice);
        } else {
            return repository.findAll();
        }
    }
}
