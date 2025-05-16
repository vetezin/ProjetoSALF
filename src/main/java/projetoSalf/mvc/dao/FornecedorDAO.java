package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Fornecedor;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.util.FormatUtils;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FornecedorDAO implements IDAO<Fornecedor>
{
    public Fornecedor gravar(Fornecedor fornecedor) {

        // Limpar e atualizar o valor do objeto
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo); // atualiza no objeto para gravar certo

        System.out.println("CNPJ recebido do objeto: [" + cnpjLimpo + "]");

        String filtro = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "'";
        System.out.println("Filtro de verificação: " + filtro);

        List<Fornecedor> jaExiste = get(filtro);
        System.out.println("Resultado do get(filtro): tamanho = " + jaExiste.size());

        if (!jaExiste.isEmpty()) {
            System.out.println("Fornecedor já cadastrado com esse CNPJ.");
            return null;
        }

        // Prossegue com INSERT já usando o CNPJ limpo
        String sql = """
        INSERT INTO fornecedor(forn_nome, forn_end, forn_cnpj, forn_telefone)
        VALUES ('#1', '#2', '#3', '#4');
    """;

        sql = sql.replace("#1", fornecedor.getForn_nome().replace("'", "''"));
        sql = sql.replace("#2", fornecedor.getForn_end().replace("'", "''"));
        sql = sql.replace("#3", fornecedor.getForn_cnpj().replace("'", "''"));
        sql = sql.replace("#4", fornecedor.getForn_telefone().replace("'", "''"));

        System.out.println("SQL gerado: " + sql);

        if (SingletonDB.getConexao().manipular(sql)) {
            return fornecedor;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Fornecedor alterar(Fornecedor fornecedor) {
        // Limpa o CNPJ e atualiza no objeto
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo);

        // Verifica se existe outro fornecedor com o mesmo CNPJ
        String filtro = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "' AND forn_cod <> " + fornecedor.getForn_cod();
        List<Fornecedor> jaExiste = get(filtro);

        if (!jaExiste.isEmpty()) {
            System.out.println("Já existe outro fornecedor com esse CNPJ.");
            return null;
        }

        // Gera e executa o SQL
        String sql = """
            UPDATE fornecedor SET
            forn_nome = '#1',
            forn_end = '#2',
            forn_cnpj = '#3',
            forn_telefone = '#4'
            WHERE forn_cod = #5
    """;

        sql = sql.replace("#1", fornecedor.getForn_nome().replace("'", "''"));
        sql = sql.replace("#2", fornecedor.getForn_end().replace("'", "''"));
        sql = sql.replace("#3", fornecedor.getForn_cnpj().replace("'", "''"));
        sql = sql.replace("#4", fornecedor.getForn_telefone().replace("'", "''"));
        sql = sql.replace("#5", String.valueOf(fornecedor.getForn_cod()));

        System.out.println("SQL UPDATE gerado: " + sql);

        if (SingletonDB.getConexao().manipular(sql)) {
            return fornecedor;
        } else {
            System.out.println("Erro no UPDATE: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Fornecedor get(int id) {
        String sql = "SELECT * FROM fornecedor WHERE forn_cod = " + id;
        Fornecedor fornecedor = null;
        try{
            ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
            if(resultSet.next())
            {
                fornecedor = new Fornecedor(
                        resultSet.getInt("forn_cod"),
                        resultSet.getString("forn_nome"),
                        resultSet.getString("forn_end"),
                        resultSet.getString("forn_cnpj"),
                        resultSet.getString("forn_telefone")
                );
            }
        }catch (Exception e){
            System.out.println("Erro ao buscar fornecedor: " + e.getMessage());
        }
        return fornecedor;
    }

    @Override
    public List<Fornecedor> get(String filtro){
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        if(!filtro.isEmpty()){
            sql += " WHERE " + filtro;
        }
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            while (resultSet.next()){
                Fornecedor fornecedor = new Fornecedor(
                        resultSet.getInt("forn_cod"),
                        resultSet.getString("forn_nome"),
                        resultSet.getString("forn_end"),
                        resultSet.getString("forn_cnpj"),
                        resultSet.getString("forn_telefone")
                );
                fornecedores.add(fornecedor);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return fornecedores;
    }

    public boolean isEmpty(){
        String sql = "SELECT * FROM fornecedor";
        ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
        try {
            return !resultSet.next();
        }catch (Exception e){
            return true;
        }
    }

    @Override
    public boolean apagar(Fornecedor fornecedor) {
        if(fornecedor == null)
            return false;
        String sql = "DELETE FROM fornecedor WHERE forn_cod = " + fornecedor.getForn_cod();
        return SingletonDB.getConexao().manipular(sql);
    }
}
