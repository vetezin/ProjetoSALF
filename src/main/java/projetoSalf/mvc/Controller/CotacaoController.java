package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Cotacao;
import projetoSalf.mvc.model.CotacaoForn;
import projetoSalf.mvc.model.ListaCompra; // Para buscar a ListaCompra associada
import projetoSalf.mvc.model.Fornecedor; // Você precisará criar esta Model e seu DAO/Controller
import projetoSalf.mvc.util.Conexao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CotacaoController {

    @Autowired
    private Cotacao cotacaoModel; // Model que injeta o DAO de Cotacao

    @Autowired
    private ListaCompra listaCompraModel; // Para consultar a ListaCompra associada

    @Autowired
    private Fornecedor fornecedorModel; // Você precisará criar esta Model e seu DAO

    @Autowired
    private CotacaoForn cotacaoFornModel; // Model que injeta o DAO de CotacaoForn

    // 1. Método para listar todas as cotações (ou com filtro)
    public List<Map<String, Object>> getCotacoes(String filtro) {
        Conexao conexao = new Conexao(); // Replicando o padrão de instanciar Conexao

        List<Cotacao> cotacoes = cotacaoModel.consultar(filtro);
        if (cotacoes == null || cotacoes.isEmpty()) {
            return null; // Retorna null se não houver cotações
        }

        List<Map<String, Object>> cotacoesResponse = new ArrayList<>();
        for (Cotacao cotacao : cotacoes) {
            Map<String, Object> json = new HashMap<>();
            json.put("cot_cod", cotacao.getCot_cod());
            json.put("cot_dtabertura", cotacao.getCot_dtabertura());
            json.put("cot_dtfechamento", cotacao.getCot_dtfechamento());

            // Buscar informações da ListaCompra associada
            ListaCompra listaCompra = listaCompraModel.consultar(cotacao.getLc_cod());
            Map<String, Object> listaCompraJson = new HashMap<>();
            if (listaCompra != null) {
                listaCompraJson.put("lc_cod", listaCompra.getId());
                listaCompraJson.put("lc_descricao", listaCompra.getDescricao());
                // Adicione mais campos da ListaCompra se necessário
            } else {
                listaCompraJson.put("lc_cod", cotacao.getLc_cod());
                listaCompraJson.put("lc_descricao", "Lista de Compra Desconhecida");
            }
            json.put("lista_compra", listaCompraJson);

            // Buscar fornecedores associados a esta cotação através de cot_forn
            List<Map<String, Object>> fornecedoresAssociados = new ArrayList<>();
            // Assumindo que CotacaoFornDAO.get(String filtro) pode listar por cot_cod
            List<CotacaoForn> cotacaoFornecedores = cotacaoFornModel.consultar("cot_cod = " + cotacao.getCot_cod());
            if (cotacaoFornecedores != null) {
                for (CotacaoForn cf : cotacaoFornecedores) {
                    Fornecedor fornecedor = fornecedorModel.consultar(cf.getForn_cod()); // Você precisa desta consulta
                    Map<String, Object> fornecedorJson = new HashMap<>();
                    if (fornecedor != null) {
                        fornecedorJson.put("forn_cod", fornecedor.getId()); // Assumindo getId() em Fornecedor
                        fornecedorJson.put("forn_nome", fornecedor.getNome()); // Assumindo getNome() em Fornecedor
                        // Adicione outros detalhes do fornecedor se precisar
                    } else {
                        fornecedorJson.put("forn_cod", cf.getForn_cod());
                        fornecedorJson.put("forn_nome", "Fornecedor Desconhecido");
                    }
                    fornecedoresAssociados.add(fornecedorJson);
                }
            }
            json.put("fornecedores", fornecedoresAssociados);

            cotacoesResponse.add(json);
        }
        return cotacoesResponse;
    }

    // 2. Método para obter uma cotação específica por ID
    public Map<String, Object> getCotacao(int cot_cod) {
        Conexao conexao = new Conexao();

        Cotacao cotacao = cotacaoModel.consultar(cot_cod);
        if (cotacao == null) {
            return Map.of("erro", "Cotação não encontrada");
        }

        Map<String, Object> json = new HashMap<>();
        json.put("cot_cod", cotacao.getCot_cod());
        json.put("cot_dtabertura", cotacao.getCot_dtabertura());
        json.put("cot_dtfechamento", cotacao.getCot_dtfechamento());

        // Buscar informações da ListaCompra associada
        ListaCompra listaCompra = listaCompraModel.consultar(cotacao.getLc_cod());
        Map<String, Object> listaCompraJson = new HashMap<>();
        if (listaCompra != null) {
            listaCompraJson.put("lc_cod", listaCompra.getId());
            listaCompraJson.put("lc_descricao", listaCompra.getDescricao());
        } else {
            listaCompraJson.put("lc_cod", cotacao.getLc_cod());
            listaCompraJson.put("lc_descricao", "Lista de Compra Desconhecida");
        }
        json.put("lista_compra", listaCompraJson);

        // Buscar fornecedores associados
        List<Map<String, Object>> fornecedoresAssociados = new ArrayList<>();
        List<CotacaoForn> cotacaoFornecedores = cotacaoFornModel.consultar("cot_cod = " + cotacao.getCot_cod());
        if (cotacaoFornecedores != null) {
            for (CotacaoForn cf : cotacaoFornecedores) {
                Fornecedor fornecedor = fornecedorModel.consultar(cf.getForn_cod()); // Você precisa desta consulta
                Map<String, Object> fornecedorJson = new HashMap<>();
                if (fornecedor != null) {
                    fornecedorJson.put("forn_cod", fornecedor.getId());
                    fornecedorJson.put("forn_nome", fornecedor.getNome());
                } else {
                    fornecedorJson.put("forn_cod", cf.getForn_cod());
                    fornecedorJson.put("forn_nome", "Fornecedor Desconhecido");
                }
                fornecedoresAssociados.add(fornecedorJson);
            }
        }
        json.put("fornecedores", fornecedoresAssociados);

        return json;
    }

    // 3. Método para registrar uma nova cotação
    public Map<String, Object> registrarCotacao(
            String cot_dtabertura,
            String cot_dtfechamento, // Pode ser null para cotações abertas
            int lc_cod,
            List<Integer> fornecedorCods // Lista de IDs de fornecedores para associar
    ) {
        Conexao conexao = new Conexao();

        if (cot_dtabertura == null || cot_dtabertura.isBlank() || lc_cod <= 0) {
            return Map.of("erro", "Dados inválidos para registrar a Cotação (data de abertura e código da lista são obrigatórios).");
        }

        // 1. Verificar se a Lista de Compra existe
        ListaCompra listaCompraExistente = listaCompraModel.consultar(lc_cod);
        if (listaCompraExistente == null) {
            return Map.of("erro", "Lista de Compra com código " + lc_cod + " não encontrada.");
        }

        // 2. Criar e gravar a cotação principal
        Cotacao novaCotacao = new Cotacao(cot_dtabertura, cot_dtfechamento, lc_cod);
        Cotacao cotacaoGravada = cotacaoModel.gravar(novaCotacao);

        if (cotacaoGravada == null || cotacaoGravada.getCot_cod() == 0) {
            return Map.of("erro", "Erro ao registrar a Cotação.");
        }

        List<Map<String, Object>> fornecedoresProcessados = new ArrayList<>();

        // 3. Associar fornecedores à cotação (cot_forn)
        if (fornecedorCods != null && !fornecedorCods.isEmpty()) {
            for (Integer fornCod : fornecedorCods) {
                // Verificar se o fornecedor existe (opcional, mas recomendado)
                Fornecedor fornecedorExistente = fornecedorModel.consultar(fornCod); // Você precisa desta consulta
                if (fornecedorExistente == null) {
                    // Se um fornecedor não existir, você pode decidir se:
                    // A) Aborta toda a cotação e a apaga (com a cot_forn já gravada)
                    // B) Ignora o fornecedor inválido e continua
                    // C) Retorna um erro e apaga a cotação e as associações já feitas
                    // Aqui, vou optar por apagar a cotação recém-criada e retornar um erro.
                    cotacaoModel.apagar(cotacaoGravada); // Apaga a cotação principal
                    // Você precisaria de um método em CotacaoFornDAO para apagar por cot_cod
                    // cotacaoFornModel.apagarPorCotacao(cotacaoGravada.getCot_cod()); // Se implementado
                    return Map.of("erro", "Fornecedor com código " + fornCod + " não encontrado. Cotação desfeita.");
                }

                CotacaoForn novaCotacaoForn = new CotacaoForn(cotacaoGravada.getCot_cod(), fornCod);
                CotacaoForn cotacaoFornGravada = cotacaoFornModel.gravar(novaCotacaoForn);

                if (cotacaoFornGravada == null) {
                    // Erro ao associar, então desfaz a cotação principal e associações anteriores
                    cotacaoModel.apagar(cotacaoGravada);
                    // cotacaoFornModel.apagarPorCotacao(cotacaoGravada.getCot_cod()); // Se implementado
                    return Map.of("erro", "Erro ao associar fornecedor " + fornCod + " à cotação.");
                }
                fornecedoresProcessados.add(Map.of("forn_cod", fornCod, "forn_nome", fornecedorExistente.getNome()));
            }
        }

        return Map.of(
                "mensagem", "Cotação registrada com sucesso!",
                "cot_cod", cotacaoGravada.getCot_cod(),
                "cot_dtabertura", cotacaoGravada.getCot_dtabertura(),
                "cot_dtfechamento", cotacaoGravada.getCot_dtfechamento() != null ? cotacaoGravada.getCot_dtfechamento() : "Aberta",
                "lc_cod", cotacaoGravada.getLc_cod(),
                "fornecedores_associados", fornecedoresProcessados
        );
    }

    // 4. Método para atualizar uma cotação
    public Map<String, Object> updtCotacao(
            int cot_cod,
            String cot_dtabertura,
            String cot_dtfechamento,
            int lc_cod,
            List<Integer> novosFornecedorCods // Para atualizar a lista de fornecedores
    ) {
        Conexao conexao = new Conexao();

        if (cot_cod <= 0 || cot_dtabertura == null || cot_dtabertura.isBlank() || lc_cod <= 0) {
            return Map.of("erro", "Dados inválidos para atualização da Cotação.");
        }

        Cotacao cotacaoParaAtualizar = new Cotacao(cot_cod, cot_dtabertura, cot_dtfechamento, lc_cod);
        Cotacao cotacaoAtualizada = cotacaoModel.alterar(cotacaoParaAtualizar);

        if (cotacaoAtualizada == null) {
            return Map.of("erro", "Erro ao alterar a cotação.");
        }

        // Atualizar os fornecedores associados
        // Essa é a parte mais complexa: você precisa sincronizar os fornecedores.
        // Uma abordagem comum é apagar todos os fornecedores associados e depois inserir os novos.
        // CUIDADO: Isso pode ser ineficiente para muitos fornecedores.
        // Uma alternativa seria comparar e fazer INSERT/DELETE apenas nas diferenças.

        // Apagar associações existentes para esta cotação
        // Você precisaria de um método em CotacaoFornDAO para apagar por cot_cod
        // cotacaoFornModel.apagarPorCotacao(cotacaoAtualizada.getCot_cod()); // Se implementado

        // Reinserir as novas associações
        List<Map<String, Object>> fornecedoresProcessados = new ArrayList<>();
        if (novosFornecedorCods != null && !novosFornecedorCods.isEmpty()) {
            // Primeiro apaga todas as associações existentes para esta cotação
            cotacaoFornModel.apagar(new CotacaoForn(cotacaoAtualizada.getCot_cod(), 0)); // Um "truque" se o apagar por cot_cod estiver no DAO. Idealmente, faça um método específico no DAO.
            // OU: implemente um método como: dao.apagarTodosPorCotacao(cotacaoAtualizada.getCot_cod());

            for (Integer fornCod : novosFornecedorCods) {
                Fornecedor fornecedorExistente = fornecedorModel.consultar(fornCod);
                if (fornecedorExistente == null) {
                    // O que fazer se um novo fornecedor na lista não existir? Abortar ou ignorar?
                    System.out.println("Aviso: Fornecedor com código " + fornCod + " não encontrado durante atualização. Ignorado.");
                    continue; // Ignorar e prosseguir
                }
                CotacaoForn novaCotacaoForn = new CotacaoForn(cotacaoAtualizada.getCot_cod(), fornCod);
                CotacaoForn cotacaoFornGravada = cotacaoFornModel.gravar(novaCotacaoForn);
                if (cotacaoFornGravada != null) {
                    fornecedoresProcessados.add(Map.of("forn_cod", fornCod, "forn_nome", fornecedorExistente.getNome()));
                } else {
                    System.out.println("Erro ao reassociar fornecedor " + fornCod + " à cotação durante atualização.");
                }
            }
        }

        return Map.of(
                "mensagem", "Cotação alterada com sucesso!",
                "cot_cod", cotacaoAtualizada.getCot_cod(),
                "cot_dtabertura", cotacaoAtualizada.getCot_dtabertura(),
                "cot_dtfechamento", cotacaoAtualizada.getCot_dtfechamento() != null ? cotacaoAtualizada.getCot_dtfechamento() : "Aberta",
                "lc_cod", cotacaoAtualizada.getLc_cod(),
                "fornecedores_associados", fornecedoresProcessados
        );
    }

    // 5. Método para excluir uma cotação
    public Map<String, Object> deletarCotacao(int cot_cod) {
        Conexao conexao = new Conexao();

        Cotacao cotacao = cotacaoModel.consultar(cot_cod);
        if (cotacao == null) {
            return Map.of("erro", "Cotação não encontrada.");
        }

        // Antes de apagar a cotação, apague todas as associações em cot_forn
        // Você precisaria de um método específico no DAO para apagar por cot_cod, ex:
        // cotacaoFornModel.apagarPorCotacao(cot_cod); // Assumindo que este método existe
        // Por enquanto, usarei um "truque" com o método apagar existente se for viável,
        // mas idealmente, o DAO de CotacaoForn deveria ter um método para isso.
        // Exemplo de como você poderia apagar todas as associações:
        boolean associacoesApagadas = false;
        try {
            List<CotacaoForn> associacoes = cotacaoFornModel.consultar("cot_cod = " + cot_cod);
            if (associacoes != null) {
                for (CotacaoForn cf : associacoes) {
                    cotacaoFornModel.apagar(cf);
                }
            }
            associacoesApagadas = true;
        } catch (Exception e) {
            System.out.println("Erro ao apagar associações CotacaoForn para cot_cod " + cot_cod + ": " + e.getMessage());
            associacoesApagadas = false;
        }

        if (!associacoesApagadas) {
            return Map.of("erro", "Erro ao apagar associações de fornecedores. Cotação não excluída.");
        }


        boolean deletado = cotacaoModel.apagar(cotacao);
        if (deletado) {
            return Map.of("mensagem", "Cotação excluída com sucesso!");
        } else {
            return Map.of("erro", "Erro ao excluir a cotação.");
        }
    }
}