package hello.itemservice.repository;

import lombok.Data;

/**
 * 상품 수정시 사용하는 객체
 * 단순 데이터 전달 용도로 사용되므로 DTO 를 클래스 이름 뒤에 붙임
 */
@Data
public class ItemUpdateDto {
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
