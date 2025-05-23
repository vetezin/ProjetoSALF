    package projetoSalf.mvc.dao;

    import projetoSalf.mvc.model.Saida;
    import projetoSalf.mvc.util.SingletonDB;
    import org.springframework.stereotype.Repository;

    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;

    @Repository
    public class SaidaDAO implements IDAO<Saida> {

        @Override
        public Saida gravar(Saida saida) {
            String sql = """
        INSERT INTO saida(s_dtsaida, s_motivo, func_cod)
        VALUES ('#1', '#2', '#3') RETURNING s_cod;
    """;
            sql = sql.replace("#1", saida.getDataSaida());
            sql = sql.replace("#2", saida.getMotivo().replace("'", "''"));
            sql = sql.replace("#3", String.valueOf(saida.getCodFuncionario()));

            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql);
                if (rs != null && rs.next()) {
                    saida.setCod(rs.getInt("s_cod")); // Preenche o ID
                    return saida;
                } else {
                    System.out.println("Erro ao recuperar ID da saída.");
                    return null;
                }
            } catch (SQLException e) {
                System.out.println("Erro ao gravar saída: " + e.getMessage());
                return null;
            }
        }

        @Override
        public Saida alterar(Saida saida) {
            String sql = """
                UPDATE saida SET
                    s_dtsaida = '#1',
                    s_motivo = '#2',
                    func_cod = #3
                WHERE s_cod = #4;
            """;
            sql = sql.replace("#1", saida.getDataSaida());
            sql = sql.replace("#2", saida.getMotivo().replace("'", "''"));
            sql = sql.replace("#3", String.valueOf(saida.getCodFuncionario()));
            sql = sql.replace("#4", String.valueOf(saida.getCod()));

            if (SingletonDB.getConexao().manipular(sql)) {
                return saida;
            } else {
                System.out.println("Erro ao alterar saída: " + SingletonDB.getConexao().getMensagemErro());
                return null;
            }
        }

        @Override
        public Saida get(int id) {
            String sql = "SELECT * FROM saida WHERE s_cod = " + id;
            Saida s = null;
            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql);
                if (rs.next()) {
                    s= new Saida(
                            rs.getInt("s_cod"),
                            rs.getString("s_dtsaida"),
                            rs.getString("s_motivo"),
                            rs.getInt("func_cod")
                    );
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar saída: " + e.getMessage());
            }
            return s;
        }

        @Override
        public List<Saida> get(String filtro) {
            List<Saida> lista = new ArrayList<>();
            String sql = "SELECT * FROM saida";
            if (!filtro.isEmpty()) {
                sql += " WHERE " + filtro;
            }
            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql);
                while (rs.next()) {
                    lista.add(new Saida(
                            rs.getInt("s_cod"),
                            rs.getString("s_dtsaida"),
                            rs.getString("s_motivo"),
                            rs.getInt("func_cod")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao listar saídas: " + e.getMessage());
            }
            return lista;
        }

        @Override
        public boolean apagar(Saida saida) {
            if (saida == null) return false;
            String sql = "DELETE FROM saida WHERE s_cod = " + saida.getCod();
            return SingletonDB.getConexao().manipular(sql);
        }
    }
