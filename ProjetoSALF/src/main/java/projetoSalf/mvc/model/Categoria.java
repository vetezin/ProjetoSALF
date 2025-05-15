package projetoSalf.mvc.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Categoria {
    //@Autowired
    //private CategoriaDAO dao

    private int Id;
    private String desc;

    public Categoria(String desc) {
        this.desc = desc;
    }

    public Categoria(int id) {
        Id = id;
    }

    public Categoria() {
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
}
