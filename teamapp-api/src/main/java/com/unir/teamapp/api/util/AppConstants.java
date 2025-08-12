package com.unir.teamapp.api.util;

import org.springframework.http.HttpHeaders;

public class AppConstants {

    /** URLs **/
    public static final String URL_BASE_PATH = "/teamapp";
    public static final String URL_SWAGGER_API_DOCS = "/v3/api-docs/**";
    public static final String URL_SWAGGER_UI = "/swagger-ui/**";
    public static final String URL_SWAGGER_UI_CUSTOM = "/swagger-ui-teamapp.html";
    public static final String URL_PUBLIC = "/public";
    public static final String URL_AUTH_LOGIN = AppConstants.URL_BASE_PATH + "/login";
    public static final String URL_AUTH_REGISTER = AppConstants.URL_BASE_PATH + "/registro";


    /** JWT token defaults **/
    public static final String TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
    public static final String TOKEN_AUTHORITIES = "authorities";

    /** Roles **/
    public static final String ROLE = "ROLE_";
    public static final String ADMINISTRADOR = "Administrador";
    public static final Integer DEFAULT_USER_ROLE_ID = 2;

    /** Default Team **/
    public static final Integer DEFAULT_TEAM_ID = 1;

}
