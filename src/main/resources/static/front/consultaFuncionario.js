const tabelaFuncionarios = document.getElementById('tabela-funcionarios');
const messageConsulta = document.getElementById('message-consulta');
const filtroInput = document.getElementById('filtro-input');
const btnFiltrarNome = document.getElementById('filtrar-nome');
const btnFiltrarLogin = document.getElementById('filtrar-login');
const filtroStatus = document.getElementById('filtro-status');
const botaoNovo = document.getElementById('btn-novo');
const logoutBtn = document.getElementById('logout-btn');
const nomeUsuario = document.getElementById('usuario-logado');

// Nível de acesso do usuário logado
const nivelAcesso = parseInt(localStorage.getItem('nivelAcesso'), 10);
const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));

// Exibe o nome do usuário logado
nomeUsuario.textContent = `Logado como: ${usuarioLogado?.login || 'Desconhecido'}`;

// Mostra ou esconde o botão "Novo"
if (nivelAcesso >= 1) {
  botaoNovo.style.display = 'inline-block';
} else {
  botaoNovo.style.display = 'none';
}

let listaFuncionarios = [];
let filtroAtual = '';

async function carregarFuncionarios() {
  try {
    const response = await fetch('http://localhost:8080/apis/funcionario');
    if (!response.ok) throw new Error('Erro ao buscar funcionários.');

    const funcionarios = await response.json();
    listaFuncionarios = funcionarios;
    atualizarTabela(funcionarios);
    messageConsulta.textContent = '';
  } catch (error) {
    messageConsulta.style.color = '#e03e3e';
    messageConsulta.textContent = 'Erro ao carregar funcionários.';
  }
}

function atualizarTabela(funcionarios) {
  tabelaFuncionarios.innerHTML = '';
  funcionarios.forEach(f => {
    const func_cod = f.func_cod || f.id || 'N/A';
    const nome = f.nome || f.name || 'N/A';
    const cpf = f.cpf || 'N/A';
    const email = f.email || 'N/A';
    const login = f.login || 'N/A';
    const nivel = f.nivel || f.level || 'N/A';

    let acoes = '';

    if (nivelAcesso >= 2) {
      acoes += `
        <button class="edit-btn" 
          data-id="${func_cod}"
          data-nome="${encodeURIComponent(nome)}"
          data-cpf="${encodeURIComponent(cpf)}"
          data-email="${encodeURIComponent(email)}"
          data-login="${encodeURIComponent(login)}"
          data-nivel="${encodeURIComponent(nivel)}"
        >Alterar</button>`;
    }

    // Impede exclusão do usuário logado
    if (nivelAcesso >= 3 && usuarioLogado?.id !== func_cod) {
      acoes += `<button class="delete-btn" data-id="${func_cod}">Excluir</button>`;
    }

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${func_cod}</td>
      <td>${nome}</td>
      <td>${cpf}</td>
      <td>${email}</td>
      <td>${login}</td>
      <td>${nivel}</td>
      <td>${acoes}</td>
    `;
    tabelaFuncionarios.appendChild(tr);
  });
}

function aplicarFiltro() {
  const termo = filtroInput.value.trim().toLowerCase();

  if (!filtroAtual || !termo) {
    atualizarTabela(listaFuncionarios);
    filtroStatus.textContent = '';
    return;
  }

  const resultado = listaFuncionarios.filter(f => {
    const campo = filtroAtual === 'nome' ? f.nome : f.login;
    return campo && campo.toLowerCase().includes(termo);
  });

  atualizarTabela(resultado);
}

filtroInput.addEventListener('input', aplicarFiltro);

btnFiltrarNome.addEventListener('click', () => {
  filtroAtual = 'nome';
  filtroStatus.textContent = 'Filtrando por Nome';
  aplicarFiltro();
});

btnFiltrarLogin.addEventListener('click', () => {
  filtroAtual = 'login';
  filtroStatus.textContent = 'Filtrando por Login';
  aplicarFiltro();
});

async function excluirFuncionario(id) {
  // Bloqueia a exclusão do usuário logado
  if (usuarioLogado?.id === id) {
    alert('Você não pode excluir o usuário atualmente logado.');
    return;
  }

  if (!confirm(`Confirma exclusão do funcionário com ID: ${id}?`)) return;

  try {
    const response = await fetch(`http://localhost:8080/apis/funcionario/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) throw new Error('Erro ao excluir funcionário.');
    await carregarFuncionarios();
  } catch (error) {
    alert('Erro ao excluir funcionário.');
  }
}

tabelaFuncionarios.addEventListener('click', (e) => {
  const target = e.target;

  if (target.classList.contains('edit-btn')) {
    const id = target.getAttribute('data-id');
    const nome = decodeURIComponent(target.getAttribute('data-nome'));
    const cpf = decodeURIComponent(target.getAttribute('data-cpf'));
    const email = decodeURIComponent(target.getAttribute('data-email'));
    const login = decodeURIComponent(target.getAttribute('data-login'));
    const nivel = decodeURIComponent(target.getAttribute('data-nivel'));

    const url = `alterar.html?id=${encodeURIComponent(id)}&nome=${encodeURIComponent(nome)}&cpf=${encodeURIComponent(cpf)}&email=${encodeURIComponent(email)}&login=${encodeURIComponent(login)}&nivel=${encodeURIComponent(nivel)}`;
    window.location.href = url;
  }

  if (target.classList.contains('delete-btn')) {
    const id = target.getAttribute('data-id');
    excluirFuncionario(id);
  }
});

// Botão de logout
logoutBtn.addEventListener('click', () => {
  localStorage.removeItem('usuarioLogado');
  localStorage.removeItem('nivelAcesso');
  window.location.href = 'login.html';
});

// Inicializa carregamento da lista
carregarFuncionarios();