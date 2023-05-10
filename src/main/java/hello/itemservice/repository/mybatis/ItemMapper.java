package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 마이바티스 매핑 XML을 호출해주는 매퍼 인터페이스
 * @Mapper 에노테이션을 붙여줘야 마이바티스에서 인식
 * 이 인터페이스의 메서드를 호출하면 xml 매핑파일(ItemMapper.xml)의 해당 SQL 을 실행하고 결과를 돌려준다
 *
 */
@Mapper
public interface ItemMapper {

    void save(Item item);

    /**
     * 파라미터가 2개이상이면 @Param 으로 이름을 지정해서 파라미터를 구분해야 한다
     *
     * @param id
     * @param updateParam
     */
    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);

}
