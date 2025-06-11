package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.DoaProdDAO;
import projetoSalf.mvc.dao.DoacaoDAO;
import projetoSalf.mvc.dao.ProdutoDAO;
import projetoSalf.mvc.model.Doacao;
import projetoSalf.mvc.model.Produto;
import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.util.Conexao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoacaoController {

    @Autowired
    private DoacaoDAO doacaoDAO;

    @Autowired
    private DoaProdDAO doaProdDAO;

    @Autowired
    private ProdutoDAO produtoDAO;


    @Autowired
    private Doacao doacaoModel;

    public boolean registrarDoacaoCompleta(int funcCod, List<DoaProd> produtos) {
        System.out.println("=== Início do registro de doação ===");
        System.out.println("Funcionário código: " + funcCod);

        Conexao conexao = new Conexao();
        Doacao doacao = new Doacao(funcCod);
        int doaCod = doacaoDAO.gravar(doacao);

        if (doaCod <= 0) {
            System.out.println("Erro ao gravar a doação no banco. Código retornado: " + doaCod);
            return false;
        }

        for (DoaProd dp : produtos) {
            dp.setDoacaoDoaCod(doaCod);

            // Correção: buscar o produto pelo ProdutoDAO
            Produto produto = produtoDAO.get(dp.getProdutoProdCod());
            if (produto == null) {
                System.out.println("Produto com código " + dp.getProdutoProdCod() + " não encontrado.");
                doacaoDAO.deletar(doaCod);
                return false;
            }

            dp.setDoaProdCatCod(produto.getCategoria()); // categoria associada ao produto

            boolean ok = doaProdDAO.gravar(dp);
            if (!ok) {
                System.out.println("Erro ao gravar produto da doação. Removendo doação criada.");
                doacaoDAO.deletar(doaCod);
                return false;
            }
        }

        System.out.println("=== Doação completa registrada com sucesso ===");
        return true;
    }

    public List<Map<String, Object>> listarTodasDoacoes(Integer funcCod, String data) {
        Conexao conexao = new Conexao();
        List<Doacao> lista = doacaoModel.consultar(funcCod, data, conexao);

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Doacao d : lista) {
            Map<String, Object> map = new HashMap<>();
            map.put("doacaoCod", d.getDoaCod());
            map.put("funcCod", d.getFuncCod());
            map.put("data", d.getDoaDtentrada());

            List<Map<String, Object>> produtos = new ArrayList<>();
            for (DoaProd dp : d.getProdutos(conexao)) {
                Produto produto = produtoDAO.get(dp.getProdutoProdCod()); // pega a descrição
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("produtoCod", dp.getProdutoProdCod());
                pMap.put("descricao", produto != null ? produto.getProd_desc() : "Produto não encontrado");
                pMap.put("quantidade", dp.getDoaProdQtd());
                pMap.put("categoria", dp.getDoaProdCatCod());
                produtos.add(pMap);
            }

            map.put("produtos", produtos);
            resultado.add(map);
        }

        return resultado;
    }

    public boolean deletarDoacao(int doaCod) {
        boolean revertido = doaProdDAO.reverterDoacao(doaCod);
        if (!revertido) return false;

        return doacaoDAO.deletar(doaCod);
    }

}
