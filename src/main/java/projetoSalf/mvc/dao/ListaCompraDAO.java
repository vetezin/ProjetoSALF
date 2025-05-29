    package projetoSalf.mvc.dao;
    
    import org.springframework.stereotype.Repository;
    import projetoSalf.mvc.model.ListaCompra;
    import projetoSalf.mvc.util.Conexao; // Mantenha esta importação
    import projetoSalf.mvc.util.SingletonDB; // Mantenha esta importação
    
    import java.sql.ResultSet; // Alterado para usar ResultSet diretamente
    import java.sql.SQLException;
    // Removidos: import java.sql.Connection; import java.sql.PreparedStatement; import java.sql.Statement;
    import java.util.ArrayList;
    import java.util.List;
    
    @Repository
    public class ListaCompraDAO implements IDAO<ListaCompra> {
    
        @Override
        public ListaCompra gravar(ListaCompra lista) {
            if (lista.getDataCriacao() == null || lista.getDataCriacao().isBlank()) {
                throw new IllegalArgumentException("Data de criação não pode estar vazia");
            }
    
            // Replicando o padrão de string.replace() e RETURNING do SaidaDAO
            String sql = """
            INSERT INTO listacompra(lc_dtlista, lc_desc, func_cod)
            VALUES ('#1', '#2', #3) RETURNING lc_cod;
            """;
            sql = sql.replace("#1", lista.getDataCriacao());
            sql = sql.replace("#2", lista.getDescricao().replace("'", "''")); // Escapar aspas simples
            sql = sql.replace("#3", String.valueOf(lista.getFuncionarioId()));
    
            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql); // Usando consultar() do SingletonDB
                if (rs != null && rs.next()) { // Verificação rs != null replicada
                    lista.setId(rs.getInt("lc_cod")); // Preenche o ID
                    return lista;
                } else {
                    System.out.println("Erro ao recuperar ID da lista de compras."); // Replicando System.out.println
                    return null;
                }
            } catch (SQLException e) {
                System.out.println("Erro ao gravar lista de compras: " + e.getMessage()); // Replicando System.out.println
                return null;
            }
        }
    
        @Override
        public List<ListaCompra> get(String filtro) {
            List<ListaCompra> listas = new ArrayList<>();
            String sql = "SELECT * FROM listacompra";
            if (filtro != null && !filtro.isBlank()) {
                // Replicando concatenação direta - CUIDADO com SQL Injection!
                sql += " WHERE " + filtro;
            }
    
            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql); // Usando consultar() do SingletonDB
                while (rs.next()) {
                    ListaCompra lista = new ListaCompra();
                    lista.setId(rs.getInt("lc_cod"));
                    lista.setDescricao(rs.getString("lc_desc"));
                    lista.setDataCriacao(rs.getString("lc_dtlista"));
                    lista.setFuncionarioId(rs.getInt("func_cod"));
                    listas.add(lista);
                }
            } catch (SQLException e) {
                System.out.println("Erro ao listar registros: " + e.getMessage()); // Replicando System.out.println
            }
            return listas;
        }

        @Override
        public ListaCompra alterar(ListaCompra lista) {
            if (lista.getDataCriacao() == null || lista.getDataCriacao().isBlank()) {
                throw new IllegalArgumentException("Data de criação não pode estar vazia.");
            }

            String sql = """
        UPDATE listacompra SET
            lc_desc = '#1',
            lc_dtlista = '#2',
            func_cod = #3
        WHERE lc_cod = #4;
        """;
            sql = sql.replace("#1", lista.getDescricao().replace("'", "''"));
            sql = sql.replace("#2", lista.getDataCriacao());
            sql = sql.replace("#3", String.valueOf(lista.getFuncionarioId()));
            sql = sql.replace("#4", String.valueOf(lista.getId()));

            if (SingletonDB.getConexao().manipular(sql)) {
                return lista;
            } else {
                System.out.println("Erro ao alterar a lista: " + SingletonDB.getConexao().getMensagemErro());
                return null;
            }
        }
    
        @Override
        public ListaCompra get(int id) {
            // Replicando o padrão de concatenação direta de ID do SaidaDAO
            String sql = "SELECT * FROM listacompra WHERE lc_cod = " + id;
            ListaCompra lista = null;
            try {
                ResultSet rs = SingletonDB.getConexao().consultar(sql); // Usando consultar() do SingletonDB
                if (rs.next()) { // Verificação rs.next() direta, replicando o SaidaDAO
                    lista = new ListaCompra();
                    lista.setId(rs.getInt("lc_cod"));
                    lista.setDescricao(rs.getString("lc_desc"));
                    lista.setDataCriacao(rs.getString("lc_dtlista"));
                    lista.setFuncionarioId(rs.getInt("func_cod"));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao buscar o registro com ID " + id + ": " + e.getMessage()); // Replicando System.out.println
            }
            return lista;
        }

        @Override
        public boolean apagar(ListaCompra lista) {
            if (lista == null) return false;
            // Replicando o padrão de concatenação direta de ID do SaidaDAO
            String sql = "DELETE FROM listacompra WHERE lc_cod = " + lista.getId();
            if (SingletonDB.getConexao().manipular(sql)) { // Usando manipular() do SingletonDB
                return true;
            } else {
                System.out.println("Erro ao apagar lista de compras: " + SingletonDB.getConexao().getMensagemErro()); // Replicando System.out.println
                return false;
            }
        }
    }