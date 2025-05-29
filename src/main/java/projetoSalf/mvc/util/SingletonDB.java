package projetoSalf.mvc.util;

import java.sql.Connection;

public class SingletonDB {
    private static Conexao conexao = null;

    private SingletonDB() {}

    public static boolean conectar() {
        conexao = new Conexao();
        return conexao.getConnection() != null;
    }

    public static Connection getConexao() {
        return conexao.getConnection();
    }
}
