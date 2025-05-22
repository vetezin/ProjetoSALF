package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.ListaCompra;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@Service
public class ListaCompraController {

    @Autowired
    private ListaCompra listaCompraModel;

    public List<Map<String, Object>> getListaCompras() {
        Conexao conexao = new Conexao();
        List<ListaCompra> lista = listaCompraModel.consultar("", conexao);
        if (!lista.isEmpty()) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ListaCompra lc : lista) {
                Map<String, Object> json = new HashMap<>();
                json.put("lc_cod", lc.getLc_cod());
                json.put("lc_desc", lc.getLc_desc());
                json.put("lc_dtlista", lc.getLc_dtlista());
                json.put("func_cod", lc.getFunc_cod());
                result.add(json);
            }
            return result;
        }
        return null;
    }

    public Map<String, Object> getListaCompra(int id) {
        Conexao conexao = new Conexao();
        ListaCompra lc = listaCompraModel.consultar(id);
        if (lc != null) {
            return Map.of(
                    "lc_cod", lc.getLc_cod(),
                    "lc_desc", lc.getLc_desc(),
                    "lc_dtlista", lc.getLc_dtlista(),
                    "func_cod", lc.getFunc_cod()
            );
        }
        return null;
    }

    public Map<String, Object> addListaCompra(String desc, String dtlista, int func_cod) {
        Conexao conexao = new Conexao();
        ListaCompra nova = new ListaCompra(desc, dtlista, func_cod);
        ListaCompra gravada = listaCompraModel.gravar(nova);

        if (gravada != null) {
            return Map.of(
                    "lc_cod", gravada.getLc_cod(),
                    "lc_desc", gravada.getLc_desc(),
                    "lc_dtlista", gravada.getLc_dtlista(),
                    "func_cod", gravada.getFunc_cod()
            );
        }
        return Map.of("erro", "Erro ao cadastrar a lista de compra");
    }

    public Map<String, Object> updtListaCompra(int cod, String desc, Date dtlista, int func_cod) {
        ListaCompra existente = listaCompraModel.consultar(cod);
        if (existente == null) {
            return Map.of("erro", "Lista de compra não encontrada");
        }

        existente.setLc_cod(cod);
        existente.setLc_desc(desc);
        existente.setLc_dtlista(dtlista);
        existente.setFunc_cod(func_cod);

        ListaCompra atualizada = listaCompraModel.update(existente);
        if (atualizada != null) {
            return Map.of(
                    "lc_cod", atualizada.getLc_cod(),
                    "lc_desc", atualizada.getLc_desc(),
                    "lc_dtlista", atualizada.getLc_dtlista(),
                    "func_cod", atualizada.getFunc_cod()
            );
        }
        return Map.of("erro", "Erro ao atualizar a lista de compra");
    }

    public Map<String, Object> deletarListaCompra(int id) {
        ListaCompra lc = listaCompraModel.consultar(id);
        if (lc == null) {
            return Map.of("erro", "Lista de compra não encontrada");
        }

        boolean deletado = listaCompraModel.deletar(lc);
        if (deletado) {
            return Map.of("mensagem", "Lista de compra removida com sucesso!");
        }
        return Map.of("erro", "Erro ao remover a lista de compra");
    }

    public List<Map<String, Object>> getListasCompra() {

    }
}
