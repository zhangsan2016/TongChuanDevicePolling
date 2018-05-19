/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import example.ldgd.com.tongchuandevicepolling.interfaces.SocketRec;


/**
 * @author CJK
 * 
 */
public class ClientUdp {

	// 通道管理器
	private Selector selector;
	private DatagramChannel channel;

	/**
	 * 获得一个Socket通道，并对该通道做一些初始化的工作
	 * 
	 * @param ip
	 *            连接的服务器的ip
	 * @param port
	 *            连接的服务器的端口号
	 * @throws IOException
	 */
	public void initClient(String ip, int port, final SocketRec socketRec)
			throws IOException {
		// 获得一个Socket通道
		channel = DatagramChannel.open();
		// 设置通道为非阻塞
		channel.configureBlocking(false);
		
	//	channel.connect(new InetSocketAddress(ip, port));
		channel.socket().bind(new InetSocketAddress(port));
		// 获得一个通道管理器
				this.selector = Selector.open();
		// 将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_READ事件。
		channel.register(selector, SelectionKey.OP_READ);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					listen(socketRec);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	
	}

	public void sendOrder(byte[] data) throws IOException {
		channel.write(ByteBuffer.wrap(data));
	}

	/**
	 * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
	 * 
	 * @param socketRec
	 * 
	 * @throws IOException
	 */
	public void listen(SocketRec socketRec) throws IOException {
		// 轮询访问selector
		  ByteBuffer byteBuffer = ByteBuffer.allocate(65536);  
		while (true) {
			/*selector.select();
			// 获得selector中选中的项的迭代器
			Iterator<SelectionKey> iter = this.selector.selectedKeys()
					.iterator();
			while (iter.hasNext()) {
				SelectionKey key = (SelectionKey) iter.next();
				// 删除已选的key,以防重复处理
				iter.remove();
				// 获得了可读的事件
				if (key.isReadable()) {
					read(key, socketRec);
				}

			}*/
			 try  
	            {  
	                // 进行选择  
	                int n = selector.select();  
	                if (n > 0)  
	                {  
	                    // 获取以选择的键的集合  
	                    Iterator iterator = selector.selectedKeys().iterator();  
	  
	                    while (iterator.hasNext())  
	                    {  
	                        SelectionKey key = (SelectionKey) iterator.next();  
	  
	                        // 必须手动删除  
	                        iterator.remove();  
	  
	                        if (key.isReadable())  
	                        {  
	                            DatagramChannel datagramChannel = (DatagramChannel) key  
	                                    .channel();  
	  
	                            byteBuffer.clear();  
	                            // 读取  
	                            InetSocketAddress address = (InetSocketAddress) datagramChannel  
	                                    .receive(byteBuffer);  
	  
	                            System.out.println(new String(byteBuffer.array()));  
	  
	                            // 删除缓冲区中的数据  
	                            byteBuffer.clear();  
	  
	                            String message = "data come from server";  
	  
	                            byteBuffer.put(message.getBytes());  
	  
	                            byteBuffer.flip();  
	  
	                            // 发送数据  
	                            datagramChannel.send(byteBuffer, address);  
	                        }  
	                    }  
	                }  
	            } catch (Exception e)  
	            {  
	                e.printStackTrace();  
	            }  

		}
	}

	/**
	 * 处理读取服务端发来的信息的事件
	 * 
	 * @param key
	 * @param socketRec
	 * @throws IOException
	 */
	public void read(SelectionKey key, SocketRec socketRec) throws IOException {
		// 服务器可读取消息:得到事件发生的Socket通道
		DatagramChannel channel = (DatagramChannel) key.channel();
		// 创建读取的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(512);
		buffer.clear();		
		int readBytes = channel.read(buffer);
		byte[] getData = buffer.array();
		
		 byte[] data = new byte[readBytes];
         System.arraycopy(getData, 0, data, 0, readBytes);
    
         
		socketRec.receive(data);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean disconnect() {
		boolean flag = false;
		try {
			if (channel != null && selector != null) {
				channel.close();
				selector.close();
				flag = true;
				
			}
		} catch (Exception e) {
			

		}
		return flag;
	}
	
	public boolean isConnecting(){
		boolean result = channel.isConnected();
		return result;
	}
}
