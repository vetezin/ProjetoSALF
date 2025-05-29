package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Cotacao;
import projetoSalf.mvc.util.Conexao; // Mantenha esta importação, se Conexao é usado por SingletonDB
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CotacaoDAO implements IDAO<Cotacao> { // Implementa IDAO para Cotacao

    @Override
    public Cotacao gravar(Cotacao cotacao) {
        // Validação básica de datas (se vazias) - você pode adicionar mais validações de formato
        if (cotacao.getCot_dtabertura() == null || cotacao.getCot_dtabertura().isBlank()) {
            throw new IllegalArgumentException("Data de abertura da cotação não pode estar vazia.");
        }
        // cot_dtfechamento pode ser nulo no banco se a cotação ainda estiver aberta
        // Adapte esta validação conforme sua regra de negócio

        String sql = """
            INSERT INTO cotacao(cot_dtabertura, cot_dtfechamento, lc_cod)
            VALUES ('#1', #2, #3) RETURNING cot_cod;
        """;
        sql = sql.replace("#1", cotacao.getCot_dtabertura().replace("'", "''")); // Escapar aspas simples para segurança mínima
        // Se cot_dtfechamento puder ser nulo, trate-o para não inserir a string 'null' literal
        sql = sql.replace("#2", cotacao.getCot_dtfechamento() != null && !cotacao.getCot_dtfechamento().isBlank() ? "'" + cotacao.getCot_dtfechamento().replace("'", "''") + "'" : "NULL");
        sql = sql.replace("#3", String.valueOf(cotacao.getLc_cod()));

        try {
            // Usando consultar() do SingletonDB, que é o padrão que você replicou do ListaCompraDAO
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs != null && rs.next()) {
                cotacao.setCot_cod(rs.getInt("cot_cod"));
                return cotacao;
            } else {
                System.err.println("Erro ao recuperar ID da cotação ou nenhuma linha retornada."); // Alterado para System.err
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gravar cotação: " + e.getMessage()); // Alterado para System.err
            e.printStackTrace(); // Adicionado para depuração
            return null;
        }
    }

    @Override
    public List<Cotacao> get(String filtro) {
        List<Cotacao> cotacoes = new ArrayList<>();
        String sql = "SELECT * FROM cotacao";
        if (filtro != null && !filtro.isBlank()) {
            // CUIDADO: SQL Injection aqui! Considere usar PreparedStatements para filtros dinâmicos.
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Cotacao cotacao = new Cotacao();
                cotacao.setCot_cod(rs.getInt("cot_cod"));
                cotacao.setCot_dtabertura(rs.getString("cot_dtabertura"));
                cotacao.setCot_dtfechamento(rs.getString("cot_dtfechamento")); // Pode vir como null se o campo for nulo no BD
                cotacao.setLc_cod(rs.getInt("lc_cod"));
                cotacoes.add(cotacao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cotações: " + e.getMessage()); // Alterado para System.err
            e.printStackTrace(); // Adicionado para depuração
        }
        return cotacoes;
    }

    @Override
    public Cotacao alterar(Cotacao cotacao) {
        if (cotacao.getCot_dtabertura() == null || cotacao.getCot_dtabertura().isBlank()) {
            throw new IllegalArgumentException("Data de abertura da cotação não pode estar vazia.");
        }

        String sql = """
            UPDATE cotacao SET
                cot_dtabertura = '#1',
                cot_dtfechamento = #2,
                lc_cod = #3
            WHERE cot_cod = #4;
        """;
        sql = sql.replace("#1", cotacao.getCot_dtabertura().replace("'", "''"));
        sql = sql.replace("#2", cotacao.getCot_dtfechamento() != null && !cotacao.getCot_dtfechamento().isBlank() ? "'" + cotacao.getCot_dtfechamento().replace("'", "''") + "'" : "NULL");
        sql = sql.replace("#3", String.valueOf(cotacao.getLc_cod()));
        sql = sql.replace("#4", String.valueOf(cotacao.getCot_cod()));

        // Para UPDATE, DELETE, use manipular()
        if (SingletonDB.getConexao().manipular(sql)) {
            return cotacao;
        } else {
            System.err.println("Erro ao alterar a cotação: " + SingletonDB.getConexao().getMensagemErro()); // Alterado para System.err
            return null;
        }
    }

    @Override
    public Cotacao get(int id) {
        String sql = "SELECT * FROM cotacao WHERE cot_cod = " + id;
        Cotacao cotacao = null;
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs != null && rs.next()) { // Verificação rs != null replicada do padrão de gravação
                cotacao = new Cotacao();
                cotacao.setCot_cod(rs.getInt("cot_cod"));
                cotacao.setCot_dtabertura(rs.getString("cot_dtabertura"));
                cotacao.setCot_dtfechamento(rs.getString("cot_dtfechamento"));
                cotacao.setLc_cod(rs.getInt("lc_cod"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a cotação com ID " + id + ": " + e.getMessage()); // Alterado para System.err
            e.printStackTrace(); // Adicionado para depuração
        }
        return cotacao;
    }


    @Override
    public boolean apagar(Cotacao cotacao) {
        if (cotacao == null) return false;
        String sql = "DELETE FROM cotacao WHERE cot_cod = " + cotacao.getCot_cod();
        // Para DELETE, use manipular()
        if (SingletonDB.getConexao().manipular(sql)) {
            return true;
        } else {
            System.err.println("Erro ao apagar cotação: " + SingletonDB.getConexao().getMensagemErro()); // Alterado para System.err
            return false;
        }
    }
}