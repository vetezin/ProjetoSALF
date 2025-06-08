package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.*;
import java.util.*;

@Service
public class AcertoEstoqueController {

    @Autowired
    private AcertoEstoque acertoModel;

    @Autowired
    private Produto produtoModel;

    @Autowired
    private Estoque estoqueModel;

    @Autowired
    private Funcionario funcionarioModel;




    public Map<String, Object> registrarAcertoProduto(
            int codProduto,
            int quantidadeASubtrair,
            int codFuncionario,
            String dataAcerto,
            String motivo
    ) {
        if (codProduto <= 0 || quantidadeASubtrair <= 0 || codFuncionario <= 0 || dataAcerto == null || dataAcerto.isBlank() || motivo == null || motivo.isBlank()) {
            return Map.of("erro", "Dados inválidos para registrar acerto");
        }

        // Consulta estoque atual do produto
        Estoque estoque = estoqueModel.consultar("").stream()
                .filter(e -> e.getProduto_prod_cod() == codProduto)
                .findFirst().orElse(null);

        if (estoque == null) {
            return Map.of("erro", "Produto com ID " + codProduto + " não encontrado no estoque");
        }

        int quantidadeAnterior = estoque.getEs_qtdprod();

        if (quantidadeASubtrair > quantidadeAnterior) {
            return Map.of("erro", "Quantidade a subtrair é maior que o estoque atual");
        }

        // Atualiza estoque subtraindo a quantidade informada
        int novaQuantidade = quantidadeAnterior - quantidadeASubtrair;
        estoque.setEs_qtdprod(novaQuantidade);

        Estoque atualizado = estoqueModel.alterarQtd(estoque);

        if (atualizado == null) {
            return Map.of("erro", "Erro ao atualizar estoque do produto ID " + codProduto);
        }

        // Registra o acerto como uma saída de estoque (redução)
        AcertoEstoque novoAcerto = new AcertoEstoque(dataAcerto, motivo, -quantidadeASubtrair, codFuncionario, codProduto);
        AcertoEstoque acertoGravado = acertoModel.gravar(novoAcerto);

        if (acertoGravado == null) {
            return Map.of("erro", "Erro ao registrar acerto do produto ID " + codProduto);
        }

        return Map.of(
                "mensagem", "Acerto (redução) registrado com sucesso",
                "produto_id", codProduto,
                "quantidade_anterior", quantidadeAnterior,
                "quantidade_subtraida", quantidadeASubtrair,
                "nova_quantidade", novaQuantidade,
                "motivo", motivo,
                "data", dataAcerto,
                "funcionario_id", codFuncionario
        );
    }


    public List<Map<String, Object>> getAcertos() {
        List<AcertoEstoque> lista = acertoModel.consultar("");
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> acertosJson = new ArrayList<>();
        for (AcertoEstoque acerto : lista) {
            Map<String, Object> json = new HashMap<>();

            // Dados do acerto
            json.put("id", acerto.getCod());
            json.put("data", acerto.getData());
            json.put("motivo", acerto.getMotivo());
            json.put("quantidade", acerto.getQtd());


            // Objeto Produto completo
            Produto produto = produtoModel.consultar(acerto.getCodProduto());

            if (produto != null) {

                Map<String, Object> jsonProduto = new HashMap<>();
                jsonProduto.put("id", produto.getProd_cod());
                jsonProduto.put("descricao", produto.getProd_desc());
                jsonProduto.put("valor", produto.getProd_valorun());
                jsonProduto.put("categoria", produto.getCategoria());
                // adicione outros campos conforme necessário
                json.put("produto", jsonProduto);
            }

            // Objeto Funcionario completo
            Funcionario funcionario = funcionarioModel.consultar(acerto.getCodFuncionario());
            if (funcionario != null) {
                Map<String, Object> jsonFuncionario = new HashMap<>();
                jsonFuncionario.put("id", funcionario.getId());
                jsonFuncionario.put("nome", funcionario.getNome());

                // adicione outros campos conforme necessário
                json.put("funcionario", jsonFuncionario);
            }

            acertosJson.add(json);
        }

        return acertosJson;
    }




public Map<String, Object> deletarAcerto(int id) {
    // 1. Verifica se o acerto existe
    AcertoEstoque acerto = acertoModel.consultar(id);
    if (acerto == null)
        return Map.of("erro", "Acerto não encontrado");

    int codProduto = acerto.getCodProduto();
    int quantidade = (int) acerto.getQtd();

    // 2. Reverter quantidade do acerto no estoque
    Estoque estoque = estoqueModel.consultar("").stream()
            .filter(e -> e.getProduto_prod_cod() == codProduto)
            .findFirst()
            .orElse(null);

    if (estoque != null) {
        estoque.setEs_qtdprod(estoque.getEs_qtdprod() - quantidade);
        estoqueModel.alterarQtd(estoque);
    } else {
        return Map.of("erro", "Estoque do produto não encontrado para reverter acerto");
    }

    // 3. Deleta o acerto
    boolean deletado = acertoModel.deletar(acerto);
    if (deletado) {
        return Map.of("mensagem", "Acerto removido e quantidade revertida do estoque!");
    } else {
        return Map.of("erro", "Erro ao remover acerto");
    }
}













