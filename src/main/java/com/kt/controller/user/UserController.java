package com.kt.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserResponse;
import com.kt.dto.user.UserUpdatePasswordRequest;
import com.kt.dto.user.UserUpdateRequest;
import com.kt.security.CurrentUser;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "👤 일반 사용자 API", description = "로그인 및 자신의 정보 조회/수정 등 일반 유저 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends SwaggerAssistance {
	private final UserService userService;

	@Operation(summary = "ID 중복 체크")
	@GetMapping("/duplicate-login-id")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Boolean> isDuplicateLoginId(@RequestParam String loginId) {
		var result = userService.isDuplicateLoginId(loginId);

		return ApiResult.ok(result);
	}

	@Operation(summary = "내 정보 조회")
	@GetMapping("/my-info")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<UserResponse.Detail> detail(@AuthenticationPrincipal CurrentUser currentUser) {
		var user = userService.detail(currentUser.getId());

		return ApiResult.ok(UserResponse.Detail.of(user));
	}

	@Operation(summary = "내 정보 수정")
	@PutMapping("/my-info")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> update(@AuthenticationPrincipal CurrentUser currentUser, UserUpdateRequest request) {
		userService.update(currentUser.getId(), request.name(), request.email(), request.mobile());

		return ApiResult.ok();
	}

	@Operation(summary = "내 비밀번호 변경")
	@PutMapping("/change-password")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> updatePassword(
		@AuthenticationPrincipal CurrentUser currentUser,
		@RequestBody @Valid UserUpdatePasswordRequest request
	) {
		userService.changePassword(currentUser.getId(), request.oldPassword(), request.newPassword());
		return ApiResult.ok();
	}

	@Operation(summary = "탈퇴")
	@DeleteMapping("/withdraw")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> delete(@AuthenticationPrincipal CurrentUser currentUser) {
		userService.delete(currentUser.getId());

		return ApiResult.ok();
	}

}
