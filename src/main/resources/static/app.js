const $ = (id) => document.getElementById(id);
let eventoId = null;

const TRANSICOES = {
  RASCUNHO: ['ABERTO', 'CANCELADO'],
  ABERTO: ['ENCERRADO', 'CANCELADO'],
  ENCERRADO: ['REALIZADO', 'CANCELADO'],
  REALIZADO: [],
  CANCELADO: []
};

/* ---------- Funções de apoio ---------- */

// Fala com a API. Devolve o JSON ou lança um erro com a mensagem do servidor.
async function api(metodo, url, corpo) {
  const resp = await fetch(url, {
    method: metodo,
    headers: { 'Content-Type': 'application/json' },
    body: corpo ? JSON.stringify(corpo) : undefined
  });
  const texto = await resp.text();
  const dados = texto ? JSON.parse(texto) : null;
  if (!resp.ok) {
    throw new Error(dados && dados.erro ? dados.erro : 'Erro ' + resp.status);
  }
  return dados;
}

// Mostra uma mensagem verde (sucesso) ou vermelha (erro) no topo.
function aviso(texto, erro) {
  const caixa = $('msg');
  caixa.textContent = texto;
  caixa.className = erro ? 'erro' : 'ok';
  caixa.style.display = 'block';
}

// Executa uma ação e, se der erro, mostra o aviso vermelho.
async function executar(acao) {
  try {
    await acao();
  } catch (e) {
    aviso(e.message, true);
  }
}

