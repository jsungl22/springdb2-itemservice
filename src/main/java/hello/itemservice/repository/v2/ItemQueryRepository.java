package hello.itemservice.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.itemservice.domain.QItem.*;

/**
 * 실용적인 구조
 * ItemQueryRepository 는 Querydsl 을 사용해서 복잡한 쿼리 문제를 해결한다
 *
 * 기본 CRUD와 단순 조회는 JpaRepository 를 상속받은 ItemRepositoryV2 가 처리하고,
 * 복잡한 조회 쿼리는 ItemQueryRepository 가 Querydsl 을 사용해서 처리한다
 *
 * Querydsl 을 사용한 쿼리 문제에 집중되어 있어서, 복잡한 쿼리는 이 부분만 유지보수 하면 되는 장점이 있다
 */
@Repository
public class ItemQueryRepository {

    private final JPAQueryFactory query;

    public ItemQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        return query.select(item)
                    .from(item)
                    .where(likeItemName(cond.getItemName()), maxPrice(cond.getMaxPrice()))
                    .fetch();
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
