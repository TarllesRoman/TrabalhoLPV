package br.com.academia.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaDeConexao {
	public static Connection fabricarConexao() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://localhost/academia","postgres","aluno");	
	}
}
