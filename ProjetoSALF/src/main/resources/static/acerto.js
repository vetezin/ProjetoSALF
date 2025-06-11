 document.addEventListener("DOMContentLoaded", () => {
            listarAcertosComProdutos();
        });

        function mostrarToast(mensagem, duracao = 3000) {
            let toast = document.getElementById("mensagemToast");
            toast.textContent = mensagem;
            toast.style.display = "block";

            setTimeout(() => {
                toast.style.display = "none";
            }, duracao);
        }

        function apagarAcerto(id) {
            if (!id) {
                alert("ID do acerto não informado.");
                return;
            }

            fetch(`http://localhost:8080/apis/acertoestoque/deletar/${id}`, {
                method: "DELETE",
            })
                .then((response) =>
                    response.json().then((data) => ({ status: response.status, body: data }))
                )
                .then(({ status, body }) => {
                    if (status === 200) {
                        mostrarToast(body.mensagem || "Acerto apagado com sucesso!");
                        listarAcertosComProdutos();
                    } else {
                        mostrarToast(body.mensagem || "Erro ao apagar acerto");
                    }
                })
                .catch((error) => {
                    console.error("Erro ao apagar acerto:", error);
                    mostrarToast("Erro ao apagar acerto");
                });
        }

      

                    async function listarAcertosComProdutos() {
    let tbody = document.querySelector("#tabelaAcertos tbody");
    tbody.innerHTML = "<tr><td colspan='7'>Carregando...</td></tr>"; // colspan 7 pois são 7 colunas na tabela

    try {
        let response = await fetch("http://localhost:8080/apis/acertoestoque/listar-com-produtos");
        if (!response.ok) {
            tbody.innerHTML = `<tr><td colspan="7">Erro ao buscar acertos: ${response.status}</td></tr>`;
            return;
        }

        let acertos = await response.json();

        if (!Array.isArray(acertos) || acertos.length === 0) {
            tbody.innerHTML = "<tr><td colspan='7'>Nenhum acerto encontrado.</td></tr>";
            return;
        }

        tbody.innerHTML = ""; // limpa antes de preencher

        acertos.forEach(acerto => {
            let produtosNomes = acerto.produto ? acerto.produto.descricao : "-";
            let nomeFuncionario = acerto.funcionario ? acerto.funcionario.nome : "Desconhecido";

            let tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${acerto.id}</td>
                <td>${new Date(acerto.data).toLocaleDateString('pt-BR')}</td>
                <td>${acerto.motivo}</td>
                <td>${nomeFuncionario}</td>
                <td>${produtosNomes}</td>
                <td>${acerto.quantidade}</td>
                <td class="acoes">
                    <button class="excluir btn btn-danger btn-sm" onclick="apagarAcerto(${acerto.id})">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Erro ao listar acertos:", error);
        tbody.innerHTML = "<tr><td colspan='7'>Erro ao carregar dados.</td></tr>";
    }
}

         async function ordenarPorFuncionarioAcerto() {
        let tbody = document.querySelector("#tabelaAcertos tbody");
        tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

        try {
            let response = await fetch("http://localhost:8080/apis/acertoestoque/listar-com-produtos");
            if (!response.ok) {
                tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar acertos: ${response.status}</td></tr>`;
                return;
            }

            let acertos = await response.json();

            if (!Array.isArray(acertos) || acertos.length === 0) {
                tbody.innerHTML = "<tr><td colspan='6'>Nenhum acerto encontrado.</td></tr>";
                return;
            }

           
            acertos.sort((a, b) => {
                let nomeA = a.funcionario && a.funcionario.nome ? a.funcionario.nome.toLowerCase() : "";
                let nomeB = b.funcionario && b.funcionario.nome ? b.funcionario.nome.toLowerCase() : "";
                if (nomeA < nomeB) return -1;
                if (nomeA > nomeB) return 1;
                return 0;
            });

            tbody.innerHTML = ""; 
            acertos.forEach(acerto => {
                let produtosNomes = acerto.produto ? acerto.produto.descricao : "-";
                let nomeFuncionario = acerto.funcionario ? acerto.funcionario.nome : "Desconhecido";

                let tr = document.createElement("tr");
                tr.innerHTML = `
                <td>${acerto.id}</td>
                <td>${new Date(acerto.data).toLocaleDateString('pt-BR')}</td>
                <td>${acerto.motivo}</td>
                <td>${nomeFuncionario}</td>
                <td>${produtosNomes}</td>
                <td class="acoes">
                <button class="excluir btn btn-danger btn-sm" onclick="apagarAcerto(${acerto.id})">Excluir</button>
                </td>
                `;
                tbody.appendChild(tr);
            });

        } catch (error) {
            console.error("Erro ao ordenar acertos:", error);
            tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
        }
    }

    async function ordenarPorMotivoAcerto() {
    let tbody = document.querySelector("#tabelaAcertos tbody");
    tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

    try {
        let response = await fetch("http://localhost:8080/apis/acertoestoque/listar-com-produtos");
        if (!response.ok) {
            tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar acertos: ${response.status}</td></tr>`;
            return;
        }

        let acertos = await response.json();

        if (!Array.isArray(acertos) || acertos.length === 0) {
            tbody.innerHTML = "<tr><td colspan='6'>Nenhum acerto encontrado.</td></tr>";
            return;
        }

       
        acertos.sort((a, b) => {
            let motivoA = a.motivo ? a.motivo.toLowerCase() : "";
            let motivoB = b.motivo ? b.motivo.toLowerCase() : "";
            if (motivoA < motivoB) return -1;
            if (motivoA > motivoB) return 1;
            return 0;
        });

        tbody.innerHTML = ""; 

        acertos.forEach(acerto => {
            let produtosNomes = acerto.produto ? acerto.produto.descricao : "-";
            let nomeFuncionario = acerto.funcionario ? acerto.funcionario.nome : "Desconhecido";

            let tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${acerto.id}</td>
                <td>${new Date(acerto.data).toLocaleDateString('pt-BR')}</td>
                <td>${acerto.motivo}</td>
                <td>${nomeFuncionario}</td>
                <td>${produtosNomes}</td>
                <td class="acoes">
                    <button class="excluir btn btn-danger btn-sm" onclick="apagarAcerto(${acerto.id})">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Erro ao ordenar acertos:", error);
        tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
    }
    }

    async function buscarPorMotivo() {
    let termo = document.getElementById("buscaMotivo").value.toLowerCase();

    let tbody = document.querySelector("#tabelaAcertos tbody");
    tbody.innerHTML = "<tr><td colspan='6'>Buscando...</td></tr>";

    try {
        let response = await fetch("http://localhost:8080/apis/acertoestoque/listar-com-produtos");
        if (!response.ok) {
            tbody.innerHTML = `<tr><td colspan="6">Erro ao buscar acertos: ${response.status}</td></tr>`;
            return;
        }

        let acertos = await response.json();
        let resultados = acertos.filter(acerto => 
            acerto.motivo && acerto.motivo.toLowerCase().includes(termo)
        );

        if (resultados.length === 0) {
            tbody.innerHTML = "<tr><td colspan='6'>Nenhum acerto encontrado com esse motivo.</td></tr>";
            return;
        }

        tbody.innerHTML = "";
        resultados.forEach(acerto => {
            let produtosNomes = acerto.produto ? acerto.produto.descricao : "-";
            let nomeFuncionario = acerto.funcionario ? acerto.funcionario.nome : "Desconhecido";

            let tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${acerto.id}</td>
                <td>${new Date(acerto.data).toLocaleDateString('pt-BR')}</td>
                <td>${acerto.motivo}</td>
                <td>${nomeFuncionario}</td>
                <td>${produtosNomes}</td>
                <td>${acerto.quantidade}</td>
                <td class="acoes">
                    <button class="excluir btn btn-danger btn-sm" onclick="apagarAcerto(${acerto.id})">Excluir</button>
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Erro ao buscar acertos:", error);
        tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar dados.</td></tr>";
    }
}
