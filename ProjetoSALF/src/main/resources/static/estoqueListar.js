document.addEventListener("DOMContentLoaded", () => {
    listarEstoque2(); // chama a nova versão
  });

  async function listarEstoque2() {
    let tbody = document.querySelector("#tabelaEstoque tbody");
    tbody.innerHTML = "";

    try {
      let response = await fetch("http://localhost:8080/apis/estoque");
      let estoque = await response.json();

      if (!Array.isArray(estoque)) {
        tbody.innerHTML = `<tr><td colspan="5">${
          estoque.mensagem || "Erro ao buscar estoque."
        }</td></tr>`;
        return;
      }

      if (estoque.length === 0) {
        tbody.innerHTML =
          "<tr><td colspan='5'>Nenhum item de estoque encontrado.</td></tr>";
        return;
      }

      estoque.forEach((item) => {
        let tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${item.id}</td>
          <td>${item.produtoId}</td>
          <td>${item.produto.nome}</td>
          <td>${item.quantidade}</td>
          <td>${item.validade}</td>
        `;
        tbody.appendChild(tr);
      });
    } catch (erro) {
      console.error("Erro ao buscar estoque:", erro);
      tbody.innerHTML =
        "<tr><td colspan='5'>Erro ao buscar estoque. Verifique a API.</td></tr>";
    }
  }

  async function ordenarEstoquePorNome() {
  let tbody = document.querySelector("#tabelaEstoque tbody");
  tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/estoque");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar estoque: ${response.status}</td></tr>`;
      return;
    }

    let estoque = await response.json();

    if (!Array.isArray(estoque) || estoque.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhum item de estoque encontrado.</td></tr>";
      return;
    }

    // Ordena pelo nome do produto (case insensitive)
    estoque.sort((a, b) => {
      let nomeA = a.produto && a.produto.nome ? a.produto.nome.toLowerCase() : "";
      let nomeB = b.produto && b.produto.nome ? b.produto.nome.toLowerCase() : "";
      return nomeA.localeCompare(nomeB);
    });

    tbody.innerHTML = "";

    estoque.forEach((item) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.id}</td>
        <td>${item.produtoId}</td>
        <td>${item.produto.nome}</td>
        <td>${item.quantidade}</td>
        <td>${item.validade}</td>
      
      `;
      tbody.appendChild(tr);
    });

  } catch (erro) {
    console.error("Erro ao ordenar estoque:", erro);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados do estoque.</td></tr>";
  }
}

async function ordenarEstoquePorQuantidade() {
  let tbody = document.querySelector("#tabelaEstoque tbody");
  tbody.innerHTML = "<tr><td colspan='5'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/estoque");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="5">Erro ao buscar estoque: ${response.status}</td></tr>`;
      return;
    }

    let estoque = await response.json();

    if (!Array.isArray(estoque) || estoque.length === 0) {
      tbody.innerHTML = "<tr><td colspan='5'>Nenhum item de estoque encontrado.</td></tr>";
      return;
    }

    // Ordena pela quantidade (do menor para o maior)
    estoque.sort((a, b) => a.quantidade - b.quantidade);

    tbody.innerHTML = "";

    estoque.forEach((item) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.id}</td>
        <td>${item.produtoId}</td>
        <td>${item.produto.nome}</td>
        <td>${item.quantidade}</td>
        <td>${item.validade}</td>
      `;
      tbody.appendChild(tr);
    });

  } catch (erro) {
    console.error("Erro ao ordenar estoque por quantidade:", erro);
    tbody.innerHTML = "<tr><td colspan='5'>Erro ao carregar dados do estoque.</td></tr>";
  }
}


async function ordenarEstoquePorQuantidade() {
  let tbody = document.querySelector("#tabelaEstoque tbody");
  tbody.innerHTML = "<tr><td colspan='5'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/estoque");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="5">Erro ao buscar estoque: ${response.status}</td></tr>`;
      return;
    }

    let estoque = await response.json();

    if (!Array.isArray(estoque) || estoque.length === 0) {
      tbody.innerHTML = "<tr><td colspan='5'>Nenhum item de estoque encontrado.</td></tr>";
      return;
    }

    // Ordena pela quantidade (do menor para o maior)
    estoque.sort((a, b) => a.quantidade - b.quantidade);

    tbody.innerHTML = "";

    estoque.forEach((item) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.id}</td>
        <td>${item.produtoId}</td>
        <td>${item.produto.nome}</td>
        <td>${item.quantidade}</td>
        <td>${item.validade}</td>
      `;
      tbody.appendChild(tr);
    });

  } catch (erro) {
    console.error("Erro ao ordenar estoque por quantidade:", erro);
    tbody.innerHTML = "<tr><td colspan='5'>Erro ao carregar dados do estoque.</td></tr>";
  }
}
