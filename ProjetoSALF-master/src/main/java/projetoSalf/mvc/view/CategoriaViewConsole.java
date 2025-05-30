package projetoSalf.mvc.view;

import projetoSalf.mvc.dao.CategoriaDAO;
import projetoSalf.mvc.model.Categoria;
import java.util.List;
import java.util.Scanner;

public class CategoriaViewConsole {

    private CategoriaDAO dao;
    private Scanner scanner;

    public CategoriaViewConsole() {
        dao = new CategoriaDAO();
        scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Gerenciar Categorias de Produtos (Console) ---");
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
        Categoria categoria = new Categoria(0, descricao);
        Categoria resultado = dao.gravar(categoria);
        if (resultado != null) {
            System.out.println("✅ Categoria cadastrada com sucesso!");
        } else {
            System.out.println("❌ Erro ao cadastrar categoria.");
        }
    }

    private void listar() {
        List<Categoria> categorias = dao.get("");
        System.out.println("\n--- Lista de Categorias ---");
        for (Categoria c : categorias) {
            System.out.println("Código: " + c.getId() + " | Descrição: " + c.getDesc());
        }
    }

    private void atualizar() {
        listar();
        System.out.print("Digite o código da categoria que deseja atualizar: ");
        int cod = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nova descrição: ");
        String novaDesc = scanner.nextLine();
        Categoria categoria = new Categoria(cod, novaDesc);
        Categoria resultado = dao.alterar(categoria);
        if (resultado != null) {
            System.out.println("✅ Categoria atualizada.");
        } else {
            System.out.println("❌ Erro ao atualizar categoria.");
        }
    }

    private void excluir() {
        listar();
        System.out.print("Digite o código da categoria que deseja excluir: ");
        int cod = scanner.nextInt();
        scanner.nextLine();
        Categoria categoria = dao.get(cod);
        if (categoria != null && dao.apagar(categoria)) {
            System.out.println("✅ Categoria excluída.");
        } else {
            System.out.println("❌ Erro ao excluir categoria.");
        }
    }

    private void buscarPorDescricao() {
        System.out.print("Digite parte da descrição para buscar: ");
        String filtro = scanner.nextLine();
        List<Categoria> resultado = dao.get("");
        System.out.println("\n--- Resultado da Busca ---");
        for (Categoria c : resultado) {
            if (c.getDesc().toLowerCase().contains(filtro.toLowerCase())) {
                System.out.println("Código: " + c.getId() + " | Descrição: " + c.getDesc());
            }
        }
    }
}
