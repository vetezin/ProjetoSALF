package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.dao.CestaBasicaDAO;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/cesta")
@CrossOrigin(origins = "*")
public class CestaBasicaRestController {

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarCesta(@RequestBody Map<String, Object> dados) {
        try {
            Map<String, Object> cestaMap = (Map<String, Object>) dados.get("cesta");
            List<Map<String, Object>> produtosMap = (List<Map<String, Object>>) dados.get("listaProdutos");

            if (cestaMap == null || produtosMap == null || produtosMap.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Dados incompletos.");
            }

            CestaBasica cesta = new CestaBasica();
            cesta.setCb_motivo((String) cestaMap.get("cb_motivo"));
            cesta.setCb_dtcriacao(Date.valueOf((String) cestaMap.get("cb_dtcriacao")));
            cesta.setCb_dtdoacao(Date.valueOf((String) cestaMap.get("cb_dtdoacao")));
            cesta.setCb_codfunc(((Number) cestaMap.get("cb_codfunc")).intValue());
            cesta.setPessoa_carente_pc_cod(((Number) cestaMap.get("cb_pessoacod")).intValue());

            if (!SingletonDB.conectar()) {
                return ResponseEntity.status(500).body("❌ Erro ao conectar no banco.");
            }

            Conexao conexao = SingletonDB.getConexao();

            int novoId = conexao.getMaxPK("cesta_basica", "cb_cod") + 1;
            cesta.setCb_cod(novoId);

            // Inserção no banco
            CestaBasicaDAO dao = new CestaBasicaDAO();
            boolean sucesso = dao.inserir(cesta, conexao);

            if (!sucesso) return ResponseEntity.status(500).body("❌ Falha ao registrar cesta.");

            // Registro dos produtos
            int idCbProd = conexao.getMaxPK("cb_prod", "cb_prod_id");
            for (Map<String, Object> p : produtosMap) {
                idCbProd++;
                conexao.getConnect().createStatement().executeUpdate(
                        "INSERT INTO cb_prod (cb_prod_id, produto_prod_cod, cesta_basica_cb_cod, prod_qtd) VALUES (" +
                                idCbProd + ", " +
                                ((Number) p.get("produto_prod_cod")).intValue() + ", " +
                                novoId + ", " +
                                ((Number) p.get("prod_qtd")).intValue() + ")"
                );
            }

            return ResponseEntity.ok("✅ Cesta registrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro interno.");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CestaBasica>> listar(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String nome) {

        if (!SingletonDB.conectar()) return ResponseEntity.status(500).build();
        Conexao conexao = SingletonDB.getConexao();

        CestaBasicaDAO dao = new CestaBasicaDAO();
        List<CestaBasica> lista = dao.listarCestasFiltrado(conexao, cpf, nome);
        return ResponseEntity.ok(lista);
    }
}
