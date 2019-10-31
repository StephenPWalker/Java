import java.net.*;
import java.io.*;

public class Client{	

	public Client(String server_host, int server_port) {	
		
		try { 
				while(true)
				{
				Socket s = new Socket(server_host, server_port);			      

				InputStream in = s.getInputStream();			
				BufferedReader bin = new BufferedReader (new InputStreamReader(in));								  		    
				System.out.println(bin.readLine());				      
				s.close();
				
				Thread.sleep(1000);
				}
			}		
			catch (java.io.IOException e) 
				{System.out.println(e); System.exit(1);} 
			catch (InterruptedException e) 
				{e.printStackTrace();}	
		
	}

	
	public static void main(String argv[]) 
	{
		if ((argv.length < 1) || (argv.length > 2))
		 { 
			System.out.println("Usage: [host] <port>") ;
			System.exit (1) ;
		 }
		String server_host = argv[0] ;
		int server_port = 5155;
		if (argv.length == 2)
		 server_port = Integer.parseInt (argv[1]) ;

		Client client = new Client(server_host, server_port);
		} // end of main
	
}
			
