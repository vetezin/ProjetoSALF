
document.addEventListener("DOMContentLoaded", () => {
    
    configurarFormularioAcerto(); 
    inicializarBootstrapValidation();
     carregarProdutosNoDatalist();
     carregarFuncionarios(); 

  let dataInput = document.getElementById("dataAcerto");
  let hoje = new Date().toISOString().split("T")[0];
  dataInput.max = hoje;



 
});




function configurarFormularioAcerto() {
  let form = document.getElementById("formAcerto");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    event.stopPropagation();

    if (!form.checkValidity()) {
      form.classList.add("was-validated");
      return;
    }

    let dataAcerto = document.getElementById("dataAcerto").value;
    let codFuncionario = document.getElementById("selectFuncionario").value;
    let motivo = document.getElementById("motivoAcerto").value.trim();
    let quantidadeAcerto = document.getElementById("quantidadeAcerto").value;
    let produtoInput = document.getElementById("produtoSelecionado").value;

    let datalist = document.getElementById("listaProdutos");
    let optionProduto = Array.from(datalist.options).find(
      (opt) => opt.value.toLowerCase() === produtoInput.toLowerCase()
    );

    if (!optionProduto) {
      mostrarToast("Por favor, selecione um produto válido da lista.");
      form.classList.remove("was-validated");
      return;
    }

    let codProduto = optionProduto.getAttribute("data-id");
    let quantidadeEstoque = parseInt(optionProduto.getAttribute("data-quantidade"), 10);
    let quantidadeSolicitada = parseInt(quantidadeAcerto, 10);

    if (!codProduto) {
      mostrarToast("Código do produto inválido.");
      form.classList.remove("was-validated");
      return;
    }

   
    let inputQuantidade = document.getElementById("quantidadeAcerto");
    if (quantidadeSolicitada > quantidadeEstoque) {
      inputQuantidade.setCustomValidity("Quantidade solicitada maior que o estoque disponível.");
      form.classList.add("was-validated");
      inputQuantidade.reportValidity();
      return;
    } else {
      inputQuantidade.setCustomValidity(""); 
    }

    // Preparar dados para envio
    let params = new URLSearchParams();
    params.append("codProduto", codProduto);
    params.append("novaQuantidade", quantidadeAcerto);
    params.append("codFuncionario", codFuncionario);
    params.append("dataAcerto", dataAcerto);
    params.append("motivo", motivo);

    try {
      let response = await fetch("http://localhost:8080/apis/acertoestoque/registrar", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: params.toString(),
      });

      let resultado = await response.json();

      if (response.ok) {
        mostrarToast(resultado.mensagem || "Acerto registrado com sucesso!");
        form.reset();
        form.classList.remove("was-validated");
        document.getElementById("codProdutoAcerto").value = "";
      } else {
        mostrarToast("Erro: " + (resultado.mensagem || "Falha ao registrar acerto."));
      }
    } catch (error) {
      console.error("Erro ao registrar acerto:", error);
      mostrarToast("Erro ao tentar registrar o acerto.");
    }
  });
}





function mostrarToast(mensagem, duracao = 3000) {
  let mensagemDiv = document.getElementById("mensagem");
  mensagemDiv.textContent = mensagem;
  mensagemDiv.style.padding = "10px 20px";
  mensagemDiv.style.backgroundColor = "#333";
  mensagemDiv.style.color = "white";
  mensagemDiv.style.borderRadius = "5px";
  mensagemDiv.style.marginTop = "10px";
  mensagemDiv.style.textAlign = "center";
  mensagemDiv.style.display = "block";

  setTimeout(() => {
    mensagemDiv.style.display = "none";
  }, duracao);
}


function inicializarBootstrapValidation() {
    let forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

async function carregarProdutosNoDatalist() {
    try {
        let response = await fetch("http://localhost:8080/apis/estoque");
        if (!response.ok) throw new Error("Erro ao buscar estoque");

        let estoque = await response.json();

        if (!Array.isArray(estoque)) {
            throw new Error("Resposta inválida da API");
        }

        let datalist = document.getElementById("listaProdutos");
        datalist.innerHTML = ""; // Limpa opções antigas

       

        estoque.forEach(item => {
        if (item.produto && item.produto.nome) {
            let option = document.createElement("option");

            
            option.value = item.produto.nome;

            
            let qtd = item.quantidade ?? 'N/A';
            let validade = item.validade ?? 'N/A';
            option.textContent = `${item.produto.nome} - ${qtd} un. - Venc: ${validade}`;

           
            option.setAttribute("data-id", item.produtoId);
            option.setAttribute("data-quantidade", item.quantidade);

            datalist.appendChild(option);
        }
        });


      
        let inputProduto = document.getElementById("produtoSelecionado");
        inputProduto.addEventListener("input", () => {
        let selectedOption = Array.from(datalist.options).find(
        (opt) => opt.value.toLowerCase() === inputProduto.value.toLowerCase()
        );
        let codInput = document.getElementById("codProdutoAcerto");
        if (selectedOption) {
        codInput.value = selectedOption.getAttribute("data-id");
        } else {
        codInput.value = "";
        }
        });


    } catch (error) {
        console.error("Erro ao carregar produtos no datalist:", error);
        mostrarToast("Erro ao carregar produtos no datalist.");
    }
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

