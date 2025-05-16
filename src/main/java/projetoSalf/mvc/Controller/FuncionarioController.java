package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.FuncionarioDAO;
import projetoSalf.mvc.model.Funcionario;

@Service
public class FuncionarioController {

    private FuncionarioDAO dao = new FuncionarioDAO();


    public Funcionario login(String email) {
        if (email == null ) {
            return null;
        }
        return dao.buscarPorEmail(email);
    }


    }

