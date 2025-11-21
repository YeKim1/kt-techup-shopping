package com.kt.controller.user;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.Paging;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserResponse;
import com.kt.dto.user.UserUpdateRequest;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "👑 관리자 - 사용자 관리 API", description = "전체 사용자 조회, 권한 부여/회수 등 관리자 기능")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController extends SwaggerAssistance {
	private final UserService userService;

	@Operation(summary = "유저 전체 조회",
		parameters = {
			@Parameter(name = "keyword", description = "검색 키워드(이름)"),
			@Parameter(name = "page", description = "페이지 번호", example = "1"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		}
	)
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Page<UserResponse.Search>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		var search = userService.search(paging.toPageable(), keyword)
			.map(user -> new UserResponse.Search(
				user.getId(),
				user.getName(),
				user.getCreatedAt()
			));

		return ApiResult.ok(search);
	}

	@Operation(summary = "유저 상세 조회")
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<UserResponse.Detail> detail(@PathVariable Long id) {
		var user = userService.detail(id);

		return ApiResult.ok(UserResponse.Detail.of(user));
	}

	@Operation(summary = "유저 정보 수정")
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
		userService.update(id, request.name(), request.email(), request.mobile());

		return ApiResult.ok();
	}

	@Operation(summary = "관리자 조회",
		parameters = {
			@Parameter(name = "page", description = "페이지 번호", example = "1"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		})
	@GetMapping("/admins")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Page<UserResponse.Search>> searchAdmin(@Parameter(hidden = true) Paging paging) {
		var search = userService.searchAdmin(paging.toPageable())
			.map(user -> new UserResponse.Search(
				user.getId(),
				user.getName(),
				user.getCreatedAt()
			));

		return ApiResult.ok(search);
	}

	@Operation(summary = "관리자 권한 부여")
	@PatchMapping("/admins/{id}/grant-admin")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> grant(@PathVariable Long id) {
		userService.grantAdminRole(id);

		return ApiResult.ok();
	}

	@Operation(summary = "관리자 권한 회수")
	@PatchMapping("/admins/{id}/revoke-admin")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> revoke(@PathVariable Long id) {
		userService.revokeAdminRole(id);

		return ApiResult.ok();
	}
}