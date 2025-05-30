document.addEventListener("DOMContentLoaded", () => {
  listarProdutos();
  carregarCategorias(); 
});

function formatarValor(e) {
  let valor = e.target.value.replace(/\D/g, ""); 

  if (!valor) {
    e.target.value = "";
    return;
  }

  valor = (parseInt(valor, 10) / 100).toFixed(2); 

  // ponto por virgula
  valor = valor.replace(".", ",");

  // milhar
  valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");

  e.target.value = `R$ ${valor}`;
}

function aplicarMascaraValor() {
  let inputValor = document.getElementById("valor");

  
  inputValor.removeEventListener("input", formatarValor);
  inputValor.addEventListener("input", formatarValor);

  
  let form = document.getElementById("formProduto");
  form.addEventListener(
    "submit",
    () => {
      let campo = document.getElementById("valor");
      campo.value = campo.value
        .replace("R$ ", "")
        .replace(/\./g, "")
        .replace(",", ".");
    },
    { once: true }
  );
}

function limparValorFormatado(valorFormatado) {
  if (!valorFormatado) return 0;

  
  let valorLimpo = valorFormatado.replace(/[^\d.,-]/g, "");

  
  valorLimpo = valorLimpo.replace(/\./g, "");

   
  valorLimpo = valorLimpo.replace(/,/g, ".");

  let numero = parseFloat(valorLimpo);

  return isNaN(numero) ? 0 : numero;
}

function mostrarToast(mensagem, duracao = 3000) {
  let toast = document.getElementById("mensagemToast");
  toast.textContent = mensagem;
  toast.style.display = "block";

  setTimeout(() => {
    toast.style.display = "none";
  }, duracao);
}

function formatarData(dataISO) {
  if (!dataISO) return ""; 
  let [ano, mes, dia] = dataISO.split("-");
  return `${dia}/${mes}/${ano}`;
}

async function listarProdutos() {
  let tbody = document.querySelector("#tabelaProdutos tbody");
  tbody.innerHTML = ""; 

  try {
    let response = await fetch("http://localhost:8080/apis/produto");
    let produtos = await response.json();

    if (!Array.isArray(produtos)) {
      tbody.innerHTML = `<tr><td colspan="6">${
        produtos.mensagem || "Erro ao buscar produtos."
      }</td></tr>`;
      return;
    }

    if (produtos.length === 0) {
      tbody.innerHTML =
        "<tr><td colspan='6'>Nenhum produto encontrado.</td></tr>";
      return;
    }

    produtos.forEach((produto) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
                <td>${produto.id}</td>
                <td>${produto.nome}</td>

                <td>${formatarData(produto.data)}</td>
                <td>R$ ${parseFloat(produto.preco).toFixed(2)}</td>

                <td>${produto.categoria.desc}</td>
                <td class="acoes">
                    <button class="editar" onclick="editarProduto(${
                      produto.id
                    })">Editar</button>
                    <button class="excluir" onclick="apagarProduto(${
                      produto.id
                    })">Excluir</button>
                </td>
            `;
      tbody.appendChild(tr);
    });
  } catch (erro) {
    console.error("Erro ao buscar produtos:", erro);
    tbody.innerHTML =
      "<tr><td colspan='6'>Erro ao buscar produtos. Verifique a API.</td></tr>";
  }
}

async function buscarPorCategoria() {
  let categoriaInput = document.getElementById("categoriaInput").value;
  let searchResults = document.getElementById("searchResults");

  if (!categoriaInput || isNaN(categoriaInput)) {
    searchResults.innerHTML =
      '<p style="color: red;">Digite um ID de categoria válido.</p>';
    return;
  }

  try {
    let response = await fetch(
      `http://localhost:8080/apis/produto/categoria/${categoriaInput}`
    );

    if (!response.ok) {
      let erro = await response.json();
      searchResults.innerHTML = `<p style="color: red;">${erro.mensagem}</p>`;
      return;
    }

    let produtos = await response.json();

    if (produtos.length === 0) {
      searchResults.innerHTML =
        "<p>Nenhum produto encontrado para essa categoria.</p>";
      return;
    }

    let html = "<ul>";
    produtos.forEach((produto) => {
      html += `
                <li>
                    <strong>${produto.nome}</strong><br/>
                    Preço: R$ ${produto.preco}<br/>
                    Validade: ${produto.data}<br/>
                    Categoria: ${produto.categoria.desc} (ID: ${produto.categoria.id})
                </li><hr/>
            `;
    });
    html += "</ul>";

    searchResults.innerHTML = html;
  } catch (error) {
    searchResults.innerHTML = `<p style="color: red;">Erro ao buscar produtos: ${error.message}</p>`;
  }
}

