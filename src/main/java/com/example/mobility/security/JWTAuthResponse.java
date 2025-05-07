package com.example.mobility.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
	private String accessToken;
	private String tokenType = "Bearer";

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String toString() {
		return "JWTAuthResponse [accessToken=" + accessToken + ", tokenType=" + tokenType + "]";
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}