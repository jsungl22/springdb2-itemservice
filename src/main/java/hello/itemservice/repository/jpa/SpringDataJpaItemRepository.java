package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 스프링 데이터 JPA 적용
 * 스프링 데이터 JPA 가 제공하는 JpaRepository 인터페이스를 상속받으면 기본적인 CRUD 기능을 사용할 수 있다
 * 여기서는 이름으로 검색하거나, 가격으로 검색하는 기능을 따로 만들었다
 * 이때 쿼리 메서드를 사용하거나 @Query 를 사용해서 직접 쿼리를 실행할 수 있다
 */
public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    //쿼리 메서드
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //쿼리 직접 실행
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
