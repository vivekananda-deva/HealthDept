'use strict';
const $ = (s, r = document) => r.querySelector(s);
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const STATUSES = ['Scheduled', 'Completed', 'Cancelled'];
const iso = d => { const z = new Date(d.getTime() - d.getTimezoneOffset() * 6e4); return z.toISOString().slice(0, 10); };
const fmtDate = s => s ? new Date(s + 'T00:00').toLocaleDateString(undefined, { day: 'numeric', month: 'short', year: 'numeric' }) : '';

/* ---------- API ---------- */
async function api(url, method = 'GET', body) {
  const res = await fetch(url, { method, headers: body ? { 'Content-Type': 'application/json' } : {}, body: body ? JSON.stringify(body) : undefined });
  const text = await res.text();
  let data = null; try { data = text ? JSON.parse(text) : null; } catch { /* non-JSON */ }
  if (!res.ok) throw Object.assign(new Error(data?.message || 'Request failed (' + res.status + ')'), { fieldErrors: data?.fieldErrors || {} });
  return data;
}
const H = '/hospital/api/v1/', D = '/doctor/api/v1/', P = '/patient/api/v1/', A = '/appointment/api/v1/';
const load = {
  hospitals: () => api(H + 'getHospitals'), doctors: () => api(D + 'getDoctors'),
  patients: () => api(P + 'getPatients'), appointments: () => api(A + 'getAllAppointments'),
};

/* ---------- UI helpers ---------- */
function toast(msg, kind = 'success') {
  const el = document.createElement('div');
  el.className = `toast align-items-center text-bg-${kind} border-0`;
  el.innerHTML = `<div class="d-flex"><div class="toast-body">${esc(msg)}</div><button class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button></div>`;
  $('#toasts').append(el);
  const t = new bootstrap.Toast(el, { delay: 3500 }); t.show(); el.addEventListener('hidden.bs.toast', () => el.remove());
}
const modalEl = $('#modal'), modal = new bootstrap.Modal(modalEl);
function openModal({ title, body, ok, danger, onOk }) {
  $('#mTitle').textContent = title; $('#mBody').innerHTML = body;
  const b = $('#mOk'); b.textContent = ok; b.className = 'btn ' + (danger ? 'btn-danger' : 'btn-primary'); b.disabled = false;
  b.onclick = async () => { b.disabled = true; try { await onOk(); } catch (e) { b.disabled = false; } };
  modal.show(); setTimeout(() => $('#mBody input,#mBody select')?.focus(), 300);
}
const badge = s => `<span class="badge-st st-${STATUSES.includes(s) ? s : 'Other'}">${esc(s)}</span>`;
const skeleton = '<div class="panel"><div class="skeleton"></div><div class="skeleton"></div><div class="skeleton"></div></div>';
const empty = (icon, text) => `<div class="empty"><i class="bi ${icon}"></i>${text}</div>`;
const setView = html => { $('#view').innerHTML = html; };
const errorView = e => setView(`<div class="alert alert-danger"><strong>Can't reach the server.</strong> ${esc(e.message)} Check that the app and database are running, then reload.</div>`);

