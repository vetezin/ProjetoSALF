package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.Conexao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FornecedorController {

    @Autowired
    private Fornecedor fornecedorModel;

    public List<Map<String, Object>> getFornecedor() {
        Conexao conexao = new Conexao();
        List<Fornecedor> lista = fornecedorModel.consultar("",conexao);

        if(lista.isEmpty()){
            return null;
        }
        else{
            List<Map<String, Object>> prodList = new ArrayList<>();
            for(Fornecedor f : lista){
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
    }

    public Map<String, Object> getForn(int id) {
        Conexao conexao = new Conexao();
        Fornecedor fornecedor  = fornecedorModel.consultar(id);

        if(fornecedor==null){
            return null;
        }
        else {
            Map<String, Object> json = new HashMap<>();
            json.put("id", fornecedor.getForn_cod());
            json.put("nome", fornecedor.getForn_nome());
            json.put("endereco", fornecedor.getForn_end());
            json.put("cnpj", fornecedor.getForn_cnpj());
            json.put("telefone", fornecedor.getForn_telefone());

            return json;
        }
    }

    public Map<String, Object> deletarFornecedor(int id) {
        Fornecedor fornecedor = fornecedorModel.consultar(id);
        if(fornecedor==null){
            return Map.of("erro", "Fornecedor não encontrado");
        }

        boolean deletado = fornecedorModel.deletar(fornecedor);

        if(deletado){
            return Map.of("mensagem", "Fornecedor removido com sucesso");
        } else {
            return Map.of("erro", "Erro ao remover Fornecedor");
        }
    }

    public Map<String, Object> addFornecedor(String forn_nome, String forn_endereco, String forn_cnpj, String forn_telefone) {

        if(forn_nome.isBlank() || forn_endereco.isBlank() || forn_cnpj.isBlank() || forn_telefone.isBlank()){
            return Map.of("Erro", "Dados inválidos");
        }
        Conexao conexao = new Conexao();
        Fornecedor novo = new Fornecedor(forn_nome, forn_endereco, forn_cnpj, forn_telefone);
        Fornecedor gravado = fornecedorModel.gravar(novo);

        if(gravado != null){
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getForn_cod());
            json.put("nome", gravado.getForn_nome());
            json.put("endereco", gravado.getForn_end());
            json.put("cnpj", gravado.getForn_cnpj());
            json.put("telefone", gravado.getForn_telefone());
            return json;
        }else{
            return Map.of("Erro", "Erro ao cadastrar fornecedor.");
        }
    }

    public Map<String, Object> updateFornecedor(int forn_cod, String forn_nome, String forn_endereco, String forn_cnpj, String forn_telefone) {

        if(forn_cod <= 0 || forn_nome.isBlank() || forn_cnpj.isBlank() || forn_telefone.isBlank())
            return Map.of("Erro", "Dados inválidos");

        Fornecedor existente = fornecedorModel.consultar(forn_cod);
        if(existente==null)
            return Map.of("Erro","Fornecedor não encontrado");

        existente.setForn_nome(forn_nome);
        existente.setForn_end(forn_endereco);
        existente.setForn_cnpj(forn_cnpj);
        existente.setForn_telefone(forn_telefone);

        Fornecedor atualizado = fornecedorModel.gravar(existente);
        if (atualizado != null) {
            return Map.of("id",atualizado.getForn_cod(),
                    "nome", atualizado.getForn_nome(),
                    "end", atualizado.getForn_end(),
                    "cnpj", atualizado.getForn_cnpj(),
                    "telefone", atualizado.getForn_telefone()
            );
        }else{
            return Map.of("Erro", "Erro ao atualizar Fornecedor");
        }
    }
}
