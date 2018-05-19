/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.basic;

/**
 * @author CJK
 *
 */
public class Session {
	private static Session son = new Session();
	private int num =0;
	private Session(){}
	public static Session getInstance(){
		return son;
	}
	
	public int getsession(){
		num++;
		return num;
	}
}
