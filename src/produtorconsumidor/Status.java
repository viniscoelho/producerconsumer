package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public interface Status {
    public final String IS_FULL = "-1";
    public final String IS_EMPTY = "-2";
    public final String IS_PRODUCING = "-3";
    public final String IS_CONSUMING = "-4";
    public final String ERROR_MSG = "-5";
}
