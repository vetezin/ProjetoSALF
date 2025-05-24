window.addEventListener('DOMContentLoaded', () => {
  const listaProdutosDiv = document.getElementById('listaProdutos');
  const form = document.getElementById('listaCompraForm');

  // Função para carregar produtos da API
  async function carregarProdutos() {
    try {
      const res = await fetch('http://localhost:5050/apis/produto');
      if (!res.ok) throw new Error('Erro ao carregar produtos');
      const produtos = await res.json();

      listaProdutosDiv.innerHTML = '';

      produtos.forEach(produto => {
        const produtoDiv = document.createElement('div');
        produtoDiv.classList.add('produto');

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.id = 'produto_' + produto.codProduto;
        checkbox.name = 'produtosSelecionados';
        checkbox.value = produto.codProduto;

        const label = document.createElement('label');
        label.htmlFor = checkbox.id;

        // Aqui tentamos vários campos para nome do produto
        label.textContent = produto.nomeProduto || produto.nome || produto.descricao || 'Produto sem nome';

        const quantidadeInput = document.createElement('input');
        quantidadeInput.type = 'number';
        quantidadeInput.min = 1;
        quantidadeInput.value = 1;
        quantidadeInput.classList.add('quantidade');
        quantidadeInput.style.display = 'none';
        quantidadeInput.id = 'quantidade_' + produto.codProduto;
        quantidadeInput.name = 'quantidade_' + produto.codProduto;

        checkbox.addEventListener('change', () => {
          if (checkbox.checked) {
            quantidadeInput.style.display = 'inline-block';
            quantidadeInput.focus();
          } else {
            quantidadeInput.style.display = 'none';
            quantidadeInput.value = 1;
          }
        });

        produtoDiv.appendChild(checkbox);
        produtoDiv.appendChild(label);
        produtoDiv.appendChild(quantidadeInput);

        listaProdutosDiv.appendChild(produtoDiv);
      });
    } catch (error) {
      alert('Erro ao carregar produtos: ' + error.message);
    }
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const dataCompra = document.getElementById('dataCompra').value;
    const codFuncionario = document.getElementById('codFuncionario').value;

    if (!dataCompra) {
      alert('Por favor, informe a data da compra.');
      return;
    }
    if (!codFuncionario || codFuncionario <= 0) {
      alert('Por favor, informe um código de funcionário válido.');
      return;
    }

    const produtosSelecionados = [];
    const checkboxes = form.querySelectorAll('input[name="produtosSelecionados"]:checked');

    if (checkboxes.length === 0) {
      alert('Selecione pelo menos um produto.');
      return;
    }

    for (const checkbox of checkboxes) {
      const codProduto = checkbox.value;
      const quantidadeInput = document.getElementById('quantidade_' + codProduto);
      const quantidade = parseInt(quantidadeInput.value);

      // Pega o nome do produto do label (irmão do checkbox)
      let nomeProduto = 'Produto desconhecido';
      const label = checkbox.nextElementSibling;
      if (label) nomeProduto = label.textContent;

      if (isNaN(quantidade) || quantidade < 1) {
        alert(`Informe uma quantidade válida para o produto ${nomeProduto}.`);
        return;
      }

      produtosSelecionados.push({
        codProduto: codProduto,
        quantidade: quantidade,
      });
    }

    const listaCompra = {
      dataCompra: dataCompra,
      codFuncionario: codFuncionario,
      produtos: produtosSelecionados,
    };

    console.log('Enviando lista de compra:', listaCompra);

    try {
      const res = await fetch('http://localhost:5050/apis/listaCompra', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // CORS NÃO É RESOLVIDO AQUI, tem que ser no backend
        },
        body: JSON.stringify(listaCompra),
      });

      if (!res.ok) throw new Error('Erro ao salvar lista de compras');

      alert('Lista de compras salva com sucesso!');
      form.reset();

      // Esconder inputs de quantidade após reset
      const quantInputs = form.querySelectorAll('.quantidade');
      quantInputs.forEach(q => (q.style.display = 'none'));
    } catch (err) {
      alert('Erro ao salvar lista de compras: ' + err.message);
    }
  });

  carregarProdutos();
});
