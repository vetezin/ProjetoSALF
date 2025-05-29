document.addEventListener("DOMContentLoaded", () => {
    // --- Referências aos Elementos HTML ---
    const listaCompraForm = document.getElementById("listaCompraForm");
    const dataListaInput = document.getElementById("lc_dtlista"); // Corrigido para lc_dtlista
    const descricaoListaInput = document.getElementById("lc_desc");   // Corrigido para lc_desc
    const codFuncionarioInput = document.getElementById("func_cod"); // Corrigido para func_cod
    const tabelaProdutosEstoque = document.getElementById("tabelaProdutosEstoque");
    const loadingProductsMessage = document.getElementById("loadingProducts");
    const productsErrorMessage = document.getElementById("productsErrorMessage");
    const submitErrorMessage = document.getElementById("submitErrorMessage");
    const submitSuccessMessage = document.getElementById("submitSuccessMessage");

    // Define a data atual como valor padrão para o campo de data
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // Mês é 0-indexado
    const dd = String(today.getDate()).padStart(2, '0');
    dataListaInput.value = `${yyyy}-${mm}-${dd}`;

    // --- Funções de Feedback ---
    function showLoading(element, message) {
        element.textContent = message;
        element.style.display = 'block';
    }

    function hideMessage(element) {
        element.style.display = 'none';
        element.textContent = '';
    }

    function showErrorMessage(element, message) {
        element.textContent = message;
        element.style.display = 'block';
        element.classList.remove('success-message');
        element.classList.add('error-message');
    }

    function showSuccessMessage(element, message) {
        element.textContent = message;
        element.style.display = 'block';
        element.classList.remove('error-message');
        element.classList.add('success-message');
    }

    // --- Carregar Produtos do Estoque ---
    async function carregarProdutosEstoque() {
        showLoading(loadingProductsMessage, "Carregando produtos do estoque...");
        hideMessage(productsErrorMessage);
        tabelaProdutosEstoque.innerHTML = ''; // Limpa a tabela antes de carregar

        try {
            const response = await fetch("http://localhost:8080/apis/estoque");
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            const estoqueItens = await response.json();

            hideMessage(loadingProductsMessage);

            if (!Array.isArray(estoqueItens) || estoqueItens.length === 0) {
                showErrorMessage(productsErrorMessage, "Nenhum item de estoque encontrado.");
                return;
            }

            estoqueItens.forEach(item => {
                const produtoNome = item.produto ? item.produto.nome : "Produto Desconhecido";

                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${produtoNome}</td>
                    <td>${item.produtoId}</td>
                    <td>${item.quantidade}</td>
                    <td>
                        <input type="checkbox" class="produto-checkbox"
                               data-produto-id="${item.produtoId}"
                               data-produto-nome="${produtoNome}"
                               data-quantidade-disponivel="${item.quantidade}" />
                    </td>
                    <td>
                        <input type="number" class="form-control quantidade-desejada"
                               min="1" value="1"
                               data-produto-id="${item.produtoId}"
                               style="display: none;" />
                    </td>
                `;
                tabelaProdutosEstoque.appendChild(tr);

                // Adiciona listener para mostrar/esconder o input de quantidade
                const checkbox = tr.querySelector('.produto-checkbox');
                const quantidadeInput = tr.querySelector('.quantidade-desejada');

                checkbox.addEventListener('change', () => {
                    if (checkbox.checked) {
                        quantidadeInput.style.display = 'block'; // Mostra o campo de quantidade
                        // Opcional: Garante que a quantidade não seja maior que a disponível ao selecionar
                        if (parseInt(quantidadeInput.value) > parseInt(item.quantidade)) {
                            quantidadeInput.value = item.quantidade;
                        }
                        quantidadeInput.max = item.quantidade; // Define o máximo com base no estoque
                    } else {
                        quantidadeInput.style.display = 'none'; // Esconde o campo de quantidade
                        quantidadeInput.value = 1; // Reseta o valor para 1
                    }
                });
            });

        } catch (error) {
            hideMessage(loadingProductsMessage);
            console.error("Erro ao carregar produtos do estoque:", error);
            showErrorMessage(productsErrorMessage, `Erro ao carregar produtos: ${error.message || error}`);
        }
    }

    // --- Enviar Formulário de Lista de Compras ---
    listaCompraForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário

        hideMessage(submitErrorMessage);
        hideMessage(submitSuccessMessage);

        const dataLista = dataListaInput.value;
        const descricaoLista = descricaoListaInput.value;
        const codFuncionario = parseInt(codFuncionarioInput.value);

        // Validação dos campos principais
        if (!dataLista || !descricaoLista || isNaN(codFuncionario) || codFuncionario <= 0) {
            showErrorMessage(submitErrorMessage, "Por favor, preencha todos os campos da lista (Data, Descrição, ID Funcionário).");
            return;
        }

        const produtosSelecionados = [];
        const checkboxes = document.querySelectorAll('.produto-checkbox:checked');

        if (checkboxes.length === 0) {
            showErrorMessage(submitErrorMessage, "Selecione ao menos um produto para a lista de compras.");
            return;
        }

        let allQuantitiesValid = true;
        for (const checkbox of checkboxes) { // Usar for...of para poder usar 'return' para sair do loop
            const produtoId = parseInt(checkbox.dataset.produtoId);
            const quantidadeInput = checkbox.closest('tr').querySelector('.quantidade-desejada');
            const quantidadeDesejada = parseInt(quantidadeInput.value);
            const quantidadeDisponivel = parseInt(checkbox.dataset.quantidadeDisponivel);

            if (isNaN(quantidadeDesejada) || quantidadeDesejada <= 0) {
                showErrorMessage(submitErrorMessage, `Quantidade inválida para o produto ${checkbox.dataset.produtoNome}.`);
                allQuantitiesValid = false;
                break; // Sai do loop
            }
            // Validação de que a quantidade desejada não excede o estoque disponível
            // (Regra de negócio: lista de compra adiciona ao estoque, mas ainda precisa de validação para não pedir mais do que o máximo permitido, se houver)
            // No seu caso, a lista de compra ADICIONA ao estoque, então a validação de "quantidadeDesejada > quantidadeDisponivel"
            // pode ser menos rigorosa aqui no front-end, ou focada em um limite razoável.
            // O backend já fará a validação de existência do produto no estoque.
            
            produtosSelecionados.push({
                codProduto: produtoId,
                quantidade: quantidadeDesejada
            });
        }

        if (!allQuantitiesValid) {
            return; // Interrompe o envio se alguma quantidade for inválida
        }

        // Constrói o payload JSON conforme o backend espera (lc_dtlista, lc_desc, func_cod, produtos)
        const payload = {
            lc_dtlista: dataLista,
            lc_desc: descricaoLista,
            func_cod: codFuncionario,
            produtos: produtosSelecionados
        };

        try {
            // Envia a requisição POST para o endpoint de cadastro
            const response = await fetch("http://localhost:8080/apis/lista/cadastrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload) // Converte o objeto payload para JSON string
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }

            const result = await response.json();
            showSuccessMessage(submitSuccessMessage, result.mensagem || "Lista de compras cadastrada com sucesso!");

            // Limpar formulário e recarregar produtos após sucesso
            listaCompraForm.reset();
            dataListaInput.value = `${yyyy}-${mm}-${dd}`; // Reseta a data para hoje
            tabelaProdutosEstoque.innerHTML = ''; // Limpa a tabela de produtos
            carregarProdutosEstoque(); // Recarrega os produtos do estoque

        } catch (error) {
            console.error("Erro ao enviar lista de compras:", error);
            showErrorMessage(submitErrorMessage, `Erro ao cadastrar lista: ${error.message || error}`);
        }
    });

    // --- Inicialização ---
    carregarProdutosEstoque(); // Carrega os produtos do estoque ao carregar a página
});