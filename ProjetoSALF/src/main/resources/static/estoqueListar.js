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