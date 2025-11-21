package com.kt.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kt.domain.user.Gender;
import com.kt.domain.user.User;

public interface UserResponse {
	record Search(
		Long id,
		String name,
		LocalDateTime createdAt
	) {
	}

	record Detail(
		Long id,
		String name,
		String email,
		String mobile,
		Gender gender,
		LocalDate birthday
	) {
		public static Detail of(User user) {
			return new Detail(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getMobile(),
				user.getGender(),
				user.getBirthday()
			);
		}
	}
}