    /*
    public Map<String, Object> registrarAcertoComProdutos(
            List<Map<String, Object>> produtos, // lista de {codProduto, novaQuantidade, motivo}
            int codFuncionario,
            String dataAcerto
    ) {
        if (produtos == null || produtos.isEmpty() || codFuncionario <= 0 || dataAcerto == null || dataAcerto.isBlank()) {
            return Map.of("erro", "Dados inválidos para registrar acerto");
        }

        List<Map<String, Object>> produtosAjustados = new ArrayList<>();

        for (Map<String, Object> info : produtos) {
            int codProduto = (int) info.get("codProduto");
            int novaQuantidade = (int) info.get("novaQuantidade");
            String motivo = (String) info.get("motivo");

            if (codProduto <= 0 || novaQuantidade < 0 || motivo == null || motivo.isBlank()) {
                return Map.of("erro", "Dados inválidos para o produto de ID " + codProduto);
            }

            // Consulta estoque atual do produto
            Estoque estoque = estoqueModel.consultar("").stream()
                    .filter(e -> e.getProduto_prod_cod() == codProduto)
                    .findFirst().orElse(null);

            if (estoque == null) {
                return Map.of("erro", "Produto com ID " + codProduto + " não encontrado no estoque");
            }

            int quantidadeAnterior = estoque.getEs_qtdprod();

            // Atualiza estoque com nova quantidade
            estoque.setEs_qtdprod(novaQuantidade);

            Estoque atualizado = estoqueModel.alterar(estoque);

            if (atualizado==null) {
                return Map.of("erro", "Erro ao atualizar estoque do produto ID " + codProduto);
            }

            // Registra o acerto
            AcertoEstoque novoAcerto = new AcertoEstoque(dataAcerto, motivo,novaQuantidade, codFuncionario, codProduto);
            AcertoEstoque acertoGravado = acertoModel.gravar(novoAcerto);

            if (acertoGravado == null) {
                return Map.of("erro", "Erro ao registrar acerto do produto ID " + codProduto);
            }

            produtosAjustados.add(Map.of(
                    "produto_id", codProduto,
                    "quantidade_anterior", quantidadeAnterior,
                    "nova_quantidade", novaQuantidade,
                    "motivo", motivo
            ));
        }

        return Map.of(
                "mensagem", "Acertos registrados com sucesso",
                "data", dataAcerto,
                "funcionario_id", codFuncionario,
                "produtos", produtosAjustados
        );
    }
    */


    /*
    public List<Map<String, Object>> getAcertos() {
        List<AcertoEstoque> lista = acertoModel.consultar("");
        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> acertosJson = new ArrayList<>();
        for (AcertoEstoque acerto : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", acerto.getId());
            json.put("data", acerto.getDataAcerto());
            json.put("motivo", acerto.getMotivo());
            json.put("produto_id", acerto.getCodProduto());
            json.put("quantidade_anterior", acerto.getQtdAnterior());
            json.put("nova_quantidade", acerto.getQtdNova());

            Produto produto = produtoModel.consultar(acerto.getCodProduto());
            if (produto != null) {
                json.put("produto_nome", produto.getProd_desc());
            }

            Funcionario funcionario = funcionarioModel.consultar(acerto.getCodFuncionario());
            if (funcionario != null) {
                json.put("funcionario_id", funcionario.getId());
                json.put("funcionario_nome", funcionario.getNome());
            }

            acertosJson.add(json);
        }

        return acertosJson;
    }
    */
}