/* ---------- Generic CRUD page ---------- */
const sel = (rows, val, label) => rows.map(r => ({ value: r[val], label: label(r) }));
const ENT = {
  hospitals: {
    title: 'Hospitals', one: 'hospital', icon: 'bi-hospital', idKey: 'hospitalId', base: H, create: 'insertHs',
    search: r => `${r.hospitalName} ${r.location}`,
    fields: () => [{ n: 'hospitalName', l: 'Hospital name', req: 1 }, { n: 'location', l: 'Location', req: 1 }],
    cols: [['Hospital', r => `<strong>${esc(r.hospitalName)}</strong>`], ['Location', r => esc(r.location)], ['Doctors', r => (r.doctors || []).length]],
  },
  doctors: {
    title: 'Doctors', one: 'doctor', icon: 'bi-person-badge', idKey: 'doctorId', base: D, create: 'insertDoc', deps: ['hospitals'],
    search: r => `${r.doctorName} ${r.specialization} ${r.hospital?.hospitalName || ''}`,
    fields: x => [{ n: 'doctorName', l: 'Doctor name', req: 1 }, { n: 'specialization', l: 'Specialization', req: 1 },
      { n: 'hospitalId', l: 'Hospital', type: 'select', req: 1, opts: sel(x.hospitals, 'hospitalId', h => `${h.hospitalName} (${h.location})`) }],
    init: r => ({ ...r, hospitalId: r.hospital?.hospitalId }),
    cols: [['Doctor', r => `<strong>${esc(r.doctorName)}</strong>`], ['Specialization', r => esc(r.specialization)], ['Hospital', r => esc(r.hospital?.hospitalName || '—')]],
  },
  patients: {
    title: 'Patients', one: 'patient', icon: 'bi-person-lines-fill', idKey: 'patientId', base: P, create: 'insertPa',
    search: r => `${r.patientName} ${r.phone}`,
    fields: () => [{ n: 'patientName', l: 'Patient name', req: 1 }, { n: 'phone', l: 'Phone (10 digits)', req: 1, attrs: 'inputmode="numeric" maxlength="10" pattern="[0-9]{10}"' }],
    cols: [['Patient', r => `<strong>${esc(r.patientName)}</strong>`], ['Phone', r => esc(r.phone)]],
  },
  appointments: {
    title: 'Appointments', one: 'appointment', icon: 'bi-calendar2-check', idKey: 'appointmentId', base: A, create: 'insertAp', deps: ['doctors', 'patients'],
    search: r => `${r.patient?.patientName || ''} ${r.doctor?.doctorName || ''} ${r.status}`,
    fields: x => [
      { n: 'patientId', l: 'Patient', type: 'select', req: 1, opts: sel(x.patients, 'patientId', p => `${p.patientName} · ${p.phone}`) },
      { n: 'doctorId', l: 'Doctor', type: 'select', req: 1, opts: sel(x.doctors, 'doctorId', d => `${d.doctorName} · ${d.specialization}`) },
      { n: 'appointmentDate', l: 'Date', type: 'date', req: 1 },
      { n: 'status', l: 'Status', type: 'select', req: 1, opts: STATUSES.map(s => ({ value: s, label: s })) }],
    init: r => ({ ...r, patientId: r.patient?.patientId, doctorId: r.doctor?.doctorId }),
    cols: [['Date', r => `<strong>${fmtDate(r.appointmentDate)}</strong>`], ['Patient', r => esc(r.patient?.patientName || '—')],
      ['Doctor', r => esc(r.doctor?.doctorName || '—')], ['Status', r => badge(r.status)]],
    filters: STATUSES,
  },
};

