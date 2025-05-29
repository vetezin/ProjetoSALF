document.addEventListener("DOMContentLoaded", async () => {
    // --- Referências aos Elementos HTML ---
    const alterarCotacaoForm = document.getElementById("alterarCotacaoForm");
    const cotIdInput = document.getElementById("cot_id");
    const dataAberturaInput = document.getElementById("cot_dtabertura");
    const dataFechamentoInput = document.getElementById("cot_dtfechamento");
    const lcCodInput = document.getElementById("lc_cod");
    const lcCodInfo = document.getElementById("lcCodInfo");
    const tabelaFornecedores = document.getElementById("tabelaFornecedores");
    const loadingFornecedoresMessage = document.getElementById("loadingFornecedores");
    const fornecedoresErrorMessage = document.getElementById("fornecedoresErrorMessage");
    const noFornecedoresFoundMessage = document.getElementById("noFornecedoresFound");
    const messageElement = document.getElementById("message");
    const errorMessageElement = document.getElementById("errorMessage");
    const successMessageElement = document.getElementById("successMessage");

    let todosFornecedores = []; // Para armazenar a lista completa de fornecedores
    let fornecedoresDaCotacaoAtual = []; // Para armazenar os fornecedores já associados à cotação

    // --- Funções de Feedback ---
    function showMessage(element, text, type) {
        element.textContent = text;
        element.style.display = 'block';
        element.className = 'message'; // Reset classes
        if (type) {
            element.classList.add(type); // Add specific type class (loading, error-message, success-message, info-message)
        }
    }

    function hideMessage(element) {
        element.style.display = 'none';
        element.textContent = '';
    }

    // --- Obter ID da URL ---
    const urlParams = new URLSearchParams(window.location.search);
    const cotacaoId = urlParams.get('id'); // Assume que o ID será passado como ?id=X

    if (!cotacaoId) {
        showMessage(errorMessageElement, "ID da Cotação não fornecido na URL.", 'error-message');
        return;
    }

    // --- Carregar Fornecedores Cadastrados e Marcar os Selecionados ---
    async function carregarFornecedores(fornecedoresJaSelecionados = []) {
        hideMessage(fornecedoresErrorMessage);
        hideMessage(noFornecedoresFoundMessage);
        showMessage(loadingFornecedoresMessage, "Carregando fornecedores...", "loading");
        tabelaFornecedores.innerHTML = ''; // Limpa a tabela antes de carregar

        try {
            const response = await fetch("http://localhost:8080/apis/fornecedor/all");
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            todosFornecedores = await response.json(); // Armazena todos os fornecedores

            hideMessage(loadingFornecedoresMessage);

            if (Array.isArray(todosFornecedores)) {
                if (todosFornecedores.length === 0) {
                    showMessage(noFornecedoresFoundMessage, "Nenhum fornecedor encontrado. Cadastre fornecedores primeiro.", "info-message");
                    return;
                }

                todosFornecedores.forEach(fornecedor => {
                    const isSelected = fornecedoresJaSelecionados.some(f => f.forn_cod === fornecedor.forn_cod);
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>${fornecedor.forn_nome || 'N/A'}</td>
                        <td>${fornecedor.forn_cod || 'N/A'}</td>
                        <td>${fornecedor.forn_cnpj || 'N/A'}</td>
                        <td>
                            <input type="checkbox" class="fornecedor-checkbox"
                                   data-forn-id="${fornecedor.forn_cod}"
                                   ${isSelected ? 'checked' : ''} />
                        </td>
                    `;
                    tabelaFornecedores.appendChild(tr);
                });
            } else {
                showMessage(fornecedoresErrorMessage, "Formato de dados inesperado ao carregar fornecedores.", "error-message");
            }

        } catch (error) {
            hideMessage(loadingFornecedoresMessage);
            console.error("Erro ao carregar fornecedores:", error);
            showMessage(fornecedoresErrorMessage, `Erro ao carregar fornecedores: ${error.message || "Erro desconhecido."}`, "error-message");
        }
    }

    // --- Buscar Informações da Lista de Compra ao Digitar o ID ---
    async function buscarListaCompraInfo() {
        const lcCod = parseInt(lcCodInput.value);
        if (isNaN(lcCod) || lcCod <= 0) {
            hideMessage(lcCodInfo);
            return;
        }

        showMessage(lcCodInfo, `Buscando Lista de Compra ID: ${lcCod}...`, "info-message");

        try {
            const response = await fetch(`http://localhost:8080/apis/lista/${lcCod}`);
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                if (errorData && errorData.erro) {
                    throw new Error(errorData.erro);
                }
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            const listaCompra = await response.json();

            if (listaCompra && listaCompra.id) {
                showMessage(lcCodInfo, `Lista encontrada: ${listaCompra.id} - ${listaCompra.descricao || 'N/A'} (Funcionário ID: ${listaCompra.funcionarioId || 'N/A'})`, "success-message");
            } else {
                showMessage(lcCodInfo, `Lista de Compra ID ${lcCod} não encontrada.`, "error-message");
            }
        } catch (error) {
            console.error("Erro ao buscar lista de compra:", error);
            showMessage(lcCodInfo, `Erro ao buscar lista: ${error.message || "Erro desconhecido."}`, "error-message");
        }
    }

    // --- Carregar Dados da Cotação para Edição ---
    async function carregarDadosCotacao(id) {
        showMessage(messageElement, "Carregando dados da cotação...", 'loading');
        hideMessage(errorMessageElement);
        hideMessage(successMessageElement);

        try {
            const response = await fetch(`http://localhost:8080/apis/cotacao/${id}`);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            const cotacaoData = await response.json();

            hideMessage(messageElement);

            if (cotacaoData) {
                cotIdInput.value = cotacaoData.cot_cod || '';
                dataAberturaInput.value = cotacaoData.cot_dtabertura || '';
                dataFechamentoInput.value = cotacaoData.cot_dtfechamento || '';

                // Verifica se o objeto lista_compra existe e pega o lc_cod
                if (cotacaoData.lista_compra && cotacaoData.lista_compra.lc_cod) {
                    lcCodInput.value = cotacaoData.lista_compra.lc_cod;
                    // Chamar a função para exibir a info da lista assim que o ID for carregado
                    buscarListaCompraInfo(); 
                } else {
                    lcCodInput.value = '';
                    showMessage(lcCodInfo, "Lista de Compra não associada ou inválida.", "error-message");
                }

                // Armazena os fornecedores da cotação para pré-seleção
                fornecedoresDaCotacaoAtual = cotacaoData.fornecedores || [];
                await carregarFornecedores(fornecedoresDaCotacaoAtual); // Recarrega/preenche a tabela de fornecedores

            } else {
                showMessage(errorMessageElement, "Dados da cotação não encontrados.", 'error-message');
            }

        } catch (error) {
            hideMessage(messageElement);
            console.error("Erro ao carregar dados da cotação:", error);
            showMessage(errorMessageElement, `Erro ao carregar dados da cotação: ${error.message || error}`, 'error-message');
        }
    }

    // --- Enviar Alterações da Cotação ---
    alterarCotacaoForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        hideMessage(errorMessageElement);
        hideMessage(successMessageElement);
        showMessage(messageElement, "Salvando alterações...", 'loading');

        const idCotacao = parseInt(cotIdInput.value);
        const dataAbertura = dataAberturaInput.value;
        const dataFechamento = dataFechamentoInput.value || null; // Envia null se vazio
        const lcCod = parseInt(lcCodInput.value);

        // Validação
        if (isNaN(idCotacao) || idCotacao <= 0 || !dataAbertura || isNaN(lcCod) || lcCod <= 0) {
            showMessage(errorMessageElement, "Por favor, preencha todos os campos obrigatórios e válidos (ID da Cotação, Data de Abertura, ID da Lista de Compra).", 'error-message');
            hideMessage(messageElement);
            return;
        }

        const fornecedorCods = [];
        const checkboxes = document.querySelectorAll('.fornecedor-checkbox:checked');

        if (checkboxes.length === 0) {
            showMessage(errorMessageElement, "Selecione ao menos um fornecedor para a cotação.", "error-message");
            hideMessage(messageElement);
            return;
        }

        checkboxes.forEach(checkbox => {
            fornecedorCods.push(parseInt(checkbox.dataset.fornId));
        });

        // Payload para o backend (usando as mesmas chaves do cadastro)
        const payload = {
            cot_cod: idCotacao,
            cot_dtabertura: dataAbertura,
            cot_dtfechamento: dataFechamento,
            lc_cod: lcCod,
            fornecedorCods: fornecedorCods
        };

        try {
            // A requisição PUT para /apis/cotacao para alteração
            const response = await fetch(`http://localhost:8080/apis/cotacao`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }

            const result = await response.json();
            showMessage(successMessageElement, result.mensagem || "Cotação alterada com sucesso!", 'success-message');
            hideMessage(messageElement);

            // Opcional: Recarregar os dados da cotação após a alteração para refletir qualquer mudança
            // await carregarDadosCotacao(cotacaoId); 

        } catch (error) {
            hideMessage(messageElement);
            console.error("Erro ao alterar cotação:", error);
            showMessage(errorMessageElement, `Erro ao alterar cotação: ${error.message || error}`, 'error-message');
        }
    });

    // --- Listeners de Eventos Adicionais ---
    lcCodInput.addEventListener('blur', buscarListaCompraInfo); // Busca info da lista ao sair do campo
    lcCodInput.addEventListener('input', () => { // Limpa mensagem ao digitar
        hideMessage(lcCodInfo);
    });

    // --- Chamada Inicial ---
    if (cotacaoId) {
        await carregarDadosCotacao(cotacaoId);
    } else {
        showMessage(errorMessageElement, "ID da Cotação não encontrado para carregamento.", 'error-message');
        await carregarFornecedores([]); // Carrega fornecedores mesmo sem ID para preenchimento manual
    }
});