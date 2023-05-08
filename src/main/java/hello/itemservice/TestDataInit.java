package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * 애플리케이션 실행시 초기 데이터 저장
 * 리스트에서 데이터가 잘 나오는지 확인할 용도로 사용
 */
@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     * @EventListener(ApplicationReadyEvent.class) : 스프링 컨테이너가 완전히 초기화를 다 끝내고 실행 준비가 되었을 때 발생하는 이벤트
     * 스프링이 해당 에노테이션이 붙은 메서드를 호출해준다
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
