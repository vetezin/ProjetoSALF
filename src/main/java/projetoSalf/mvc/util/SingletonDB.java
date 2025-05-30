package projetoSalf.mvc.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {}

    public static boolean conectar() {
        conexao = new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/", "SALF", "Caiopros", "Senha123");
    }

    public static Conexao getConexao() {
        return conexao;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        if (conexao == null || conexao.getConnect() == null) {
            throw new SQLException("Banco de dados não conectado.");
        }
        return conexao.getConnect().prepareStatement(sql);
    }
}
