package com.thiha.roomrent.constant;

public class JwtConstants {

    //need to move to a more secure place
    public static final String SECRET_KEY="secretforjwtaddedtomakeitsecureenoughdidnotacceptexclamationlolandstillnotenoughzzzz";

    public static final long TOKEN_VALIDITY = 60*60*1000;

    public static final String  TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

}