async function buscarPorValorMenor() {
  let valorInput = document.getElementById("valorMaxInput").value;
  let searchResults = document.getElementById("searchResults");

 
  searchResults.innerHTML = "";

  if (!valorInput || isNaN(valorInput) || Number(valorInput) <= 0) {
    searchResults.innerHTML =
      '<p style="color: red;">Digite um valor máximo válido maior que zero.</p>';
    return;
  }

  let valor = Number(valorInput);

  try {
    let response = await fetch(
      `http://localhost:8080/apis/produto/menor-que/${valor}`
    );

    if (!response.ok) {
      let erro = await response.json();
      searchResults.innerHTML = `<p style="color: red;">${erro.mensagem}</p>`;
      return;
    }

    let produtos = await response.json();

    if (produtos.length === 0) {
      searchResults.innerHTML = `<p>Nenhum produto encontrado com valor menor que R$ ${valor.toFixed(
        2
      )}.</p>`;
      return;
    }

    let html = "<ul>";
    produtos.forEach((produto) => {
      html += `
                <li>
                    <strong>${produto.nome}</strong><br/>
                    Preço: R$ ${produto.preco.toFixed(2)}<br/>
                    Validade: ${produto.data}<br/>
                    Categoria: ${produto.categoria.desc}
                </li><br/>
            `;
    });
    html += "</ul>";

    searchResults.innerHTML = html;
  } catch (error) {
    searchResults.innerHTML = `<p style="color: red;">Erro ao buscar produtos: ${error.message}</p>`;
  }
}
function mostrarFormulario() {
  let modal = document.getElementById("modal");
  modal.style.display = "block";

  aplicarMascaraValor();
}

function fecharModal() {
  let modal = document.getElementById("modal");
  modal.style.display = "none";

  let form = document.getElementById("formProduto");
  form.reset();

  let campoValor = document.getElementById("valor");
  campoValor.value = "";
}

function cadastrarProduto(event) {
  event.preventDefault();

  let form = document.getElementById("formProduto");
  if (!form.checkValidity()) {
    form.classList.add("was-validated");
    return false;
  }

  let valorRaw = document.getElementById("valor").value;
  let valorNumerico = limparValorFormatado(valorRaw);

  let dados = {
    prod_desc: document.getElementById("descricao").value,
    prod_dtvalid: document.getElementById("dtvalidade").value,
    prod_valorun: valorNumerico,
    categoria: parseInt(document.getElementById("categoria").value),
  };

  fetch("http://localhost:8080/apis/produto", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams(dados),
  })
    .then((response) => response.json())
    .then((res) => {
      console.log(res);
      mostrarToast(res.mensagem || "Produto cadastrado");
      form.reset();
      form.classList.remove("was-validated");
      fecharModal();
      listarProdutos();
    })
    .catch((error) => {
      console.error("Erro ao cadastrar:", error);
      mostrarToast("Erro ao cadastrar produto");
    });

  return false;
}

async function carregarCategorias() {
  let selectCategoria = document.getElementById("categoria");

  try {
    let response = await fetch("http://localhost:8080/apis/categoria");
    let categorias = await response.json();

    if (!Array.isArray(categorias)) {
      console.error("Resposta de categoria inválida", categorias);
      return;
    }

    categorias.forEach((cat) => {
      let option = document.createElement("option");
      option.value = cat.id;
      option.textContent = cat.desc;
      selectCategoria.appendChild(option);
    });
  } catch (erro) {
    console.error("Erro ao carregar categorias:", erro);
  }
}

