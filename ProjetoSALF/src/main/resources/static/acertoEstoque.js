
document.addEventListener("DOMContentLoaded", () => {
    
    configurarFormularioAcerto(); 
    inicializarBootstrapValidation();
     carregarProdutosNoDatalist();
});




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
        let codFuncionario = document.getElementById("codigoFuncionario").value;
        let motivo = document.getElementById("motivoAcerto").value.trim();
        let quantidadeAcerto = document.getElementById("quantidadeAcerto").value;

        
        let codProduto = document.getElementById("codProdutoAcerto").value;

        
        if (!codProduto) {
            mostrarToast("Por favor, selecione um produto na tabela.");
            form.classList.remove("was-validated"); 
            return;
        }

       
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
                document.querySelectorAll(".produto-checkbox").forEach(cb => cb.checked = false);
                document.getElementById("codProdutoAcerto").value = "";
                listarEstoque();
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
                option.setAttribute("data-id", item.produtoId);
                datalist.appendChild(option);
            }
        });

        let inputProduto = document.getElementById("produtoSelecionado");
        inputProduto.addEventListener("input", () => {
            let selectedOption = Array.from(datalist.options).find(opt => opt.value === inputProduto.value);
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

