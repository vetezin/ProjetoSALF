
document.addEventListener("DOMContentLoaded", () => {
    listarEstoque();
    configurarFormularioAcerto(); // Renomeada para clareza
    inicializarBootstrapValidation();
});

// Função para carregar e preencher tabela do estoque
async function listarEstoque() {
    let tbody = document.querySelector("#tabelaEstoque tbody");
    tbody.innerHTML = ""; // limpa tabela antes de preencher

    try {
        let response = await fetch("http://localhost:8080/apis/estoque");
        let estoque = await response.json();

        if (!Array.isArray(estoque)) {
            tbody.innerHTML = `<tr><td colspan="6">${
                estoque.mensagem || "Erro ao buscar estoque."
            }</td></tr>`;
            return;
        }

        if (estoque.length === 0) {
            tbody.innerHTML =
                "<tr><td colspan='6'>Nenhum item de estoque encontrado.</td></tr>";
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
        });
    } catch (erro) {
        console.error("Erro ao buscar estoque:", erro);
        tbody.innerHTML =
            "<tr><td colspan='6'>Erro ao buscar estoque. Verifique a API.</td></tr>";
    }
}

document.addEventListener("change", function (event) {
    if (event.target.classList.contains("produto-checkbox")) {
        let checkbox = event.target;

        document.querySelectorAll(".produto-checkbox").forEach((cb) => {
            if (cb !== checkbox) {
                cb.checked = false;
            }
        });

        let codProdutoAcertoInput = document.getElementById("codProdutoAcerto");
        if (checkbox.checked) {
            codProdutoAcertoInput.value = checkbox.getAttribute("data-produto-id");
        } else {
            codProdutoAcertoInput.value = ""; 
        }
    }
});

// Configura a submissão do formulário de acerto
function configurarFormularioAcerto() {
    let form = document.getElementById("formAcerto");

    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário
        event.stopPropagation(); // Impede a propagação do evento

        // Validação Bootstrap
        if (!form.checkValidity()) {
            form.classList.add("was-validated");
            return;
        }

        // Coleta os dados do formulário
        let dataAcerto = document.getElementById("dataAcerto").value;
        let codFuncionario = document.getElementById("codigoFuncionario").value;
        let motivo = document.getElementById("motivoAcerto").value.trim();
        let quantidadeAcerto = document.getElementById("quantidadeAcerto").value;

        // Pega o código do produto do campo hidden
        let codProduto = document.getElementById("codProdutoAcerto").value;

        // Verifica se um produto foi selecionado (o campo hidden está preenchido)
        if (!codProduto) {
            mostrarToast("Por favor, selecione um produto na tabela.");
            form.classList.remove("was-validated"); // Remove validação para não mostrar erro persistente
            return;
        }

        // Monta os parâmetros para a requisição POST
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
                form.reset(); // Limpa o formulário
                form.classList.remove("was-validated"); // Remove os estilos de validação
                // Desmarca todos os checkboxes após o sucesso
                document.querySelectorAll(".produto-checkbox").forEach(cb => cb.checked = false);
                document.getElementById("codProdutoAcerto").value = ""; // Limpa o campo hidden
                listarEstoque(); // Atualiza a tabela de estoque
            } else {
                mostrarToast("Erro: " + (resultado.mensagem || "Falha ao registrar acerto."));
            }
        } catch (error) {
            console.error("Erro ao registrar acerto:", error);
            mostrarToast("Erro ao tentar registrar o acerto.");
        }
    });
}

// Toast simples de mensagens
function mostrarToast(mensagem, duracao = 3000) {
    let toast = document.getElementById("mensagemToast");
    toast.textContent = mensagem;
    toast.style.position = "fixed";
    toast.style.bottom = "20px";
    toast.style.right = "20px";
    toast.style.backgroundColor = "#333";
    toast.style.color = "white";
    toast.style.padding = "10px 20px";
    toast.style.borderRadius = "5px";
    toast.style.zIndex = 1000;
    toast.style.display = "block";

    setTimeout(() => {
        toast.style.display = "none";
    }, duracao);
}

// Inicializa validação Bootstrap para o formulário
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