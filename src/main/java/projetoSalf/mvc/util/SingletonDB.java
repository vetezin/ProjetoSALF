package projetoSalf.mvc.util;

<<<<<<< HEAD
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
=======
public class SingletonDB {
    private static Conexao conexao=null;

    private SingletonDB() {
    }

    public static boolean conectar()
    {
        conexao=new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/","salf_db","postgres","postgres");
    }
    public static Conexao getConexao() {
        return conexao;
    }

}
>>>>>>> Geral
