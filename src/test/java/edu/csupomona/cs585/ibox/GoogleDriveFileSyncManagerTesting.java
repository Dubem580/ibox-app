package edu.csupomona.cs585.ibox;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.*;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import edu.csupomona.cs585.ibox.sync.GoogleDriveFileSyncManager;

public class GoogleDriveFileSyncManagerTesting {

	public Drive mockDrive;
	public GoogleDriveFileSyncManager FileSyncManager;
	public Files files;
	public File file;
	public java.io.File savedFile;
	public Insert insert;
	public Update update;
	public Delete delete;
	public List request;
	public FileList fileList;
	public ArrayList<File> ListFiles;
	public String fileName = "Testthis", fileId = "Tested";
	
	@Before
	public void  setupTest () {
		savedFile = mock(java.io.File.class);
		mockDrive = mock(Drive.class);
		files = mock(Files.class);
		insert = mock(Insert.class);
		update = mock(Update.class);
		delete = mock(Delete.class);
		request = mock(List.class);
		
		FileSyncManager = new GoogleDriveFileSyncManager(mockDrive);
		
		//new file
		file = new File();
		ListFiles = new ArrayList<File>();
		ListFiles.add(file);
		
		fileList = new FileList();
		fileList.setItems(ListFiles);
		
		file.setId(fileId);
		file.setTitle(fileName);
		
	}
	
	@Test
	public void testAddfile() throws IOException {
	   when(mockDrive.files()).thenReturn(files);
	   when(insert.execute()).thenReturn(file);
	   when(files.insert(any(File.class), any (FileContent.class))).thenReturn(insert);
		
	   FileSyncManager.addFile(savedFile);
	   
	   verify(insert).execute();
	   verify(files).insert(any(File.class), any(FileContent.class));
	   Assert.assertEquals(fileId,file.getId());
	}
	
	@Test
	public void testGetFileID_Found() throws IOException {
		
		when(mockDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		Assert.assertEquals(fileId, FileSyncManager.getFileId(fileName));
	}
	
	@Test
	public void testGetFileID_NotFound() throws IOException {
		fileName = " ";
		
		when(mockDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		Assert.assertNull(FileSyncManager.getFileId(fileName));
	}
	
	@Test
	public void testUpdateFile_PresentFile() throws IOException {
		//update file
		java.io.File presentsavedFile = new java.io.File ("~/Java/ibox-app/testing/testing.txt");
		
		when(mockDrive.files()).thenReturn(files);
		when(insert.execute()).thenReturn(file);
		when(files.insert(any(File.class), any (FileContent.class))).thenReturn(insert);
		
		//get id
		when(mockDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
			
		FileSyncManager.updateFile(presentsavedFile);

		Assert.assertEquals(fileId, file.getId());
		verify(request).execute();
	}
	
	@Test
	public void testUpdateFile_NewFile() throws IOException {
		java.io.File savedFile = new java.io.File ("~/Java/ibox-app/testing/testing.txt");
	
		when(mockDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		when(mockDrive.files()).thenReturn(files);
		when(insert.execute()).thenReturn(file);
		when(files.insert(any(File.class), any (FileContent.class))).thenReturn(insert);
	
		FileSyncManager.updateFile(savedFile);
	
		Assert.assertEquals(fileId, file.getId());
		verify(request).execute();
	}
	
	@Test  
	public void testDeleteFile() throws IOException {
		
		when(savedFile.getName()).thenReturn(fileName);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		when(mockDrive.files()).thenReturn(files);
		when(files.delete(any(String.class))).thenReturn(delete);
		when(delete.execute()).thenReturn(null);
	
		FileSyncManager.deleteFile(savedFile);
		verify(delete).execute();
	}
	@Test (expected =FileNotFoundException.class)
	public void testDeleteFile_NotFound() throws IOException {
		java.io.File savedFile = new java.io.File("~Java/ibox-app/test/testing.txt");
		
		when(mockDrive.files()).thenReturn(files);
		when(files.list()).thenReturn(request);
		when(request.execute()).thenReturn(fileList);
		
		FileSyncManager.deleteFile(savedFile);
		
}
  
}
 