package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.ParametrizacaoDAO;
import projetoSalf.mvc.model.Parametrizacao;

@Service
public class ParametrizacaoController {

    public ParametrizacaoController() {
        this.PaDAO = new ParametrizacaoDAO();
    }


    private ParametrizacaoDAO PaDAO;

    public boolean salvarOuAtualizar(Parametrizacao PA) {

        if(PaDAO.existeRegistro(PA) == false){
            PaDAO.gravar(PA);
            return true;
        }
        else
            PaDAO.alterar(PA);

        return false;
    }

    public Parametrizacao get(String email) {
        return PaDAO.getRegistro(email);
    }




}
