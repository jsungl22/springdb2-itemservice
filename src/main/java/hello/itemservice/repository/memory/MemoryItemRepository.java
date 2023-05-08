package hello.itemservice.repository.memory;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ItemRepository 인터페이스를 구현한 메모리 저장소(구현체)
 * 메모리이기 때문에 서버를 껐다 다시 실행하면 저장된 데이터는 모두 사라진다
 */
@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
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
        return Optional.ofNullable(store.get(id));
    }

    /**
     * ItemSearchCond 라는 검색 조건을 받아서 내부에서 데이터를 검색
     * itemName 이나 maxPrice 가 null 이거나 비었으면 해당 조건을 무시한다
     * itemName 이나 maxPrice 에 값이 있을 때만 해당 조건으로 필터링 기능을 수행한다
     * @param cond
     * @return
     */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        return store.values().stream()
                .filter(item -> {
                    if (ObjectUtils.isEmpty(itemName)) {
                        return true;
                    }
                    return item.getItemName().contains(itemName);
                }).filter(item -> {
                    if (maxPrice == null) {
                        return true;
                    }
                    return item.getPrice() <= maxPrice;
                })
                .collect(Collectors.toList());
    }

    /**
     * 메모리에 저장된 Item 을 모두 삭제해서 초기화
     * 테스트 용도로만 사용
     */
    public void clearStore() {
        store.clear();
    }

}
