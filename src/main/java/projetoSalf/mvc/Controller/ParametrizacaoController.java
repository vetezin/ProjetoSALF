package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.ParametrizacaoDAO;
import projetoSalf.mvc.model.Parametrizacao;

@Service
public class ParametrizacaoController {

    @Autowired
    private ParametrizacaoDAO PaDAO;

    public boolean salvarOuAtualizar(Parametrizacao PA) {
        if (!PaDAO.existeRegistro(PA)) {
            PaDAO.gravar(PA);
            return true; // Novo
        } else {
            PaDAO.alterar(PA);
            return false; // Atualizado
        }
    }

    public Parametrizacao get(String email) {
        return PaDAO.getRegistro(email);
    }
}
