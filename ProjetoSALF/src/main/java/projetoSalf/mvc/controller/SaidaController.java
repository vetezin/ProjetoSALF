package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.*;
import projetoSalf.mvc.util.Conexao;

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

    @Autowired
    private Funcionario funcionarioModel;

    public List<Map<String, Object>> getSaidas() {
        Conexao conexao = new Conexao();
        List<Saida> lista = saidaModel.consultar("");
        if (lista.isEmpty())
            return null;

        List<Map<String, Object>> saidaList = new ArrayList<>();
        for (Saida s : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", s.getCod());
            json.put("data", s.getDataSaida());
            json.put("motivo", s.getMotivo());

            // criando objeto de funcionário
            Funcionario funcionario = funcionarioModel.consultar(s.getCodFuncionario());
            Map<String, Object> funcionarioJson = new HashMap<>();
            funcionarioJson.put("id", funcionario.getId());
            funcionarioJson.put("nome", funcionario.getNome());

            json.put("funcionario", funcionarioJson);

            saidaList.add(json);
        }
        return saidaList;
    }

    public Map<String, Object> getSaida(int id) {
        Conexao conexao = new Conexao();
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
        Conexao conexao = new Conexao();
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

    public Map<String, Object> registrarSaidaComProdutos(
            List<Map<String, Integer>> produtos, // lista de {codProduto, quantidadeSaida}
            int codFuncionario,
            String dataSaida,
            String motivo
    ) {
        if (produtos == null || produtos.isEmpty() || codFuncionario <= 0 || dataSaida == null || motivo == null || motivo.isBlank()) {
            return Map.of("erro", "Dados inválidos para registrar a saída");
        }

        // 1. Cria a saída principal
        Saida novaSaida = new Saida(dataSaida, motivo, codFuncionario);
        Saida saidaGravada = saidaModel.gravar(novaSaida);

        if (saidaGravada == null || saidaGravada.getCod() == 0) {
            return Map.of("erro", "Erro ao registrar a saída");
        }

        // 2. Processa todos os produtos
        List<Map<String, Object>> produtosRegistrados = new ArrayList<>();

        for (Map<String, Integer> produtoInfo : produtos) {
            int codProduto = produtoInfo.get("codProduto");
            int quantidadeSaida = produtoInfo.get("quantidade");

            // Consulta estoque atual do produto
            Estoque estoque = estoqueModel.consultar("").stream()
                    .filter(e -> e.getProduto_prod_cod() == codProduto)
                    .findFirst().orElse(null);

            if (estoque == null || quantidadeSaida <= 0 || quantidadeSaida > estoque.getEs_qtdprod()) {
                return Map.of("erro", "Produto com ID " + codProduto + " não encontrado ou quantidade inválida");
            }

            // 3. Grava saída do produto (1 a 1 na tabela saida_prod)
            SaidaProd novaSaidaProd = new SaidaProd(codProduto, saidaGravada.getCod(), quantidadeSaida);
            SaidaProd saidaProdGravada = saidaProdModel.gravar(novaSaidaProd);
            if (saidaProdGravada == null) {
                return Map.of("erro", "Erro ao registrar saída do produto ID " + codProduto);
            }

            // 4. Atualiza estoque
            int novaQtd = estoque.getEs_qtdprod() - quantidadeSaida;
            estoque.setEs_qtdprod(novaQtd);
            estoqueModel.alterar(estoque);

            // 5. Remove produto se estoque zerado
            if (novaQtd == 0) {
                Produto produto = produtoModel.consultar(codProduto);
                if (produto != null) {
                    produtoModel.deletarProduto(produto);
                }
            }

            // 6. Adiciona ao resultado final
            produtosRegistrados.add(Map.of(
                    "produto_id", codProduto,
                    "quantidade_saida", quantidadeSaida,
                    "estoque_restante", novaQtd
            ));
        }

        // 7. Retorna resposta final
        return Map.of(
                "mensagem", "Saída registrada com sucesso",
                "saida_id", saidaGravada.getCod(),
                "data_saida", saidaGravada.getDataSaida(),
                "motivo", saidaGravada.getMotivo(),
                "funcionario_id", saidaGravada.getCodFuncionario(),
                "produtos", produtosRegistrados
        );
    }



    public Map<String, Object> registrarSaidaComProduto(
            int codProduto,
            int quantidadeSaida,
            int codFuncionario,
            String dataSaida,
            String motivo
    )
    {
        Conexao conexao = new Conexao();
        // buscar todos os estoques
        List<Estoque> estoques = estoqueModel.consultar("");

        // procurar o estoque do produto informado
        Estoque estoqueEncontrado = null;
        for (Estoque est : estoques) {
            if (est.getProduto_prod_cod() == codProduto) {
                estoqueEncontrado = est;

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
        int aux;
        Saida saidaGravada = saidaModel.gravar(novaSaida);

        if (saidaGravada == null || saidaGravada.getCod() == 0) {
           return Map.of("erro", "Erro ao registrar a saída");
        }

        // registrar na tabela SAIDA_PROD
        SaidaProd novaSaidaProd = new SaidaProd(codProduto, saidaGravada.getCod(), quantidadeSaida);
        SaidaProd saidaProdGravada = saidaProdModel.gravar(novaSaidaProd);
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

        Map<String, Object> json = new HashMap<>();
        json.put("mensagem", "Saída registrada com sucesso");
        json.put("saida_id", saidaGravada.getCod());
        json.put("produto_id", codProduto);
        json.put("quantidade_saida", quantidadeSaida);
        json.put("estoque_restante", novaQtd);


        json.put("data_saida", saidaGravada.getDataSaida());
        json.put("motivo", saidaGravada.getMotivo());
        json.put("funcionario_id", saidaGravada.getCodFuncionario());

        return json;
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
