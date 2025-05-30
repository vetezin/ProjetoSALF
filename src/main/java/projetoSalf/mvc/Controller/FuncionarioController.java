package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import projetoSalf.mvc.dao.FuncionarioDAO;
import projetoSalf.mvc.model.Funcionario;

@RestController
public class FuncionarioController {

    private FuncionarioDAO dao = new FuncionarioDAO();


    public Funcionario login(String email) {
        if (email == null ) {
            return null;
        }
        return dao.buscarPorEmail(email);
    }


    }

