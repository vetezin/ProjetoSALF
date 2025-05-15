package projetoSalf.mvc.controller;

import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FuncionarioController {

    @Autowired
    private Funcionario funcionarioModel;

    public List<Map<String, Object>> getFuncionario() {
        Conexao conexao = new Conexao();
        List<Funcionario> lista = funcionarioModel.consultar("", conexao);
        if (!lista.isEmpty()) {
            List<Map<String, Object>> funcList = new ArrayList<>();
            for (Funcionario func : lista) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", func.getId());
                json.put("nome", func.getNome());
                json.put("cpf", func.getCpf());
                json.put("email", func.getEmail());
                json.put("login", func.getLogin());
                json.put("nivel", func.getNivel());
                json.put("senha", func.getSenha());
                funcList.add(json);
            }
            return funcList;
        } else {
            return null;
        }
    }

    public Map<String, Object> addFuncionario(String nome, String cpf, String senha, String email, String login, int nivel, MultipartFile file) {
        if (funcionarioModel.isEmpty()) {

                Funcionario func = new Funcionario(0, nome, cpf, senha, email, login, nivel);
                if (funcionarioModel.gravar(func) != null) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", func.getId());
                    json.put("nome", func.getNome());
                    json.put("cpf", func.getCpf());
                    json.put("email", func.getEmail());
                    json.put("login", func.getLogin());
                    json.put("nivel", func.getNivel());
                    json.put("senha", func.getSenha());
                    return json;
                } else {
                    Map<String, Object> json = new HashMap<>();
                    json.put("erro", "Erro ao cadastrar o funcionário");
                    return json;
                }
        } else {
            Map<String, Object> json = new HashMap<>();
            json.put("erro", "Já existe um funcionário cadastrado");
            return json;
        }
    }

    public Map<String, Object> updtFuncionario(String nome, String cpf, String senha, String email, String login, int nivel, MultipartFile file) {
        if (funcionarioModel.deletarFuncionario()) {
                Funcionario func = new Funcionario(0, nome, cpf, senha, email, login, nivel);
                if (funcionarioModel.gravar(func) != null) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", func.getId());
                    json.put("nome", func.getNome());
                    json.put("cpf", func.getCpf());
                    json.put("email", func.getEmail());
                    json.put("login", func.getLogin());
                    json.put("nivel", func.getNivel());
                    json.put("senha", func.getSenha());
                    return json;
                } else {
                    Map<String, Object> json = new HashMap<>();
                    json.put("erro", "Erro ao alterar o funcionário");
                    return json;
                }
        } else {
            Map<String, Object> json = new HashMap<>();
            json.put("erro", "Erro ao deletar o funcionário");
            return json;
        }
    }
}