function apagarProduto(id) {
  if (!id) {
    alert("ID do produto não informado.");
    return;
  }

  fetch(`http://localhost:8080/apis/produto/${id}`, {
    method: "DELETE",
  })
    .then((response) =>
      response.json().then((data) => ({ status: response.status, body: data }))
    )
    .then(({ status, body }) => {
      if (status === 200) {
        mostrarToast(body.mensagem || "Produto apagado com sucesso!");
        listarProdutos(); 
      } else {
        mostrarToast(body.mensagem || "Erro ao apagar produto");
      }
    })
    .catch((error) => {
      console.error("Erro ao apagar produto:", error);
      mostrarToast(body.mensagem || "Erro ao apagar produto");
    });
}

function abrirModal(idModal) {
  let modal = document.getElementById(idModal);
  if (modal) {
    modal.style.display = "block";
  } else {
    console.warn(`Modal com id "${idModal}" não encontrado.`);
  }
}

function fecharModal(idModal) {
  let modal = document.getElementById(idModal);
  if (modal) {
    modal.style.display = "none";
  }
}

function formatarValorEdicao(e) {
  let valor = e.target.value.replace(/\D/g, "");

  if (!valor) {
    e.target.value = "";
    return;
  }

  valor = (parseInt(valor, 10) / 100).toFixed(2);
  valor = valor.replace(".", ",");
  valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
  e.target.value = `R$ ${valor}`;
}

function aplicarMascaraValorEdicao() {
  let inputValor = document.getElementById("valorEdicao");
  if (!inputValor) {
    console.warn("Campo #valorEdicao não encontrado.");
    return;
  }

  inputValor.removeEventListener("input", formatarValorEdicao);
  inputValor.addEventListener("input", formatarValorEdicao);

  let form = document.getElementById("formEditarProduto");
  if (form) {
    form.addEventListener(
      "submit",
      () => {
        let campo = document.getElementById("valorEdicao");
        campo.value = campo.value
          .replace("R$ ", "")
          .replace(/\./g, "")
          .replace(",", ".");
      },
      { once: true }
    );
  }
}

function atualizarProduto(event) {
  event.preventDefault();

  let form = document.getElementById("formEditarProduto");
  if (!form.checkValidity()) {
    form.classList.add("was-validated");
    return false;
  }

  let id = document.getElementById("idEdicao").value;
  let valorRaw = document.getElementById("valorEdicao").value;
  let valorNumerico = limparValorFormatado(valorRaw);

  let dados = new URLSearchParams({
    prod_cod: id, 
    prod_desc: document.getElementById("descricaoEdicao").value,
    prod_dtvalid: document.getElementById("dtvalidadeEdicao").value,
    prod_valorun: valorNumerico,
    categoria: document.getElementById("categoriaEdicao").value,
  });

  fetch(`http://localhost:8080/apis/produto?${dados.toString()}`, {
    method: "PUT",
  })
    .then((response) => response.json())
    .then((res) => {
      mostrarToast(res.mensagem || "Produto atualizado");
      form.reset();
      form.classList.remove("was-validated");
      fecharModal("modalEdicao");
      listarProdutos();
    })
    .catch((error) => {
      console.error("Erro ao atualizar:", error);
      mostrarToast("Erro ao atualizar produto");
    });

  return false;
}

async function carregarCategoriasEdicao(categoriaSelecionadaId = null) {
  let selectCategoria = document.getElementById("categoriaEdicao");
  selectCategoria.innerHTML = ""; 

  try {
    let response = await fetch("http://localhost:8080/apis/categoria");
    let categorias = await response.json();

    if (!Array.isArray(categorias)) {
      console.error("Resposta de categoria inválida", categorias);
      return;
    }

    categorias.forEach((cat) => {
      let option = document.createElement("option");
      option.value = cat.id;
      option.textContent = cat.desc;

      
      if (categoriaSelecionadaId && cat.id === categoriaSelecionadaId) {
        option.selected = true;
      }

      selectCategoria.appendChild(option);
    });
  } catch (erro) {
    console.error("Erro ao carregar categorias para edição:", erro);
  }
}

