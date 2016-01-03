package tree;

public class Piece {

	private long addr;
	
	private String name;
	
	private PieceDataType datatype;
	private String mapname;
	
	//This is a string representation of a byte string
	private String value;
	
	//The numbers of bytes that a field should have
	private int length;
	
	
	public Piece ( 	String name , long addr , 
								PieceDataType datatype , String mapname , String value  )
	{
		
		this.name = name;
		
		this.addr = addr;
		
		
		this.value = new String ( value );
		
		this.length = value.length() / 2;
		
		
		this.datatype = datatype;
		
		this.mapname = mapname;
		
	}
	
	
	//Getters
	public long getAddr()
	{
		
		return addr;
		
	}
	
	public String getName()
	{
		
		return name;
		
	}
	
	public PieceDataType getDataType()
	{
		
		return datatype;
		
	}
	
	public String getMapName()
	{
		
		return mapname;
		
	}
	
	public int getLength()
	{
		
		return length;
		
	}
	
	public String getValue()
	{
		
		return value + "";
		
	}
	
	
	
	//Setters
	public void setValue ( String val ) throws Exception
	{
		
		//We need to check if the input length is not right
		if ( length != ( val.length() /2 ) )
			throw new Exception ( "Inputted length is not equal to field length" );
		
		value = new String ( val );
		
	}
	
}
