const form = document.getElementById('form-cadastro');
const message = document.getElementById('message');

window.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);

  if (params.has('id')) {
    document.getElementById('func_cod').value = params.get('id');
    document.getElementById('nome').value = params.get('nome') || '';
    document.getElementById('cpf').value = params.get('cpf') || '';
    document.getElementById('senha').value = ''; // senha não deve vir na URL, então vazio
    document.getElementById('email').value = params.get('email') || '';
    document.getElementById('login').value = params.get('login') || '';
    document.getElementById('nivel').value = params.get('nivel') || '';
  }
});

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  message.textContent = '';
  message.style.color = '#fff';

  const func_cod = document.getElementById('func_cod').value.trim();
  const nome = document.getElementById('nome').value.trim();
  const cpf = document.getElementById('cpf').value.trim();
  const senha = document.getElementById('senha').value.trim();
  const email = document.getElementById('email').value.trim();
  const login = document.getElementById('login').value.trim();
  const nivel = document.getElementById('nivel').value.trim();

  try {
    let response;

    if (func_cod) {
      // Atualizar via PUT com application/x-www-form-urlencoded
      const params = new URLSearchParams();
      params.append('func_cod', func_cod);
      params.append('func_nome', nome);
      params.append('func_cpf', cpf);
      params.append('func_senha', senha);
      params.append('func_email', email);
      params.append('func_login', login);
      params.append('func_nivel', nivel);

      response = await fetch('http://localhost:5050/apis/funcionario', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params.toString(),
      });
    } else {
      // Criar via POST enviando JSON
      const funcionario = { nome, cpf, senha, email, login, nivel };

      response = await fetch('http://localhost:5050/apis/funcionario', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(funcionario),
      });
    }

    if (!response.ok) {
      const errText = await response.text();
      throw new Error(errText || 'Erro ao salvar funcionário');
    }

    message.style.color = '#7eff7e';
    message.textContent = 'Funcionário salvo com sucesso!';

    if (!func_cod) form.reset();

  } catch (error) {
    message.style.color = '#e03e3e';
    message.textContent = 'Erro ao salvar funcionário.';
    console.error(error);
  }
});
