package projetoSalf.mvc.util;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private Connection conn;

    // Construtor: carrega o driver e cria a conexão
    public Conexao() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retorna a conexão ativa
    public Connection getConnection() {
        return conn;
    }

    // Fecha a conexão, se aberta
    public void fechar() {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
=======
import java.sql.*;

public class Conexao
{
    private Connection connect;
    private String erro;
    public Conexao()
    {   erro="";
        connect=null;
    }

    public Connection getConnect() {
        return connect;
    }

    public boolean conectar(String local, String banco, String usuario, String senha)
    {   boolean conectado=false;
        try {
            //Class.forName(driver); "org.postgresql.Driver");
            String url = local+banco; //"jdbc:postgresql://localhost/"+banco;
            connect = DriverManager.getConnection( url, usuario,senha);
            conectado=true;
        }
        catch ( SQLException sqlex )
        { erro="Impossivel conectar com a base de dados: " + sqlex.toString(); }
        catch ( Exception ex )
        { erro="Outro erro: " + ex.toString(); }
        return conectado;
    }
    public String getMensagemErro() {
        return erro;
    }
    public boolean getEstadoConexao() {
        return (connect!=null);
    }
    public boolean manipular(String sql) // inserir, alterar,excluir
    {   boolean executou=false;
        try {
            Statement statement = connect.createStatement();
            int result = statement.executeUpdate( sql );
            statement.close();
            if(result>=1)
                executou=true;
        }
        catch ( SQLException sqlex )
        {  erro="Erro: "+sqlex.toString();
        }
        return executou;
    }
    public ResultSet consultar(String sql)
    {   ResultSet rs=null;
        try {
            Statement statement = connect.createStatement();
            //ResultSet.TYPE_SCROLL_INSENSITIVE,
            //ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery( sql );
            //statement.close();
        }
        catch ( SQLException sqlex )
        { erro="Erro: "+sqlex.toString();
            rs = null;
        }
        return rs;
    }
    public int getMaxPK(String tabela,String chave)
    {
        String sql="select max("+chave+") from "+tabela;
        int max=0;
        ResultSet rs= consultar(sql);
        try
        {
            if(rs.next())
                max=rs.getInt(1);
        }
        catch (SQLException sqlex)
        {
            erro="Erro: " + sqlex.toString();
            max = -1;
        }
        return max;
    }
}
>>>>>>> Geral
