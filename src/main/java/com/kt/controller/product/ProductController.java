package com.kt.controller.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.Paging;
import com.kt.common.SwaggerAssistance;
import com.kt.domain.product.ProductStatus;
import com.kt.dto.product.ProductResponse;
import com.kt.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "🛍️ 일반 사용자 - 상품 조회 API", description = "활성화된 상품 목록 검색 및 상세 정보 조회")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends SwaggerAssistance {
	private final static List<ProductStatus> PUBLIC_VIEWABLE_STATUS = List.of(ProductStatus.ACTIVATED,
		ProductStatus.SOLD_OUT);

	private final ProductService productService;

	@Operation(summary = "전체 상품 조회",
		description = "활성, 품절 상태인 전체 상품을 조회합니다.",
		parameters = {
			@Parameter(name = "keyword", description = "검색 키워드(이름)"),
			@Parameter(name = "page", description = "페이지 번호", example = "1"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		}
	)
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Page<ProductResponse.Detail>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		var search = productService.searchByKeywordAndStatus(keyword, PUBLIC_VIEWABLE_STATUS, paging.toPageable())
			.map(ProductResponse.Detail::of);

		return ApiResult.ok(search);
	}

	@Operation(summary = "상품 상세 조회")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<ProductResponse.Detail> detail(@PathVariable Long id) {
		var product = productService.detail(id);

		return ApiResult.ok(ProductResponse.Detail.of(product));
	}

}