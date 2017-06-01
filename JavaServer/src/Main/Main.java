package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main 
{
	private static final int portnumber = 61000;
	
	public static void main(String[] args)
	{
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		
		
			System.out.println("Server starting at port number: " + portnumber);
			try {
				serverSocket = new ServerSocket(portnumber);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Waiting for clients to connect.");
		
			//Client Connecting
			while (true){
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Runnable multiple_connections = new multiple_connections(socket);
			new Thread(multiple_connections).start();
			}
	}
	//--------------multiple connections handling
	static class multiple_connections implements Runnable {
		
		private final Socket socket;
		public boolean user_found = false;
		public String user_info_name = null;
		public multiple_connections(Socket clientSocket){
			this.socket = clientSocket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Connected to a new client");
			try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String data;
			String usrID;
			
						
			while ((data = br.readLine())!=null)
			{
				//String splitting to get user Name and Associated data
				String[] mydata = data.split("\t");
				//-------------------Sign In request----------------------------
				if(mydata[0].equals("signin"))
				{
					user_found = false;
					System.out.println("Sign In Request...!");
					String line1 = "";
					try (BufferedReader br2 = new BufferedReader(new FileReader("signup.txt")))
					{

						while ((line1 = br2.readLine()) != null)		            
						{
							// use \t as separator
							String[] userinfo = line1.split("\t");
							if(userinfo[1].equals(mydata[1]) && userinfo[2].equals(mydata[2]))
							{
								user_found = true;
								user_info_name = userinfo[0];
								break;		//user found
							}
						}
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					//----------Inform Client---------------------
					try
					{
					BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String server_response = "no"+"\n";
					if(user_found)
					{
						server_response ="yes" + "\t" + user_info_name+"\n"; 
						System.out.println("User Found!" + user_info_name);
					}
					else
					{
						server_response = "no"+"\n";
						System.out.println("User Not Found!");
					}
					bw2.write(server_response);
//					bw2.newLine();
					bw2.flush();
					System.out.println("Response sent to client! " + server_response);
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}

				//-------------------End of Sign In request----------------------------
				

				//-------------------Sign Up----------------------------
				else if(mydata[0].equals("signup"))
				{
					user_found = false;
					System.out.println("Sign Up Request...!");
					String line1 = "";
					try (BufferedReader br3 = new BufferedReader(new FileReader("signup.txt")))
					{

						while ((line1 = br3.readLine()) != null)		            
						{
							// use \t as separator
							String[] userinfo = line1.split("\t");
							if(userinfo[1].equals(mydata[2]))
							{
								user_found = true;
								break;		//user found
							}
						}
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					//----------Inform Client---------------------
					try
					{
					BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String server_response_signup = "no"+"\n";
					if(user_found)
					{
						server_response_signup ="yes\n"; 
						System.out.println("User Name already exists!");
					}
					else
					{
						server_response_signup = "no\n";
						try(FileWriter fw = new FileWriter(mydata[0]+".txt", true);
								BufferedWriter bw1= new BufferedWriter(fw);
								PrintWriter out = new PrintWriter(bw1))
						{
							out.println(mydata[1] + "\t" + mydata[2]+ "\t" + mydata[3]);
							}
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						System.out.println("Sign Up Success!");
					}

					bw3.write(server_response_signup);
					bw3.flush();
					System.out.println("Response sent to client! " + server_response_signup);
					}
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}

				//-------------------Data Submit----------------------------
				else
				{
					usrID = mydata[0]+".txt";
					System.out.println("Message from the client: " + mydata[1]);
					//Saving data to a file
					
					try(FileWriter fw = new FileWriter(usrID, true);
							BufferedWriter bw2= new BufferedWriter(fw);
							PrintWriter out = new PrintWriter(bw2))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \t HH:mm");
						Calendar calobj = Calendar.getInstance();
						String strsdf = sdf.format(calobj.getTime());
						String[] temp = null;
						
						//-----------------get temperature value--------------------------------
						//search for all csv files----------------------------------------------
						File dir = new File("C:/Users/Javed/workspace/JavaServer");
						File[] ListOfFiles = dir.listFiles();
						String name = null;
						for (File file: ListOfFiles) 
						{
						      if (file.isFile()) 
						      {
						    	  //System.out.println("File " + name);
						    	  if (file.getName().endsWith("csv"))
						    	  {
						    		  name = file.getName();
						    		  System.out.println("CSV file found with name: " + name);
						    	  }
						      }
						 }
						
						//end search----------------------------------------------------------------------
						
						
						//String csvFile = "2016-9-2 20h20m49s.csv";
						String csvFile = name;
						if (csvFile != null )
						{
							System.out.println(csvFile);
							String line = "";
							String cvsSplitBy = ",";
	
							try (BufferedReader br1 = new BufferedReader(new FileReader(csvFile)))
							{
	
								while ((line = br1.readLine()) != null)
				            
								{
	
									// use comma as separator
									temp = line.split(cvsSplitBy);
								}
							}
							catch (IOException e) 
							{
								e.printStackTrace();
							}
							//-----------------end get temperature value----------------------------
							out.println(strsdf + "\t" + mydata[1]+ "\t" + temp[1]);
						}
						else
						{
							System.out.println("No Temperature Record found");
						}
						
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
//				PrintWriter writedata = new PrintWriter("datafile.txt");
//				writedata.println(data);
//				writedata.close();
			}
			
			System.out.println("Connection has closed");
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		}
		
	}	
}