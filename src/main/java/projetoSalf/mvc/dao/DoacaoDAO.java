package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Doacao;
import projetoSalf.mvc.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoDAO {
    public int gravar(Doacao doacao) {
        String sql = String.format(
                "INSERT INTO doacao(doa_dtentrada, func_cod) VALUES ('%s', %d) RETURNING doa_cod",
                doacao.getDoaDtentrada(), doacao.getFuncCod()
        );

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return rs.getInt("doa_cod");
            }
        } catch (Exception e) {
            System.out.println("Erro ao gravar doacao: " + e.getMessage());
        }
        return -1;
    }

    public List<Doacao> listar(Integer funcCod, String data) {
        String sql = "SELECT * FROM doacao WHERE 1=1";
        if (funcCod != null) sql += " AND func_cod = " + funcCod;
        if (data != null && !data.isEmpty()) sql += " AND doa_dtentrada = '" + data + "'";

        List<Doacao> lista = new ArrayList<>();
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Doacao d = new Doacao();
                d.setDoaCod(rs.getInt("doa_cod"));
                d.setDoaDtentrada(rs.getString("doa_dtentrada"));
                d.setFuncCod(rs.getInt("func_cod"));
                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar doações: " + e.getMessage());
        }
        return lista;
    }

    public List<Doacao> get(String filtro) {
        String sql = "SELECT * FROM doacao";
        if (filtro != null && !filtro.trim().isEmpty()) {
            sql += " WHERE " + filtro;
        }

        List<Doacao> lista = new ArrayList<>();
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Doacao d = new Doacao();
                d.setDoaCod(rs.getInt("doa_cod"));
                d.setDoaDtentrada(rs.getString("doa_dtentrada"));
                d.setFuncCod(rs.getInt("func_cod"));
                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro ao executar get em DoacaoDAO: " + e.getMessage());
        }
        return lista;
    }

    public boolean deletar(int doaCod) {
        if(doaCod == 0) return false;
        else {
            String sqlDoaProd = "DELETE FROM doa_prod WHERE doa_cod = " + doaCod;
            SingletonDB.getConexao().manipular(sqlDoaProd);
            String sql = "DELETE FROM doacao WHERE doa_cod = " + doaCod;
            return SingletonDB.getConexao().manipular(sql);
        }
    }
}