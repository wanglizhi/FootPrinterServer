package dataServiceImpl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bean.FilePO;
import dataService.UploadDataService;

public class UploadServiceImpl extends UnicastRemoteObject implements
		UploadDataService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadServiceImpl(int port) throws RemoteException {
		super(port);
	}

	@Override
	public void upload(FilePO filePO) throws RemoteException {
		System.out.println("Upload--> filename: "+filePO.getFilename());
			File outputFile = new File("D:/apache/htdocs/footPrinter/"+filePO.getFilename());			
			try {				
				if (!outputFile.exists())
					outputFile.createNewFile();
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile));
//				byte[] b = filePO.getFielcontent().getBytes();
				os.write(filePO.getFielcontent());
				System.out.println(outputFile.getAbsolutePath() + "bytes : " + filePO.getFielcontent().length);
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		string2File(filePO.getFielcontent(), "D:/apache/htdocs/footPrinter/"+filePO.getFilename());
	}

	private boolean string2File(String res, String filepath){
		
		boolean flag = false;
		
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		
		File outputFile = new File(filepath);
		try {
			if (!outputFile.exists())
					outputFile.createNewFile();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
			char buf[] = new char[1024];
			int len = 0;
			while ((len = bufferedReader.read(buf)) != -1){
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
			return flag;
		}finally {
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return flag;
		
	}

	@Override
	public void mkdir(int blogID) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("mkdir-->"+blogID);
		File fileDir=new File("D:/apache/htdocs/footPrinter/blog/"+blogID);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
	}
}
