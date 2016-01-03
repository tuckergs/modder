package xml;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import exceptions.InvalidXMLException;
import main.Main;
import tree.Category;
import tree.PieceDataType;


public class CategoryTreeParser extends XMLParser{

	private Category root;

	
	public Category getRoot()
	{
		
		return root;
		
	}
	
	
	@Override
	public void parse( File xmlFile ) throws Exception 
	{
		
		
		//Initialize xml stuff
		init ( xmlFile );
		
		//Get root element
		Element cur = docu.getDocumentElement();
		
		//Create root category
		root = Category.newRootCategory();
		
		
		//Create category tree
		createCategoryTree ( cur , root );
		
		
	}
	
	
	private void createCategoryTree ( Element curE , Category curCat ) 
									throws Exception
	{
		
		NodeList children = curE.getChildNodes();
		
		
		Node ch;
		Element child;
		
		String childName;
		
		//Main loop
		for ( int i = 0 ; i < children.getLength() ; ++i )
		{
			
			
			
			//Check if the current node is not an Element
			ch = children.item(i);
			if ( ! ( ch instanceof Element ) )
				continue;
			
			//Get child
			child = (Element) children.item(i);
			
			//Get child's name
			childName = child.getTagName();
			
			
			//Do stuff based on name
			if ( 	childName.equals("cat") || 
					childName.equals("category"))
			{
				
				handleCatTag ( child , curCat );
				
			}
			else if ( childName.equals("piece") )
			{
				
				handlePieceTag ( child , curCat );
				
			}
			else if ( childName.equals("map"))
			{
				
				handleMapTag ( child , curCat );
				
			}
			else
			{
				
				InvalidXMLException f = new InvalidXMLException();
				
				f.setInvalidTag("cat, piece, map", childName );
				
				throw f;
				
			}
				
			
			
			
		}
		
		
	}
	
	private void handleCatTag ( Element child , Category curCat ) throws Exception
	{
		
		//Add new category
		String newCatName = child.getAttribute("name");
		Category childCat = curCat.newCategory(newCatName);
		
		//Recurse
		createCategoryTree ( child , childCat );
		
	}

	private void handlePieceTag ( Element child , Category curCat ) throws Exception
	{
		
		//Add new piece
		String newPieceName = child.getAttribute("name");
		long newPieceAddr = Long.parseLong( child.getAttribute("addr") , 16 );
		PieceDataType newPieceType = PieceDataType.stringToType( child.getAttribute("type") );
		String newPieceMap = child.getAttribute("map");
		int newPieceLength = Integer.parseInt ( child.getAttribute("length") , 10 );
		String newPieceValue = Main.data.readBytes( newPieceAddr , newPieceLength );
		curCat.newPiece(newPieceName, newPieceAddr, newPieceType, newPieceMap, newPieceValue );
			
	}
	
	private void handleMapTag ( Element child , Category curCat ) throws Exception
	{
		
		HashMap < String , String > inputMap = new HashMap < String , String >();
		
		NodeList items = child.getElementsByTagName("item");
		
		//Loop to make maps
		for ( int i = 0 ; i < items.getLength() ; ++i )
		{
			
			
			
			//Get item
			Element item = (Element) items.item(i);
			
			//Get key and value
			String data = item.getAttribute ( "bytes" );
			String str = item.getAttribute("alias");
			
			//Put to inputMap
			inputMap.put(data, str);
			
		}
		
		//Put to the map of maps
		String mapName = child.getAttribute("name");
		Main.maps.put( mapName , inputMap );
		
	}
	
	
}
