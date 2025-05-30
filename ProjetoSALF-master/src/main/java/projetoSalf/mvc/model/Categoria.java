package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.CategoriaDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

@Component
public class Categoria {
    @Autowired
    private CategoriaDAO dao;

    private int Id;
    private String desc;

    public Categoria() {}

    public Categoria(int Id, String desc) {
        this.Id = Id;
        this.desc = desc;
    }

    public Categoria(String desc) {
        this.desc = desc;
    }

    public Categoria(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Categoria> consultar(String filtro, Conexao conexao) {
        return dao.get(filtro);
    }

    public Categoria consultar(int id) {
        return dao.get(id);
    }
}
