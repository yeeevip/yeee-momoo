package vip.yeee.memo.common.appauth.client.exception;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/30 11:14
 */
public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) {
        super(message);
    }
}
