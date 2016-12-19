package main;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import helper.HexStuff;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import tree.Category;
import tree.Piece;
import tree.PieceDataType;
import xml.CategoryTreeParser;

public class Main extends Application{

	
	public static Data data;
	
	public static Category rootCategory;
	public static Category curCategory;
	
	//public static Piece curPiece;
	
	
	private class ModifyPieceWindow{
		
		public ModifyPieceWindow(Piece p) {
			piece = p;
		}
		public Stage stage;
		public Piece piece;
		public Label l_pieceName;
		public TextArea f_byteEditor;
		public ComboBox<String> valueChooser;
		public Button btn_modifyPiece;
		public VBox vb_pieceEditor;
		
		//This is to prevent an infinite loop in a combobox's set on action
		public boolean valueChooserChangeAlreadyHandled = false;
		
	}
	
	
	//Main stage UI elements
	private VBox vb_categoryBrowser;
	//private VBox vb_pieceEditor;
	
	//For category browser
	private TextField f_UCI;
	private TextArea f_categories;
	private TextField f_browserInput;
	
	private Button btn_saveFile;
	private Button btn_saveAs;

	//For detection of newline in browser input
	private boolean keyDownInBrowserInput;
	

	
	
	
	
	//This map is a map of all input maps -_-
	/*
	 * The first string represents the name of a map
	 * The second string represents the text that is dealt with in a particular textfield
	 * The third string is what is shown in that particular textfield
	 */
	public static HashMap < String , HashMap < String , String >> maps;

	
	public static void main ( String[] args )
	{
		launch ( args );
	}
	

	@Override
	public void start(Stage stOpen) throws Exception {
		
		//Initialize stuff
		maps = new HashMap < String , HashMap < String , String >>();
		
		//Title
		stOpen.setTitle("Modder Lobby");
		
		//Make grid
		GridPane grid = new GridPane ();
		grid.setHgap(8);
		grid.setVgap(8);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets ( 8 , 8 , 8 , 8 ));
		
		
		//Create category file field
		Label l_xmlField = new Label ( "Category file: ");
		grid.add( l_xmlField , 0 , 0 );
		TextField f_xmlField = new TextField ();
		grid.add( f_xmlField , 1 , 0 );
		Button btn_xmlField = new Button ( "..." );
		grid.add( btn_xmlField , 2, 0 );
		
		//Create data file field
		Label l_dataField = new Label ( "Data file: ");
		grid.add( l_dataField , 0 , 1 );
		TextField f_dataField = new TextField ();
		grid.add( f_dataField , 1 , 1 );
		Button btn_dataField = new Button ( "..." );
		grid.add( btn_dataField , 2, 1 );
		
		//Create load button
		Button btn_load = new Button ( "Let's do it!" );
		grid.add( btn_load , 1, 2 );
		
		//Add button logic
		btn_xmlField.setOnAction( event -> {
			
			FileChooser chooser = new FileChooser ();
			
			chooser.setTitle("Open category file");
			
			chooser.getExtensionFilters().add(
								new ExtensionFilter ( "Category files" , "*.xml" ));
			
			File f = chooser.showOpenDialog(stOpen);
			
			f_xmlField.setText(f.getAbsolutePath());
			
			
		});
		btn_dataField.setOnAction( event -> {

			FileChooser chooser = new FileChooser ();

			chooser.setTitle("Open data file");
			
			chooser.getExtensionFilters().add(
					new ExtensionFilter ( "All files" , "*.*" ));

			File f = chooser.showOpenDialog(stOpen);

			f_dataField.setText(f.getAbsolutePath());

		});
		
		btn_load.setOnAction( event -> {
			
			String categoryFileName = f_xmlField.getText();
			String dataFileName = f_dataField.getText();
			
			try {
				
				loadStuff ( categoryFileName , dataFileName , stOpen );
			
			} catch (Exception e) {
			
				printException ( e );
				
			}
			
		});
		
