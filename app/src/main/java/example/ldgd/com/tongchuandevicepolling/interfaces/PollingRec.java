/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.interfaces;

/**
 * @author CJK
 *
 */
public interface PollingRec {
    public final static String OFFLINE = "1";
    public final static String PARAM_ERROR = "2";
    void onPollingRec(String ErrorType, int row, boolean result);

}
