package projetoSalf.mvc.dao;

import projetoSalf.mvc.dao.IDAO;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.FormatUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO implements IDAO<Fornecedor> {

    public Fornecedor gravar(Fornecedor fornecedor, Conexao conexao) {
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo);

        String filtro = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "'";
        List<Fornecedor> jaExiste = get(filtro, conexao);

        if (!jaExiste.isEmpty()) {
            return null;
        }

        String sql = """
            INSERT INTO fornecedor(
                forn_nome, forn_logradouro, forn_numero, forn_cep, forn_cidade, forn_complemento,
                forn_cnpj, forn_telefone, forn_contato, forn_email)
            VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8', '#9', '#10');
        """;

        sql = sql.replace("#10", fornecedor.getForn_email().replace("'", "''"));
        sql = sql.replace("#9", fornecedor.getForn_contato().replace("'", "''"));
        sql = sql.replace("#8", fornecedor.getForn_telefone().replace("'", "''"));
        sql = sql.replace("#7", fornecedor.getForn_cnpj().replace("'", "''"));
        sql = sql.replace("#6", fornecedor.getForn_complemento().replace("'", "''"));
        sql = sql.replace("#5", fornecedor.getForn_cidade().replace("'", "''"));
        sql = sql.replace("#4", fornecedor.getForn_cep().replace("'", "''"));
        sql = sql.replace("#3", fornecedor.getForn_numero().replace("'", "''"));
        sql = sql.replace("#2", fornecedor.getForn_logradouro().replace("'", "''"));
        sql = sql.replace("#1", fornecedor.getForn_nome().replace("'", "''"));

        if (conexao.manipular(sql)) {
            return fornecedor;
        } else {
            System.out.println("Erro: " + conexao.getMensagemErro());
            return null;
        }
    }

    @Override
    public Fornecedor alterar(Fornecedor fornecedor, Conexao conexao) {
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo);

        String filtro = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "' AND forn_cod <> " + fornecedor.getForn_cod();
        List<Fornecedor> jaExiste = get(filtro, conexao);

        if (!jaExiste.isEmpty()) {
            return null;
        }

        String sql = """
            UPDATE fornecedor SET
            forn_nome = '#1',
            forn_logradouro = '#2',
            forn_numero = '#3',
            forn_cep = '#4',
            forn_cidade = '#5',
            forn_complemento = '#6',
            forn_cnpj = '#7',
            forn_telefone = '#8',
            forn_contato = '#9',
            forn_email = '#10'
            WHERE forn_cod = #11
        """;
        sql = sql.replace("#11", String.valueOf(fornecedor.getForn_cod()));
        sql = sql.replace("#10", fornecedor.getForn_email().replace("'", "''"));
        sql = sql.replace("#9", fornecedor.getForn_contato().replace("'", "''"));
        sql = sql.replace("#8", fornecedor.getForn_telefone().replace("'", "''"));
        sql = sql.replace("#7", fornecedor.getForn_cnpj().replace("'", "''"));
        sql = sql.replace("#6", fornecedor.getForn_complemento().replace("'", "''"));
        sql = sql.replace("#5", fornecedor.getForn_cidade().replace("'", "''"));
        sql = sql.replace("#4", fornecedor.getForn_cep().replace("'", "''"));
        sql = sql.replace("#3", fornecedor.getForn_numero().replace("'", "''"));
        sql = sql.replace("#2", fornecedor.getForn_logradouro().replace("'", "''"));
        sql = sql.replace("#1", fornecedor.getForn_nome().replace("'", "''"));
        System.out.println("COD FORNECEDOR: " + fornecedor.getForn_cod());
        System.out.println("SQL ATUALIZAÇÃO:");
        System.out.println(sql);
        if (conexao.manipular(sql)) {
            System.out.println("ERRO SQL:");
            System.out.println(conexao.getMensagemErro());
            return fornecedor;
        } else {
            System.out.println("Erro no UPDATE: " + conexao.getMensagemErro());
            return null;
        }
    }

    @Override
    public Fornecedor get(int id, Conexao conexao) {
        String sql = "SELECT * FROM fornecedor WHERE forn_cod = " + id;
        Fornecedor fornecedor = null;
        try {
            ResultSet resultSet = conexao.consultar(sql);
            if (resultSet.next()) {
                fornecedor = new Fornecedor(
                        resultSet.getInt("forn_cod"),
                        resultSet.getString("forn_nome"),
                        resultSet.getString("forn_logradouro"),
                        resultSet.getString("forn_numero"),
                        resultSet.getString("forn_cep"),
                        resultSet.getString("forn_cidade"),
                        resultSet.getString("forn_complemento"),
                        resultSet.getString("forn_cnpj"),
                        resultSet.getString("forn_telefone"),
                        resultSet.getString("forn_contato"),
                        resultSet.getString("forn_email")
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar fornecedor: " + e.getMessage());
        }
        return fornecedor;
    }

    @Override
    public List<Fornecedor> get(String filtro, Conexao conexao) {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet resultSet = conexao.consultar(sql);
            while (resultSet.next()) {
                Fornecedor fornecedor = new Fornecedor(
                        resultSet.getInt("forn_cod"),
                        resultSet.getString("forn_nome"),
                        resultSet.getString("forn_logradouro"),
                        resultSet.getString("forn_numero"),
                        resultSet.getString("forn_cep"),
                        resultSet.getString("forn_cidade"),
                        resultSet.getString("forn_complemento"),
                        resultSet.getString("forn_cnpj"),
                        resultSet.getString("forn_telefone"),
                        resultSet.getString("forn_contato"),
                        resultSet.getString("forn_email")
                );
                fornecedores.add(fornecedor);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return fornecedores;
    }

    public boolean apagar(Fornecedor fornecedor, Conexao conexao) {
        if (fornecedor == null) return false;
        String sql = "DELETE FROM fornecedor WHERE forn_cod = " + fornecedor.getForn_cod();
        return conexao.manipular(sql);
    }
}
