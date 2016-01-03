package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import helper.HexStuff;

public class Data {

	private RandomAccessFile raf;
	
	
	public Data ( File path ) throws FileNotFoundException
	{
		
		raf = new RandomAccessFile ( path , "rw" );
		
	}
	
	public String readBytes ( long pos , int length ) throws IOException
	{
		
		//Set file pointer
		raf.seek(pos);
		
		//Create array
		byte[] bytes = new byte [ length ];
		
		//Read
		raf.readFully ( bytes );
		
		
		return HexStuff.bytesToHexString ( bytes );
		
	}
	

	
	public void writeBytes ( long pos , String byteString ) throws IOException
	{
		
		//Set file pointer
		raf.seek(pos);
		
		//Write
		raf.write( HexStuff.hexStringToBytes ( byteString ) );
		
	}
	
}