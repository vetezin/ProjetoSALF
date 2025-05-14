package projetoSalf.mvc.util;

public class SingletonDB {
    private static Conexao conexao=null;

    private SingletonDB() {
    }

    public static boolean conectar()
    {
        conexao=new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/","salf_db","postgres","postgres123");
    }
    public static Conexao getConexao() {
        return conexao;
    }

}
