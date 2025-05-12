package projetoSalf.mvc.util;

public class SingletonDB {
    private static Conexao conexao=null;

    public static Conexao getConexao() {
        return conexao;
    }


    public static boolean conectar()
    {
        conexao=new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/","casofa_db","postgres","postgres123");
    }
}