// Evita que textos digitados quebrem o HTML.
function esc(t) {
  return String(t ?? '').replace(/[&<>"']/g, (c) =>
    ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
}

function formatar(dataHora) {
  return dataHora ? dataHora.replace('T', ' ').substring(0, 16) : '';
}

/* ---------- Eventos ---------- */

async function carregarEventos() {
  const eventos = await api('GET', '/api/eventos');
  $('listaEventos').innerHTML = eventos.map((e) => {
    const ROTULOS = {
      ABERTO: 'ABRIR',
      ENCERRADO: 'ENCERRAR',
      REALIZADO: 'REALIZAR',
      CANCELADO: 'CANCELAR'
    };
    const botoes = TRANSICOES[e.status]
      .map((novo) => `<button onclick="mudarStatus(${e.id}, '${novo}')">${ROTULOS[novo] || novo}</button>`)
      .join('');
    return `<tr>
      <td>${esc(e.nome)}</td><td>${esc(e.tipo)}</td>
      <td>${formatar(e.dataHora)}</td><td>${formatar(e.prazoAlteracoes)}</td>
      <td><span class="badge ${e.status}">${e.status}</span></td>
      <td><button onclick="selecionar(${e.id})">Abrir painel</button> ${botoes}</td>
    </tr>`;
  }).join('');
}

function criarEvento() {
  return executar(async () => {
    await api('POST', '/api/eventos', {
      nome: $('evNome').value,
      descricao: $('evDescricao').value,
      tipo: $('evTipo').value,
      dataHora: $('evDataHora').value || null,
      prazoAlteracoes: $('evPrazo').value || null
    });
    aviso('Evento criado.');
    await carregarEventos();
  });
}

function mudarStatus(id, novo) {
  return executar(async () => {
    await api('PATCH', `/api/eventos/${id}/status?status=${novo}`);
    aviso('Status alterado para ' + novo + '.');
    await carregarEventos();
    if (id === eventoId) await recarregarPainel();
  });
}

function selecionar(id) {
  return executar(async () => {
    eventoId = id;
    const evento = await api('GET', `/api/eventos/${id}`);
    $('tituloPainel').textContent = `Evento: ${evento.nome} (${evento.status})`;
    $('painel').style.display = 'block';
    await recarregarPainel();
    $('painel').scrollIntoView();
  });
}

async function recarregarPainel() {
  await carregarParticipantes();
  await carregarAlergenos();
  await carregarCategorias();
  await carregarAlertas();
  await carregarCobertura();
}

/* ---------- Participantes ---------- */

async function carregarParticipantes() {
  const todos = await api('GET', '/api/participantes');
  const inscritos = await api('GET', `/api/eventos/${eventoId}/participantes`);
  $('selParticipante').innerHTML = todos
    .map((p) => `<option value="${p.id}">${esc(p.nome)} (${esc(p.cpf)})</option>`).join('');
  $('contribParticipante').innerHTML = inscritos
    .map((p) => `<option value="${p.id}">${esc(p.nome)}</option>`).join('');
  $('listaInscritos').textContent = inscritos.map((p) => p.nome).join(', ') || 'ninguém ainda';
}

function criarParticipante() {
  return executar(async () => {
    await api('POST', '/api/participantes', {
      nome: $('pNome').value, email: $('pEmail').value, cpf: $('pCpf').value
    });
    aviso('Participante cadastrado.');
    await carregarParticipantes();
  });
}

function inscrever() {
  return executar(async () => {
    await api('POST', `/api/eventos/${eventoId}/participantes/${$('selParticipante').value}`);
    aviso('Participante inscrito no evento.');
    await carregarParticipantes();
  });
}

/* ---------- Alérgenos ---------- */

async function carregarAlergenos() {
  const lista = await api('GET', '/api/alergenos');
  const itemCheck = lista
    .map((a) => `<label><input type="checkbox" class="chkAlergeno" value="${a.id}"> ${esc(a.nome)}</label>`)
    .join(' ') || 'nenhum cadastrado';
  const restricaoCheck = lista
    .map((a) => `<label><input type="checkbox" class="chkRestricao" value="${a.id}"> ${esc(a.nome)}</label>`)
    .join(' ') || 'nenhum cadastrado';
  $('alergenosCheck').innerHTML = itemCheck;
  $('restricaoAlergenosCheck').innerHTML = restricaoCheck;
}

function criarAlergeno() {
  return executar(async () => {
    await api('POST', '/api/alergenos', { nome: $('alNome').value, descricao: $('alDescricao').value });
    aviso('Alérgeno cadastrado.');
    await carregarAlergenos();
  });
}

/* ---------- Categorias e itens ---------- */

async function carregarCategorias() {
  const categorias = await api('GET', `/api/eventos/${eventoId}/categorias`);
  let html = '';
  let opcoesCategoria = '';
  let opcoesItem = '';

  for (const c of categorias) {
    const itens = await api('GET', `/api/categorias/${c.id}/itens`);
    opcoesCategoria += `<option value="${c.id}">${esc(c.nome)}</option>`;
    let linhas = '';

    for (const i of itens) {
      const contribs = await api('GET', `/api/itens/${i.id}/contribuicoes`);
      opcoesItem += `<option value="${i.id}">${esc(c.nome)} - ${esc(i.nome)}</option>`;
      const alergenos = i.alergenos.map((a) => esc(a.nome)).join(', ') || '-';
      const lista = contribs.map((x) =>
        `${esc(x.participanteNome)}: ${x.quantidade} <button onclick="removerContribuicao(${x.id})">x</button>`
      ).join('<br>') || '-';
      linhas += `<tr>
        <td>${esc(i.nome)}</td><td>${i.quantidadeNecessaria}</td>
        <td>${i.obrigatorio ? 'Sim' : 'Não'}</td><td>${alergenos}</td>
        <td>${lista}</td>
        <td><button onclick="removerItem(${i.id})">Excluir</button></td>
      </tr>`;
    }

    html += `<h3>${esc(c.nome)} (meta: ${c.meta ?? '-'})
      <button onclick="removerCategoria(${c.id})">Excluir categoria</button></h3>
      <table>
        <tr><th>Item</th><th>Necessário</th><th>Obrigatório</th><th>Alérgenos</th><th>Contribuições</th><th></th></tr>
        ${linhas}
      </table>`;
  }

  $('listaCategorias').innerHTML = html || '<p>Nenhuma categoria ainda.</p>';
  $('itemCategoria').innerHTML = opcoesCategoria;
  $('contribItem').innerHTML = opcoesItem;
}

function criarCategoria() {
  return executar(async () => {
    await api('POST', `/api/eventos/${eventoId}/categorias`, {
      nome: $('catNome').value,
      meta: $('catMeta').value ? Number($('catMeta').value) : null
    });
    aviso('Categoria criada.');
    await carregarCategorias();
  });
}

function criarItem() {
  return executar(async () => {
    const alergenoIds = [...document.querySelectorAll('.chkAlergeno:checked')].map((c) => Number(c.value));
    await api('POST', `/api/categorias/${$('itemCategoria').value}/itens`, {
      nome: $('itemNome').value,
      descricao: '',
      quantidadeNecessaria: Number($('itemQtd').value),
      obrigatorio: $('itemObrig').checked,
      alergenoIds: alergenoIds
    });
    aviso('Item criado.');
    await recarregarPainel();
  });
}

function removerCategoria(id) {
  return executar(async () => {
    await api('DELETE', `/api/categorias/${id}`);
    aviso('Categoria excluída.');
    await recarregarPainel();
  });
}

function removerItem(id) {
  return executar(async () => {
    await api('DELETE', `/api/itens/${id}`);
    aviso('Item excluído.');
    await recarregarPainel();
  });
}

/* ---------- Contribuições ---------- */

function contribuir() {
  return executar(async () => {
    await api('POST', `/api/itens/${$('contribItem').value}/contribuicoes`, {
      participanteId: Number($('contribParticipante').value),
      quantidade: Number($('contribQtd').value)
    });
    aviso('Contribuição registrada.');
    await recarregarPainel();
  });
}

function removerContribuicao(id) {
  return executar(async () => {
    await api('DELETE', `/api/contribuicoes/${id}`);
    aviso('Contribuição removida.');
    await recarregarPainel();
  });
}

/* ---------- Cobertura ---------- */

async function carregarCobertura() {
  const c = await api('GET', `/api/eventos/${eventoId}/cobertura`);
  const linhas = c.categorias.map((k) => `<tr>
    <td>${esc(k.nome)}</td>
    <td>${k.totalContribuido} / ${k.totalNecessario}</td>
    <td>${k.percentual}%</td>
    <td>${k.meta == null ? '-' : (k.metaAtingida ? 'Atingida' : 'Não atingida (' + k.totalContribuido + '/' + k.meta + ')')}</td>
  </tr>`).join('');
  const pendentes = c.obrigatoriosPendentes.length
    ? `<p class="alerta">Obrigatórios pendentes: ${c.obrigatoriosPendentes.map(esc).join(', ')}</p>`
    : '<p>Nenhum item obrigatório pendente.</p>';

  $('cobertura').innerHTML = `
    <p class="grande">${c.percentual}%</p>
    <p>${c.totalContribuido} de ${c.totalNecessario} unidades cobertas</p>
    <progress value="${c.percentual}" max="100"></progress>
    ${pendentes}
    <table>
      <tr><th>Categoria</th><th>Contribuído / Necessário</th><th>Cobertura</th><th>Meta</th></tr>
      ${linhas}
    </table>`;
}
/* ---------- Restrições e alertas ---------- */

function criarRestricao() {
  return executar(async () => {
    const alergenoIds = [...document.querySelectorAll('.chkRestricao:checked')].map((c) => Number(c.value));
    await api('POST', `/api/participantes/${$('selParticipante').value}/restricoes`, {
      tipo: $('rTipo').value,
      observacoes: $('rObs').value,
      alergenoIds: alergenoIds
    });
    aviso('Restrição adicionada.');
    $('rTipo').value = '';
    $('rObs').value = '';
    await recarregarPainel();
  });
}

async function carregarAlertas() {
  const alertas = await api('GET', `/api/eventos/${eventoId}/alertas-alergenos`);
  if (alertas.length === 0) {
    $('alertas').innerHTML = '<p>Nenhum conflito entre os itens e as restrições dos inscritos.</p>';
    return;
  }
  const linhas = alertas.map((a) => `<tr>
    <td>${esc(a.itemNome)}</td>
    <td>${a.alergenos.map(esc).join(', ')}</td>
    <td class="alerta">${a.participantesAfetados.map(esc).join(', ')}</td>
  </tr>`).join('');
  $('alertas').innerHTML = `<table>
    <tr><th>Item</th><th>Contém</th><th>Atenção: afeta</th></tr>
    ${linhas}
  </table>`;
}
/* ---------- Início ---------- */
executar(carregarEventos);