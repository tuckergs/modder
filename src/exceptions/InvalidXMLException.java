package exceptions;


/**
 * 
 * @class InvalidXMLException
 * 
 * @brief An exception thrown by XML parsers
 * 
 * You set what went wrong with the setXXX functions
 * in here; so this requires more than one line of code
 * from initializing the exception to throwing it
 * 
 * @author Gabe Tucker
 * 
 * @contact gst06@roadrunner.com
 * 
 */
@SuppressWarnings("serial")
public class InvalidXMLException extends Exception{

	
	private static final int UNSPECIFIED = -1;
	private static final int INVALID_TAG = 0;
	private static final int MISSING_TAG = 1;
	private static final int CANT_PARSE_XML = 2;
	private static final int CANT_PARSE_FILE = 3;
	
	
	private int err_no;
	
	String[] strings;
	
	
	
	
	//Constructors
	public InvalidXMLException ()
	{
		
		//TODO: Do stuff
		
		err_no = UNSPECIFIED;
		
	}
	
	
	
	
	
	
	public void setCantParseXML ( String xml ){
	
		strings = new String[1];
		
		strings[0] = xml;
		
		err_no = CANT_PARSE_XML;		
		
	}
	
	public void setCantParseFile ( String filename )
	{
		
		strings = new String[1];
		
		strings[0] = filename;
		
		err_no = CANT_PARSE_FILE;
		
	}
	
	public void setMissingTag ( String expected )
	{
	
		//TODO: Do stuff
		
		err_no = MISSING_TAG;
		
		strings = new String[1];
		
		strings[0] = expected;
		
	}
	
	public void setInvalidTag ( String expected , String received )
	{
		
		//TODO: Do stuff
		
		err_no = INVALID_TAG;
		
		strings = new String[2];
		
		strings[0] = expected;
		
		strings[1] = received;
		
	}
	
	
	@Override
	public String getMessage()
	{
		
		
		if ( err_no == INVALID_TAG )
		{
			
			return "The parser expected \"" + strings[0]
					+ "\" but received \"" + strings[1] + "\"";
			
		} 
		else if ( err_no == MISSING_TAG )
		{
			
			return "The parser expected at least one \"" + strings[0] + "\"";
					
		} 
		else if ( err_no == CANT_PARSE_XML )
		{
			
			return "XML snippet \"" + strings[0] +"\" can't be parsed";
			
		} 
		else if ( err_no == CANT_PARSE_FILE )
		{
			
			return "File \"" + strings[0] + "\" can't be opened or parsed";
			
		}
		else //UNSPECIFIED
		{
			
			return "InvalidXMLException: Unspecified error";
			
		}
		
	}
	
	
}
