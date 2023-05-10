package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 상품 자체를 나타내는 객체
 */
//@Data
//public class Item {
//
//    private Long id;
//
//    private String itemName;
//    private Integer price;
//    private Integer quantity;
//
//    public Item() {
//    }
//
//    public Item(String itemName, Integer price, Integer quantity) {
//        this.itemName = itemName;
//        this.price = price;
//        this.quantity = quantity;
//    }
//}


/**
 * JPA 적용
 * JPA 가 제공하는 에노테이션을 사용해서 Item 객체와 테이블을 매핑한다
 *
 */
@Data
@Entity //JPA가 사용하는 객체. 이 에노테이션이 있어야 JPA가 인식한다
public class Item {
    
    //@Id : 테이블의 PK 와 해당 필드를 매핑한다
    //@GeneratedValue : PK 생성 값을 데이터베이스에서 생성하는 IDENTITY 방식을 사용한다 ex) MySQL auto increment
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column : 객체의 필드를 테이블의 컬럼과 매핑한다
    //JPA 의 매핑정보로 DDL(create table)도 생성할 수 있는데, 그때 컬럼의 길이 값으로 활용된다
    @Column(name = "item_name", length = 10)
    private String itemName;
    //@Column 을 생략할 경우 필드의 이름을 테이블의 컬럼 이름으로 사용한다
    //스프링부트와 통합해서 사용하면 필드 이름을 테이블 컬럼 이름으로 변경할 때, 객체 필드의 카멜 케이스를 테이블 컬럼의 언더스코어로 자동으로 변환해준다
    private Integer price;
    private Integer quantity;

    //JPA 는 public 또는 protected 기본 생성자가 필수
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}