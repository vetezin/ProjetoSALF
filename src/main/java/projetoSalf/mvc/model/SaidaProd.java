package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.SaidaProdDAO;

import java.util.List;

@Component
public class SaidaProd {
    private int produtoCod;
    private int saidaCod;
    private int qtd;

    @Autowired
    private SaidaProdDAO dao;

    public SaidaProd() {}

    public SaidaProd(int produtoCod, int saidaCod, int qtd) {
        this.produtoCod = produtoCod;
        this.saidaCod = saidaCod;
        this.qtd = qtd;
    }

    public int getProdutoCod() {
        return produtoCod;
    }

    public void setProdutoCod(int produtoCod) {
        this.produtoCod = produtoCod;
    }

    public int getSaidaCod() {
        return saidaCod;
    }

    public void setSaidaCod(int saidaCod) {
        this.saidaCod = saidaCod;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }


    public SaidaProd gravar(SaidaProd sp) {
        return dao.gravar(sp);
    }

    public SaidaProd alterar(SaidaProd sp) {
        return dao.alterar(sp);
    }

    public boolean deletar(SaidaProd sp) {
        return dao.apagar(this);
    }



    /*
    public List<SaidaProd> consultarPorSaida(int saidaCod) {
        return dao.getPorSaida(saidaCod);
    }*/
}
