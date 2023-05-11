package hello.itemservice.repository.v2;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 실용적인 구조
 * ItemRepositoryV2 는 JpaRepository 를 인터페이스 상속받아서 스프링 데이터 JPA 의 기능을 제공하는 리포지토리가 된다
 * 기본적인 CRUD 는 여기서 해결한다
 *
 * 추가로 단순한 조회 쿼리들을 추가해도 된다
 */
public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
