package projetoSalf.mvc.view;

import projetoSalf.mvc.dao.CategoriaProdutoDAO;
import projetoSalf.mvc.model.CategoriaProduto;
import projetoSalf.mvc.util.SingletonDB;


import java.util.List;
import java.util.Scanner;

public class CategoriaProdutoView {

    private CategoriaProdutoDAO dao;
    private Scanner scanner;

    public CategoriaProdutoView() {
        dao = new CategoriaProdutoDAO();
        scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Categorias de Produtos ---");
            System.out.println("1. Cadastrar nova categoria");
            System.out.println("2. Listar todas as categorias");
            System.out.println("3. Atualizar categoria");
            System.out.println("4. Excluir categoria");
            System.out.println("5. Buscar por descrição");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> excluir();
                case 5 -> buscarPorDescricao();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.print("Descrição da nova categoria: ");
        String descricao = scanner.nextLine();
        CategoriaProduto categoria = new CategoriaProduto(0, descricao);
        boolean sucesso = dao.inserir(categoria);
        if (sucesso) {
            System.out.println("Categoria cadastrada com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar categoria.");
        }
    }

    private void listar() {
        List<CategoriaProduto> categorias = dao.consultarTodos();
        System.out.println("\n--- Lista de Categorias ---");
        for (CategoriaProduto c : categorias) {
            System.out.println(c);
        }
    }

    private void atualizar() {
        listar();
        System.out.print("Digite o código da categoria que deseja atualizar: ");
        int cod = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nova descrição: ");
        String novaDesc = scanner.nextLine();
        CategoriaProduto categoria = new CategoriaProduto(cod, novaDesc);
        boolean sucesso = dao.atualizar(categoria);
        if (sucesso) {
            System.out.println("Categoria atualizada.");
        } else {
            System.out.println("Erro ao atualizar categoria.");
        }
    }

    private void excluir() {
        listar();
        System.out.print("Digite o código da categoria que deseja excluir: ");
        int cod = scanner.nextInt();
        scanner.nextLine();
        boolean sucesso = dao.excluir(cod);
        if (sucesso) {
            System.out.println("Categoria excluída.");
        } else {
            System.out.println("Erro ao excluir categoria.");
        }
    }

    private void buscarPorDescricao() {
        System.out.print("Digite parte da descrição para buscar: ");
        String filtro = scanner.nextLine();
        List<CategoriaProduto> resultado = dao.consultarTodos();
        System.out.println("\n--- Resultado da Busca ---");
        for (CategoriaProduto c : resultado) {
            if (c.getDesc().toLowerCase().contains(filtro.toLowerCase())) {
                System.out.println(c);
            }
        }
    }
}
