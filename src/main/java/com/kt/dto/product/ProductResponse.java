package com.kt.dto.product;

import com.kt.domain.product.Product;
import com.kt.domain.product.ProductStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductResponse {

	/*
	상품 상세 정보 등.. 검색 조회와 상세 조회가 구분이 된다면 DTO 분리가 필요할 거 같은데
	현재 Domain 구조로는 분리가 의미 없는 것 같아서 일단 User / Admin으로만 분리했습니다.
	 */

	@Getter
	@AllArgsConstructor
	@Schema(name = "ProductResponse.Detail")
	public static class Detail {
		private Long id;
		private String name;
		private Long price;

		public static Detail of(Product product) {
			return new Detail(
				product.getId(),
				product.getName(),
				product.getPrice()
			);
		}
	}

	@Getter
	@AllArgsConstructor
	@Schema(name = "ProductResponse.ManagementDetail")
	public static class ManagementDetail {
		private Long id;
		private String name;
		private Long price;
		private Long stock;
		private ProductStatus status;

		public static ManagementDetail of(Product product) {
			return new ManagementDetail(
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getStock(),
				product.getStatus()
			);
		}
	}
}
