package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.ListaCompra;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ListaCompraController {

    @Autowired
    private ListaCompra listaCompraModel;

    public List<Map<String, Object>> getListasCompra(String filtro) {
        List<ListaCompra> listas = listaCompraModel.consultar(filtro);
        if (listas != null && !listas.isEmpty()) {
            List<Map<String, Object>> listaResponse = new ArrayList<>();
            for (ListaCompra lista : listas) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", lista.getId());
                json.put("descricao", lista.getDescricao());
                json.put("dataCriacao", lista.getDataCriacao());
                json.put("funcionarioId", lista.getFuncionarioId());
                listaResponse.add(json);
            }
            return listaResponse;
        } else {
            return List.of(Map.of("erro", "Nenhuma lista encontrada"));
        }
    }

    public Map<String, Object> getListaCompra(int id) {
        ListaCompra lista = listaCompraModel.consultar(id);
        if (lista != null) {
            return Map.of(
                    "id", lista.getId(),
                    "descricao", lista.getDescricao(),
                    "dataCriacao", lista.getDataCriacao(),
                    "funcionarioId", lista.getFuncionarioId()
            );
        } else {
            return Map.of("erro", "Lista não encontrada");
        }
    }

    public Map<String, Object> addListaCompra(String descricao, int funcionarioId, String dataCriacao) {
        if (descricao == null || descricao.isBlank()) return Map.of("erro", "A descrição é obrigatória.");
        if (funcionarioId <= 0) return Map.of("erro", "O ID do funcionário é inválido.");

        if (dataCriacao == null || dataCriacao.isBlank()) {
            dataCriacao = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        }

        ListaCompra nova = new ListaCompra(descricao, dataCriacao, funcionarioId);
        ListaCompra gravada = nova.gravar();
        if (gravada != null) {
            return Map.of("sucesso", "Lista cadastrada com sucesso!");
        } else {
            return Map.of("erro", "Erro ao cadastrar a lista.");
        }
    }


    public Map<String, Object> updtListaCompra(int id, String descricao, String dataCriacao, int funcionarioId) {
        if (descricao == null || descricao.isBlank()) return Map.of("erro", "A descrição é obrigatória.");
        if (funcionarioId <= 0) return Map.of("erro", "O ID do funcionário é inválido.");
        if (dataCriacao == null || dataCriacao.isBlank()) return Map.of("erro", "A data de criação é obrigatória.");

        ListaCompra lista = new ListaCompra(id, descricao, dataCriacao, funcionarioId);
        ListaCompra atualizada = lista.alterar();
        if (atualizada != null) {
            return Map.of("sucesso", "Lista alterada com sucesso!");
        } else {
            return Map.of("erro", "Erro ao alterar a lista.");
        }
    }


    // Método para excluir uma lista de compras
    public Map<String, String> deletarListaCompra(int id) {
        ListaCompra lista = listaCompraModel.consultar(id);
        if (lista != null) {
            boolean deletado = listaCompraModel.apagar(lista);
            if (deletado) {
                return Map.of("sucesso", "Lista excluída com sucesso");
            } else {
                return Map.of("erro", "Erro ao excluir a lista");
            }
        } else {
            return Map.of("erro", "Lista não encontrada");
        }
    }
}