document.addEventListener("DOMContentLoaded", () => {
  listarEstoque();
});

// <button class="btn btn-danger btn-sm" onclick="abrirModalSaida(${item.id}, ${item.quantidade})">Saída</button>

async function listarEstoque() {
  let tbody = document.querySelector("#tabelaEstoque tbody");
  tbody.innerHTML = ""; // limpa tabela antes de preencher

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
      tbody.innerHTML = "<tr><td colspan='5'>Nenhum item de estoque encontrado.</td></tr>";
      return;
    }

    estoque.forEach(item => {
      let tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.id}</td>
         <td>${item.produtoId}</td>
        <td>${item.produto.nome}</td>
        <td>${item.quantidade}</td>
        <td>${item.validade}</td>
        <td>
          <button class="btn btn-warning btn-sm me-1" onclick="abrirModalAcerto(${item.id}, ${item.quantidade})">Acerto</button>
         
          <button class="btn btn-danger btn-sm" onclick="abrirModalSaida(${item.id}, ${item.quantidade}, ${item.produtoId})">Saída</button>

        </td>
      `;
      tbody.appendChild(tr);
    });
  } catch (erro) {
    console.error("Erro ao buscar estoque:", erro);
    tbody.innerHTML = "<tr><td colspan='5'>Erro ao buscar estoque. Verifique a API.</td></tr>";
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

  const codProduto = document.getElementById('saidaProdutoId').value;
  const quantidadeSaida = document.getElementById('quantidadeSaida').value;
  const codFuncionario = document.getElementById('codigoFuncionario').value;
  const dataSaida = document.getElementById('dataSaida').value;
  const motivo = document.getElementById('motivoSaida').value;

  try {
    const resposta = await fetch('http://localhost:8080/apis/saida/registrar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: new URLSearchParams({
        codProduto,
        quantidadeSaida,
        codFuncionario,
        dataSaida,
        motivo
      })
    });

    const resultado = await resposta.json();

    if (resposta.ok) {
      alert(resultado.mensagem || "Saída registrada com sucesso!");
      fecharModal('modalSaidaProduto');
      listarEstoque();
    } else {
      alert("Erro: " + resultado.mensagem || "Falha ao registrar saída.");
    }
  } catch (erro) {
    console.error("Erro ao registrar saída:", erro);
    alert("Erro ao tentar registrar a saída.");
  }

  return false;
}