async function crudPage(key) {
  const E = ENT[key]; setView(skeleton);
  let rows, deps = {};
  try {
    [rows] = await Promise.all([load[key](), ...(E.deps || []).map(async d => { deps[d] = await load[d](); })]);
  } catch (e) { return errorView(e); }
  if (key === 'appointments') rows.sort((a, b) => b.appointmentDate.localeCompare(a.appointmentDate));
  let q = '', f = '';
  const draw = () => {
    const list = rows.filter(r => (!q || E.search(r).toLowerCase().includes(q)) && (!f || r.status === f));
    $('#tbl').innerHTML = list.length ? `<div class="table-responsive"><table class="table table-hover"><thead><tr>${E.cols.map(c => `<th>${c[0]}</th>`).join('')}<th class="text-end">Actions</th></tr></thead><tbody>${
      list.map(r => `<tr>${E.cols.map(c => `<td>${c[1](r)}</td>`).join('')}<td class="text-end text-nowrap">
        <button class="btn btn-sm btn-light" data-edit="${r[E.idKey]}" aria-label="Edit"><i class="bi bi-pencil"></i></button>
        <button class="btn btn-sm btn-light text-danger" data-del="${r[E.idKey]}" aria-label="Delete"><i class="bi bi-trash"></i></button></td></tr>`).join('')}</tbody></table></div>`
      : rows.length ? empty('bi-search', 'Nothing matches your search or filter.') : empty(E.icon, `No ${E.title.toLowerCase()} yet. Add your first ${E.one}.`);
    $('#count').textContent = `${list.length} of ${rows.length}`;
  };
  setView(`<div class="page-head"><h1>${E.title}</h1><button class="btn btn-primary" id="add"><i class="bi bi-plus-lg me-1"></i>Add ${E.one}</button></div>
    <div class="d-flex flex-wrap gap-2 align-items-center mb-3">
      <input class="form-control" style="max-width:320px" id="q" type="search" placeholder="Search ${E.title.toLowerCase()}" aria-label="Search">
      ${(E.filters || []).map(s => `<button class="chip" data-f="${s}">${s}</button>`).join('')}
      <span class="ms-auto text-secondary small" id="count"></span></div>
    <div class="panel" id="tbl"></div>`);
  draw();
  $('#q').oninput = e => { q = e.target.value.trim().toLowerCase(); draw(); };
  document.querySelectorAll('[data-f]').forEach(b => b.onclick = () => { f = f === b.dataset.f ? '' : b.dataset.f; document.querySelectorAll('[data-f]').forEach(x => x.classList.toggle('on', x.dataset.f === f)); draw(); });

  const form = (row) => {
    const flds = E.fields(deps), v = row ? (E.init ? E.init(row) : row) : {};
    const missing = flds.find(x => x.type === 'select' && !x.opts.length);
    if (missing) return { html: `<p class="mb-0">You need at least one ${missing.l.toLowerCase()} first. Add one, then come back.</p>`, flds: [] };
    return { flds, html: flds.map(x => {
      const val = v[x.n] ?? (x.n === 'status' ? 'Scheduled' : '');
      const inp = x.type === 'select'
        ? `<select class="form-select" name="${x.n}" id="f-${x.n}">${row ? '' : '<option value="">Select…</option>'}${x.opts.map(o => `<option value="${esc(o.value)}" ${String(o.value) === String(val) ? 'selected' : ''}>${esc(o.label)}</option>`).join('')}</select>`
        : `<input class="form-control" name="${x.n}" id="f-${x.n}" type="${x.type || 'text'}" value="${esc(val)}" ${x.type === 'date' && !row ? `min="${iso(new Date())}"` : ''} ${x.attrs || ''}>`;
      return `<div class="mb-3"><label class="form-label" for="f-${x.n}">${x.l}</label>${inp}<div class="invalid-feedback"></div></div>`;
    }).join('') };
  };
  const showErrors = (fe, msg) => {
    document.querySelectorAll('#mBody .is-invalid').forEach(i => i.classList.remove('is-invalid'));
    let shown = 0;
    Object.entries(fe || {}).forEach(([k, m]) => { const i = $(`#mBody [name=${k}]`); if (i) { i.classList.add('is-invalid'); i.nextElementSibling.textContent = m; shown++; } });
    if (!shown) toast(msg, 'danger');
  };
  const save = (row) => {
    const { html, flds } = form(row);
    openModal({ title: row ? `Edit ${E.one}` : `Add ${E.one}`, body: `<form id="ef" novalidate>${html}</form>`, ok: flds.length ? 'Save changes' : 'Close',
      onOk: async () => {
        if (!flds.length) return modal.hide();
        const body = {};
        flds.forEach(x => { const val = $(`#mBody [name=${x.n}]`).value.trim(); body[x.n] = x.type === 'select' && x.n.endsWith('Id') ? (val ? Number(val) : null) : val; });
        try {
          await api(E.base + (row ? `updateById/${row[E.idKey]}` : E.create), row ? 'PUT' : 'POST', body);
          modal.hide(); toast(row ? `${cap(E.one)} updated` : `${cap(E.one)} added`); crudPage(key);
        } catch (e) { showErrors(e.fieldErrors, e.message); throw e; }
      } });
  };
  $('#add').onclick = () => save(null);
  $('#tbl').onclick = e => {
    const ed = e.target.closest('[data-edit]'), dl = e.target.closest('[data-del]');
    if (ed) save(rows.find(r => String(r[E.idKey]) === ed.dataset.edit));
    if (dl) openModal({ title: `Delete ${E.one}?`, body: `<p class="mb-0">This permanently removes the ${E.one}. This can't be undone.</p>`, ok: `Delete ${E.one}`, danger: true,
      onOk: async () => { try { await api(E.base + `deleteById/${dl.dataset.del}`, 'DELETE'); modal.hide(); toast(`${cap(E.one)} deleted`); crudPage(key); } catch (er) { toast(er.message.includes('constraint') ? `This ${E.one} is linked to other records. Remove those first.` : er.message, 'danger'); throw er; } } });
  };
}
const cap = s => s[0].toUpperCase() + s.slice(1);

