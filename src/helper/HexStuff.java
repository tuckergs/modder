package helper;

import java.io.UnsupportedEncodingException;

//Most of these were found on Stack Overflow
public class HexStuff {

	
	public static byte[] hexStringToBytes(String s) {

		int len = s.length();

		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));

		}

		return data;

	}
	
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	
	public static String bytesToHexString ( byte[] bytes ) {

		char[] hexChars = new char[bytes.length * 2];

		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	
	}
	
	//These use ASCII
	public static String hexStringtoTextString ( String s ) throws UnsupportedEncodingException
	{
		
		byte [] b = hexStringToBytes ( s );
		
		return new String ( b , "US-ASCII" );
		
	}
	
	public static String textStringToHexString ( String s )
	{
		
		char[] c = s.toCharArray();
		
		byte[] b = new byte [ c.length ];
		
		//Create new 
		for ( int i = 0 ; i < b.length ; ++i )
			b[i] = (byte) c[i];
		
		return bytesToHexString ( b );		
		
	}
	
}
