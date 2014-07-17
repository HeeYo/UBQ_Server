import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static final int port = 9999;				//포트으으으
	static ServerSocket serverSocket;					//서버소케에에에에
	private static Socket socket;						//소케에에엣
	private static InputStream is;						//입력스트림 is
	private static DataInputStream in;					//데이터 입력 스트립 in
	static byte [] received = new byte[10];			//입력된 배열
	static boolean CRC = false;						//데이터 에러 플레그
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
    			socket = serverSocket.accept();		//서버 연결 Confirm
        		System.out.println("Connected");
        		is = socket.getInputStream();
        		in = new DataInputStream(is);
        		/*
        		 * 		1. 데이터 수신 -> 배열에 저장
        		 * 		2. 데이터 체크 (STX,OPCODE, ADDR, CHKSUM, EXT)
        		 * 			1) 문제 발생시 배열 초기화
        		 * 				-1 CRC 플레그 true 설정
        		 * 			2) 문제 - 이상무시 CRC false 
        		 */
        		for(int i = 0; i < 10; i++)			//데이터 수신(배열에 쑤셔넣음)
        			received[i] = (byte) in.read();
        		chkData();
        		show();
        		execute();
    		} catch(IOException e) {
    			e.printStackTrace();
    			System.out.println("에러에러 무슨에러일까염ㅋ");
    		} finally {
    			in.close();
    			socket.close();
    		}
    	}
    }
    public static void execute()
    {
    	/*
    	 * 		DATA0 : 디바이스 타입
    	 * 		DATA1 : 명령(실행)
    	 * 		DATA2 : etc.
    	 * 		DATA3 :	NULL
    	 */
    	if(CRC == true)					//데이터 실행전 에러 존재시 데이터 초기화후 프로그램 종료 / 쓰래기값 실행 조기차단
	    	return;
    	
    }
    public static void chkData()		//데이터 기본 설정 체크 STX OPCODE ADDR EXT 등  
    {	
    	byte tmp = 0;
    	for(int i = 1; i <= 7; i++)
    		tmp += received[i];
    	if(received[0] == 0x02 && received[1] == 0x0A && received[2] == 0x01 && received[8] == ~tmp && received[9] == 0x03)		
    		CRC = false;				//데이터 에러 플레그
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