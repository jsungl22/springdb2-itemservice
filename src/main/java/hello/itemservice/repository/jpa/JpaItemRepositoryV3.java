package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;

/**
 * Querydsl 적용
 * Querydsl 을 사용하려면 JPAQueryFactory 가 필요한데, JPAQueryFactory 는 JPA 쿼리인 JPQL을 만들기 때문에 EntityManager 가 필요하다
 */
@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }


    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }


    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    /**
     * Querydsl 을 사용해서 동적 쿼리 문제를 해결한다
     * 쿼리 문장에 오타가 있어도 컴파일 시점에 오류를 막을 수 있다
     * BooleanBuilder 를 사용해서 원하는 where 조건들을 넣어준다
     */

    public List<Item> findAllOld(ItemSearchCond cond) {

        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        QItem item = QItem.item; //Querydsl 이 제공하는 Item
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }

        if(maxPrice != null) {
            builder.and(item.price.loe(maxPrice));
        }

        List<Item> result = query
                                .select(item)
                                .from(item)
                                .where(builder)
                                .fetch();

        return result;
    }

    /**
     * 위에서 작성한 findAllOld 메서드를 리팩토링 ver
     * Querydsl 에서 where 에 다양한 조건들을 넣을 수 있는데 다음과 같이 , 로 구분하면 AND 조건으로 처리된다
     * 참고로 where 에 null 을 입력하면 해당 조건을 무시한다
     *
     * 그리고 likeItemName(), maxPrice() 를 다른 쿼리를 작성할 때 재사용 할 수 있다 -> 메서드 추출을 통해 코드 재사용
     * 즉, 쿼리 조건을 부분적으로 모듈화 할 수 있다
     */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        List<Item> result = query.select(item)
                                .from(item)
                                .where(likeItemName(itemName), maxPrice(maxPrice))
                                .fetch();

        return result;


    }

    private BooleanExpression likeItemName(String itemName) {
        if(StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if(maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }
}
