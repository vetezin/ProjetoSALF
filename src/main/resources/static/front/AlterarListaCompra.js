document.addEventListener("DOMContentLoaded", async () => {
    // --- Referências aos Elementos HTML ---
    const alterarListaCompraForm = document.getElementById("alterarListaCompraForm");
    const lcIdInput = document.getElementById("lc_id");
    const funcCodInput = document.getElementById("func_cod");
    // Removida referência a funcNomeSpan, pois não exibimos mais o nome
    const dataListaInput = document.getElementById("lc_dtlista");
    const descricaoListaInput = document.getElementById("lc_desc");
    const messageElement = document.getElementById("message");
    const errorMessageElement = document.getElementById("errorMessage");
    const successMessageElement = document.getElementById("successMessage");

    // --- Funções de Feedback ---
    function showMessage(element, text, type) {
        element.textContent = text;
        element.style.display = 'block';
        element.className = 'message'; // Reset classes
        if (type) {
            element.classList.add(type); // Add specific type class (loading, error-message, success-message)
        }
    }

    function hideMessage(element) {
        element.style.display = 'none';
        element.textContent = '';
    }

    // --- Obter ID da URL ---
    const urlParams = new URLSearchParams(window.location.search);
    const listaCompraId = urlParams.get('id'); // Assume que o ID será passado como ?id=X

    if (!listaCompraId) {
        showMessage(errorMessageElement, "ID da Lista de Compra não fornecido na URL.", 'error-message');
        return;
    }

    // --- Carregar Dados da Lista de Compra ---
    async function carregarDadosListaCompra(id) {
        showMessage(messageElement, "Carregando dados da lista de compras...", 'loading');
        hideMessage(errorMessageElement);
        hideMessage(successMessageElement);

        try {
            const response = await fetch(`http://localhost:8080/apis/lista/${id}`);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.erro || `Erro HTTP: ${response.status}`);
            }
            const listaData = await response.json();

            hideMessage(messageElement);

            // Preencher o formulário
            lcIdInput.value = listaData.id;
            funcCodInput.value = listaData.funcionarioId; // Apenas o ID do funcionário

            // Não há mais lógica para buscar e exibir o nome do funcionário aqui

            dataListaInput.value = listaData.dataCriacao; 
            descricaoListaInput.value = listaData.descricao;

        } catch (error) {
            hideMessage(messageElement);
            console.error("Erro ao carregar dados da lista de compras:", error);
            showMessage(errorMessageElement, `Erro ao carregar dados: ${error.message || error}`, 'error-message');
        }
    }

    // --- Enviar Alterações da Lista de Compra ---
    alterarListaCompraForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        hideMessage(errorMessageElement);
        hideMessage(successMessageElement);
        showMessage(messageElement, "Salvando alterações...", 'loading');

        const idLista = parseInt(lcIdInput.value);
        const dataLista = dataListaInput.value;
        const descricaoLista = descricaoListaInput.value;
        const codFuncionario = parseInt(funcCodInput.value); 

        // Validação básica
        if (!dataLista || !descricaoLista || isNaN(idLista) || idLista <= 0 || isNaN(codFuncionario) || codFuncionario <= 0) {
            showMessage(errorMessageElement, "Por favor, preencha todos os campos obrigatórios e válidos.", 'error-message');
            hideMessage(messageElement);
            return;
        }

        // Payload com as chaves esperadas pelo backend (@RequestBody Map<String, Object> dados)
        const payload = {
            lc_cod: idLista,          // Corresponde a "lc_cod" no backend
            lc_dtlista: dataLista,    // Corresponde a "lc_dtlista" no backend
            lc_desc: descricaoLista,  // Corresponde a "lc_desc" no backend
            func_cod: codFuncionario  // Corresponde a "func_cod" no backend
        };

        try {
            // Requisição PUT para /apis/lista (sem ID no path, pois o ID está no body)
            const response = await fetch(`http://localhost:8080/apis/lista`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.erro || `Erro HTTP: ${response.status}`);
            }

            const result = await response.json();
            // CORREÇÃO AQUI: Espera a chave "mensagem" do backend
            showMessage(successMessageElement, result.mensagem || "Lista de compras alterada com sucesso!", 'success-message');
            hideMessage(messageElement);
            
            // Opcional: Redirecionar para a página de consulta ou atualizar a exibição
            // setTimeout(() => {
            //     window.location.href = "ConsultaListaCompra.html";
            // }, 2000);

        } catch (error) {
            hideMessage(messageElement);
            console.error("Erro ao alterar lista de compras:", error);
            showMessage(errorMessageElement, `Erro ao alterar lista: ${error.message || error}`, 'error-message');
        }
    });

    // --- Chamada Inicial ---
    if (listaCompraId) {
        carregarDadosListaCompra(listaCompraId);
    } else {
        showMessage(errorMessageElement, "ID da Lista de Compra não encontrado para carregamento.", 'error-message');
    }
});