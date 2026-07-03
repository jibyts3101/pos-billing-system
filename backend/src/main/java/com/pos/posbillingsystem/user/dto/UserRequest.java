package com.pos.posbillingsystem.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.pos.posbillingsystem.user.entity.Role;
import com.pos.posbillingsystem.user.entity.Status;

public class UserRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

    private String phone;

    @NotNull
    private Status status;

    // Generate getters and setters
   
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

} 