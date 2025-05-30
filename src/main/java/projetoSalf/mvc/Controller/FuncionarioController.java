package projetoSalf.mvc.Controller;

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
    public Map<String, Object> buscarFuncionarioPorEmail(String email) {
        Funcionario func = funcionarioModel.buscarPorEmail(email);

        if (func != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", func.getId());
            json.put("nome", func.getNome());
            json.put("cpf", func.getCpf());
            json.put("email", func.getEmail());
            json.put("login", func.getLogin());
            json.put("nivel", func.getNivel());
            json.put("senha", func.getSenha()); // para comparar no JS
            return json;
        } else {
            return null;
        }
    }


    public Map<String, Object> getFuncionario(int id) {
        Conexao conexao = new Conexao();
        Funcionario func = funcionarioModel.consultar(id, conexao);
        Map<String, Object> json = new HashMap<>();
        json.put("id", func.getId());
        json.put("nome", func.getNome());
        json.put("cpf", func.getCpf());
        json.put("email", func.getEmail());
        json.put("login", func.getLogin());
        json.put("nivel", func.getNivel());
        json.put("senha", func.getSenha());
        return json;
    }

    public Map<String, Object> addFuncionario(String nome, String cpf, String senha, String email, String login, int nivel) {


        Conexao conexao = new Conexao();
        Funcionario novo = new Funcionario(nome, cpf, senha, email, login, nivel);
        Funcionario gravado = funcionarioModel.gravar(novo);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getId());
            json.put("nome", gravado.getNome());
            json.put("cpf", gravado.getCpf());
            json.put("email", gravado.getEmail());
            json.put("login", gravado.getLogin());
            json.put("nivel", gravado.getNivel());
            json.put("senha", gravado.getSenha());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }

    }

    public Map<String, Object> updtFuncionario(int cod, String nome, String cpf, String senha, String email, String login, int nivel) {

        Funcionario existente = funcionarioModel.consultar(cod);
        if (existente == null) {
            return Map.of("erro", "Funcionario não encontrado");
        }

        existente.setFunc_cod(cod);
        existente.setFunc_cpf(cpf);
        existente.setFunc_email(email);
        existente.setFunc_login(login);
        existente.setFunc_nivel(nivel);
        existente.setFunc_nome(nome);
        existente.setFunc_senha(senha);

        Funcionario atualizado = funcionarioModel.update(existente);
        if (atualizado != null) {
            return Map.of(
                    "id",        atualizado.getId(),
                    "nome",      atualizado.getNome(),
                    "cpf",     atualizado.getCpf(),
                    "email",      atualizado.getEmail(),
                    "senha",      atualizado.getSenha(),
                    "login",      atualizado.getLogin(),
                    "nivel",      atualizado.getNivel()
            );
        }
        return Map.of("erro", "Erro ao atualizar o produto");
    }

    public Map<String, Object> deletarFuncionario(int  id) {
        Funcionario func = funcionarioModel.consultar(id); // Busca o produto pelo ID
        if (func == null) {
            return Map.of("erro", "Funcionario não encontrado");
        }

        boolean deletado = funcionarioModel.deletarFuncionario(func); // Deleta o produto

        if (deletado) {
            return Map.of("mensagem", "Funcionario removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover o funcionario");
        }
    }
}