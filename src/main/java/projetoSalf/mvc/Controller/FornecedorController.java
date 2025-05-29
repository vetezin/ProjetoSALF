package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.FormatUtils;

import java.util.*;

@RestController // Geralmente @RestController para endpoints REST
public class FornecedorController {

    @Autowired
    private Fornecedor fornecedorModel; // Instância da Model Fornecedor, que tem o FornecedorDAO injetado.

    // Método para listar fornecedores com filtro
    public List<Map<String, Object>> getFornecedores(String filtro) {
        List<Fornecedor> lista = fornecedorModel.consultar(filtro != null ? filtro : "");

        if (lista == null || lista.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> fornecedorList = new ArrayList<>();
        for (Fornecedor f : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("forn_cod", f.getForn_cod());
            json.put("forn_nome", f.getForn_nome());
            json.put("forn_end", f.getForn_end()); // Alterado para forn_end
            json.put("forn_cnpj", f.getForn_cnpj());
            json.put("forn_telefone", f.getForn_telefone());
            fornecedorList.add(json);
        }
        return fornecedorList;
    }

    // Método para buscar fornecedor por ID
    public Map<String, Object> getFornecedor(int id) {
        Fornecedor fornecedor = fornecedorModel.consultar(id);

        if (fornecedor == null) {
            return Map.of("erro", "Fornecedor não encontrado.");
        }

        Map<String, Object> json = new HashMap<>();
        json.put("forn_cod", fornecedor.getForn_cod());
        json.put("forn_nome", fornecedor.getForn_nome());
        json.put("forn_end", fornecedor.getForn_end()); // Alterado para forn_end
        json.put("forn_cnpj", fornecedor.getForn_cnpj());
        json.put("forn_telefone", fornecedor.getForn_telefone());
        return json;
    }

    // Método para deletar fornecedor
    public Map<String, Object> deletarFornecedor(int id) {
        Fornecedor fornecedorParaDeletar = fornecedorModel.consultar(id);
        if (fornecedorParaDeletar == null) {
            return Map.of("erro", "Fornecedor não encontrado para exclusão.");
        }

        boolean deletado = fornecedorModel.apagar(fornecedorParaDeletar);
        if (deletado) {
            return Map.of("mensagem", "Fornecedor removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover fornecedor. Verifique dependências ou erro interno.");
        }
    }

    // Método para registrar novo fornecedor
    public Map<String, Object> registrarFornecedor(
            String nome, String end, String cnpj, String telefone) { // Parâmetros ajustados

        if (nome == null || nome.isBlank() || end == null || end.isBlank() ||
                cnpj == null || cnpj.isBlank()) {
            return Map.of("erro", "Campos obrigatórios (Nome, Endereço, CNPJ) não preenchidos.");
        }

        cnpj = FormatUtils.limparCNPJ(cnpj);
        if (!FormatUtils.validarCNPJ(cnpj)) {
            return Map.of("erro", "CNPJ inválido.");
        }

        if (telefone != null && !telefone.isBlank()) {
            telefone = FormatUtils.limparTelefone(telefone);
            if (!FormatUtils.validarTelefone(telefone)) {
                return Map.of("erro", "Telefone inválido.");
            }
        } else {
            telefone = ""; // Garante que seja string vazia se nulo/blank
        }

        // Crie uma nova instância de Fornecedor com os campos corretos
        Fornecedor novoFornecedor = new Fornecedor(nome, end, cnpj, telefone);

        Fornecedor gravado = fornecedorModel.gravar(novoFornecedor);

        if (gravado != null) {
            return Map.of(
                    "mensagem", "Fornecedor cadastrado com sucesso!",
                    "forn_cod", gravado.getForn_cod(),
                    "forn_nome", gravado.getForn_nome()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar fornecedor. Possível CNPJ duplicado ou erro interno.");
        }
    }

    // Método para atualizar fornecedor
    public Map<String, Object> updtFornecedor(
            int cod, String nome, String end, String cnpj, String telefone) { // Parâmetros ajustados

        if (cod <= 0 || nome == null || nome.isBlank() || cnpj == null || cnpj.isBlank()) {
            return Map.of("erro", "Campos obrigatórios (ID, Nome, CNPJ) não preenchidos ou ID inválido.");
        }

        cnpj = FormatUtils.limparCNPJ(cnpj);
        if (!FormatUtils.validarCNPJ(cnpj)) {
            return Map.of("erro", "CNPJ inválido.");
        }

        if (telefone != null && !telefone.isBlank()) {
            telefone = FormatUtils.limparTelefone(telefone);
            if (!FormatUtils.validarTelefone(telefone)) {
                return Map.of("erro", "Telefone inválido.");
            }
        } else {
            telefone = "";
        }

        Fornecedor existente = fornecedorModel.consultar(cod);
        if (existente == null) {
            return Map.of("erro", "Fornecedor não encontrado para o ID fornecido.");
        }

        // Atualize os campos da instância existente
        existente.setForn_nome(nome);
        existente.setForn_end(end); // Alterado para forn_end
        existente.setForn_cnpj(cnpj);
        existente.setForn_telefone(telefone);

        Fornecedor atualizado = fornecedorModel.alterar(existente);

        if (atualizado != null) {
            return Map.of(
                    "mensagem", "Fornecedor atualizado com sucesso!",
                    "forn_cod", atualizado.getForn_cod(),
                    "forn_nome", atualizado.getForn_nome()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar fornecedor. Possível CNPJ duplicado ou erro interno.");
        }
    }
}