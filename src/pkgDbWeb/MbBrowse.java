/*
 * MbBrowse
 * JSF 2.3 DB-Anwendung
 */

 package pkgDbWeb;

 import static java.lang.System.*;

 import java.io.Serializable;
 import java.sql.Connection;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.sql.Date;
 import java.sql.PreparedStatement;

 import javax.annotation.PostConstruct;
 import javax.enterprise.context.SessionScoped;
 import javax.faces.application.FacesMessage;
 import javax.faces.context.FacesContext;
 import javax.faces.event.ActionEvent;
import javax.inject.Named;


 /**
 * Backing bean der JSF-Seite browse.xhtml
 *
 * @author Haris, Ibrahim, Seweryn
 * @version 2.3.0, 2018-07-05
 * @see "Foliensatz zur Vorlesung"
 */
 @Named
 @SessionScoped
 public class MbBrowse implements Serializable {

 private static final long serialVersionUID = 1L;

 final String SQL_SELECT = "select KundenID, Nachname, Vorname, Adresse, Postleitzahl, Telefonnummer from Kunde";

 private boolean connected = false;
 private boolean prevButtonDisabled = true;
 private boolean nextButtonDisabled = true;

 /* Util ist eine Hilfsklasse, die u. a. den Verbindungsaufbau zur Datenbank
 * vereinfacht: */
 private Util util = new Util();

 private Connection con = null;
 private Statement stm = null;
 private ResultSet rs = null;
 //private PreparedStatement ps = null;

 private int kundenId = 0;
 private String nachname = "";
 private String vorname = "";
 private String adresse = "";
 private int telefonnummer=0;
 private int postleitzahl= 0;
 private int bestellID;
 public int getBestellID() {
	return bestellID;
}

public void setBestellID(int bestellID) {
	this.bestellID = bestellID;
}
private int groesse =0;
 


 
 
 

 /*--------------------------------------------------------------------------*/

 public MbBrowse() { System.out.println( "MbBrowse.<init>..." ); }

 @PostConstruct
public void init() { System.out.println( "@PostConstruct.MbBrowse" ); }

 public void preRenderAction() { System.out.println( "MbBrowse.preRenderAction" ); }
 public void postRenderAction() { System.out.println( "MbBrowse.postRenderAction" ); }



 public int getKundenId() {
	return kundenId;
}

public void setKundenId(int kundenId) {
	this.kundenId = kundenId;
}

public String getNachname() {
	return nachname;
}

public void setNachname(String nachname) {
	this.nachname = nachname;
}

public String getVorname() {
	return vorname;
}

public void setVorname(String vorname) {
	this.vorname = vorname;
}

public String getAdresse() {
	return adresse;
}

public void setAdresse(String adresse) {
	this.adresse = adresse;
}


public int getTelefonnummer() {
	return telefonnummer;
}

public void setTelefonnummer(int telefonnummer) {
	this.telefonnummer = telefonnummer;
}

public int getPostleitzahl() {
	return postleitzahl;
}

public void setPostleitzahl(int postleitzahl) {
	this.postleitzahl = postleitzahl;
	
	
}

public int getGroesse() {
	return groesse;
}

public void setGroesse(int groesse) {
	this.groesse = groesse;
}


public boolean getPrevButtonDisabled(){ return prevButtonDisabled; }
 public boolean getNextButtonDisabled(){ return nextButtonDisabled; }
 public boolean getConnected(){ return connected; }
 public void setConnected( boolean b ){ connected = b; }

 /*--------------------------------------------------------------------------*/

 private void showData() throws SQLException {
 setKundenId ( rs.getInt ( "KundenID" ) );
setNachname ( rs.getString ( "Nachname") );
 setVorname ( rs.getString ( "Vorname") );
 setPostleitzahl ( rs.getInt ( "Postleitzahl") );
 setTelefonnummer( rs.getInt( "Telefonnummer") );
 setAdresse(rs.getString("Adresse"));
 }

 /*--------------------------------------------------------------------------*/

 /**
 * Verbindung zur Datenbank herstellen und disconnect button und browse
 * buttons freigeben
 * @param ae ActionEvent
 */
 public void connect( ActionEvent ae ) {

 //out.println( "connect()..." );

 if( util != null ) con = util.getCon();
 if( con != null ) {
 try {
 stm = con.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,
 ResultSet.CONCUR_UPDATABLE );
 rs = stm.executeQuery( SQL_SELECT );
 if( rs.first() ) showData();
 connected = true;
 prevButtonDisabled = false;
 nextButtonDisabled = false;
 } catch( Exception ex ) {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage())
 );
 out.println( "Error: " + ex );
 ex.printStackTrace();
 }
 }
 else {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "Exception",
 "Keine Verbindung zur Datenbank (Treiber nicht gefunden?)" ));
 out.println( "Keine Verbingung zur Datenbank" );
 }
 }

 /*--------------------------------------------------------------------------*/

 /**
 * Verbindung zur Datenbank beenden
 * @param ae ActionEvent
 */
 public void disconnect( ActionEvent ae ) {

 if( con != null ) {
 try {
 if( rs != null ) rs.close();
 if( stm != null ) stm.close();

 util.closeConnection( con );

 connected = false;
 prevButtonDisabled = true;
 nextButtonDisabled = true;

 setKundenId ( 0 );
 setNachname ( "" );
 setVorname ( "" );
 setPostleitzahl ( 0 );
 setAdresse( "" );
 setTelefonnummer(0);
 

 } catch( Exception ex ) {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "Exception", ex.getLocalizedMessage())
 );
 out.println( "Error: " + ex );
 ex.printStackTrace();
 }
 }
 }

 /*--------------------------------------------------------------------------*/

 /**
 * Zum vorherigen Datensatz scrollen
 * @param ae ActionEvent
 */
 public void prev( ActionEvent ae ) {
 try {
 if( (rs != null) && rs.previous() ){
 showData();
 nextButtonDisabled = false;
 }
 else
 prevButtonDisabled = true;

 } catch( Exception ex ) {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "Exception", ex.getLocalizedMessage())
 );
 out.println( "Error: " + ex );
 ex.printStackTrace();
 }
 }

 /*--------------------------------------------------------------------------*/

 /**
 * Weiterscrollen
 * @param ae ActionEvent
 */
 public void next( ActionEvent ae ) {
 try {
 if( (rs != null) && rs.next() ) {
 showData();
 prevButtonDisabled = false;
 }
 else
 nextButtonDisabled = true;

 } catch( Exception ex ) {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "Exception", ex.getLocalizedMessage())
 );
 out.println( "Error: " + ex );
 ex.printStackTrace();
 }
 }

 

 
 /*--------------------------------------------------------------------------*/
 /**
 * Datensatz einfügen
 * @param ae ActionEvent
 */
 public void insert( ActionEvent ae ) {
	
	 

 try {
 //if( ps == null ){
 String sQl = "INSERT INTO Kunde( "
 +"KundenID, Vorname, Nachname, Adresse, Postleitzahl, Telefonnummer ) " +
 "VALUES ( ?, ?, ?, ?, ?, ? )";
 PreparedStatement ps = con.prepareStatement( sQl );
 //}

 
 
 
 ps.setInt ( 1, kundenId );
 ps.setString ( 2, nachname );
 ps.setString ( 3, vorname );
 ps.setString ( 4, adresse );
 ps.setInt( 5, postleitzahl );
 ps.setInt(6, telefonnummer);

 int n = ps.executeUpdate();
 if( n == 1 ) {
 out.println( "O.K., Datensatz eingefügt.");
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_INFO, "O. K.",
 "Ihre Daten wurden Erfolgreich gespeichert." )
 );
 
 String sqlBestellung = "INSERT INTO Bestellung (BestellID, Preis,KundenID) VALUES (null, 0,"+kundenId+")";
 
 ps.executeUpdate(sqlBestellung);
 

 
 
 
 }

 ps.close();

 // Result set neu aufbauen:
 rs = stm.executeQuery( SQL_SELECT );
 } catch( SQLException ex ) {
 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage())
 );
 out.println( "Error: " + ex );
 ex.printStackTrace();
 }
 }

 /*--------------------------------------------------------------------------*/

 
 public void bestellen( ActionEvent ae) {
	 	 
	 
	 try {
		 
		 
			Statement sta = con.createStatement();

			
//		
			System.out.println("Test Ab hier  String:_________________");
			String query = "SELECT MAX(BestellID) FROM Bestellung";
			ResultSet bID =	sta.executeQuery(query); //...
			while(bID.next()) {
				bID.getMetaData().getColumnCount();
			
			setBestellID(bID.getInt(1));
			System.out.println(bestellID+"Testbesttid");
				
			}
			
		
		int pizzaId = 0;
		
		if(groesse==26)
			pizzaId = 20;
		
		if(groesse==32)
			pizzaId = 19;
		
		if(groesse==48)
			pizzaId = 32;
			
		String sqlIns = "INSERT INTO BestelltePizzen (BestellID, PizzaID) VALUES ("+bestellID+","+pizzaId+")";
		sta.executeUpdate(sqlIns);
		
		
		sta.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
 }
 
 
 /**
 * Datensatz löschen
 * @param ae ActionEvent
 */
 public void delete( ActionEvent ae ) {

 if( util != null ) util.log( "delete()..." );

 FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
 FacesMessage.SEVERITY_WARN, "Datensatz nicht gelöscht!",
 "Löschen nicht erlaubt." )
 );
 }
 public class PanelView {
     
	   
 /*--------------------------------------------------------------------------*/

 /**
 * Datensatz aktualisieren
 * @param ae ActionEvent
 */
 public void update( ActionEvent ae ) {

 //out.println( "update()..." );
 if( util != null ) util.log( "update()..." );

 try {
 PreparedStatement ps = con.prepareStatement( "UPDATE Kunde SET " +
 "Vorname = ?, Nachname= ?, Adresse= ?, Postleitzahl=?, Telefonnummer=? " +
 "WHERE KundenID = ?" );

ps.setString ( 1, vorname );
ps.setString ( 2, nachname );
ps.setString ( 3, adresse );
ps.setInt( 4, postleitzahl);
ps.setInt ( 5, telefonnummer );
ps.setInt(6, kundenId);

int n = ps.executeUpdate();
if( n == 1 ) {
out.println( "O.K., Datensatz geändert.");
FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
FacesMessage.SEVERITY_INFO, "O. K.",

"Datensatz wurde erfolgreich geändert." )
);
}
else if( n == 0 ) {
out.println( "Keine Änderung!!");
FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
FacesMessage.SEVERITY_WARN, "Datensatz nicht geändert!",
"PK-Änderung nicht erlaubt." )
);
}

ps.close();

// Result set neu aufbauen:
rs = stm.executeQuery( SQL_SELECT );
} catch( SQLException ex ) {
FacesContext.getCurrentInstance().addMessage( null, new FacesMessage(
FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage())
);
out.println( "Error: " + ex );
ex.printStackTrace();
}
}
}
 }