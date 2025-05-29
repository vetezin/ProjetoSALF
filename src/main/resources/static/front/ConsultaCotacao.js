document.addEventListener('DOMContentLoaded', () => {
    // --- Referências aos Elementos HTML ---
    const tabelaCotacoes = document.getElementById('tabela-cotacoes');
    const messageConsulta = document.getElementById('message-consulta');
    const filtroInput = document.getElementById('filtro-input');
    const btnFiltrarData = document.getElementById('filtrar-data');
    const btnFiltrarListaId = document.getElementById('filtrar-status'); // Botão "Filtrar por Lista/ID"
    const filtroStatusText = document.getElementById('filtro-status-text'); // Parágrafo para exibir o tipo de filtro ativo
    const botaoNovo = document.getElementById('btn-novo'); // Botão "Nova Cotação"
    const logoutBtn = document.getElementById('logout-btn');
    const nomeUsuario = document.getElementById('usuario-logado');
    const btnVoltarMenu = document.getElementById('btn-voltar-menu'); // Botão "Menu"

    // --- Variáveis de Estado da Aplicação ---
    const nivelAcesso = parseInt(localStorage.getItem('nivelAcesso'), 10);
    const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));

    let listaCotacoes = []; // Armazena a lista completa de cotações carregadas
    let filtroAtual = ''; // 'data_abertura' ou 'lista_ou_id' para indicar o tipo de filtro ativo

    // --- Inicialização e Exibição de Informações do Usuário ---
    nomeUsuario.textContent = `Logado como: ${usuarioLogado?.login || 'Desconhecido'}`;

    // Lógica para mostrar/esconder o botão "Nova Cotação" baseada no nível de acesso
    if (nivelAcesso < 2) { // Ajuste o nível de acesso conforme sua regra para cadastro de cotações
        if (botaoNovo && botaoNovo.parentElement) {
            botaoNovo.parentElement.style.display = 'none';
        }
    } else {
        if (botaoNovo && botaoNovo.parentElement) {
            botaoNovo.parentElement.style.display = 'inline-block';
        }
    }

    // --- Funções Auxiliares de Mensagens ---
    function displayMessage(element, text, type) {
        element.textContent = text;
        element.style.display = 'block';
        element.style.color = ''; // Reset color
        switch (type) {
            case 'success':
                element.style.color = '#4CAF50'; // Green
                break;
            case 'error':
                element.style.color = '#F44336'; // Red
                break;
            case 'info':
                element.style.color = '#2196F3'; // Blue
                break;
            case 'loading':
                element.style.color = '#333'; // Dark gray
                break;
            default:
                element.style.color = '#555'; // Default gray
        }
    }

    function clearMessage(element) {
        element.textContent = '';
        element.style.display = 'none';
    }

    /**
     * Carrega as cotações do backend, aplicando um filtro se especificado,
     * e então atualiza a tabela na interface.
     * Exibe mensagens de carregamento, sucesso ou erro.
     * @param {string} termoFiltro - O termo a ser usado no filtro. Pode ser uma data, parte da descrição da lista de compra, ou um ID de cotação/lista.
     */
    async function carregarCotacoes(termoFiltro = '') {
        displayMessage(messageConsulta, 'Carregando cotações...', 'loading');

        try {
            const url = termoFiltro
                ? `http://localhost:8080/apis/cotacao?filtro=${encodeURIComponent(termoFiltro)}`
                : 'http://localhost:8080/apis/cotacao/all';

            const response = await fetch(url);

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.mensagem || `Erro ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            }

            listaCotacoes = await response.json();
            atualizarTabela(listaCotacoes);

            if (listaCotacoes.length === 0) {
                displayMessage(messageConsulta, 'Nenhuma cotação encontrada.', 'info');
            } else {
                clearMessage(messageConsulta); // Limpa a mensagem de carregamento/info se houver dados
            }

        } catch (error) {
            console.error("Erro ao carregar cotações:", error);
            displayMessage(messageConsulta, `Erro ao carregar cotações: ${error.message}`, 'error');
            tabelaCotacoes.innerHTML = ''; // Limpa a tabela em caso de erro
        }
    }

    /**
     * Atualiza o corpo da tabela de cotações com os dados fornecidos.
     * Se a lista estiver vazia, exibe uma mensagem indicando.
     * @param {Array<Object>} cotacoes - Uma lista de objetos de cotação a serem exibidos na tabela.
     */
    function atualizarTabela(cotacoes) {
        tabelaCotacoes.innerHTML = ''; // Limpa todas as linhas existentes na tabela

        if (!cotacoes || cotacoes.length === 0) {
            // A mensagem já foi tratada em `carregarCotacoes`
            return;
        }

        cotacoes.forEach(cotacao => {
            const tr = document.createElement('tr');

            // Formatando a data de abertura e fechamento
            const dataAberturaFormatada = cotacao.cot_dtabertura ? new Date(cotacao.cot_dtabertura).toLocaleDateString('pt-BR') : 'N/A';
            const dataFechamentoFormatada = cotacao.cot_dtfechamento ? new Date(cotacao.cot_dtfechamento).toLocaleDateString('pt-BR') : 'N/A';

            // Construindo a string de fornecedores
            let fornecedoresStr = 'N/A';
            if (cotacao.fornecedores && Array.isArray(cotacao.fornecedores) && cotacao.fornecedores.length > 0) {
                fornecedoresStr = cotacao.fornecedores
                    .map(f => `${f.forn_cod} - ${f.forn_nome}`)
                    .join('<br>'); // Exibe cada fornecedor em uma nova linha
            }

            // CORREÇÃO AQUI: Acessar o lc_cod do objeto aninhado 'lista_compra'
            const listaCompraId = cotacao.lista_compra ? cotacao.lista_compra.lc_cod : 'N/A';

            tr.innerHTML = `
                <td>${cotacao.cot_cod || 'N/A'}</td>
                <td>${dataAberturaFormatada}</td>
                <td>${dataFechamentoFormatada}</td>
                <td>${listaCompraId}</td> <td>${fornecedoresStr}</td>
                <td>
                    <button class="edit-btn btn-action" data-id="${cotacao.cot_cod}">Editar</button>
                    <button class="delete-btn btn-action" data-id="${cotacao.cot_cod}">Excluir</button>
                </td>
            `;
            tabelaCotacoes.appendChild(tr);
        });
    }

    /**
     * Aplica o filtro atual com base no texto do input de filtro.
     */
    function aplicarFiltro() {
        const termo = filtroInput.value.trim();
        let filtroSQL = '';

        if (termo === '') { // Se o campo de filtro estiver vazio, carrega tudo
            carregarCotacoes();
            filtroStatusText.textContent = '';
            filtroAtual = '';
            return;
        }

        switch (filtroAtual) {
            case 'data_abertura':
                // Assumindo que o filtro por data no backend espera um formato AAAA-MM-DD
                // e que o campo no DB é de data.
                filtroSQL = `cot_dtabertura = '${termo}'`;
                break;
            case 'lista_ou_id':
                // Tenta filtrar por ID da cotação ou ID da lista de compra
                // ou descrição da lista de compra (se o backend permitir)
                const isNumeric = /^\d+$/.test(termo);
                if (isNumeric) {
                    filtroSQL = `cot_cod = ${termo} OR lc_cod = ${termo}`; // lc_cod aqui refere-se ao ID da lista.
                } else {
                    // Se não for numérico, assume que é para filtrar pela descrição da lista de compra
                    // ou outro campo textual relevante na cotação.
                    // Isso dependerá de como seu backend (`CotacaoController.getCotacoes`) trata o filtro.
                    // A string do filtro será passada para o DAO como 'filtro'.
                    // Exemplo: `LOWER(lc_desc) LIKE LOWER('%${termo}%')` OU `LOWER(algum_campo_texto) LIKE LOWER('%${termo}%')`
                    // O backend precisará fazer um JOIN com a tabela de lista de compra para filtrar pela descrição.
                    // Para o seu modelo atual, onde o filtro é aplicado diretamente na tabela Cotacao pelo DAO,
                    // se o filtro for texto e não numérico, ele terá que buscar por campos textuais na tabela COTACAO.
                    // Adaptei para tentar buscar por IDs como texto (para LIKE) ou na descrição (se houver).
                    // **PONTO DE ATENÇÃO:** Se o backend não faz JOIN para o filtro, buscar por descrição da lista NÃO VAI FUNCIONAR.
                    filtroSQL = `LOWER(CAST(cot_cod AS TEXT)) LIKE LOWER('%${termo}%') OR LOWER(CAST(lc_cod AS TEXT)) LIKE LOWER('%${termo}%')`; // Filtra por IDs como texto
                }
                break;
            default: // Se nenhum filtro específico for selecionado, tenta ID ou descrição
                const isNum = /^\d+$/.test(termo);
                if (isNum) {
                    filtroSQL = `cot_cod = ${termo} OR lc_cod = ${termo}`;
                } else {
                    filtroSQL = `LOWER(CAST(cot_cod AS TEXT)) LIKE LOWER('%${termo}%') OR LOWER(CAST(lc_cod AS TEXT)) LIKE LOWER('%${termo}%')`;
                }
                break;
        }
        carregarCotacoes(filtroSQL);
    }

    /**
     * Envia uma requisição DELETE para o backend para excluir uma cotação.
     * Exibe uma confirmação ao usuário e mensagens de sucesso ou erro.
     * @param {number} id - O ID da cotação a ser excluída.
     */
    async function excluirCotacao(id) {
        if (!confirm(`Tem certeza que deseja excluir a cotação com ID: ${id}?`)) {
            return;
        }

        displayMessage(messageConsulta, `Excluindo cotação ID: ${id}...`, 'loading');

        try {
            const response = await fetch(`http://localhost:8080/apis/cotacao/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || 'Erro desconhecido ao excluir cotação.');
            }

            const successData = await response.json();
            displayMessage(messageConsulta, successData.mensagem || `Cotação ID ${id} excluída com sucesso!`, 'success');
            
            await carregarCotacoes(); // Recarrega a lista para refletir a alteração

        } catch (error) {
            console.error("Erro ao excluir cotação:", error);
            displayMessage(messageConsulta, `Erro ao excluir cotação: ${error.message}`, 'error');
        }
    }

    // --- Listeners de Eventos ---

    // Listener para o input de filtro (dispara a cada digitação)
    filtroInput.addEventListener('input', aplicarFiltro);

    // Listener para o botão "Filtrar por Data Abertura"
    btnFiltrarData.addEventListener('click', () => {
        filtroAtual = 'data_abertura';
        filtroStatusText.textContent = 'Filtrando por Data de Abertura (formato AAAA-MM-DD)';
        aplicarFiltro(); // Aplica o filtro imediatamente com o termo atual
    });

    // Listener para o botão "Filtrar por Lista/ID"
    btnFiltrarListaId.addEventListener('click', () => {
        filtroAtual = 'lista_ou_id';
        filtroStatusText.textContent = 'Filtrando por ID da Cotação ou ID da Lista de Compra'; // Removido "Descrição" pois o filtro atual não suporta
        aplicarFiltro(); // Aplica o filtro imediatamente com o termo atual
    });

    // Listener de clique na tabela para lidar com os botões "Editar" e "Excluir"
    tabelaCotacoes.addEventListener('click', (e) => {
        const target = e.target;

        if (target.tagName === 'BUTTON' && target.hasAttribute('data-id')) {
            const id = parseInt(target.getAttribute('data-id'), 10); // Converte para número inteiro

            if (target.classList.contains('edit-btn')) {
                // Redireciona para a página de edição de cotação
                window.location.href = `AlterarCotacao.html?id=${id}`;
            }

            if (target.classList.contains('delete-btn')) {
                excluirCotacao(id); // Chama a função de exclusão
            }
        }
    });

    // Listener para o botão de Logout
    logoutBtn.addEventListener('click', () => {
        localStorage.removeItem('usuarioLogado');
        localStorage.removeItem('nivelAcesso');
        window.location.href = 'login.html';
    });

    // Listener para o botão "Menu"
    if (btnVoltarMenu) {
        btnVoltarMenu.addEventListener('click', () => {
            window.location.href = 'menu.html';
        });
    }

    // --- Chamada Inicial ---
    carregarCotacoes();
});