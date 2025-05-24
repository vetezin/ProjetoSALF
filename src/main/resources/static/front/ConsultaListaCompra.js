const tabelaCompras = document.getElementById('tabela-compras');
const messageConsulta = document.getElementById('message-consulta');
const filtroInput = document.getElementById('filtro-input');
const btnFiltrarData = document.getElementById('filtrar-data');
const btnFiltrarStatus = document.getElementById('filtrar-status');
const filtroStatus = document.getElementById('filtro-status');
const botaoNovo = document.getElementById('btn-novo');
const logoutBtn = document.getElementById('logout-btn');
const nomeUsuario = document.getElementById('usuario-logado');

// Nível de acesso do usuário logado
const nivelAcesso = parseInt(localStorage.getItem('nivelAcesso'), 10);
const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));

// Exibe o nome do usuário logado
nomeUsuario.textContent = `Logado como: ${usuarioLogado?.login || 'Desconhecido'}`;

// Mostra ou esconde o botão de "Novo" com base no nível de acesso
if (nivelAcesso < 2) {
  botaoNovo.style.display = 'none';
}

let listaCompras = [];
let filtroAtual = '';

async function carregarCompras(filtro = '') {
  try {
    const url = filtro
      ? `http://localhost:5050/apis/lista?filtro=${filtro}`
      : 'http://localhost:5050/apis/lista/all';

    const response = await fetch(url);
    if (!response.ok) throw new Error('Erro ao carregar as compras.');
    
    listaCompras = await response.json();
    atualizarTabela(listaCompras);
  } catch (error) {
    console.error(error);
    messageConsulta.textContent = 'Erro ao carregar lista de compras.';
  }
}

// Atualiza a tabela com a lista de compras
function atualizarTabela(compras) {
  tabelaCompras.innerHTML = '';
  if (compras.length === 0) {
    messageConsulta.textContent = 'Nenhuma compra encontrada.';
    return;
  }

  messageConsulta.textContent = ''; // Limpa mensagens anteriores

  compras.forEach(compra => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${compra.id}</td>
      <td>${compra.descricao}</td>
      <td>${compra.funcionarioId}</td>
      <td>${compra.dataCriacao || 'N/A'}</td>
      <td>
        <button class="edit-btn" data-id="${compra.id}">Editar</button>
        <button class="delete-btn" data-id="${compra.id}">Excluir</button>
      </td>
    `;
    tabelaCompras.appendChild(tr);
  });
}

// Filtra compras por data ou status
function aplicarFiltro() {
  const termo = filtroInput.value.trim().toLowerCase();

  if (!filtroAtual || !termo) {
    carregarCompras();
    filtroStatus.textContent = '';
    return;
  }

  carregarCompras(termo);
}

filtroInput.addEventListener('input', aplicarFiltro);

btnFiltrarData.addEventListener('click', () => {
  filtroAtual = 'data';
  filtroStatus.textContent = 'Filtrando por Data';
  aplicarFiltro();
});

btnFiltrarStatus.addEventListener('click', () => {
  filtroAtual = 'status';
  filtroStatus.textContent = 'Filtrando por Status';
  aplicarFiltro();
});

// Excluir compra
async function excluirCompra(id) {
  if (!confirm(`Confirma exclusão da compra com ID: ${id}?`)) return;

  try {
    const response = await fetch(`http://localhost:5050/apis/lista/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) throw new Error('Erro ao excluir compra.');
    await carregarCompras();
  } catch (error) {
    alert('Erro ao excluir compra.');
  }
}

// Lidar com botões da tabela
tabelaCompras.addEventListener('click', (e) => {
  const target = e.target;
  const id = target.getAttribute('data-id');
  
  if (target.classList.contains('edit-btn')) {
    window.location.href = `detalhesCompra.html?id=${id}`;
  }

  if (target.classList.contains('delete-btn')) {
    excluirCompra(id);
  }
});

// Logout
logoutBtn.addEventListener('click', () => {
  localStorage.removeItem('usuarioLogado');
  localStorage.removeItem('nivelAcesso');
  window.location.href = 'login.html';
});

// Carrega as compras na inicialização
carregarCompras();

// Função para cadastrar uma nova lista de compras
async function cadastrarLista(descricao, funcionarioId, dataCriacao) {
  try {
    const response = await fetch('http://localhost:5050/apis/lista', {
      method: 'POST',
      body: new URLSearchParams({
        descricao,
        funcionarioId,
        dataCriacao,
      }),
    });

    const resultado = await response.json();
    if (!response.ok) throw new Error(resultado.mensagem || 'Erro ao cadastrar lista.');
    alert('Lista cadastrada com sucesso!');
    carregarCompras();
  } catch (error) {
    alert(`Erro: ${error.message}`);
  }
}
