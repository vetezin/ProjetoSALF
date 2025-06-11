    document.addEventListener("DOMContentLoaded", () => {
      listarSaidasComProdutos();
    });
function mostrarToast(mensagem, duracao = 3000) {
  let toast = document.getElementById("mensagemToast");
  toast.textContent = mensagem;
  toast.style.display = "block";

  setTimeout(() => {
    toast.style.display = "none";
  }, duracao);
}


function apagarSaida(id) {
  if (!id) {
    alert("ID da saída não informado.");
    return;
  }

  fetch(`http://localhost:8080/apis/saida/deletar/${id}`, {
    method: "DELETE",
  })
    .then((response) =>
      response.json().then((data) => ({ status: response.status, body: data }))
    )
    .then(({ status, body }) => {
      if (status === 200) {
        mostrarToast(body.mensagem || "Saída apagada com sucesso!");
        listarSaidasComProdutos(); // Atualiza a lista após apagar
      } else {
        mostrarToast(body.mensagem || "Erro ao apagar saída");
      }
    })
    .catch((error) => {
      console.error("Erro ao apagar saída:", error);
      mostrarToast("Erro ao apagar saída"); // ✅ removido uso de "body"
    });
}


async function listarSaidasComProdutos() {
      let tbody = document.querySelector("#tabelaSaidas tbody");
      tbody.innerHTML = "<tr><td colspan='5'>Carregando...</td></tr>";

      try {
        let response = await fetch("http://localhost:8080/apis/saida/listar-com-produtos");
        if (!response.ok) {
          tbody.innerHTML = `<tr><td colspan="5">Erro ao buscar saídas: ${response.status}</td></tr>`;
          return;
        }
        let saidas = await response.json();

        if (!Array.isArray(saidas) || saidas.length === 0) {
          tbody.innerHTML = "<tr><td colspan='5'>Nenhuma saída encontrada.</td></tr>";
          return;
        }

        tbody.innerHTML = ""; // limpa antes de preencher

        saidas.forEach(saida => {
          //let produtosNomes = saida.produtos.map(p => p.nome).join(", ") || "-";
          let produtosNomes = saida.produtos.map(p => `${p.nome} (${p.quantidade})`).join(", ") || "-";

          let nomeFuncionario = saida.funcionario ? saida.funcionario.nome : "Desconhecido";

          let tr = document.createElement("tr");
          tr.innerHTML = `
            <td>${saida.id}</td>
            <td>${new Date(saida.data).toLocaleDateString('pt-BR')}</td>
            <td>${saida.motivo}</td>
            <td>${nomeFuncionario}</td>
            <td>${produtosNomes}</td>
             <td class="acoes">
               <button class="excluir btn btn-danger btn-sm" onclick="apagarSaida(${saida.id})">Excluir</button>
            </td>
          `;
          tbody.appendChild(tr);
        });
      } catch (error) {
        console.error("Erro ao listar saídas:", error);
        tbody.innerHTML = "<tr><td colspan='5'>Erro ao carregar dados.</td></tr>";
      }
    }

async function ordenarPorMotivo() {
  let tbody = document.querySelector("#tabelaSaidas tbody");
  tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/saida/listar-com-produtos");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar saídas: ${response.status}</td></tr>`;
      return;
    }
    let saidas = await response.json();

    if (!Array.isArray(saidas) || saidas.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhuma saída encontrada.</td></tr>";
      return;
    }

    // Ordena o array pelo motivo (case insensitive)
    saidas.sort((a, b) => {
      let motivoA = a.motivo ? a.motivo.toLowerCase() : "";
      let motivoB = b.motivo ? b.motivo.toLowerCase() : "";
      if (motivoA < motivoB) return -1;
      if (motivoA > motivoB) return 1;
      return 0;
    });

    tbody.innerHTML = ""; // limpa a tabela antes de preencher

    saidas.forEach(saida => {
      //let produtosNomes = saida.produtos.map(p => p.nome).join(", ") || "-";
      let produtosNomes = saida.produtos.map(p => `${p.nome} (${p.quantidade})`).join(", ") || "-";

      let nomeFuncionario = saida.funcionario ? saida.funcionario.nome : "Desconhecido";

      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${saida.id}</td>
        <td>${new Date(saida.data).toLocaleDateString('pt-BR')}</td>
        <td>${saida.motivo}</td>
        <td>${nomeFuncionario}</td>
        <td>${produtosNomes}</td>
        <td class="acoes">
          <button class="excluir btn btn-danger btn-sm" onclick="apagarSaida(${saida.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });

  } catch (error) {
    console.error("Erro ao ordenar saídas:", error);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
  }
}

