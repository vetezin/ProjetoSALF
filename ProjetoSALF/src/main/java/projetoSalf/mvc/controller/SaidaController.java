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


    public Map<String, Object> deletarSaidaComProdutos(int id) {
        // 1. Consulta a saída para verificar se existe
        Saida existente = saidaModel.consultar(id);
        if (existente == null)
            return Map.of("erro", "Saída não encontrada");

        // 2. Busca todos os SaidaProd relacionados a essa saída
        List<SaidaProd> produtosDaSaida = saidaProdModel.consultar("saida_s_cod = " + id);

        // 3. Para cada SaidaProd, repor a quantidade no estoque
        for (SaidaProd sp : produtosDaSaida) {
            int codProduto = sp.getProdutoCod();
            int qtdSaida = sp.getQtd();

            // Consulta o estoque do produto
            Estoque estoque = estoqueModel.consultar("").stream()
                    .filter(e -> e.getProduto_prod_cod() == codProduto)
                    .findFirst()
                    .orElse(null);

            if (estoque != null) {
                // Repor quantidade no estoque
                estoque.setEs_qtdprod(estoque.getEs_qtdprod() + qtdSaida);
                estoqueModel.alterar(estoque);
            } else {
                // Caso não tenha estoque, cria um novo registro (se aplicável)
                Estoque novoEstoque = new Estoque();
                novoEstoque.setProduto_prod_cod(codProduto);
                novoEstoque.setEs_qtdprod(qtdSaida);
                estoqueModel.gravar(novoEstoque);
            }
        }

        // 4. Excluir todos os SaidaProd vinculados de uma vez usando o novo método
        saidaProdModel.deletarPorSaida(id);  //método deve chamar apagar(int saidaCod)

        // 5. Excluir a saída
        boolean deletado = saidaModel.deletar(existente);
        if (deletado) {
            return Map.of("mensagem", "Saída e seus produtos removidos com sucesso, e estoque atualizado!");
        } else {
            return Map.of("erro", "Erro ao remover saída");
        }
    }


    public List<Map<String, Object>> getSaidasComProdutos() {
        // 1. Recupera todas as saídas normalmente
        List<Saida> listaSaidas = saidaModel.consultar("");
        if (listaSaidas.isEmpty()) return null;

        // 2. Recupera todos os SaidaProd SEM filtro (todos os registros da tabela)
        List<SaidaProd> todosProdutosSaida = saidaProdModel.consultar("");

        List<Map<String, Object>> saidasComProdutos = new ArrayList<>();

        // 3. Para cada saída, filtra os produtos que pertencem a ela
        for (Saida s : listaSaidas) {
            Map<String, Object> saidaJson = new HashMap<>();
            saidaJson.put("id", s.getCod());
            saidaJson.put("data", s.getDataSaida());
            saidaJson.put("motivo", s.getMotivo());

            // Buscar funcionário
            Funcionario funcionario = funcionarioModel.consultar(s.getCodFuncionario());
            if (funcionario != null) {
                Map<String, Object> funcionarioJson = new HashMap<>();
                funcionarioJson.put("id", funcionario.getId());
                funcionarioJson.put("nome", funcionario.getNome());
                saidaJson.put("funcionario", funcionarioJson);
            }

            // Filtra os produtos que tem saida_s_cod == s.getCod()
            List<SaidaProd> produtosDaSaida = new ArrayList<>();
            for (SaidaProd sp : todosProdutosSaida) {
                if (sp.getSaidaCod() == s.getCod()) { // supondo que o método getSaidaCod() retorna saida_s_cod
                    produtosDaSaida.add(sp);
                }
            }

            List<Map<String, Object>> produtosJson = new ArrayList<>();

            for (SaidaProd sp : produtosDaSaida) {
                Produto p = produtoModel.consultar(sp.getProdutoCod());

                Map<String, Object> produtoJson = new HashMap<>();
                produtoJson.put("produto_id", sp.getProdutoCod());
                produtoJson.put("quantidade", sp.getQtd());

                if (p != null) {
                    produtoJson.put("nome", p.getProd_desc());
                    produtoJson.put("categoria", p.getCategoria());
                    produtoJson.put("valor", p.getProd_valorun());
                    // Adicione mais campos conforme necessário
                }

                produtosJson.add(produtoJson);
            }

            saidaJson.put("produtos", produtosJson);
            saidasComProdutos.add(saidaJson);
        }

        return saidasComProdutos;
    }


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
            /*
            if (novaQtd == 0) {
                Produto produto = produtoModel.consultar(codProduto);
                if (produto != null) {
                    produtoModel.deletarProduto(produto);
                }
            }
            */
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
