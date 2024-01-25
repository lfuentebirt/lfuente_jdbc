package ejercicioJDBC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OfficialLanguagesReport {

	public static void main(String[] args) {
		/*
		 * Crea una clase Java llamada OfficialLanguagesReport que genere un informe de los 
		 * idiomas oficiales de los países en un continente específico. El programa 
		 * solicitará al usuario que introduzca el nombre del continente y luego mostrará una lista de todos los países en ese continente junto con sus idiomas oficiales y el porcentaje de hablantes de ese idioma en el país.
		 */
		// parametros para la conexion
		String url = "jdbc:mysql://localhost:3306/world";
        String usuario = "demodb";
        String contrasena = "demodb";
        
        try {
        	// 1. Nos conectamos a la BD
			Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
			
			//2. Pedimos por consola
			BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Introduce el nombre del continente para ver sus idiomas oficiales:");
			String continente = consola.readLine();
			
			// vemos si existe el continente
			Statement sentencia = conexion.createStatement();
			String sql = "SELECT continent FROM country WHERE UPPER(continent) =UPPER('" + continente + "');";
			
			//solo queremos el primero
			ResultSet rs = sentencia.executeQuery(sql);
			String continenteBD = "";
			int cont = 0;
			
			while(rs.next() && cont < 1) {
				continenteBD= rs.getString(1);
				cont++;
			}
			
			if(continenteBD.toUpperCase().equals(continente.toUpperCase())) {
				System.out.println(continenteBD.toUpperCase());
			}
			//sacamos la informacion
			String sqlDatos = "select c.name, cl.language, cl.percentage from country c, countrylanguage cl where c.code = cl.countrycode and cl.isOfficial ='T' and  c.continent ='" + continenteBD + "';";
			
			ResultSet resultado = sentencia.executeQuery(sqlDatos);
			System.out.printf("%-20s  %-20s  %-20s \n", "País" , "Lengua oficial", "% habitantes");
			System.out.printf("---------------------------------------------------- \n");
			while(resultado.next()) {
				System.out.printf("%-20s | %-20s | %-30.1f \n", resultado.getString(1), resultado.getString(2), resultado.getFloat(3));
			}
			
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
