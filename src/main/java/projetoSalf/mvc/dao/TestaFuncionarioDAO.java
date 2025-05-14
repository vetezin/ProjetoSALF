package projetoSalf.mvc.dao;


import projetoSalf.mvc.model.Funcionario;

import java.util.Scanner;

public class TestaFuncionarioDAO {
    public static void main(String[] args) {
        Funcionario func = new Funcionario();
        func.setFuncNome("João da Silva");
        func.setFuncCpf("123.456.789-00");
        func.setFuncSenha("senha123");
        func.setFuncEmail("joao@example.com");
        func.setFuncLogin("joaosilva");
        func.setFuncNivel(1);

        FuncionarioDAO daoF = new FuncionarioDAO();
        daoF.gravar(func);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o email: ");
        String email = scanner.nextLine();


//        Funcionario resultado = daoF.alterarNome("jao",email);
        boolean resultado = daoF.apagarNome(email);

        if (resultado == true) {
            System.out.println("Funcionário alterado com sucesso!");
        } else {
            System.out.println("Erro ao salvar funcionário.");
        }
    }
}
