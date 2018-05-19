/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.interfaces;

import java.net.DatagramPacket;
import java.net.Socket;

/**
 * @author CJK
 *
 */
public interface PacketRec {
	public void Receive(byte[] data, DatagramPacket packet, int session);
}
