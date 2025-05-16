const tabelaFuncionarios = document.getElementById('tabela-funcionarios');
const messageConsulta = document.getElementById('message-consulta');

// Função para carregar funcionários e preencher a tabela
async function carregarFuncionarios() {
  try {
    const response = await fetch('http://localhost:5050/apis/funcionario');
    if (!response.ok) throw new Error('Erro ao buscar funcionários.');

    const funcionarios = await response.json();

    tabelaFuncionarios.innerHTML = '';

    funcionarios.forEach(f => {
      // Ajuste os nomes das propriedades conforme seu JSON real aqui:
      const func_cod = f.func_cod || f.id || 'N/A';
      const nome = f.nome || f.name || 'N/A';
      const cpf = f.cpf || 'N/A';
      const email = f.email || 'N/A';
      const login = f.login || 'N/A';
      const nivel = f.nivel || f.level || 'N/A';

      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${func_cod}</td>
        <td>${nome}</td>
        <td>${cpf}</td>
        <td>${email}</td>
        <td>${login}</td>
        <td>${nivel}</td>
        <td><button class="delete-btn" data-cpf="${cpf}">Excluir</button></td>
      `;

      tabelaFuncionarios.appendChild(tr);
    });

    messageConsulta.textContent = '';

  } catch (error) {
    messageConsulta.style.color = '#e03e3e';
    messageConsulta.textContent = 'Erro ao carregar funcionários.';
  }
}

// Função para excluir funcionário pelo CPF
async function excluirFuncionario(cpf) {
  if (!confirm(`Confirma exclusão do funcionário com CPF: ${cpf}?`)) return;

  try {
    const response = await fetch(`http://localhost:5050/apis/funcionario/${cpf}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Erro ao excluir funcionário.');

    alert('Funcionário excluído com sucesso!');
    carregarFuncionarios();  // Recarrega a lista após exclusão

  } catch (error) {
    alert('Erro ao excluir funcionário.');
  }
}

// Evento delegado para o botão excluir
tabelaFuncionarios.addEventListener('click', (event) => {
  if (event.target.classList.contains('delete-btn')) {
    const cpf = event.target.getAttribute('data-cpf');
    excluirFuncionario(cpf);
  }
});

// Carrega funcionários ao abrir a página
carregarFuncionarios();
