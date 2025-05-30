package projetoSalf.mvc.model;
<<<<<<< HEAD
import java.time.LocalDate;

public class Produto {
    private int prodCod;
    private LocalDate prodDtvalid;
    private String prodDesc;
    private float prodValorun;
    private int catCod;
=======
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

>>>>>>> Geral

    public Produto() {
    }

<<<<<<< HEAD
    public Produto(int prodCod, LocalDate prodDtvalid, String prodDesc, float prodValorun, int catCod) {
        this.prodCod = prodCod;
        this.prodDtvalid = prodDtvalid;
        this.prodDesc = prodDesc;
        this.prodValorun = prodValorun;
        this.catCod = catCod;
    }

    public int getProdCod() {
        return prodCod;
    }

    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }

    public LocalDate getProdDtvalid() {
        return prodDtvalid;
    }

    public void setProdDtvalid(LocalDate prodDtvalid) {
        this.prodDtvalid = prodDtvalid;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public float getProdValorun() {
        return prodValorun;
    }

    public void setProdValorun(float prodValorun) {
        this.prodValorun = prodValorun;
    }

    public int getCatCod() {
        return catCod;
    }

    public void setCatCod(int catCod) {
        this.catCod = catCod;
    }
}

=======
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

    public int getId() {
        return prod_cod;
    }
}
>>>>>>> Geral
