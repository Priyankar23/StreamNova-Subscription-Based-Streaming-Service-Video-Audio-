package com.example.demo.dto;

public class Userdto {
	 private String username;
	    private String password;
	    private String email;
	    private Long userId;
	    private Long phoneNumber;
		public Userdto() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Userdto(String username, String password, String email, Long userId, Long phoneNumber) {
			super();
			this.username = username;
			this.password = password;
			this.email = email;
			this.userId = userId;
			this.phoneNumber = phoneNumber;
		}
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
		}
		public Long getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(Long phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
}