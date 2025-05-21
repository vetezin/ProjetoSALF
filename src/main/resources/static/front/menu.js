const logoutBtn = document.getElementById('logout-btn');
const nomeUsuario = document.getElementById('usuario-logado');

// Exibe o nome do usuário logado
const usuarioLogado = JSON.parse(localStorage.getItem('usuarioLogado'));
nomeUsuario.textContent = `Logado como: ${usuarioLogado?.login || 'Desconhecido'}`;

// Função para abrir página de tema
function abrirPagina(pagina) {
  window.location.href = pagina;
}

// Logout
logoutBtn.addEventListener('click', () => {
  localStorage.removeItem('usuarioLogado');
  localStorage.removeItem('nivelAcesso');
  window.location.href = 'login.html';
});
