package edu.csupomona.cs585.ibox;


import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.File;


import java.io.BufferedWriter;
import java.io.FileWriter; 

import com.google.api.services.drive.Drive;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;
import edu.csupomona.cs585.ibox.sync.GoogleDriveServiceProvider;

public class IntegrationTest {
	
	public static GoogleDriveFileSyncManager FileSyncManager;
	public static   Drive drive;
	public static  File localFile;
	public   String filepath, fileName;
	
	@Before
	public void setup() throws IOException {
		drive = GoogleDriveServiceProvider.get().getGoogleDriveClient();
		FileSyncManager = new GoogleDriveFileSyncManager(drive);
		filepath = "/Users/dubem/Desktop/testthis.txt";
	
	}
	@Test
	public void addFileTest() throws IOException {
		localFile = new File(filepath);
		fileName = localFile.getName();
		localFile.createNewFile();
		System.out.println("New file :- " + fileName + "  found in " + filepath);
		
		FileSyncManager.addFile(localFile);
		
		System.out.println("File was added");
		System.out.println("File:" + fileName + "id: " + FileSyncManager.getFileId(fileName));
		localFile.delete();
		System.out.println("File deleted");
	}
	
	@Test
	public void deleteFiletest() throws IOException {
		localFile = new File(filepath);
		fileName = localFile.getName();
		localFile.createNewFile();
		System.out.println("New file :- " + fileName + "  found in " + filepath);
		
		FileSyncManager.addFile(localFile);
		
		FileSyncManager.deleteFile(localFile);
		localFile.delete();
		Assert.assertFalse(localFile.exists());
	}

	@Test
	public void updateFiletest() throws IOException {
		localFile = new File(filepath);
		fileName = localFile.getName();
		localFile.createNewFile();
		String updatelocalfile = "it works";
		
		FileSyncManager.addFile(localFile);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,true));
		writer.write(updatelocalfile);
		writer.flush();
		writer.close();
		
		FileSyncManager.updateFile(localFile);
		
		System.out.println("changed file");
		localFile.delete();
		System.out.println("deleted");	
		
	}
}