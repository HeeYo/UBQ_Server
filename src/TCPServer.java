import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static final int port = 9999;				//��Ʈ������
	static ServerSocket serverSocket;					//�������ɿ�������
	private static Socket socket;						//���ɿ�����
	private static InputStream is;						//�Է½�Ʈ�� is
	private static DataInputStream in;					//������ �Է� ��Ʈ�� in
	static byte [] received = new byte[10];			//�Էµ� �迭
	static boolean CRC = false;						//������ ���� �÷���
    public static void start() throws IOException
    {
    	try {
    		serverSocket = new ServerSocket(port);
    		System.out.println("Server Launched");
    	} catch(IOException e){
    		System.out.println("Error Occured");
    		e.printStackTrace();
    	}
    	while(true)
    	{
    		try{
    			socket = serverSocket.accept();		//���� ���� Confirm
        		System.out.println("Connected");
        		is = socket.getInputStream();
        		in = new DataInputStream(is);
        		/*
        		 * 		1. ������ ���� -> �迭�� ����
        		 * 		2. ������ üũ (STX,OPCODE, ADDR, CHKSUM, EXT)
        		 * 			1) ���� �߻��� �迭 �ʱ�ȭ
        		 * 				-1 CRC �÷��� true ����
        		 * 			2) ���� - �̻󹫽� CRC false 
        		 */
        		for(int i = 0; i < 10; i++)			//������ ����(�迭�� ���ų���)
        			received[i] = (byte) in.read();
        		chkData();
        		show();
        		execute();
    		} catch(IOException e) {
    			e.printStackTrace();
    			System.out.println("�������� ���������ϱ��");
    		} finally {
    			in.close();
    			socket.close();
    		}
    	}
    }
    public static void execute()
    {
    	/*
    	 * 		DATA0 : ����̽� Ÿ��
    	 * 		DATA1 : ���(����)
    	 * 		DATA2 : etc.
    	 * 		DATA3 :	NULL
    	 */
    	if(CRC == true)					//������ ������ ���� ����� ������ �ʱ�ȭ�� ���α׷� ���� / �����Ⱚ ���� ��������
	    	return;
    	
    }
    public static void chkData()		//������ �⺻ ���� üũ STX OPCODE ADDR EXT ��  
    {	
    	byte tmp = 0;
    	for(int i = 1; i <= 7; i++)
    		tmp += received[i];
    	if(received[0] == 0x02 && received[1] == 0x0A && received[2] == 0x01 && received[8] == ~tmp && received[9] == 0x03)		
    		CRC = false;				//������ ���� �÷���
    	else 
    		CRC = true;
    }
    public static void clearData()
    {
    	for(int i = 0; i < 10; i++)
    		received[i] = 0;
    }
    public static void show()
    {
    	for(int i = 0; i < 10; i++)
    		System.out.println(received[i]);
    	System.out.println("CRC : " + CRC);
    }
    public static void main(String[] args) throws IOException {
    	start();
    }
}