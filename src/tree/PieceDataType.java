package tree;

public enum PieceDataType {

	
	HEX , TEXT , MAP;
	
	public static PieceDataType stringToType ( String str )
	{
		
		if ( str.equals("map") )
		{
			
			return MAP;
			
		}
		else if ( str.equals("text") )
		{
			
			return TEXT;
			
		}
		else
		{
			
			return HEX;
			
		}
			
	}
	
	
}
