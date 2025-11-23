package com.kt.controller.product;

import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.Paging;
import com.kt.common.SwaggerAssistance;
import com.kt.domain.product.ProductStatus;
import com.kt.dto.product.ProductRequest;
import com.kt.dto.product.ProductResponse;
import com.kt.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "👑 관리자 - 상품 관리 API", description = "상품 등록, 수정, 삭제 및 상태(품절/활성화) 변경 등 관리자 전용 기능")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController extends SwaggerAssistance {
	private final ProductService productService;

	@Operation(summary = "상품 등록")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResult<Void> create(@RequestBody @Valid ProductRequest.Create request) {
		productService.create(
			request.getName(),
			request.getPrice(),
			request.getQuantity()
		);

		return ApiResult.ok();
	}

	@Operation(summary = "전체 상품 조회",
		description = "활성, 품절, 비활성인 전체 상품을 조회합니다.",
		parameters = {
			@Parameter(name = "keyword", description = "검색 키워드(이름)"),
			@Parameter(name = "page", description = "페이지 번호", example = "1"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		}
	)
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Page<ProductResponse.ManagementDetail>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		var search = productService.searchByKeywordAndStatus(keyword, Arrays.stream(ProductStatus.values()).toList(),
				paging.toPageable())
			.map(ProductResponse.ManagementDetail::of);

		return ApiResult.ok(search);
	}

	@Operation(summary = "관리자용 상품 상세 조회")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<ProductResponse.ManagementDetail> detail(@PathVariable Long id) {
		var product = productService.detail(id);

		return ApiResult.ok(ProductResponse.ManagementDetail.of(product));
	}

	@Operation(summary = "상품 수정")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid ProductRequest.Update request) {
		productService.update(
			id,
			request.getName(),
			request.getPrice(),
			request.getQuantity()
		);

		return ApiResult.ok();
	}

	@Operation(summary = "상품 삭제")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> delete(@PathVariable Long id) {
		productService.delete(id);

		return ApiResult.ok();
	}

	@Operation(summary = "상품 품절")
	@PatchMapping("/{id}/toggle-sold-out")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> soldOut(@PathVariable Long id) {
		productService.soldOut(id);

		return ApiResult.ok();
	}

	@Operation(summary = "상품 비활성화")
	@PatchMapping("/{id}/in-activate")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> inActivate(@PathVariable Long id) {
		productService.inActivate(id);

		return ApiResult.ok();
	}

	@Operation(summary = "상품 활성화")
	@PatchMapping("/{id}/activate")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> activate(@PathVariable Long id) {
		productService.activate(id);

		return ApiResult.ok();
	}

}
