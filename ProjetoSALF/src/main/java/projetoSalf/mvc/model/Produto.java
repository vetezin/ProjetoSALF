package projetoSalf.mvc.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.dao.ProdutoDAO;

import java.util.List;

@Component
public class Produto {
    //Model representa as entidades do sistema, sao as classes e seus atributos getter e setters

    @Autowired
    private ProdutoDAO dao;

    private int prod_cod;
    private String prod_dtvalid;
    private String prod_desc;
    private float prod_valorun;

    private int cat_cod;


    public Produto() {
    }

    public Produto( int prod_cod, String prod_dtvalid, String prod_desc, float prod_valorun, int cat_cod) {

        this.prod_cod = prod_cod;
        this.prod_dtvalid = prod_dtvalid;
        this.prod_desc = prod_desc;
        this.prod_valorun = prod_valorun;
        this.cat_cod = cat_cod;
    }

    public Produto(String prod_dtvalid, String prod_desc, float prod_valorun, int cat_cod) {

        this.prod_dtvalid = prod_dtvalid;
        this.prod_desc = prod_desc;
        this.prod_valorun = prod_valorun;
        this.cat_cod = cat_cod;
    }



    public int getProd_cod() {
        return prod_cod;
    }

    public void setProd_cod(int prod_cod) {
        this.prod_cod = prod_cod;
    }

    public String getProd_dtvalid() {
        return prod_dtvalid;
    }

    public void setProd_dtvalid(String prod_dtvalid) {
        this.prod_dtvalid = prod_dtvalid;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public float getProd_valorun() {
        return prod_valorun;
    }

    public void setProd_valorun(float prod_valorun) {
        this.prod_valorun = prod_valorun;
    }

    public int getCategoria() {
        return cat_cod;
    }

    public void setCategoria(int cat_cod) {
        this.cat_cod = cat_cod;
    }

    public List<Produto> consultar(String filtro, Conexao conexao){
        return dao.get(filtro);
    }
    public Produto consultar(int id){
        return dao.get(id);
    }
    public boolean isEmpty(){
        return dao.get("").isEmpty();
    }
    public Produto gravar(Produto produto){
        return dao.gravar(produto);
    }
    public boolean deletarProduto(Produto produto){
        return dao.apagar(produto);
    }
    public Produto alterar(Produto produto) {
        return dao.alterar(produto);
    }
}
