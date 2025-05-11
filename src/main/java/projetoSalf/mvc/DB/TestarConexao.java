package projetoSalf.mvc.DB;

import projetoSalf.mvc.DB.Conexao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestarConexao {

    public static void main(String[] args) {
        // Testando a conexão com o banco
        try (Connection conn = Conexao.getConnection()) {
            // Se a conexão for bem-sucedida, imprime uma mensagem
            System.out.println("Conectado ao banco de dados com sucesso!");

            // Vamos executar uma consulta simples para testar
            Statement stmt = conn.createStatement();
            String sql = "SELECT version()"; // Consulta para verificar a versão do PostgreSQL
            ResultSet rs = stmt.executeQuery(sql);

            // Imprimir o resultado da consulta
            while (rs.next()) {
                System.out.println("Versão do PostgreSQL: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
