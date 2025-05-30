package projetoSalf.mvc.controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.FormatUtils;

import java.util.*;

@Service
public class FornecedorController {

    public List<Map<String, Object>> getFornecedor() {
        List<Fornecedor> lista = Fornecedor.consultarTodos();

        if (lista == null || lista.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> fornecedorList = new ArrayList<>();
        for (Fornecedor f : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", f.getForn_cod());
            json.put("nome", f.getForn_nome());
            json.put("logradouro", f.getForn_logradouro());
            json.put("numero", f.getForn_numero());
            json.put("cep", f.getForn_cep());
            json.put("cidade", f.getForn_cidade());
            json.put("complemento", f.getForn_complemento());
            json.put("cnpj", f.getForn_cnpj());
            json.put("telefone", f.getForn_telefone());
            json.put("contato", f.getForn_contato());
            json.put("email", f.getForn_email());
            fornecedorList.add(json);
        }
        return fornecedorList;
    }

    public Map<String, Object> deletarFornecedor(int id) {
        Fornecedor fornecedor = Fornecedor.consultarPorId(id);
        if (fornecedor == null) {
            return Map.of("erro", "Fornecedor não encontrado");
        }

        boolean deletado = Fornecedor.deletar(fornecedor);
        if (deletado) {
            return Map.of("mensagem", "Fornecedor removido com sucesso");
        } else {
            return Map.of("erro", "Erro ao remover fornecedor");
        }
    }

    public Map<String, Object> addFornecedor(
            String nome, String logradouro, String numero, String cep, String cidade,
            String complemento, String cnpj, String telefone, String contato, String email) {

        if (nome.isBlank() || logradouro.isBlank() || numero.isBlank() || cidade.isBlank() || cnpj.isBlank()) {
            return Map.of("erro", "Campos obrigatórios não preenchidos.");
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
        }

        complemento = (complemento != null) ? complemento : "";
        contato = (contato != null) ? contato : "";
        email = (email != null) ? email : "";
        telefone = (telefone != null) ? telefone : "";

        Fornecedor novo = new Fornecedor(nome, logradouro, numero, cep, cidade, complemento,
                cnpj, telefone, contato, email);

        Fornecedor gravado = novo.inserir();

        if (gravado != null) {
            return Map.of(
                    "id", gravado.getForn_cod(),
                    "nome", gravado.getForn_nome()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar fornecedor");
        }
    }

    public Map<String, Object> updateFornecedor(
            int cod, String nome, String logradouro, String numero, String cep, String cidade,
            String complemento, String cnpj, String telefone, String contato, String email) {

        if (cod <= 0 || nome.isBlank() || cnpj.isBlank() || telefone.isBlank()) {
            return Map.of("erro", "Dados inválidos");
        }

        cnpj = FormatUtils.limparCNPJ(cnpj);
        if (!FormatUtils.validarCNPJ(cnpj)) {
            return Map.of("erro", "CNPJ inválido.");
        }

        telefone = FormatUtils.limparTelefone(telefone);
        if (!FormatUtils.validarTelefone(telefone)) {
            return Map.of("erro", "Telefone inválido.");
        }

        Fornecedor existente = Fornecedor.consultarPorId(cod);
        if (existente == null) {
            return Map.of("erro", "Fornecedor não encontrado");
        }

        complemento = (complemento != null) ? complemento : "";
        contato = (contato != null) ? contato : "";
        email = (email != null) ? email : "";
        telefone = (telefone != null) ? telefone : "";

        existente.setForn_nome(nome);
        existente.setForn_logradouro(logradouro);
        existente.setForn_numero(numero);
        existente.setForn_cep(cep);
        existente.setForn_cidade(cidade);
        existente.setForn_complemento(complemento);
        existente.setForn_cnpj(cnpj);
        existente.setForn_telefone(telefone);
        existente.setForn_contato(contato);
        existente.setForn_email(email);

        Fornecedor atualizado = existente.alterar();

        if (atualizado != null) {
            return Map.of(
                    "id", atualizado.getForn_cod(),
                    "nome", atualizado.getForn_nome()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar fornecedor");
        }
    }
}
