// Listar produtos

/*
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

*/

document.addEventListener("DOMContentLoaded", listarProdutos);

async function listarProdutos() {
    const resultadoContainer = document.getElementById("searchResults");

    try {
        const response = await fetch("http://localhost:8080/apis/produto");
        const produtos = await response.json();

        if (!Array.isArray(produtos)) {
            resultadoContainer.innerHTML = `<p>${produtos.mensagem || "Erro ao buscar produtos."}</p>`;
            return;
        }

        if (produtos.length === 0) {
            resultadoContainer.innerHTML = "<p>Nenhum produto encontrado.</p>";
            return;
        }

        produtos.forEach(prod => {
            const item = document.createElement("div");
            item.classList.add("produto-item");
            item.innerHTML = `
                <p><strong>ID:</strong> ${prod.id}</p>
                <h3>${prod.nome}</h3>
                <p>Preço: R$ ${prod.preco.toFixed(2)}</p>
                <p>Validade: ${prod.data}</p>
                <p>Categoria: ${prod.categoria.desc}</p>
                <hr/>
            `;
            resultadoContainer.appendChild(item);
        });

    } catch (erro) {
        console.error("Erro ao buscar produtos:", erro);
        resultadoContainer.innerHTML = "<p>Erro ao buscar produtos. Verifique a API.</p>";
    }
}
