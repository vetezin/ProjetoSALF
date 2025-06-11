package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.PessoaCarenteDAO;
import projetoSalf.mvc.model.PessoaCarente;

import java.util.*;

@Service
public class PessoaCarenteController {

    @Autowired
    private PessoaCarenteDAO dao;

    public List<Map<String, Object>> listarTodos() {
        List<PessoaCarente> lista = dao.get("");
        List<Map<String, Object>> result = new ArrayList<>();

        for (PessoaCarente pc : lista) {
            Map<String, Object> map = new HashMap<>();
            map.put("pcCod", pc.getPcCod());
            map.put("pcNome", pc.getPcNome());
            map.put("pcCpf", pc.getPcCpf());
            map.put("pcTelefone", pc.getPcTelefone());
            map.put("pcEndereco", pc.getPcEndereco());
            map.put("pcCep", pc.getPcCep());
            result.add(map);
        }

        return result;
    }

    public Map<String, Object> buscarPorId(int id) {
        PessoaCarente pc = dao.get(id);
        Map<String, Object> map = new HashMap<>();

        if (pc != null) {
            map.put("pcCod", pc.getPcCod());
            map.put("pcNome", pc.getPcNome());
            map.put("pcCpf", pc.getPcCpf());
            map.put("pcTelefone", pc.getPcTelefone());
            map.put("pcEndereco", pc.getPcEndereco());
            map.put("pcCep", pc.getPcCep());
        } else {
            map.put("erro", "Pessoa carente não encontrada");
        }

        return map;
    }

    public Map<String, Object> cadastrar(Map<String, Object> dados) {
        Map<String, Object> response = new HashMap<>();

        try {
            PessoaCarente pc = new PessoaCarente();
            pc.setPcNome((String) dados.get("pcNome"));
            pc.setPcCpf((String) dados.get("pcCpf"));
            pc.setPcTelefone((String) dados.get("pcTelefone"));
            pc.setPcEndereco((String) dados.get("pcEndereco"));
            pc.setPcCep((String) dados.get("pcCep"));

            PessoaCarente resultado = dao.gravar(pc);

            if (resultado != null) {
                response.put("status", "ok");
                response.put("mensagem", "Pessoa carente cadastrada com sucesso!");
                response.put("dados", buscarPorId(resultado.getPcCod()));
            } else {
                response.put("status", "erro");
                response.put("mensagem", "Erro ao cadastrar pessoa carente");
            }

        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> atualizar(int id, Map<String, Object> dados) {
        Map<String, Object> response = new HashMap<>();

        try {
            PessoaCarente pc = dao.get(id);
            if (pc == null) {
                response.put("status", "erro");
                response.put("mensagem", "Pessoa carente não encontrada");
                return response;
            }

            pc.setPcNome((String) dados.get("pcNome"));
            pc.setPcCpf((String) dados.get("pcCpf"));
            pc.setPcTelefone((String) dados.get("pcTelefone"));
            pc.setPcEndereco((String) dados.get("pcEndereco"));
            pc.setPcCep((String) dados.get("pcCep"));

            PessoaCarente resultado = dao.alterar(pc);

            if (resultado != null) {
                response.put("status", "ok");
                response.put("mensagem", "Pessoa carente atualizada com sucesso!");
                response.put("dados", buscarPorId(id));
            } else {
                response.put("status", "erro");
                response.put("mensagem", "Erro ao atualizar pessoa carente");
            }

        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> deletar(int id) {
        Map<String, Object> response = new HashMap<>();

        try {
            PessoaCarente pc = dao.get(id);
            if (pc == null) {
                response.put("status", "erro");
                response.put("mensagem", "Pessoa carente não encontrada");
                return response;
            }

            boolean deletado = dao.apagar(pc);

            if (deletado) {
                response.put("status", "ok");
                response.put("mensagem", "Pessoa carente removida com sucesso!");
            } else {
                response.put("status", "erro");
                response.put("mensagem", "Erro ao remover pessoa carente");
            }

        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro: " + e.getMessage());
        }

        return response;
    }
}