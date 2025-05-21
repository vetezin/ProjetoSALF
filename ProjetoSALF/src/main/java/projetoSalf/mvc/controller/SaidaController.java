package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Saida;

import java.util.*;

@Service
public class SaidaController {

    @Autowired
    private Saida saidaModel;

    public List<Map<String, Object>> getSaidas() {
        List<Saida> lista = saidaModel.consultar("");
        if (lista.isEmpty())
            return null;

        List<Map<String, Object>> saidaList = new ArrayList<>();
        for (Saida s : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", s.getCod());
            json.put("data", s.getDataSaida());
            json.put("motivo", s.getMotivo());
            json.put("funcionarioId", s.getCodFuncionario());
            saidaList.add(json);
        }
        return saidaList;
    }

    public Map<String, Object> getSaida(int id) {
        Saida s = saidaModel.consultar(id);
        if (s == null)
            return Map.of("erro", "Saída não encontrada");

        return Map.of(
                "id", s.getCod(),
                "data", s.getDataSaida(),
                "motivo", s.getMotivo(),
                "funcionarioId", s.getCodFuncionario()
        );
    }

    public Map<String, Object> addSaida(String dataSaidaStr, String motivo, int codFuncionario) {
        if (dataSaidaStr == null || motivo == null || motivo.isBlank() || codFuncionario <= 0)
            return Map.of("erro", "Dados inválidos para cadastro");

        Saida nova = new Saida(dataSaidaStr, motivo, codFuncionario);
        Saida gravada = saidaModel.gravar(nova);

        if (gravada != null) {
            return Map.of(
                    "id", gravada.getCod(),
                    "data", gravada.getDataSaida(),
                    "motivo", gravada.getMotivo(),
                    "funcionarioId", gravada.getCodFuncionario()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar saída");
        }
    }

    public Map<String, Object> updtSaida(int cod, String dataSaidaStr, String motivo, int codFuncionario) {
        if (cod <= 0 || dataSaidaStr == null || motivo == null || motivo.isBlank() || codFuncionario <= 0)
            return Map.of("erro", "Dados inválidos para atualização");

        Saida existente = saidaModel.consultar(cod);
        if (existente == null)
            return Map.of("erro", "Saída não encontrada");

        existente.setDataSaida(dataSaidaStr);
        existente.setMotivo(motivo);
        existente.setCodFuncionario(codFuncionario);

        Saida atualizada = saidaModel.alterar(existente);
        if (atualizada != null) {
            return Map.of(
                    "id", atualizada.getCod(),
                    "data", atualizada.getDataSaida(),
                    "motivo", atualizada.getMotivo(),
                    "funcionarioId", atualizada.getCodFuncionario()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar saída");
        }
    }

    public Map<String, Object> deletarSaida(int id) {
        Saida existente = saidaModel.consultar(id);
        if (existente == null)
            return Map.of("erro", "Saída não encontrada");

        boolean deletado = saidaModel.deletar(existente);
        if (deletado) {
            return Map.of("mensagem", "Saída removida com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover saída");
        }
    }
}
