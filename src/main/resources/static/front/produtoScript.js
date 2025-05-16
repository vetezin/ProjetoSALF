// Listar produtos
function listarProdutos() {
    fetch("http://localhost:8080/api/produtos")
        .then(response => response.json())
        .then(produtos => {
            
            console.log(produtos);
        })
        .catch(error => console.error("Erro ao listar produtos:", error));
}

window.cadastrarProduto = function() {
    const formData = new FormData();
    const desc = document.getElementById("descricao").value;
    const validade = document.getElementById("dtvalidade").value;
    const valor = document.getElementById("valor").value;
    const categoria = document.getElementById("categoria").value;

    console.log("Dados a enviar:", { desc, validade, valor, categoria }); // ✅

    formData.append("prod_desc", desc);
    formData.append("prod_dtvalid", validade);
    formData.append("prod_valorun", valor);
    formData.append("categoria", categoria);

    fetch("http://localhost:8080/apis/produto", {
        method: "POST",
        body: formData
    })
    if (!response.ok) {
    response.text().then(text => {
        console.error("Erro detalhado da API:", text);
    });
    throw new Error("Erro ao cadastrar");
    }

    .then(json => {
        document.getElementById("mensagem").textContent = "Produto cadastrado com sucesso!";
        document.getElementById("formProduto").reset();
        console.log(json);
    })
    .catch(error => {
        console.error("Erro ao cadastrar produto:", error);
        document.getElementById("mensagem").textContent = "Erro ao cadastrar produto.";
    });
}