		//Make a scene and show
		Scene scene = new Scene ( grid , 300 , 289 );
		stOpen.setScene ( scene );
		stOpen.show();
		
		
	}
	
	private void loadStuff ( String categoryFileName , String dataFileName  , Stage curStage ) throws Exception
	{
		

		//Load the data file
		data = new Data ( new File ( dataFileName ) );

		//Parse category file
		CategoryTreeParser p = new CategoryTreeParser ();
		p.parse( new File ( categoryFileName ) );
		rootCategory = p.getRoot();
		curCategory = rootCategory;


		//Create main stage
		Stage stMain = createMainStage();

		curStage.hide();
		stMain.show();


		
	}
	
	
	private Stage createMainStage () 
	{
		
		//New stage
		Stage newSt = new Stage();
		
		//Set title
		newSt.setTitle("Modder");
		
		
		//Create pane
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets ( 15 , 15 , 15 , 15 ));
		
		
		//Create category browser box
		vb_categoryBrowser = new VBox ( 12 );
		
		//Create UCI field (UCI stands for Universal Category Identifier
		f_UCI = new TextField ();
		f_UCI.setEditable( false );
		f_UCI.setText(":");
		vb_categoryBrowser.getChildren().add(f_UCI);
		
		//Create categories field
		f_categories = new TextArea ();
		f_categories.setEditable(false);
		f_categories.setPrefColumnCount(16);
		f_categories.setPrefRowCount(20);
		f_categories.setWrapText(false);
		updateCategoriesField();
		vb_categoryBrowser.getChildren().add(f_categories);
		
		//Create browser input field
		f_browserInput =  new TextField();
		f_browserInput.setPrefColumnCount(16);
		vb_categoryBrowser.getChildren().add(f_browserInput);
		
		addCategoryBrowserLogic();
		
		//Add to pane
		pane.setLeft(vb_categoryBrowser);
		
		
		//Add save button
		btn_saveFile = new Button ( "Save file" );
		addSaveButtonLogic();
		btn_saveFile.setAlignment(Pos.CENTER);
		btn_saveFile.setVisible(false);
		pane.setCenter(btn_saveFile);
		
		
		
		
		//Initialize piece editor
		VBox vb = new VBox ( 12 );
		
		//Add to pane
		pane.setRight(vb);
		
		//Add scene
		Scene scene =  new Scene ( pane , 600 , 300 );
		newSt.setScene(scene);
		
		return newSt;
		
	}
	
	
	

	//GLORIFIED HELPERS
	
	private void addCategoryBrowserLogic()
	{
		
		//Pressing Enter in the browser input field should do stuff
		f_browserInput.setOnKeyPressed( new EventHandler < KeyEvent > (){

			@Override
			public void handle(KeyEvent ke) {

				keyDownInBrowserInput = true;

			}

		});
		f_browserInput.setOnKeyReleased( new EventHandler < KeyEvent >(){

			@Override
			public void handle(KeyEvent ke) {

				if ( ke.getCode().equals(KeyCode.ENTER) && keyDownInBrowserInput)
				{

					keyDownInBrowserInput = false;
					
					try {
						
						browserInputQuery();
					
					} catch (Exception e)  {
						
						printException ( e );
						
					}

				}

			}

		});
		
	}
	
	private void addModifyButtonLogic(ModifyPieceWindow wn)
	{
		
		wn.btn_modifyPiece.setOnAction( event -> {


			try {
				
				if ( wn.piece.getDataType() == PieceDataType.TEXT )
					wn.piece.setValue( HexStuff.textStringToHexString( wn.f_byteEditor.getText() ) );
				else
					wn.piece.setValue( wn.f_byteEditor.getText() );
			
			} catch (Exception e) {
				
				printException ( e );
				
			}

		});
		
	}
	
	private void addSaveButtonLogic()
	{
		
		btn_saveFile.setOnAction( event -> {
			
			try {
				
				saveAllPieces ( rootCategory );
				
				toast ( "Saved!" , "Save successful" , false );
				
			}
			catch ( Exception e )
			{
				
				printException ( e );
				
			}
			
		});
		
	}
	
	private void saveAllPieces ( Category cat ) throws Exception
	{
		
		Collection < Piece > pieces = cat.getAllPieces();
		Collection < Category > cats = cat.getAllChildCategories();
		
		//Write pieces
		for ( Iterator<Piece> itr = pieces.iterator() ; itr.hasNext() ; )
		{
			
			Piece p = (Piece) itr.next();
			savePiece ( p );
		
		}
		
		//Recurse
		for ( Iterator<Category> itr = cats.iterator() ; itr.hasNext() ; )
		{
			
			Category child = (Category) itr.next();
			saveAllPieces ( child );
			
		}
			
	}
	
	private void savePiece ( Piece p ) throws Exception
	{
		
		long pos =  p.getAddr();
		
		String byteString = p.getValue();
		
		data.writeBytes( pos, byteString );
		
	}

	private void browserInputQuery() throws Exception
	{
		
		
		String query = f_browserInput.getText();
		char[] queryChars = query.toCharArray();
		
		
		//Handle parent call
		if ( query.equals("..") || query.equals("..:") )
		{
			
			if ( curCategory.getParent() == null )
				throw new Exception ( "The root has no parent directory" );
			
			f_browserInput.setText("");
			
			changeCurrentCategory ( curCategory.getParent() );
			
		}
		else
		{
			
			boolean categoryForced = false , pieceForced = false;

			char endChar = queryChars [ queryChars.length - 1 ];

			if ( endChar == ':' ) categoryForced = true;

			if ( endChar == '+' ) pieceForced = true;

			//Get rid of the ending colon or plus if either exists
			if ( categoryForced || pieceForced )
				query = new String ( queryChars , 0 , queryChars.length - 1 );

			boolean nameInCategory , nameInPiece;

			nameInCategory = curCategory.hasCategory(query);
			nameInPiece = curCategory.hasPiece(query);
			
			
			//Check if name exists
			if ( !nameInPiece && !nameInCategory )
				throw new Exception ( "Name not found" );
			
			//Check if one thing was forced but it doesn't exist
			if ( categoryForced && !nameInCategory )
				throw new Exception ( "\"" + query + "\" is not a category" );
			if ( pieceForced && !nameInPiece )
				throw new Exception ( "\"" + query + "\" is not a piece" );

			//We want to test if there is a category and a piece of the same name
			//and neither a category nor a piece was forced
			if ( nameInCategory && nameInPiece && !categoryForced && !pieceForced )
				throw new Exception ( "Can't decide between category or piece" );


			boolean useCategory = true;


			//Figure out whether or not to look for a category or a piece
			if ( categoryForced )
				useCategory = true;
			if ( pieceForced )
				useCategory = false;
			if ( nameInCategory && !nameInPiece )
				useCategory = true;
			if ( nameInPiece && !nameInCategory )
				useCategory = false;
			
			
			//Do stuff
			if ( useCategory )
			{
				
				changeCurrentCategory ( curCategory.getCategory(query) );
				
			}
			else
			{
				
				makeModifyPieceWindow ( curCategory.getPiece(query) );
				
			}
			

			f_browserInput.setText("");

		}
		
	}
	
	
	
	
	
	
	//HELPERS
	
	
	//Dialog box
	private void toast ( String title , String message , boolean error)
	{

		//Dialog box object
		Alert al = new Alert(AlertType.INFORMATION);

		if ( error )
			al.setAlertType(AlertType.ERROR);

		al.setTitle(title);
		al.setHeaderText(null);
		al.setContentText(message);

		al.showAndWait();

	}
	
	private void printException ( Exception e )
	{
		
		e.printStackTrace();
		
		toast ( e.getClass().getName() , e.getMessage() , true );
		
	}
	
	private void updateCategoriesField()
	{
		
		Set<String> categoryNames = curCategory.getAllChildCategoryNames();
		Set<String> pieceNames = curCategory.getAllPieceNames();
		
		String text = "";
		
		if ( curCategory.getParent() != null )
			text = "..";
		
		//List categories
		for ( Iterator<String> itr = categoryNames.iterator() ; itr.hasNext() ; )
		{
			
			String curName = (String) itr.next();
			
			text = text + "\n" + curName + ":";
			
		}
		
		//List pieces
		for ( Iterator<String> itr = pieceNames.iterator() ; itr.hasNext() ; )
		{
			
			String curName = (String) itr.next();
			
			text = text + "\n" + curName + "+";
			
		}
		
		f_categories.setText(text);
		
		
	}
	
	
	private String getUCI ( Category cat )
	{
		
		//If there is no parent, it's the root
		if ( cat.getParent() == null )
			return ":";
		else
			return getUCI ( cat.getParent() ) + cat.getName() + ":";
		
	}
	
	
	
	
	
	private void makeTextInput() throws Exception
	{
		
		/*//Remove all things in the VBox
		vb_pieceEditor.getChildren().clear();
		
		
		//Create label
		l_pieceName = new Label( curPiece.getName() );
		vb_pieceEditor.getChildren().add( l_pieceName );
		
		//Create byte editor field
		f_byteEditor = new TextArea();
		f_byteEditor.setPrefColumnCount( 16 );
		f_byteEditor.setPrefRowCount(16);
		f_byteEditor.setText( HexStuff.hexStringtoTextString( curPiece.getValue() ) );
		vb_pieceEditor.getChildren().add ( f_byteEditor );
		
		
		//Create modify button
		createModifyButton(wn);
		
		
		//Unhide save button
		btn_saveFile.setVisible(true);*/
		
	}
	
	private void createModifyButton (ModifyPieceWindow wn) throws Exception
	{
		
		//Create modify button
		wn.btn_modifyPiece = new Button ( "Modify piece" );
		addModifyButtonLogic (wn);
		wn.vb_pieceEditor.getChildren().add( wn.btn_modifyPiece );
		
	}
	
	
	
	private void changeCurrentCategory( Category newCat )
	{
		
		curCategory = newCat;
		
		f_UCI.setText( getUCI ( curCategory ) );
		
		updateCategoriesField();
		
	}
	
	
	
	


	private ModifyPieceWindow makeModifyPieceWindow(Piece p) throws Exception
	{

		ModifyPieceWindow wn = new ModifyPieceWindow(p);
		
		PieceDataType datatype = p.getDataType();
		
		if ( datatype == PieceDataType.HEX || datatype == PieceDataType.MAP )
		{
			
			makeHexInput(wn);
			
		}
		else
		{
			
			//makeTextInput (wn);
			throw new Exception("Text boxes aren't implemented yet");
			
		}
		
		return wn;

	}
	
	private void makeHexInput(ModifyPieceWindow wn) throws Exception
	{
		
		wn.stage = new Stage();
		wn.stage.setTitle("Modify piece...");
		
		wn.vb_pieceEditor = new VBox(12);

		//Create label
		wn.l_pieceName = new Label( getUCI(curCategory) + wn.piece.getName() );
		wn.vb_pieceEditor.getChildren().add( wn.l_pieceName );

		//Create byte editor field
		wn.f_byteEditor = new TextArea();
		wn.f_byteEditor.setPrefColumnCount( 16 );
		wn.f_byteEditor.setPrefRowCount(2);

		//Set byte editor text
		wn.f_byteEditor.setText( wn.piece.getValue() );


		wn.vb_pieceEditor.getChildren().add ( wn.f_byteEditor );

		//Change the combobox selection when needed
		wn.f_byteEditor.setOnKeyTyped( event -> {

			Platform.runLater( new Runnable()
			{

				public void run() {

					HashMap<String, String> m;
					//Get necessary map
					m = maps.get(wn.piece.getMapName());
					String key = wn.f_byteEditor.getText();

					if (m != null && m.containsKey(key)) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								wn.valueChooserChangeAlreadyHandled = true;
								wn.valueChooser.setValue(m.get(key));
							}
						});
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								wn.valueChooserChangeAlreadyHandled = true;
								wn.valueChooser.setValue("");
							}
						});
					}	
				}
			});
		});



		//Get map referenced by the piece
		HashMap < String , String > map_ 
		= maps.get	(wn.piece.getMapName());

		boolean noMap = false;

		if (map_ == null )
			noMap = true;

		final HashMap < String , String > map;

		if ( noMap )
			map = new HashMap < String , String >();
		else
			map = map_;


		//Create value chooser
		wn.valueChooser = new ComboBox<String> ();
		if ( noMap ) 
			wn.valueChooser.setDisable(true);
		wn.valueChooser.setEditable(false);
		wn.valueChooser.getItems().addAll( map.keySet() );

		//On start, check if the byte editor is a key in the map
		if ( map.containsKey(wn.f_byteEditor.getText()) )
		{

			wn.valueChooser.setValue(map.get(wn.f_byteEditor.getText()));

		}

		//Replace the byte sequences with the name
		wn.valueChooser.setCellFactory( new Callback<ListView<String>, ListCell<String>>() {

			@Override 
			public ListCell<String> call(ListView<String> param) {

				final ListCell<String> cell = new ListCell<String>() {
					{
						super.setPrefWidth(100);
					}    

					@Override 
					public void updateItem(String item, boolean empty) {

						super.updateItem(item, empty);
						if (item != null) {
							setText( map.get(item) );    
						}

					}

				};

				return cell;

			}

		});

		wn.valueChooser.setOnAction( event -> {

			//This is to prevent an infinite loop caused by setValue
			if ( !wn.valueChooserChangeAlreadyHandled ) {

				wn.valueChooserChangeAlreadyHandled = true;
				wn.f_byteEditor.setText(wn.valueChooser.getValue());

				//Change select text
				HashMap<String, String> m = maps.get(wn.piece.getMapName());
				if (m != null)
				{
					Platform.runLater( new Runnable (){
						@Override
						public void run() {
							wn.valueChooser.setValue(m.get(wn.valueChooser.getValue()));
						}
					});
				}
			}
			else
			{

				wn.valueChooserChangeAlreadyHandled = false;

			}

		});


		wn.vb_pieceEditor.getChildren().add(wn.valueChooser);


		//Create modify button
		createModifyButton(wn);

		//Unhide save button
		btn_saveFile.setVisible(true);
		
		Scene scene =  new Scene ( wn.vb_pieceEditor );
		wn.stage.setScene(scene);
		
		wn.stage.show();
		
	}

}
