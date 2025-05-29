const form = document.getElementById('login-form');
const loginMessage = document.getElementById('login-message');

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  const loginInput = document.getElementById('login').value;
  const senhaInput = document.getElementById('senha').value;

  try {
    // Passo 1: Busca todos os funcionários (pode ser outro endpoint como /todos)
    const response = await fetch('http://localhost:8080/apis/funcionario');
    if (!response.ok) throw new Error('Erro ao buscar funcionários');

    const funcionarios = await response.json();

    // Passo 2: Filtra o funcionário com login e senha corretos
    const usuario = funcionarios.find(f => f.login === loginInput && f.senha === senhaInput);

    if (!usuario) {
      throw new Error('Login ou senha incorretos');
    }

    // Passo 3: Salva os dados no localStorage
    localStorage.setItem('nivelAcesso', usuario.nivel);
    localStorage.setItem('usuarioLogado', JSON.stringify(usuario));

    // Redireciona para tela principal
    window.location.href = 'menu.html';

  } catch (error) {
    loginMessage.textContent = 'Login ou senha incorretos.';
    loginMessage.style.color = 'red';
  }
});
