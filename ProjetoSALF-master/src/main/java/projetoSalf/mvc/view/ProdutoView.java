    package projetoSalf.mvc.view;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import projetoSalf.mvc.controller.ProdutoController;
    import projetoSalf.mvc.util.Mensagem;

    import java.util.List;
    import java.util.Map;


    @CrossOrigin
    @RestController
    @RequestMapping("apis/produto")
    public class ProdutoView {

        @Autowired
        private ProdutoController produtoController;

        @GetMapping
        public ResponseEntity<Object> getAll() {

            List<Map<String, Object>> lista = produtoController.getProd();
            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Nenhum produto encontrado."));
        }

        @GetMapping("{id}")
        public ResponseEntity<Object> getId(@PathVariable int id) {
            Map<String, Object> produto = produtoController.getProd(id);
            if (produto != null)
                return ResponseEntity.ok(produto);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Produto não encontrado."));
        }


        @GetMapping("/categoria/{cat_cod}")
        public ResponseEntity<Object> getByCategoria(@PathVariable("cat_cod") int cat_cod) {
            List<Map<String, Object>> produtos = produtoController.getProdutosPorCategoria(cat_cod);

            if (produtos != null && !produtos.isEmpty()) {
                return ResponseEntity.ok(produtos);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Nenhum produto encontrado para essa categoria."));
            }
        }

        @GetMapping("/busca-nome")
        public ResponseEntity<Object> getPorNome(@RequestParam("termo") String termo) {
            List<Map<String, Object>> produtos = produtoController.getProdutosPorNome(termo);

            if (produtos != null && !produtos.isEmpty()) {
                return ResponseEntity.ok(produtos);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Nenhum produto encontrado com o termo informado."));
            }
        }


        @GetMapping("/ordenados")
        public ResponseEntity<Object> getProdutosOrdenados() {
            List<Map<String, Object>> lista = produtoController.getProdutosOrdenados();
            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Nenhum produto encontrado."));
        }



        //fazer o getID
        @PostMapping
        public ResponseEntity<Object> addProduto(

                @RequestParam("prod_desc") String descricao,
                @RequestParam("prod_dtvalid") String validade,

                @RequestParam("prod_valorun") float valor,
                @RequestParam("categoria") int cat_cod
        ) {
            Map<String, Object> json = produtoController.addProd(descricao, validade, valor, cat_cod);
            if (json.get("erro") == null)
                return ResponseEntity.ok(new Mensagem("Produto cadastrado com sucesso!"));
            else
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }


        @PutMapping
        public ResponseEntity<Object> updtProduto(
                @RequestParam("prod_cod") int cod,
                @RequestParam("prod_dtvalid") String validade,
                @RequestParam("prod_desc") String descricao,

                @RequestParam("prod_valorun") float valor,
                @RequestParam("categoria") int cat_cod
        ) {
            Map<String, Object> json = produtoController.updtProd(cod, validade , descricao, valor, cat_cod);
            if (json.get("erro") == null)
                return ResponseEntity.ok(new Mensagem("Produto alterado com sucesso!"));
            else
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deletarProduto(@PathVariable("id") int id) {
            Map<String, Object> json = produtoController.deletarProduto(id);

            if (json.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

    }
