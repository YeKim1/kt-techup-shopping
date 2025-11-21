package com.kt.domain.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kt.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
	private String loginId;
	private String password;
	private String name;
	private String email;
	private String mobile;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	private LocalDate birthday;
	@Enumerated(EnumType.STRING)
	private Role role;

/*	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<>();*/

	public User(String loginId, String password, String name, String email, String mobile, Gender gender,
		LocalDate birthday, LocalDateTime createdAt, LocalDateTime updatedAt, Role role) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.gender = gender;
		this.birthday = birthday;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.role = role;
	}

	public static User normalUser(String loginId, String password, String name, String email, String mobile,
		Gender gender,
		LocalDate birthday) {
		LocalDateTime now = LocalDateTime.now();

		return new User(
			loginId,
			password,
			name,
			email,
			mobile,
			gender,
			birthday,
			now,
			now,
			Role.USER
		);
	}

	public void changePassword(String password) {
		this.password = password;
	}

	public void update(String name, String email, String mobile) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.updatedAt = LocalDateTime.now();
	}

	public void grantAdminRole() {
		this.role = Role.ADMIN;
		this.updatedAt = LocalDateTime.now();
	}

	public void revokeAdminRole() {
		if (this.role == Role.ADMIN) {
			this.role = Role.USER;
			this.updatedAt = LocalDateTime.now();
		}
	}
}
