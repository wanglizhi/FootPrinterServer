package startUp;

import java.rmi.server.*;  
import java.io.*;  
import java.net.*;  
public class SMRMISocket extends RMISocketFactory {  
    public Socket createSocket(String host, int port)   
        throws IOException{  
        return new Socket(host,port);  
    }  
    public ServerSocket createServerSocket(int port)   
        throws IOException {  
        if (port == 0)  
            port = 2098;//不指定就随机  
        return new ServerSocket(port);  
    }  
} 