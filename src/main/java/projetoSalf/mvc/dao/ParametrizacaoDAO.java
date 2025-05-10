package projetoSalf.mvc.dao;

import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.model.Parametrizacao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ParametrizacaoDAO implements IDAO<Parametrizacao> {
    @Override
    public Parametrizacao gravar(Parametrizacao entidade) {
        String sql = """
            INSERT INTO empresa(nome_empresa, cnpj, endereco, telefone) 
            VALUES ('#1', '#2', '#3', '#4');
            """;
        sql = sql.replace("#1", entidade.getNomeEmpresa());
        sql = sql.replace("#2", entidade.getCnpj());
        sql = sql.replace("#3", entidade.getEndereco());
        sql = sql.replace("#4", entidade.getTelefone());
        if (SingletonDB.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }
    @Override
    public Parametrizacao alterar(Parametrizacao entidade) {
        String sql = """
        UPDATE empresa SET 
            nome_empresa = '#1',
            cnpj = '#2',
            endereco = '#3',
            telefone = '#4',
        WHERE id = #6;
        """;
        sql = sql.replace("#1", entidade.getNomeEmpresa())
                .replace("#2", entidade.getCnpj())
                .replace("#3", entidade.getEndereco())
                .replace("#4", entidade.getTelefone())
                .replace("#6", String.valueOf(entidade.getId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Parametrizacao entidade) {
        return false;
    }

    @Override
    public Parametrizacao get(int id) {
        String sql = "SELECT * FROM empresa WHERE id_empresa = " + id;
        var rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Parametrizacao p = new Parametrizacao();
                p.setId(rs.getInt("id_empresa"));
                p.setNomeEmpresa(rs.getString("nome_empresa"));
                p.setCnpj(rs.getString("cnpj"));
                p.setEndereco(rs.getString("endereco"));
                p.setTelefone(rs.getString("telefone"));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar empresa: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Parametrizacao> get(String filtro) {
        List<Parametrizacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        var rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Parametrizacao p = new Parametrizacao();
                p.setId(rs.getInt("id"));
                p.setNomeEmpresa(rs.getString("nome_empresa"));
                p.setCnpj(rs.getString("cnpj"));
                p.setEndereco(rs.getString("endereco"));
                p.setTelefone(rs.getString("telefone"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar empresas: " + e.getMessage());
        }
        return lista;
    }

    public boolean isEmpty() {
        String sql = "SELECT * FROM empresa";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }
    public boolean deletarEmpresa() {
        String sql = "DELETE FROM empresa";
        return SingletonDB.getConexao().manipular(sql);
    }
}
