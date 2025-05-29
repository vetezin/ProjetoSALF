// Função para validar CPF
function validarCPF(cpf) {
  cpf = cpf.replace(/[^\d]+/g, '');

  if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

  let soma = 0;
  for (let i = 0; i < 9; i++) soma += parseInt(cpf.charAt(i)) * (10 - i);
  let resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpf.charAt(9))) return false;

  soma = 0;
  for (let i = 0; i < 10; i++) soma += parseInt(cpf.charAt(i)) * (11 - i);
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  return resto === parseInt(cpf.charAt(10));
}

// Aplicar máscara no CPF
document.addEventListener('DOMContentLoaded', () => {
  const cpfInput = document.getElementById('func_cpf');
  Inputmask("999.999.999-99").mask(cpfInput);
});

// Validações adicionais para CPF e login, integradas ao Bootstrap
const form = document.getElementById('formCadastro');
const message = document.getElementById('message');

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  event.stopPropagation();

  // Limpa mensagens anteriores
  message.textContent = '';
  message.style.color = '';

  // Campos
  const nome = form.func_nome.value.trim();
  const cpf = form.func_cpf.value.trim();
  const senha = form.func_senha.value.trim();
  const email = form.func_email.value.trim();
  const login = form.func_login.value.trim();
  const nivel = form.func_nivel.value;

  // Validações padrão Bootstrap
  if (!form.checkValidity()) {
    form.classList.add('was-validated');
    return;
  }

  // Validação CPF customizada
  if (!validarCPF(cpf)) {
    const cpfInput = form.func_cpf;
    cpfInput.classList.add('is-invalid');
    cpfInput.setCustomValidity('CPF inválido');
    form.classList.add('was-validated');
    return;
  } else {
    form.func_cpf.classList.remove('is-invalid');
    form.func_cpf.setCustomValidity('');
  }

  // Validação login (mínimo 4 caracteres, sem espaços)
  const loginPattern = /^[a-zA-Z0-9._-]{4,}$/;
  if (!loginPattern.test(login)) {
    const loginInput = form.func_login;
    loginInput.classList.add('is-invalid');
    loginInput.setCustomValidity('Login inválido');
    form.classList.add('was-validated');
    return;
  } else {
    form.func_login.classList.remove('is-invalid');
    form.func_login.setCustomValidity('');
  }

  // Buscar funcionários existentes para evitar duplicidade
  try {
    const response = await fetch('http://localhost:5050/apis/funcionario');
    if (!response.ok) throw new Error('Erro ao buscar funcionários.');
    const funcionarios = await response.json();

    const cpfExistente = funcionarios.find(f => f.cpf === cpf);
    const loginExistente = funcionarios.find(f => f.login === login);

    if (cpfExistente) {
      message.style.color = '#e03e3e';
      message.textContent = 'Este CPF já está cadastrado.';
      return;
    }

    if (loginExistente) {
      message.style.color = '#e03e3e';
      message.textContent = 'Este login já está em uso.';
      return;
    }
  } catch (error) {
    message.style.color = '#e03e3e';
    message.textContent = 'Erro ao conectar com o servidor.';
    return;
  }

  // Preparar dados para envio
  const data = new URLSearchParams();
  data.append('func_nome', nome);
  data.append('func_cpf', cpf);
  data.append('func_senha', senha);
  data.append('func_email', email);
  data.append('func_login', login);
  data.append('func_nivel', nivel);

  // Enviar cadastro
  try {
    const res = await fetch('http://localhost:5050/apis/funcionario', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: data.toString(),
    });
    const result = await res.json();

    if (res.ok) {
      message.style.color = '#4caf50';
      message.textContent = 'Funcionário cadastrado com sucesso!';
      form.reset();
      form.classList.remove('was-validated');
    } else {
      message.style.color = '#e03e3e';
      message.textContent = result.message || 'Erro ao cadastrar funcionário.';
    }
  } catch (error) {
    message.style.color = '#e03e3e';
    message.textContent = 'Erro ao conectar com o servidor.';
  }
});
