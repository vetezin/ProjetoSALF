package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CestaBasicaDAO {

    public boolean inserir(CestaBasica cb, Conexao conexao) {
        String sql = "INSERT INTO cesta_basica (cb_cod, cb_motivo, cb_dtcriacao, pessoa_carente_pc_cod, funcionario_func_cod, cb_dtdoacao) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, cb.getCb_cod());
            stmt.setString(2, cb.getCb_motivo());
            stmt.setDate(3, cb.getCb_dtcriacao());
            stmt.setInt(4, cb.getPessoa_carente_pc_cod());
            stmt.setInt(5, cb.getCb_codfunc());
            stmt.setDate(6, cb.getCb_dtdoacao());

            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir cesta básica: " + e.getMessage());
            return false;
        }
    }

    public List<CestaBasica> listarCestasFiltrado(Conexao conexao, String cpf, String nome) {
        List<CestaBasica> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cb.cb_cod, cb.cb_motivo, cb.cb_dtcriacao, cb.cb_dtdoacao, ");
        sql.append("pc.pc_nome, pc.pc_cpf, f.func_nome ");
        sql.append("FROM cesta_basica cb ");
        sql.append("JOIN pessoa_carente pc ON cb.pessoa_carente_pc_cod = pc.pc_cod ");
        sql.append("JOIN funcionario f ON cb.funcionario_func_cod = f.func_cod ");
        sql.append("WHERE 1=1 ");

        if (nome != null && !nome.trim().isEmpty()) {
            sql.append("AND LOWER(pc.pc_nome) LIKE LOWER('%").append(nome).append("%') ");
        }

        if (cpf != null && !cpf.trim().isEmpty()) {
            sql.append("AND pc.pc_cpf LIKE '%").append(cpf).append("%' ");
        }

        try (ResultSet rs = conexao.getConnect().createStatement().executeQuery(sql.toString())) {
            while (rs.next()) {
                CestaBasica cb = new CestaBasica();
                cb.setCb_cod(rs.getInt("cb_cod"));
                cb.setCb_motivo(rs.getString("cb_motivo"));
                cb.setCb_dtcriacao(rs.getDate("cb_dtcriacao"));
                cb.setCb_dtdoacao(rs.getDate("cb_dtdoacao"));
                cb.setNomePessoa(rs.getString("pc_nome"));
                cb.setCpfPessoa(rs.getString("pc_cpf"));
                cb.setNomeFuncionario(rs.getString("func_nome"));
                lista.add(cb);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar cestas filtradas: " + e.getMessage());
        }

        return lista;
    }
}