async function editarProduto(id) {
  try {
    let response = await fetch(`http://localhost:8080/apis/produto/${id}`);
    if (!response.ok) throw new Error("Produto não encontrado");

    let produto = await response.json();

    
    document.getElementById("idEdicao").value = produto.id;
    document.getElementById("descricaoEdicao").value =
      produto.nome || produto.descricao;

    
    if (produto.data) {
      document.getElementById("dtvalidadeEdicao").value = produto.data;
    }

    
    document.getElementById("valorEdicao").value = `R$ ${parseFloat(
      produto.preco
    )
      .toFixed(2)
      .replace(".", ",")}`;
    aplicarMascaraValorEdicao();

   
    await carregarCategoriasEdicao(produto.categoria.id);

    abrirModal("modalEdicao");
  } catch (error) {
    console.error("Erro ao carregar produto para edição:", error);
    mostrarToast("Erro ao carregar produto para edição");
  }
}

async function buscarPorNome() {
  let termo = document.getElementById("buscaNome").value.trim();
  let tbody = document.querySelector("#tabelaProdutos tbody");
  tbody.innerHTML = "";

  if (termo === "") {
    listarProdutos();
    return;
  }

  try {
   let response = await fetch(`http://localhost:8080/apis/produto/busca-nome?termo=${encodeURIComponent(termo)}`);

    let produtos = await response.json();

    if (!Array.isArray(produtos)) {
      tbody.innerHTML = `<tr><td colspan="6">${produtos.mensagem || "Erro ao buscar produtos."}</td></tr>`;
      return;
    }

    if (produtos.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhum produto encontrado.</td></tr>";
      return;
    }

    produtos.forEach((produto) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${produto.id}</td>
        <td>${produto.nome}</td>
        <td>${formatarData(produto.data)}</td>
        <td>R$ ${parseFloat(produto.preco).toFixed(2)}</td>
        <td>${produto.categoria.desc}</td>
        <td class="acoes">
          <button class="editar" onclick="editarProduto(${produto.id})">Editar</button>
          <button class="excluir" onclick="apagarProduto(${produto.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (erro) {
    console.error("Erro ao buscar produtos:", erro);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao buscar produtos.</td></tr>";
  }
}

document.getElementById("buscaNome").addEventListener("keypress", function (e) {
  if (e.key === "Enter") {
    buscarPorNome();
  }
});

async function ordenarPorNome() {
  let tbody = document.querySelector("#tabelaProdutos tbody");
  tbody.innerHTML = "";

  try {
    let response = await fetch("http://localhost:8080/apis/produto/ordenados");
    let produtos = await response.json();

    if (!Array.isArray(produtos)) {
      tbody.innerHTML = `<tr><td colspan="6">${produtos.mensagem || "Erro ao buscar produtos."}</td></tr>`;
      return;
    }

    if (produtos.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhum produto encontrado.</td></tr>";
      return;
    }

    produtos.forEach((produto) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${produto.id}</td>
        <td>${produto.nome}</td>
        <td>${formatarData(produto.data)}</td>
        <td>R$ ${parseFloat(produto.preco).toFixed(2)}</td>
        <td>${produto.categoria.desc}</td>
        <td class="acoes">
          <button class="editar" onclick="editarProduto(${produto.id})">Editar</button>
          <button class="excluir" onclick="apagarProduto(${produto.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (erro) {
    console.error("Erro ao buscar produtos ordenados:", erro);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao buscar produtos.</td></tr>";
  }
}

async function ordenarPorData() {
  let tbody = document.querySelector("#tabelaProdutos tbody");
  tbody.innerHTML = "";

  try {
    let response = await fetch("http://localhost:8080/apis/produto");
    let produtos = await response.json();

    if (!Array.isArray(produtos)) {
      tbody.innerHTML = `<tr><td colspan="6">${produtos.mensagem || "Erro ao buscar produtos."}</td></tr>`;
      return;
    }

    if (produtos.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhum produto encontrado.</td></tr>";
      return;
    }

    
    produtos.sort((a, b) => new Date(a.data) - new Date(b.data));


    produtos.forEach((produto) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${produto.id}</td>
        <td>${produto.nome}</td>
        <td>${formatarData(produto.data)}</td>
        <td>R$ ${parseFloat(produto.preco).toFixed(2)}</td>
        <td>${produto.categoria.desc}</td>
        <td class="acoes">
          <button class="editar" onclick="editarProduto(${produto.id})">Editar</button>
          <button class="excluir" onclick="apagarProduto(${produto.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (erro) {
    console.error("Erro ao buscar produtos:", erro);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao buscar produtos.</td></tr>";
  }
}