async function ordenarPorFuncionario() {
  let tbody = document.querySelector("#tabelaSaidas tbody");
  tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/saida/listar-com-produtos");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar saídas: ${response.status}</td></tr>`;
      return;
    }
    let saidas = await response.json();

    if (!Array.isArray(saidas) || saidas.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhuma saída encontrada.</td></tr>";
      return;
    }

    // Ordena pelo nome do funcionário (case insensitive)
    saidas.sort((a, b) => {
      let nomeA = a.funcionario && a.funcionario.nome ? a.funcionario.nome.toLowerCase() : "";
      let nomeB = b.funcionario && b.funcionario.nome ? b.funcionario.nome.toLowerCase() : "";
      if (nomeA < nomeB) return -1;
      if (nomeA > nomeB) return 1;
      return 0;
    });

    tbody.innerHTML = ""; // limpa a tabela antes de preencher

    saidas.forEach(saida => {
      //let produtosNomes = saida.produtos.map(p => p.nome).join(", ") || "-";
      let produtosNomes = saida.produtos.map(p => `${p.nome} (${p.quantidade})`).join(", ") || "-";

      let nomeFuncionario = saida.funcionario ? saida.funcionario.nome : "Desconhecido";

      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${saida.id}</td>
        <td>${new Date(saida.data).toLocaleDateString('pt-BR')}</td>
        <td>${saida.motivo}</td>
        <td>${nomeFuncionario}</td>
        <td>${produtosNomes}</td>
        <td class="acoes">
          <button class="excluir btn btn-danger btn-sm" onclick="apagarSaida(${saida.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });

  } catch (error) {
    console.error("Erro ao ordenar saídas:", error);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
  }
}

async function buscarPorMotivo() {
  let termo = document.getElementById("buscaMotivo").value.trim().toLowerCase();
  let tbody = document.querySelector("#tabelaSaidas tbody");

  tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

  try {
    let response = await fetch("http://localhost:8080/apis/saida/listar-com-produtos");
    if (!response.ok) {
      tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar saídas: ${response.status}</td></tr>`;
      return;
    }

    let saidas = await response.json();

    // Filtra apenas os que contêm o termo no motivo
    let filtradas = saidas.filter(saida => 
      saida.motivo && saida.motivo.toLowerCase().includes(termo)
    );

    if (filtradas.length === 0) {
      tbody.innerHTML = "<tr><td colspan='6'>Nenhuma saída encontrada para o motivo buscado.</td></tr>";
      return;
    }

    tbody.innerHTML = "";

    filtradas.forEach(saida => {
      //let produtosNomes = saida.produtos.map(p => p.nome).join(", ") || "-";
      let produtosNomes = saida.produtos.map(p => `${p.nome} (${p.quantidade})`).join(", ") || "-";

      let nomeFuncionario = saida.funcionario ? saida.funcionario.nome : "Desconhecido";

      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${saida.id}</td>
        <td>${new Date(saida.data).toLocaleDateString('pt-BR')}</td>
        <td>${saida.motivo}</td>
        <td>${nomeFuncionario}</td>
        <td>${produtosNomes}</td>
        <td class="acoes">
          <button class="excluir btn btn-danger btn-sm" onclick="apagarSaida(${saida.id})">Excluir</button>
        </td>
      `;
      tbody.appendChild(tr);
    });

  } catch (error) {
    console.error("Erro ao filtrar saídas:", error);
    tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
  }
}
