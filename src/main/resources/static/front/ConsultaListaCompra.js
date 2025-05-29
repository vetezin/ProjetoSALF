document.addEventListener('DOMContentLoaded', () => {
    // --- Referências aos Elementos HTML ---
    const tabelaCompras = document.getElementById('tabela-compras');
    const messageConsulta = document.getElementById('message-consulta');
    const filtroInput = document.getElementById('filtro-input');
    const btnFiltrarData = document.getElementById('filtrar-data');
    const btnFiltrarStatus = document.getElementById('filtrar-status'); // Este é o ID do botão "Filtrar por Descrição/ID"
    const filtroStatus = document.getElementById('filtro-status'); // Parágrafo para exibir o tipo de filtro ativo
    const botaoNovo = document.getElementById('btn-novo'); // Botão "Nova Compra"
    const logoutBtn = document.getElementById('logout-btn');
    const nomeUsuario = document.getElementById('usuario-logado');
    const btnVoltarMenu = document.getElementById('btn-voltar-menu'); // Botão "Menu"

    // --- Variáveis de Estado da Aplicação ---
    const nivelAcesso = parseInt(localStorage.getItem('nivelAcesso'), 10);
    const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));

    let listaCompras = []; // Armazena a lista completa de compras carregadas
    let filtroAtual = ''; // 'data' ou 'descricao_ou_id' para indicar o tipo de filtro ativo

    // --- Inicialização e Exibição de Informações do Usuário ---
    nomeUsuario.textContent = `Logado como: ${usuarioLogado?.login || 'Desconhecido'}`;

    // Lógica para mostrar/esconder o botão "Nova Compra" baseada no nível de acesso
    if (nivelAcesso < 2) {
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
     * Carrega as listas de compras do backend, aplicando um filtro se especificado,
     * e então atualiza a tabela na interface.
     * Exibe mensagens de carregamento, sucesso ou erro.
     * @param {string} termoFiltro - O termo a ser usado no filtro (ex: uma data, parte da descrição, ou um ID).
     */
    async function carregarCompras(termoFiltro = '') {
        displayMessage(messageConsulta, 'Carregando listas de compras...', 'loading');

        try {
            const url = termoFiltro
                ? `http://localhost:8080/apis/lista?filtro=${encodeURIComponent(termoFiltro)}`
                : 'http://localhost:8080/apis/lista/all';

            const response = await fetch(url);

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.mensagem || `Erro ${response.status}: ${response.statusText}`;
                throw new Error(errorMessage);
            }

            listaCompras = await response.json();
            atualizarTabela(listaCompras);

            if (listaCompras.length === 0) {
                displayMessage(messageConsulta, 'Nenhuma lista de compra encontrada.', 'info');
            } else {
                clearMessage(messageConsulta); // Limpa a mensagem de carregamento/info se houver dados
            }

        } catch (error) {
            console.error("Erro ao carregar compras:", error);
            displayMessage(messageConsulta, `Erro ao carregar listas de compras: ${error.message}`, 'error');
            tabelaCompras.innerHTML = ''; // Limpa a tabela em caso de erro
        }
    }

    /**
     * Atualiza o corpo da tabela de compras com os dados fornecidos.
     * Se a lista estiver vazia, exibe uma mensagem indicando.
     * @param {Array<Object>} compras - Uma lista de objetos de compra a serem exibidos na tabela.
     * Cada objeto deve ter: id, descricao, dataCriacao, e um objeto 'funcionario' com id e nome.
     */
    function atualizarTabela(compras) {
        tabelaCompras.innerHTML = ''; // Limpa todas as linhas existentes na tabela

        if (!compras || compras.length === 0) {
            // A mensagem já foi tratada em `carregarCompras`
            return;
        }

        compras.forEach(compra => {
            const tr = document.createElement('tr');

            const funcionarioId = compra.funcionario?.id || 'N/A';
            const funcionarioNome = compra.funcionario?.nome || 'N/A';

            tr.innerHTML = `
                <td>${compra.id}</td>
                <td>${compra.descricao}</td>
                <td>${funcionarioId} - ${funcionarioNome}</td>
                <td>${compra.dataCriacao || 'N/A'}</td>
                <td>
                    <button class="edit-btn btn-action" data-id="${compra.id}">Editar</button>
                    <button class="delete-btn btn-action" data-id="${compra.id}">Excluir</button>
                </td>
            `;
            tabelaCompras.appendChild(tr);
        });
    }

    /**
     * Aplica o filtro atual com base no texto do input de filtro.
     */
    function aplicarFiltro() {
        const termo = filtroInput.value.trim();
        carregarCompras(termo); // Sempre chama carregarCompras, que decide a URL
        filtroStatus.textContent = ''; // Limpa o texto de status do filtro ao aplicar um filtro
        filtroAtual = ''; // Reseta o tipo de filtro atual
    }

    /**
     * Envia uma requisição DELETE para o backend para excluir uma lista de compras.
     * Exibe uma confirmação ao usuário e mensagens de sucesso ou erro.
     * @param {number} id - O ID da lista de compra a ser excluída.
     */
    async function excluirCompra(id) {
        if (!confirm(`Tem certeza que deseja excluir a lista de compra com ID: ${id}?`)) {
            return;
        }

        displayMessage(messageConsulta, `Excluindo lista de compra ID: ${id}...`, 'loading');

        try {
            const response = await fetch(`http://localhost:8080/apis/lista/${id}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensagem || 'Erro desconhecido ao excluir compra.');
            }

            const successData = await response.json();
            displayMessage(messageConsulta, successData.mensagem || `Lista de compra ID ${id} excluída com sucesso!`, 'success');
            
            await carregarCompras(); // Recarrega a lista para refletir a alteração

        } catch (error) {
            console.error("Erro ao excluir compra:", error);
            displayMessage(messageConsulta, `Erro ao excluir lista de compra: ${error.message}`, 'error');
        }
    }

    // --- Listeners de Eventos ---

    // Listener para o input de filtro (dispara a cada digitação)
    filtroInput.addEventListener('input', aplicarFiltro);

    // Listener para o botão "Filtrar por Data"
    btnFiltrarData.addEventListener('click', () => {
        filtroAtual = 'data';
        filtroStatus.textContent = 'Filtrando por Data (digite a data no formato YYYY-MM-DD)';
    });

    // Listener para o botão "Filtrar por Descrição/ID"
    btnFiltrarStatus.addEventListener('click', () => {
        filtroAtual = 'descricao_ou_id';
        filtroStatus.textContent = 'Filtrando por Descrição ou ID (digite no campo acima)';
    });

    // Listener de clique na tabela para lidar com os botões "Editar" e "Excluir"
    tabelaCompras.addEventListener('click', (e) => {
        const target = e.target;

        if (target.tagName === 'BUTTON' && target.hasAttribute('data-id')) {
            const id = parseInt(target.getAttribute('data-id'), 10); // Converte para número inteiro

            if (target.classList.contains('edit-btn')) {
                window.location.href = `AlterarListaCompra.html?id=${id}`;
            }

            if (target.classList.contains('delete-btn')) {
                excluirCompra(id); // Chama a função de exclusão
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
    carregarCompras();
});