package com.falah.thefuturetest.config.securety;

public final class SecurityConstants {

  public static final String AUTH_LOGIN_URL = "/api/authenticate";


  public static final String JWT_SECRET = "5F220E6E2830441A9CBC320E0CE5EB28509F2E5ABB5B4D00A0E9F92EBB24148FBA567BF9B33B4C128FF8882F451153E7814E014F228F46499FF885C988D25239AEF8BD6A20434AA68E34CEB11671139C";

  // JWT token defaults
  public static final String TOKEN_HEADER = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String TOKEN_TYPE = "JWT";
  public static final String TOKEN_ISSUER = "secure-api";
  public static final String TOKEN_AUDIENCE = "secure-app";

  private SecurityConstants() {
    throw new IllegalStateException("Cannot create instance of static util class");
  }

  public static String extractToken(String header) {
    return header.replace(TOKEN_PREFIX, "");
  }
}