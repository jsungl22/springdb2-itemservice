package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 이름지정 바인딩 - NamedParameterJdbcTemplate
 *
 * 이름지정 바인딩에서 자주 사용하는 파라미터 종류
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * Map
 *
 * ResultSet 의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환
 * BeanPropertyRowMapper
 *
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public Item save(Item item) {
        // ? 대신 :파라미터 이름 을 받는다
        String sql = "insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)";

        /**
         * BeanPropertySqlParameterSource 는 자바빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체를 생성한다
         */
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        Long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item " + "set item_name=:itemName, price=:price, quantity=:quantity " + "where id=:id";

        /**
         * MapSqlParameterSource 는 Map 과 유사한데, SQL 타입을 지정할 수 있는 등 SQL 에 좀 더 특화된 기능을 제공
         * MapSqlParameterSource 는 메서드 체인을 통해 편리한 사용법도 제공
         */
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId); //이 부분이 별도로 필요하다.

        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id= :id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) { //결과가 없으면 EmptyResultDataAccessException 예외 발생
            //결과가 없을 때 Optional 을 반환해야 하므로 Optional.empty 를 대신 반환한다
            return Optional.empty();
        }

    }

    /**
     * 데이터베이스 조회 결과를 객체로 변환할 때 사용
     * BeanPropertyRowMapper 는 ResultSet 의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환
     * 데이터베이스에서 조회한 결과 이름을 기반으로 setId() , setPrice() 처럼 자바빈 프로퍼티 규약에 맞춘 메서드를 호출
     *
     * BeanPropertyRowMapper 는 언더스코어 표기법을 카멜 표기법으로 자동 변환해준다
     * 만약 컬럼이름과 객체 이름이 완전히 다른 경우에는 SQL 에서 별칭을 사용한다
     * @return
     */
    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item";

        //동적쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper()); //query 는 결과 로우가 하나 이상일 때 사용, 결과가 없으면 빈 컬렉션을 반환
    }
}
