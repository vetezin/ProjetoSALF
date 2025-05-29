package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.*;
import projetoSalf.mvc.util.Conexao; // Mantenha a importação de Conexao para replicar o padrão

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ListaCompraController {

    @Autowired
    private ListaCompra listaCompraModel; // Esta é a sua instância do DAO (ou Service que usa o DAO)

    @Autowired
    private Produto produtoModel;

    @Autowired
    private Estoque estoqueModel;

    @Autowired
    private Funcionario funcionarioModel;

    @Autowired
    private ListaCompraProduto listaCompraProdutoModel;

    // Métodos de consulta de lista de compras (getListasCompra e getListaCompra)
    public List<Map<String, Object>> getListasCompra(String filtro) {
        Conexao conexao = new Conexao(); // Replicando o padrão de instanciar Conexao

        List<ListaCompra> listas = listaCompraModel.consultar(filtro);
        if (listas == null || listas.isEmpty()) {
            return null; // Retorna null para o caso de "Nenhuma lista encontrada." na View
        }

        List<Map<String, Object>> listaResponse = new ArrayList<>();
        for (ListaCompra lista : listas) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", lista.getId());
            json.put("descricao", lista.getDescricao());
            json.put("dataCriacao", lista.getDataCriacao());

            Funcionario funcionario = funcionarioModel.consultar(lista.getFuncionarioId());
            Map<String, Object> funcionarioJson = new HashMap<>();
            if (funcionario != null) {
                funcionarioJson.put("id", funcionario.getId());
                funcionarioJson.put("nome", funcionario.getNome());
            } else {
                funcionarioJson.put("id", lista.getFuncionarioId());
                funcionarioJson.put("nome", "Funcionário Desconhecido");
            }
            json.put("funcionario", funcionarioJson);

            listaResponse.add(json);
        }
        return listaResponse;
    }

    public Map<String, Object> getListaCompra(int id) {
        Conexao conexao = new Conexao(); // Replicando o padrão de instanciar Conexao

        ListaCompra lista = listaCompraModel.consultar(id); // Usa o DAO para consultar
        if (lista == null) {
            return Map.of("erro", "Lista não encontrada");
        }

        return Map.of(
                "id", lista.getId(),
                "descricao", lista.getDescricao(),
                "dataCriacao", lista.getDataCriacao(),
                "funcionarioId", lista.getFuncionarioId()
        );
    }

    // Métodos de registro e atualização
    public Map<String, Object> registrarListaCompra(
            List<Map<String, Integer>> produtos,
            int codFuncionario,
            String dataCriacao,
            String descricao
    ) {
        Conexao conexao = new Conexao(); // Replicando o padrão de instanciar Conexao

        if (produtos == null || produtos.isEmpty() || codFuncionario <= 0 || dataCriacao == null || dataCriacao.isBlank() || descricao == null || descricao.isBlank()) {
            return Map.of("erro", "Dados inválidos para registrar a Lista de Compra");
        }

        // 1. Cria a lista de compras principal
        ListaCompra novaLista = new ListaCompra(descricao, dataCriacao, codFuncionario);
        ListaCompra listaGravada = listaCompraModel.gravar(novaLista); // Usa o DAO para gravar

        if (listaGravada == null || listaGravada.getId() == 0) {
            return Map.of("erro", "Erro ao registrar a Lista de Compras");
        }

        // 2. Processa todos os produtos da lista
        List<Map<String, Object>> produtosProcessados = new ArrayList<>();

        for (Map<String, Integer> produtoInfo : produtos) {
            int codProduto = produtoInfo.get("codProduto");
            int quantidade = produtoInfo.get("quantidade");

            if (quantidade <= 0) {
                // Se a quantidade for inválida, desfaz a criação da lista
                listaCompraModel.apagar(listaGravada);
                // NOTA: Você precisaria de um método para apagar itens da lc_prod associados aqui se já houver algum gravado
                // listaCompraProdutoModel.apagarPorListaCompra(listaGravada.getId()); // Se implementado
                return Map.of("erro", "Quantidade inválida para o produto ID " + codProduto);
            }

            Estoque estoqueExistente = estoqueModel.consultar("").stream()
                    .filter(e -> e.getProduto_prod_cod() == codProduto)
                    .findFirst().orElse(null);

            if (estoqueExistente == null) {
                // Desfaz a criação da lista se um produto não for encontrado no estoque
                listaCompraModel.apagar(listaGravada);
                // listaCompraProdutoModel.apagarPorListaCompra(listaGravada.getId()); // Se implementado
                return Map.of("erro", "Produto com ID " + codProduto + " não encontrado no estoque. Não é possível adicionar à lista de compra.");
            }

            // 3. Grava o produto na tabela de ligação (lc_prod)
            ListaCompraProduto novaListaCompraProd = new ListaCompraProduto(codProduto, listaGravada.getId(), quantidade);
            ListaCompraProduto listaCompraProdGravada = listaCompraProdutoModel.gravar(novaListaCompraProd); // Usa o DAO para gravar
            if (listaCompraProdGravada == null) {
                listaCompraModel.apagar(listaGravada);
                // listaCompraProdutoModel.apagarPorListaCompra(listaGravada.getId()); // Se implementado
                return Map.of("erro", "Erro ao registrar produto com ID " + codProduto + " na lista de compras");
            }

            // 4. Adiciona ao resultado final
            produtosProcessados.add(Map.of(
                    "produto_id", codProduto,
                    "quantidade_solicitada", quantidade
            ));
        }

        // 5. Retorna resposta final
        return Map.of(
                "mensagem", "Lista de Compras registrada com sucesso! O estoque será atualizado por outra função.",
                "lista_compra_id", listaGravada.getId(),
                "data_criacao", listaGravada.getDataCriacao(),
                "descricao", listaGravada.getDescricao(),
                "funcionario_id", listaGravada.getFuncionarioId(),
                "produtos_registrados", produtosProcessados
        );
    }

    public Map<String, Object> updtListaCompra(int id, String descricao, String dataCriacao, int funcionarioId) {
        Conexao conexao = new Conexao(); // Pode ser removido se não for usado explicitamente

        if (id <= 0 || descricao == null || descricao.isBlank() || dataCriacao == null || dataCriacao.isBlank() || funcionarioId <= 0) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        ListaCompra listaParaAtualizar = new ListaCompra(id, descricao, dataCriacao, funcionarioId);
        // CORREÇÃO AQUI: Chame o método alterar do seu DAO/Model injetado
        ListaCompra atualizada = listaCompraModel.alterar(listaParaAtualizar);

        if (atualizada != null) {
            return Map.of(
                    "mensagem", "Lista alterada com sucesso!", // Chave "mensagem" para sucesso
                    "id", atualizada.getId(),
                    "descricao", atualizada.getDescricao(),
                    "dataCriacao", atualizada.getDataCriacao(),
                    "funcionarioId", atualizada.getFuncionarioId()
            );
        } else {
            return Map.of("erro", "Erro ao alterar a lista.");
        }
    }

    // Método para excluir uma lista de compras
    public Map<String, Object> deletarListaCompra(int id) {
        Conexao conexao = new Conexao(); // Pode ser removido se não for usado explicitamente

        ListaCompra lista = listaCompraModel.consultar(id); // Usa o DAO para consultar
        if (lista == null) {
            return Map.of("erro", "Lista não encontrada");
        }

        boolean deletado = listaCompraModel.apagar(lista); // Usa o DAO para apagar
        if (deletado) {
            return Map.of("mensagem", "Lista excluída com sucesso!"); // Chave "mensagem" para sucesso
        } else {
            return Map.of("erro", "Erro ao excluir a lista");
        }
    }
}