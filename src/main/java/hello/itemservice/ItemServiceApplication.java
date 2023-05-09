package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @Import(MemoryConfig.class) : MemoryConfig 를 설정파일로 사용
 * scanBasePackages = "hello.itemservice.web" -> 컴포넌트 스캔 경로 지정, 나머지는 직접 수동 등록
 */
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
@Import(JdbcTemplateV3Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	/**
	 * 특정 프로필의 경우에만 해당 스프링 빈을 등록
	 * 여기서는 local 이라는 이름의 프로필이 사용되는 경우에만 해당 메서드(testDataInit) 를 스프링 빈으로 등록
	 *
	 * 스프링은 로딩 시점에 application.properties 에 있는 spring.profiles.active 속성을 읽어서 프로필로 사용
	 * /src/main/resources 하위의 application.properties 는 /src/main 하위의 자바 객체를 실행할 때 동작하는 스프링 설정이다
	 * spring.profiles.active=local 이라고 하면 스프링은 local 이라는 프로필로 동작한다
	 * 그래서 아래의 @Profile("local")이 동작하고, testDataInit 메서드가 스프링 빈으로 등록된다
	 *
	 * @param itemRepository
	 * @return
	 */
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

}
