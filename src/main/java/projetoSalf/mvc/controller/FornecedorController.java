package projetoSalf.mvc.controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.FornecedorDAO;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.CNPJUtils;

import java.util.*;

@Service
public class FornecedorController {

    private final FornecedorDAO dao = new FornecedorDAO();

    public List<Map<String, Object>> getFornecedor() {
        List<Fornecedor> lista = dao.get("");

        if (lista.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> prodList = new ArrayList<>();
        for (Fornecedor f : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", f.getForn_cod());
            json.put("nome", f.getForn_nome());
            json.put("endereco", f.getForn_end());
            json.put("cnpj", f.getForn_cnpj());
            json.put("telefone", f.getForn_telefone());
            prodList.add(json);
        }
        return prodList;
    }

    public Map<String, Object> getForn(int id) {
        Fornecedor fornecedor = dao.get(id);
        if (fornecedor == null) {
            return Map.of("erro", "Fornecedor não encontrado");
        }

        Map<String, Object> json = new HashMap<>();
        json.put("id", fornecedor.getForn_cod());
        json.put("nome", fornecedor.getForn_nome());
        json.put("endereco", fornecedor.getForn_end());
        json.put("cnpj", fornecedor.getForn_cnpj());
        json.put("telefone", fornecedor.getForn_telefone());
        return json;
    }

    public Map<String, Object> deletarFornecedor(int id) {
        Fornecedor fornecedor = dao.get(id);
        if (fornecedor == null) {
            return Map.of("erro", "Fornecedor não encontrado");
        }

        boolean deletado = dao.apagar(fornecedor);
        if (deletado) {
            return Map.of("mensagem", "Fornecedor removido com sucesso");
        } else {
            return Map.of("erro", "Erro ao remover fornecedor");
        }
    }

    public Map<String, Object> addFornecedor(String forn_nome, String forn_endereco, String forn_cnpj, String forn_telefone) {
        if (forn_nome.isBlank() || forn_endereco.isBlank() || forn_cnpj.isBlank() || forn_telefone.isBlank()) {
            return Map.of("erro", "Dados inválidos");
        }

        forn_cnpj = CNPJUtils.limparCNPJ(forn_cnpj);
        if (!CNPJUtils.validarCNPJ(forn_cnpj)) {
            return Map.of("erro", "CNPJ inválido.");
        }

        Fornecedor novo = new Fornecedor(forn_nome, forn_endereco, forn_cnpj, forn_telefone);
        Fornecedor gravado = dao.gravar(novo);

        if (gravado != null) {
            return Map.of(
                    "id", gravado.getForn_cod(),
                    "nome", gravado.getForn_nome(),
                    "endereco", gravado.getForn_end(),
                    "cnpj", gravado.getForn_cnpj(),
                    "telefone", gravado.getForn_telefone()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar fornecedor");
        }
    }

    public Map<String, Object> updateFornecedor(int forn_cod, String forn_nome, String forn_endereco, String forn_cnpj, String forn_telefone) {
        if (forn_cod <= 0 || forn_nome.isBlank() || forn_cnpj.isBlank() || forn_telefone.isBlank()) {
            return Map.of("erro", "Dados inválidos");
        }

        forn_cnpj = CNPJUtils.limparCNPJ(forn_cnpj);
        if (!CNPJUtils.validarCNPJ(forn_cnpj)) {
            return Map.of("erro", "CNPJ inválido.");
        }

        Fornecedor existente = dao.get(forn_cod);
        if (existente == null) {
            return Map.of("erro", "Fornecedor não encontrado");
        }

        existente.setForn_nome(forn_nome);
        existente.setForn_end(forn_endereco);
        existente.setForn_cnpj(forn_cnpj);
        existente.setForn_telefone(forn_telefone);

        Fornecedor atualizado = dao.alterar(existente);

        if (atualizado != null) {
            return Map.of(
                    "id", atualizado.getForn_cod(),
                    "nome", atualizado.getForn_nome(),
                    "endereco", atualizado.getForn_end(),
                    "cnpj", atualizado.getForn_cnpj(),
                    "telefone", atualizado.getForn_telefone()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar fornecedor");
        }
    }
}