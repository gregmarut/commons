package filecache;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.gregmarut.commons.filecache.CacheException;
import com.gregmarut.commons.filecache.FileCache;
import com.gregmarut.commons.filecache.FileSystemCache;
import com.gregmarut.commons.filecache.IllegalFileException;

public class FileSystemCacheTest
{
	public static final String ROOT = "target/fileCache";
	public static final String FILE_NAME = "text.txt";
	public static final String FILE_CONTENT = "Hello World";
	
	private File root;
	
	public FileSystemCacheTest()
	{
		root = new File(ROOT);
	}
	
	/**
	 * A simple test to write a file and then read it back
	 * 
	 * @throws CacheException
	 * @throws IOException
	 */
	@Test
	public void readWriteFileTest() throws CacheException, IOException
	{
		// create the new file cache implementation
		FileCache fileCache = new FileSystemCache(root);
		
		// write the file to disk
		fileCache.save(FILE_NAME, FILE_CONTENT.getBytes());
		
		// read the file
		byte[] content = fileCache.load(FILE_NAME);
		
		Assert.assertEquals(FILE_CONTENT, new String(content));
	}
	
	@Test(expected = IllegalFileException.class)
	public void writeAboveRootTest() throws CacheException
	{
		// create the new file cache implementation
		FileCache fileCache = new FileSystemCache(root);
		
		final String fileName = "../fail.txt";
		
		// write the file to disk
		fileCache.save(fileName, FILE_CONTENT.getBytes());
	}
	
	@Test(expected = IllegalFileException.class)
	public void readAboveRootTest() throws CacheException
	{
		// create the new file cache implementation
		FileCache fileCache = new FileSystemCache(root);
		
		final String fileName = "../fail.txt";
		
		// load the file from disk
		fileCache.load(fileName);
	}
}
