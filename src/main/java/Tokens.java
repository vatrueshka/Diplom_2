public class Tokens {
    public static String accessToken;
    public static String refreshToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Tokens.accessToken = accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        Tokens.refreshToken = refreshToken;
    }
}