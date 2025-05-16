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
        <td><button class="delete-btn" data-id="${func_cod}">Excluir</button></td>
      `;

      tabelaFuncionarios.appendChild(tr);
    });

    messageConsulta.textContent = '';

  } catch (error) {
    messageConsulta.style.color = '#e03e3e';
    messageConsulta.textContent = 'Erro ao carregar funcionários.';
  }
}

// Função para excluir funcionário pelo ID
async function excluirFuncionario(id) {
  if (!confirm(`Confirma exclusão do funcionário com ID: ${id}?`)) return;

  try {
    const response = await fetch(`http://localhost:5050/apis/funcionario/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      const erroTexto = await response.text();
      console.error('Erro ao excluir:', erroTexto);
      throw new Error('Erro ao excluir funcionário.');
    }

    alert('Funcionário excluído com sucesso!');
    carregarFuncionarios();  // Recarrega a lista após exclusão

  } catch (error) {
    console.error(error);
    alert('Erro ao excluir funcionário.');
  }
}

// Evento delegado para o botão excluir
tabelaFuncionarios.addEventListener('click', (event) => {
  if (event.target.classList.contains('delete-btn')) {
    const id = event.target.getAttribute('data-id');
    excluirFuncionario(id);
  }
});

// Carrega funcionários ao abrir a página
carregarFuncionarios();
