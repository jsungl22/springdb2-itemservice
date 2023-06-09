package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @Import(MemoryConfig.class) : V2Config 를 설정파일로 사용
 * scanBasePackages = "hello.itemservice.web" -> 컴포넌트 스캔 경로 지정, 나머지는 직접 수동 등록
 */
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(QuerydslConfig.class)
@Import(V2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
@Slf4j
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
	 */
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}



	/**
	 * 프로필이 test 인 경우에만 데이터소스를 스프링 빈으로 등록
	 * 즉, 테스트케이스에서만 이 데이터소스를 스프링 빈으로 등록해서 사용한다
	 *
	 * 테스트를 실행할 때 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용 -> 실제 데이터베이스를 사용할 필요 X
	 * 
	 * 하지만 스프링부트는 이런 임베디드 데이터베이스에 대한 설정도 기본으로 제공하므로 생략가능하다
	 */
//	@Bean
//	@Profile("test")
//	public DataSource dataSource() {
//		log.info("메모리 데이터베이스 초기화");
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.h2.Driver");
//		//jdbc:h2:mem:db 은 임베디드 모드에서 동작하는 H2 데이터베이스를 사용할 수 있다
//		//DB_CLOSE_DELAY=-1 은 임베디드 모드에서 데이터베이스 커넥션 연결이 모두 끊어지는 경우 데이터베이스 종료를 방지하는 설정
//		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//		dataSource.setUsername("sa");
//		dataSource.setPassword("");
//		return dataSource;
//	}

}
