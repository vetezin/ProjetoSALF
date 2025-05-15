package projetoSalf.mvc.dao;


import projetoSalf.mvc.model.Funcionario;

import javax.xml.transform.Source;
import java.util.List;
import java.util.Scanner;

public class TestaFuncionarioDAO {
    public static void main(String[] args) {
        Funcionario func = new Funcionario();
        func.setFuncNome("Victor");
        func.setFuncEmail("peterson@hotmail.com");
        func.setFuncCpf("1234567");
        func.setFuncLogin("Peterson");
        func.setFuncSenha("123456");
        func.setFuncNivel(1);


        FuncionarioDAO daoF = new FuncionarioDAO();
//        Funcionario resultado = daoF.gravar(func);


//          boolean resultado = daoF.apagar(func);
//
        Funcionario resultado = daoF.get(10);

        List<Funcionario> resultado2 = daoF.get("Victor");



        if(!resultado2.isEmpty()){
            System.out.println(resultado2);
        }
//        if (resultado !=null) {
//            System.out.println("Funcionário alterado com sucesso!");
//            System.out.println("Nome: " + resultado.getFuncNome() + "\n" + "Email: " + resultado.getFuncEmail());
//        } else {
//            System.out.println("Erro ao salvar funcionário.");
//        }


//        if (resultado != false) {
//            System.out.println("Funcionário alterado com sucesso!");
//        } else {
//            System.out.println("Erro ao salvar funcionário.");
//        }
    }
}
