const formCadastro = document.getElementById('formCadastro');
const message = document.getElementById('message');

formCadastro.addEventListener('submit', async (e) => {
  e.preventDefault();

  const data = new URLSearchParams();
  data.append('func_nome', formCadastro.func_nome.value.trim());
  data.append('func_cpf', formCadastro.func_cpf.value.trim());
  data.append('func_senha', formCadastro.func_senha.value.trim());
  data.append('func_email', formCadastro.func_email.value.trim());
  data.append('func_login', formCadastro.func_login.value.trim());
  data.append('func_nivel', formCadastro.func_nivel.value);

  try {
    const response = await fetch('http://localhost:5050/apis/funcionario', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: data.toString(),
    });

    const result = await response.json();

    if (response.ok) {
      message.style.color = '#4caf50';
      message.textContent = 'Funcionário cadastrado com sucesso!';
      formCadastro.reset();
    } else {
      message.style.color = '#e03e3e';
      message.textContent = result.mensagem || 'Erro ao cadastrar funcionário.';
    }
  } catch (error) {
    message.style.color = '#e03e3e';
    message.textContent = 'Erro ao conectar com o servidor.';
  }
});
