package tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Category {

	private String name;
	
	private HashMap < String , Category > cats;
	
	private HashMap < String , Piece > pieces;
	
	private Category parent;
	
	
	private Category 
	( 	
		String name ,
		HashMap < String , Category > cats ,
		HashMap < String , Piece > pieces ,
		Category parent 
	)
	{

		this.name = name;

		this.cats = cats;

		this.pieces = pieces;
		
		this.parent = parent;

	}
	

	//Creator
	public static Category newRootCategory()
	{
		
		return new Category (
							"",
							new HashMap < String , Category > (),
							new HashMap < String , Piece > (),
							null
							);
		
	}
	
	public String getName ()
	{
		
		return name;
		
	}
	
	public Category getCategory ( String id )
	{
		
		return cats.get(id);
		
	}
	
	public Category getParent()
	{
		
		return parent;
		
	}
	
	public Set<String> getAllChildCategoryNames ()
	{
		
		return cats.keySet();
		
	}
	
	public Collection<Category> getAllChildCategories ()
	{
		
		return cats.values();
		
	}
	
	public Piece getPiece ( String id )
	{
		
		return pieces.get(id);
		
	}
	
	public Collection<Piece> getAllPieces()
	{
		
		return pieces.values();
		
	}
	
	public Set<String> getAllPieceNames()
	{
		
		return pieces.keySet();
		
	}
	
	
	//Setters
	public Category newCategory ( String id ) throws Exception
	{
		
		if ( cats.containsKey(id) )
			throw new Exception ( "Duplicate \"" + id + "\"" );
		
		
		//Set new category's stuff
		String chName = id;
		HashMap < String , Category > chCats
					= new HashMap < String , Category > ();
		HashMap < String , Piece > chPieces
					= new HashMap < String , Piece > ();
		
		Category chParent = (Category) this;
		
		
		//Make new category
		Category cat = new Category ( 	chName , chCats , 
										chPieces , chParent );
		
		
		//Add category
		cats.put( chName , cat );
		
		
		return cat;
		
		
	}
	
	
	public Piece newPiece ( 	String id , long addr , 
							PieceDataType datatype , String mapname ,  String value )
	{
		
		Piece p = new Piece ( id , addr , datatype , mapname , value );
		
		pieces.put( id , p );
		
		return p;
		
	}
	
	
	//Testers
	public boolean hasCategory ( String id )
	{
		
		return cats.containsKey(id);
		
	}
	public boolean hasPiece ( String id )
	{
		
		return pieces.containsKey(id);
		
	}
	
}
