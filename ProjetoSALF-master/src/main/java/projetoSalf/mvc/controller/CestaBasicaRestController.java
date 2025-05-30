package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/cesta")
public class CestaBasicaRestController {

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarCesta(@RequestBody Map<String, Object> dados) {
        try {
            Map<String, Object> cestaMap = (Map<String, Object>) dados.get("cesta");
            List<Map<String, Object>> produtosMap = (List<Map<String, Object>>) dados.get("listaProdutos");

            CestaBasica cesta = new CestaBasica();
            cesta.setCbMotivo((String) cestaMap.get("cb_motivo"));
            cesta.setCbDtCriacao(LocalDate.parse((String) cestaMap.get("cb_dtcriacao")));
            cesta.setCbDtDoacao(LocalDate.parse((String) cestaMap.get("cb_dtdoacao")));
            cesta.setFuncCod((int) cestaMap.get("cb_codfunc"));
            cesta.setPessoaCarentePcCod((int) cestaMap.get("cb_pessoacod"));

            List<CbProd> produtos = produtosMap.stream().map(p -> {
                CbProd prod = new CbProd();
                prod.setProdutoProdCod((int) p.get("produto_prod_cod"));
                prod.setProdQtd((int) p.get("prod_qtd"));
                return prod;
            }).toList();

            // Conecta ao banco
            Conexao conexao = new Conexao();
            conexao.conectar("jdbc:postgresql://localhost/", "salf", "postgres", "postgres");

            // Passa a conexão real (java.sql.Connection)
            CestaBasicaController controller = new CestaBasicaController();
            boolean sucesso = controller.registrarCesta(cesta, produtos, conexao.getConnect());

            if (sucesso) {
                return ResponseEntity.ok("Cesta registrada com sucesso!");
            } else {
                return ResponseEntity.status(500).body("Falha ao registrar cesta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao registrar cesta básica: " + e.getMessage());
        }
    }
}
