document.addEventListener("DOMContentLoaded", () => {
  listarEstoque();
  carregarFuncionarios(); 

  let dataInput = document.getElementById("dataSaida");
  let hoje = new Date().toISOString().split("T")[0];
  dataInput.max = hoje;



   document.addEventListener("change", function (event) {
    if (event.target.classList.contains("produto-checkbox")) {
      document.querySelectorAll(".produto-checkbox").forEach((cb) => {
        if (cb !== event.target) {
          cb.checked = false;
        }
      });
    }
  });



});

function mostrarToast(mensagem, duracao = 3000) {
  let toast = document.getElementById("mensagemToast");
  toast.textContent = mensagem;
  toast.style.display = "block";

  setTimeout(() => {
    toast.style.display = "none";
  }, duracao);
}


async function carregarFuncionarios() {
  let select = document.getElementById("selectFuncionario");

  try {
    let response = await fetch("http://localhost:8080/apis/funcionario");
    if (!response.ok) {
      console.error("Erro ao buscar funcionários");
      return;
    }

    let funcionarios = await response.json();
    funcionarios.forEach(func => {
      let option = document.createElement("option");
      option.value = func.id;
      option.textContent = func.nome;
      select.appendChild(option);
    });
  } catch (error) {
    console.error("Erro ao carregar funcionários:", error);
  }
}


async function listarEstoque() {
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

    /*
    estoque.forEach((item) => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.id}</td>
         <td>${item.produtoId}</td>
        <td>${item.produto.nome}</td>
        <td>${item.quantidade}</td>
        <td>${item.validade}</td>
            <td class="text-center">
            <input type="checkbox" class="form-check-input produto-checkbox"
             data-estoque-id="${item.id}"
             data-produto-id="${item.produtoId}"
              data-produto-nome="${item.produto.nome}"
              data-quantidade="${item.quantidade}"
              data-validade="${item.validade}" />
       </td>

      `;
      tbody.appendChild(tr);
    });*/
    estoque.forEach((item) => {
  let nomeProduto = item.produto?.nome || "Produto excluído";

  let tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${item.id}</td>
    <td>${item.produtoId}</td>
    <td>${nomeProduto}</td>
    <td>${item.quantidade}</td>
    <td>${item.validade}</td>
    <td class="text-center">
      <input type="checkbox" class="form-check-input produto-checkbox"
        data-estoque-id="${item.id}"
        data-produto-id="${item.produtoId}"
        data-produto-nome="${nomeProduto}"
        data-quantidade="${item.quantidade}"
        data-validade="${item.validade}" />
    </td>
  `;
  tbody.appendChild(tr);
});

  } catch (erro) {
    console.error("Erro ao buscar estoque:", erro);
    tbody.innerHTML =
      "<tr><td colspan='5'>Erro ao buscar estoque. Verifique a API.</td></tr>";
  }
}

function abrirModalAcerto(id, qtdAtual) {
  document.getElementById("acertoEstoqueId").value = id;
  document.getElementById("quantidadeAcerto").value = qtdAtual;
  document.getElementById("modalAcertoEstoque").style.display = "block";
}

function abrirModalSaida(estoqueId, qtdAtual, produtoId) {
  document.getElementById("saidaEstoqueId").value = estoqueId;
  document.getElementById("saidaProdutoId").value = produtoId;
  document.getElementById("quantidadeSaida").max = qtdAtual;
  document.getElementById("quantidadeSaida").value = "";
  document.getElementById("codigoFuncionario").value = "";
  document.getElementById("dataSaida").value = "";
  document.getElementById("motivoSaida").value = "";
  document.getElementById("modalSaidaProduto").style.display = "block";
}

function fecharModal(idModal) {
  document.getElementById(idModal).style.display = "none";
}

async function registrarSaida(event) {
  event.preventDefault();

  let codProduto = document.getElementById("saidaProdutoId").value;
  let quantidadeSaida = document.getElementById("quantidadeSaida").value;
  let codFuncionario = document.getElementById("codigoFuncionario").value;
  let dataSaida = document.getElementById("dataSaida").value;
  let motivo = document.getElementById("motivoSaida").value;

  try {
    let resposta = await fetch("http://localhost:8080/apis/saida/registrar", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams({
        codProduto,
        quantidadeSaida,
        codFuncionario,
        dataSaida,
        motivo,
      }),
    });

    let resultado = await resposta.json();

    if (resposta.ok) {
      mostrarToast("Saida registrada com sucesso",5000);
      
      fecharModal("modalSaidaProduto");
      listarEstoque();
    } else {
       mostrarToast("Erro ao registrar saida",5000);
    }
  } catch (erro) {
    console.error("Erro ao registrar saída:", erro);
    mostrarToast("Erro ao registrar saida",5000);
  }

  return false;
}

function adicionarSelecionados() {
  let checkboxes = document.querySelectorAll(".produto-checkbox:checked");
  let tabelaDestino = document.querySelector("#tabelaItensSelecionados tbody");
  let qtd = document.getElementById("quantidadeItem").value;

  if (checkboxes.length === 0) {
    mostrarToast("Nenhum produto selecionado",5000);
    return;
  }

  if (!qtd || qtd <= 0) {
    mostrarToast("Informe uma quantidade válida para todos os produtos.",5000);
    return;
  }

  checkboxes.forEach(cb => {
    let id = cb.dataset.produtoId;
    let nome = cb.dataset.produtoNome;

    let novaLinha = document.createElement("tr");
    novaLinha.innerHTML = `
      <td>${id}</td>
      <td>${nome}</td>
      <td>${qtd}</td>
      <td><button class="btn btn-sm btn-danger" onclick="this.parentElement.parentElement.remove()">Remover</button></td>
    `;

    tabelaDestino.appendChild(novaLinha);
    cb.checked = false; 
  });

    document.getElementById("quantidadeItem").value = "";
}

async function registrarSaidaFinal() {
  let dataSaida = document.getElementById("dataSaida").value;
  
  let codFuncionario = document.getElementById("selectFuncionario").value;

  let motivo = document.getElementById("motivoSaida").value;

  let linhas = document.querySelectorAll("#tabelaItensSelecionados tbody tr");
  let produtos = [];

  linhas.forEach(linha => {
    let codProduto = linha.children[0].textContent;
    let quantidade = linha.children[2].textContent;

    produtos.push({
      codProduto: parseInt(codProduto),
      quantidade: parseInt(quantidade)
    });
  });

  if (produtos.length === 0) {
    mostrarToast("Nenhum produto selecionado.",5000);
    return;
  }

  try {
    let resposta = await fetch("http://localhost:8080/apis/saida/registrarMultiplos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        produtos,
        codFuncionario: parseInt(codFuncionario),
        dataSaida,
        motivo
      })
    });

    let resultado = await resposta.json();

    if (resposta.ok) {
      mostrarToast("Saída registrada com sucesso!",5000);
      window.location.reload(); 
    } else {
      mostrarToast("Falha ao registrar saída.",5000);
    }
  } catch (erro) {
    console.error("Erro ao registrar saída:", erro);
    mostrarToast("Falha ao registrar saída.",5000);
  }
}





(() => {
  'use strict'

  let form = document.getElementById('formSaida');

  form.addEventListener('submit', event => {
    if (!form.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    } else {
      event.preventDefault(); 
      adicionarSelecionados(); 
    }

    form.classList.add('was-validated');
  });
})();

