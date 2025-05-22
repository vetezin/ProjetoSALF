package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Estoque;
import projetoSalf.mvc.model.Produto;
import projetoSalf.mvc.model.Saida;
import projetoSalf.mvc.model.SaidaProd;

import java.util.*;

@Service
public class SaidaController {

    @Autowired
    private Saida saidaModel;

    @Autowired
    private Produto produtoModel;

    @Autowired
    private Estoque estoqueModel;

    @Autowired
    private SaidaProd saidaProdModel;

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




    public Map<String, Object> registrarSaidaComProduto(
            int codProduto,
            int quantidadeSaida,
            int codFuncionario,
            String dataSaida,
            String motivo
    ) {
        // buscar todos os estoques
        List<Estoque> estoques = estoqueModel.consultar("");

        // procurar o estoque do produto informado
        Estoque estoqueEncontrado = null;
        for (Estoque est : estoques) {
            if (est.getProduto_prod_cod() == codProduto) {
                estoqueEncontrado = est;
                break;
            }
        }

        if (estoqueEncontrado == null) {
            return Map.of("erro", "Produto não encontrado no estoque");
        }

        int qtdAtualEstoque = estoqueEncontrado.getEs_qtdprod();

        // verificar se há quantidade suficiente
        if (quantidadeSaida <= 0 || quantidadeSaida > qtdAtualEstoque) {
            return Map.of("erro", "Quantidade inválida ou insuficiente em estoque");
        }

        // registrar a saída na tabela SAIDA
        Saida novaSaida = new Saida(dataSaida, motivo, codFuncionario);
        Saida saidaGravada = saidaModel.gravar(novaSaida);
        if (saidaGravada == null || saidaGravada.getCod() == 0) {
            return Map.of("erro", "Erro ao registrar a saída");
        }

        // registrar na tabela SAIDA_PROD
        SaidaProd novaSaidaProd = new SaidaProd(codProduto, saidaGravada.getCod(), quantidadeSaida);
        SaidaProd saidaProdGravada = novaSaidaProd.gravar();
        if (saidaProdGravada == null) {
            return Map.of("erro", "Erro ao registrar a saída do produto");
        }

        // atualizar o estoque
        int novaQtd = qtdAtualEstoque - quantidadeSaida;
        estoqueEncontrado.setEs_qtdprod(novaQtd);
        estoqueModel.alterar(estoqueEncontrado);

        // se acabou o estoque, remover o produto
        if (novaQtd == 0) {
            Produto produto = produtoModel.consultar(codProduto);
            if (produto != null) {
                produtoModel.deletarProduto(produto); // remove da tabela produto
            }
        }

        return Map.of(
                "mensagem", "Saída registrada com sucesso",
                "saida_id", saidaGravada.getCod(),
                "produto_id", codProduto,
                "quantidade_saida", quantidadeSaida,
                "estoque_restante", novaQtd
        );
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
