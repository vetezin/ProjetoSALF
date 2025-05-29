package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.CotacaoForn;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CotacaoFornDAO { // Não implementa IDAO<T> porque o 'get' é composto

    public CotacaoForn gravar(CotacaoForn cotacaoForn) {
        String sql = """
            INSERT INTO cot_forn (cot_cod, forn_cod)
            VALUES (#1, #2);
        """;
        sql = sql.replace("#1", String.valueOf(cotacaoForn.getCot_cod()));
        sql = sql.replace("#2", String.valueOf(cotacaoForn.getForn_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return cotacaoForn;
        } else {
            System.out.println("Erro ao inserir CotacaoForn: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public CotacaoForn alterar(CotacaoForn cotacaoForn) {
        // Para tabelas de associação pura como cot_forn, alterar geralmente não faz sentido
        // a não ser que haja outros atributos além das chaves compostas.
        // Se não houver, este método fará nada ou pode ser removido/adaptado para outros campos.
        // Replicando o padrão de alteração, mas observe a utilidade.
        // Se houver algum campo a ser alterado no futuro:
        /*
        String sql = """
            UPDATE cot_forn
            SET algum_campo = #3
            WHERE cot_cod = #1 AND forn_cod = #2;
        """;
        sql = sql.replace("#1", String.valueOf(cotacaoForn.getCot_cod()));
        sql = sql.replace("#2", String.valueOf(cotacaoForn.getForn_cod()));
        // sql = sql.replace("#3", String.valueOf(cotacaoForn.getAlgumCampo())); // Exemplo

        if (SingletonDB.getConexao().manipular(sql)) {
            return cotacaoForn;
        } else {
            System.out.println("Erro ao alterar CotacaoForn: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
        */
        System.out.println("Alteração de CotacaoForn chamada, mas não há campos para alterar além das chaves.");
        return null; // Retorna null pois não há alteração de fato para uma tabela de associação pura.
    }


    public boolean apagar(CotacaoForn cotacaoForn) {
        if (cotacaoForn == null) return false;
        String sql = String.format(
                "DELETE FROM cot_forn WHERE cot_cod = %d AND forn_cod = %d",
                cotacaoForn.getCot_cod(), cotacaoForn.getForn_cod()
        );

        return SingletonDB.getConexao().manipular(sql);
    }

    // Método 'get' para chave composta (cot_cod e forn_cod)
    public CotacaoForn get(int cot_cod, int forn_cod) {
        String sql = String.format(
                "SELECT * FROM cot_forn WHERE cot_cod = %d AND forn_cod = %d",
                cot_cod, forn_cod
        );
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                return new CotacaoForn(
                        rs.getInt("cot_cod"),
                        rs.getInt("forn_cod")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar CotacaoForn: " + e.getMessage());
        }
        return null;
    }

    // Método 'get' com filtro para listar associações (ex: todas as associações de uma cotação)
    public List<CotacaoForn> get(String filtro) {
        List<CotacaoForn> lista = new ArrayList<>();
        String sql = "SELECT * FROM cot_forn";

        if (filtro != null && !filtro.isEmpty()) {
            // CUIDADO: SQL Injection aqui!
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                CotacaoForn cf = new CotacaoForn(
                        rs.getInt("cot_cod"),
                        rs.getInt("forn_cod")
                );
                lista.add(cf);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar CotacaoForn: " + e.getMessage());
        }
        return lista;
    }

    public boolean isEmpty() {
        ResultSet rs = SingletonDB.getConexao().consultar("SELECT * FROM cot_forn LIMIT 1"); // LIMIT 1 para otimizar
        try {
            return !rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar se CotacaoForn está vazio: " + e.getMessage());
            return true; // Considera vazio em caso de erro
        }
    }
}