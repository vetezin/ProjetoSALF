





document.addEventListener("DOMContentLoaded", listarProdutos);

async function listarProdutos() {
    const tbody = document.querySelector("#tabelaProdutos tbody");
    tbody.innerHTML = ""; // limpa a tabela antes de inserir

    try {
        const response = await fetch("http://localhost:8080/apis/produto");
        const produtos = await response.json();

        if (!Array.isArray(produtos)) {
            tbody.innerHTML = `<tr><td colspan="6">${produtos.mensagem || "Erro ao buscar produtos."}</td></tr>`;
            return;
        }

        if (produtos.length === 0) {
            tbody.innerHTML = "<tr><td colspan='6'>Nenhum produto encontrado.</td></tr>";
            return;
        }

        produtos.forEach(produto => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${produto.id}</td>
                <td>${produto.nome}</td>
                <td>${produto.data}</td>
                <td>R$ ${produto.preco.toFixed(2)}</td>
                <td>${produto.categoria.desc}</td>
                <td class="acoes">
                    <button class="editar" onclick="editarProduto(${produto.id})">Editar</button>
                    <button class="excluir" onclick="excluirProduto(${produto.id})">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (erro) {
        console.error("Erro ao buscar produtos:", erro);
        tbody.innerHTML = "<tr><td colspan='6'>Erro ao buscar produtos. Verifique a API.</td></tr>";
    }
}

async function buscarPorCategoria() {
    const categoriaInput = document.getElementById('categoriaInput').value;
    const searchResults = document.getElementById('searchResults');

    if (!categoriaInput || isNaN(categoriaInput)) {
        searchResults.innerHTML = '<p style="color: red;">Digite um ID de categoria válido.</p>';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/apis/produto/categoria/${categoriaInput}`);

        if (!response.ok) {
            const erro = await response.json();
            searchResults.innerHTML = `<p style="color: red;">${erro.mensagem}</p>`;
            return;
        }

        const produtos = await response.json();

        if (produtos.length === 0) {
            searchResults.innerHTML = '<p>Nenhum produto encontrado para essa categoria.</p>';
            return;
        }

        let html = '<ul>';
        produtos.forEach(produto => {
            html += `
                <li>
                    <strong>${produto.nome}</strong><br/>
                    Preço: R$ ${produto.preco}<br/>
                    Validade: ${produto.data}<br/>
                    Categoria: ${produto.categoria.desc} (ID: ${produto.categoria.id})
                </li><hr/>
            `;
        });
        html += '</ul>';

        searchResults.innerHTML = html;

    } catch (error) {
        searchResults.innerHTML = `<p style="color: red;">Erro ao buscar produtos: ${error.message}</p>`;
    }
}

async function buscarPorValorMenor() {
    let valorInput = document.getElementById('valorMaxInput').value;
    let searchResults = document.getElementById('searchResults');

    // Limpar resultados anteriores
    searchResults.innerHTML = "";

    if (!valorInput || isNaN(valorInput) || Number(valorInput) <= 0) {
        searchResults.innerHTML = '<p style="color: red;">Digite um valor máximo válido maior que zero.</p>';
        return;
    }

    let valor = Number(valorInput);

    try {
        let response = await fetch(`http://localhost:8080/apis/produto/menor-que/${valor}`);

        if (!response.ok) {
            let erro = await response.json();
            searchResults.innerHTML = `<p style="color: red;">${erro.mensagem}</p>`;
            return;
        }

        let produtos = await response.json();

        if (produtos.length === 0) {
            searchResults.innerHTML = `<p>Nenhum produto encontrado com valor menor que R$ ${valor.toFixed(2)}.</p>`;
            return;
        }

        let html = '<ul>';
        produtos.forEach(produto => {
            html += `
                <li>
                    <strong>${produto.nome}</strong><br/>
                    Preço: R$ ${produto.preco.toFixed(2)}<br/>
                    Validade: ${produto.data}<br/>
                    Categoria: ${produto.categoria.desc}
                </li><br/>
            `;
        });
        html += '</ul>';

        searchResults.innerHTML = html;

    } catch (error) {
        searchResults.innerHTML = `<p style="color: red;">Erro ao buscar produtos: ${error.message}</p>`;
    }
}
function mostrarFormulario() {
  const modal = document.getElementById("modal");
  modal.style.display = "block";
}

function fecharModal() {
  const modal = document.getElementById("modal");
  modal.style.display = "none";

  // Limpa o formulário ao fechar
  document.getElementById("formProduto").reset();
}


function cadastrarProduto(event) {
  event.preventDefault();

  const form = document.getElementById('formProduto');
  if (!form.checkValidity()) {
    form.classList.add('was-validated');
    return false;
  }

  const dados = {
    prod_desc: document.getElementById("descricao").value,
    prod_dtvalid: document.getElementById("dtvalidade").value,
    prod_valorun: parseFloat(document.getElementById("valor").value),
    categoria: parseInt(document.getElementById("categoria").value),
  };

  fetch("http://localhost:8080/apis/produto", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams(dados)
  })
    .then(response => response.json())
    .then(res => {
      console.log(res);
      alert(res.mensagem || "Produto cadastrado!");
      form.reset();
      form.classList.remove("was-validated");
      fecharModal();
      listarProdutos(); // atualizar tabela após cadastrar
    })
    .catch(error => {
      console.error("Erro ao cadastrar:", error);
      alert("Erro ao cadastrar produto.");
    });

  return false;
}