/* ---------- Dashboard ---------- */
async function dashboard() {
  setView(skeleton);
  let h, d, p, a;
  try { [h, d, p, a] = await Promise.all([load.hospitals(), load.doctors(), load.patients(), load.appointments()]); } catch (e) { return errorView(e); }
  const today = iso(new Date()), todays = a.filter(x => x.appointmentDate === today);
  const upcoming = a.filter(x => x.appointmentDate > today && x.status === 'Scheduled').sort((x, y) => x.appointmentDate.localeCompare(y.appointmentDate)).slice(0, 5);
  const stat = (href, icon, n, l) => `<div class="col-6 col-xl-3"><a class="panel stat" href="${href}"><div class="ic"><i class="bi ${icon}"></i></div><div><b>${n}</b><span>${l}</span></div></a></div>`;
  const slot = x => `<div class="slot"><span class="dot ${esc(x.status)}"></span><div class="flex-grow-1"><strong>${esc(x.patient?.patientName || 'Patient')}</strong><div class="small text-secondary">with ${esc(x.doctor?.doctorName || 'doctor')}${x.doctor?.specialization ? ' · ' + esc(x.doctor.specialization) : ''}</div></div><div class="text-end">${badge(x.status)}<div class="small text-secondary mt-1">${fmtDate(x.appointmentDate)}</div></div></div>`;
  const counts = STATUSES.map(s => [s, a.filter(x => x.status === s).length]);
  setView(`<div class="page-head"><div><h1>${new Date().toLocaleDateString(undefined, { weekday: 'long', day: 'numeric', month: 'long' })}</h1>
      <div class="text-secondary">${todays.length ? `${todays.length} appointment${todays.length > 1 ? 's' : ''} today` : 'No appointments today'}</div></div>
      <a class="btn btn-primary" href="#/appointments"><i class="bi bi-plus-lg me-1"></i>Book appointment</a></div>
    <div class="row g-3 mb-4">${stat('#/appointments', 'bi-calendar2-check', a.length, 'Appointments')}${stat('#/patients', 'bi-person-lines-fill', p.length, 'Patients')}${stat('#/doctors', 'bi-person-badge', d.length, 'Doctors')}${stat('#/hospitals', 'bi-hospital', h.length, 'Hospitals')}</div>
    <div class="row g-3"><div class="col-lg-7"><div class="panel"><div class="panel-h">Today's schedule</div>${todays.length ? todays.map(slot).join('') : empty('bi-calendar-heart', 'Nothing booked for today.')}</div></div>
    <div class="col-lg-5"><div class="panel mb-3"><div class="panel-h">Coming up</div>${upcoming.length ? upcoming.map(slot).join('') : empty('bi-calendar-event', 'No upcoming scheduled visits.')}</div>
    <div class="panel p-3"><div class="fw-semibold mb-2">By status</div>${counts.map(([s, n]) => `<div class="d-flex align-items-center gap-2 mb-2"><span style="width:90px">${s}</span><div class="progress flex-grow-1" style="height:8px"><div class="progress-bar" style="width:${a.length ? n / a.length * 100 : 0}%"></div></div><span class="small text-secondary" style="width:24px;text-align:right">${n}</span></div>`).join('')}</div></div></div>`);
}

/* ---------- Router & theme ---------- */
function route() {
  const key = (location.hash.replace('#/', '') || 'dashboard');
  document.querySelectorAll('#nav .nav-link').forEach(l => l.classList.toggle('active', l.getAttribute('href') === '#/' + key));
  bootstrap.Offcanvas.getInstance($('#side'))?.hide();
  (key === 'dashboard' || !ENT[key]) ? dashboard() : crudPage(key);
  document.title = (ENT[key]?.title || 'Dashboard') + ' · HealthBridge';
}
const applyTheme = t => { document.documentElement.dataset.bsTheme = t; $('#theme span').textContent = t === 'dark' ? 'Light mode' : 'Dark mode'; $('#theme i').className = 'bi ' + (t === 'dark' ? 'bi-sun' : 'bi-moon-stars'); };
let theme = 'light'; try { theme = localStorage.getItem('hb-theme') || (matchMedia('(prefers-color-scheme:dark)').matches ? 'dark' : 'light'); } catch { /* storage blocked */ }
applyTheme(theme);
$('#theme').onclick = () => { theme = theme === 'dark' ? 'light' : 'dark'; applyTheme(theme); try { localStorage.setItem('hb-theme', theme); } catch { /* ignore */ } };
addEventListener('hashchange', route); route();
