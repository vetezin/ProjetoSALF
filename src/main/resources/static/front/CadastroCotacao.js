document.addEventListener("DOMContentLoaded", () => {
    // --- Referências aos Elementos HTML ---
    const cotacaoForm = document.getElementById("cotacaoForm");
    const dataAberturaInput = document.getElementById("cot_dtabertura");
    const dataFechamentoInput = document.getElementById("cot_dtfechamento");
    const lcCodInput = document.getElementById("lc_cod");
    const lcCodInfo = document.getElementById("lcCodInfo");
    const tabelaFornecedores = document.getElementById("tabelaFornecedores");
    const loadingFornecedoresMessage = document.getElementById("loadingFornecedores");
    const fornecedoresErrorMessage = document.getElementById("fornecedoresErrorMessage");
    const noFornecedoresFoundMessage = document.getElementById("noFornecedoresFound"); // Novo elemento
    const submitErrorMessage = document.getElementById("submitErrorMessage");
    const submitSuccessMessage = document.getElementById("submitSuccessMessage");

    // Define a data atual como valor padrão para o campo de data de abertura
    const today = new Date();
    const yyyy = today.getFullYear(); // Corrigido o nome da variável
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // Mês é 0-indexado
    const dd = String(today.getDate()).padStart(2, '0');
    dataAberturaInput.value = `${yyyy}-${mm}-${dd}`;

    // --- Funções de Feedback ---
    function showMessage(element, message, type) {
        element.textContent = message;
        element.style.display = 'block';
        element.classList.remove('loading', 'error-message', 'success-message', 'info-message');
        element.classList.add(`${type}-message`);
    }

    function hideMessage(element) {
        element.style.display = 'none';
        element.textContent = '';
    }

    // --- Carregar Fornecedores Cadastrados ---
    async function carregarFornecedores() {
        hideMessage(fornecedoresErrorMessage);
        hideMessage(noFornecedoresFoundMessage); // Esconde a mensagem de "nenhum encontrado"
        showMessage(loadingFornecedoresMessage, "Carregando fornecedores...", "loading");
        tabelaFornecedores.innerHTML = ''; // Limpa a tabela antes de carregar

        try {
            const response = await fetch("http://localhost:8080/apis/fornecedor/all");
            if (!response.ok) {
                // Tenta ler a mensagem de erro do backend se disponível
                const errorData = await response.json().catch(() => ({})); 
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            const fornecedores = await response.json();

            hideMessage(loadingFornecedoresMessage);

            // Verifica se a resposta é um array e se há algum erro interno do backend
            if (Array.isArray(fornecedores)) {
                if (fornecedores.length === 1 && fornecedores[0].erro) {
                    // Se o backend retornou um erro em uma lista, exiba-o
                    showMessage(fornecedoresErrorMessage, `Erro do servidor: ${fornecedores[0].erro}`, "error");
                    return;
                }
                if (fornecedores.length === 0) {
                    showMessage(noFornecedoresFoundMessage, "Nenhum fornecedor encontrado. Cadastre fornecedores primeiro.", "info");
                    return;
                }

                fornecedores.forEach(fornecedor => {
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>${fornecedor.forn_nome || 'N/A'}</td>
                        <td>${fornecedor.forn_cod || 'N/A'}</td>
                        <td>${fornecedor.forn_cnpj || 'N/A'}</td>
                        <td>
                            <input type="checkbox" class="fornecedor-checkbox"
                                   data-forn-id="${fornecedor.forn_cod}"
                                   data-forn-nome="${fornecedor.forn_nome}" />
                        </td>
                    `;
                    tabelaFornecedores.appendChild(tr);
                });
            } else {
                // Se a resposta não for um array (e não for um erro direto com .erro)
                showMessage(fornecedoresErrorMessage, "Formato de dados inesperado ao carregar fornecedores.", "error");
            }

        } catch (error) {
            hideMessage(loadingFornecedoresMessage);
            console.error("Erro ao carregar fornecedores:", error);
            showMessage(fornecedoresErrorMessage, `Erro ao carregar fornecedores: ${error.message || "Erro desconhecido."}`, "error");
        }
    }

    // --- Buscar Informações da Lista de Compra ao Digitar o ID ---
    async function buscarListaCompraInfo() {
        const lcCod = parseInt(lcCodInput.value);
        if (isNaN(lcCod) || lcCod <= 0) {
            hideMessage(lcCodInfo);
            return;
        }

        showMessage(lcCodInfo, `Buscando Lista de Compra ID: ${lcCod}...`, "info");

        try {
            const response = await fetch(`http://localhost:8080/apis/lista/${lcCod}`);
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                // Se o backend retornar um erro com 'erro' como chave (ex: {"erro": "Lista não encontrada"})
                if (errorData && errorData.erro) {
                    throw new Error(errorData.erro); // Lança o erro específico do backend
                }
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }
            const listaCompra = await response.json();

            // Verifique se a resposta contém a chave 'erro' do backend
            if (listaCompra && listaCompra.erro) {
                showMessage(lcCodInfo, `Erro: ${listaCompra.erro}`, "error");
            } else if (listaCompra && listaCompra.id) { // Agora verifica por 'id' e não 'lc_cod'
                // Aqui, você precisará decidir como exibir o nome do funcionário.
                // O backend retorna 'funcionarioId'. Se você quer o nome,
                // precisaria fazer outra requisição para o endpoint de funcionário
                // ou ajustar o backend para incluir o nome diretamente.
                // Por enquanto, vou exibir apenas o ID do funcionário.
                showMessage(lcCodInfo, `Lista encontrada: ${listaCompra.id} - ${listaCompra.descricao || 'N/A'} (Funcionário ID: ${listaCompra.funcionarioId || 'N/A'})`, "success");
            } else {
                // Se o backend retornou 200 OK, mas sem um ID válido
                showMessage(lcCodInfo, `Lista de Compra ID ${lcCod} não encontrada.`, "error");
            }
        } catch (error) {
            console.error("Erro ao buscar lista de compra:", error);
            showMessage(lcCodInfo, `Erro ao buscar lista: ${error.message || "Erro desconhecido."}`, "error");
        }
    }

    // --- Enviar Formulário de Cotação ---
    cotacaoForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // Impede o envio padrão do formulário

        hideMessage(submitErrorMessage);
        hideMessage(submitSuccessMessage);

        const dataAbertura = dataAberturaInput.value;
        const dataFechamento = dataFechamentoInput.value; // Pode ser vazia
        const lcCod = parseInt(lcCodInput.value);

        // Validação dos campos principais
        if (!dataAbertura) {
            showMessage(submitErrorMessage, "Por favor, preencha a Data de Abertura.", "error");
            return;
        }
        if (isNaN(lcCod) || lcCod <= 0) {
            showMessage(submitErrorMessage, "Por favor, preencha um ID de Lista de Compra válido.", "error");
            return;
        }

        const fornecedorCods = [];
        const checkboxes = document.querySelectorAll('.fornecedor-checkbox:checked');

        if (checkboxes.length === 0) {
            showMessage(submitErrorMessage, "Selecione ao menos um fornecedor para a cotação.", "error");
            return;
        }

        checkboxes.forEach(checkbox => {
            fornecedorCods.push(parseInt(checkbox.dataset.fornId));
        });

        // Constrói o payload JSON conforme o backend espera
        const payload = {
            cot_dtabertura: dataAbertura,
            cot_dtfechamento: dataFechamento || null, // Envia null se a data de fechamento for vazia
            lc_cod: lcCod,
            fornecedorCods: fornecedorCods // Lista de IDs de fornecedores
        };

        try {
            // Envia a requisição POST para o endpoint de cadastro de cotação
            const response = await fetch("http://localhost:8080/apis/cotacao/cadastrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload) // Converte o objeto payload para JSON string
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.mensagem || `Erro HTTP: ${response.status}`);
            }

            const result = await response.json();
            showMessage(submitSuccessMessage, result.mensagem || "Cotação cadastrada com sucesso!", "success");

            // Limpar formulário e recarregar fornecedores após sucesso
            cotacaoForm.reset();
            dataAberturaInput.value = `${yyyy}-${mm}-${dd}`; // Reseta a data de abertura para hoje
            hideMessage(lcCodInfo); // Esconde a informação da lista de compra
            tabelaFornecedores.innerHTML = ''; // Limpa a tabela de fornecedores
            carregarFornecedores(); // Recarrega os fornecedores
            
        } catch (error) {
            console.error("Erro ao enviar cotação:", error);
            showMessage(submitErrorMessage, `Erro ao cadastrar cotação: ${error.message || "Erro desconhecido."}`, "error");
        }
    });

    // --- Listeners de Eventos Adicionais ---
    lcCodInput.addEventListener('blur', buscarListaCompraInfo); // Busca info da lista ao sair do campo
    lcCodInput.addEventListener('input', () => { // Limpa mensagem ao digitar
        hideMessage(lcCodInfo);
    });

    // --- Inicialização ---
    carregarFornecedores(); // Carrega os fornecedores ao carregar a página
});