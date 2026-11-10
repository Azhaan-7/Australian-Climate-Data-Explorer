document.documentElement.classList.add('js');

const navigation = document.querySelector('.navbar-button');
const toggle = document.querySelector('.menu-toggle');
function setMenu(open) {
    navigation?.classList.toggle('is-open', open);
    toggle?.setAttribute('aria-expanded', String(open));
}
toggle?.addEventListener('click', () => setMenu(toggle.getAttribute('aria-expanded') !== 'true'));
navigation?.addEventListener('keydown', event => {
    if (event.key === 'Escape') {
        setMenu(false);
        toggle.focus();
    }
});

const labels = {
    states_drop: 'State', station_drop: 'Reference station', metrics_drop: 'Climate metric',
    metric_drop: 'Climate metric', flags_drop: 'Quality flag', orderby_drop: 'Sort by',
    ascdesc_drop: 'Sort direction', station_id_min: 'Minimum station number',
    station_id_max: 'Maximum station number', num_metrics: 'Number of metrics',
    no_of_stations: 'Number of stations', start_lat: 'Northern latitude', end_lat: 'Southern latitude'
};
// Add persistent labels to controls that previously relied on placeholder text.
document.querySelectorAll('main select, main input:not([type="hidden"])').forEach((control, index) => {
    if (!control.id) control.id = `search-field-${index}`;
    if (control.labels.length || control.closest('label')) return;
    const labelText = labels[control.name] || control.placeholder
        || (control.classList.contains('tutorial-drop-down') ? 'Example selection' : '')
        || (control.type === 'date' ? 'Example date' : '');
    if (!labelText) return;
    const field = document.createElement('div');
    field.className = 'field';
    const label = document.createElement('label');
    label.htmlFor = control.id;
    label.textContent = labelText.replace(/\.\.\.$/, '');
    control.before(field);
    field.append(label, control);
});

// Keep native date inputs while presenting each range as one labelled group.
const dateRanges = [...document.querySelectorAll('main .date-selector')];
dateRanges.forEach((range, index) => {
    const inputs = [...range.querySelectorAll('input[type="date"]')];
    if (inputs.length !== 2) return;
    const group = document.createElement('fieldset');
    group.className = 'date-selector';
    const legend = document.createElement('legend');
    legend.textContent = dateRanges.length > 1 ? `Period ${index + 1}` : 'Date range';
    group.append(legend);
    inputs.forEach(input => {
        const field = document.createElement('div');
        field.className = 'date-field';
        const label = input.labels[0];
        field.append(label, input);
        group.append(field);
    });
    const hint = document.createElement('span');
    hint.className = 'date-hint';
    hint.textContent = `Records from ${inputs[0].min.slice(0, 4)} to ${inputs[1].max.slice(0, 4)}`;
    legend.append(hint);
    range.replaceWith(group);
});

document.querySelectorAll('.table-container').forEach(table => {
    table.tabIndex = 0;
    table.setAttribute('role', 'region');
    table.setAttribute('aria-label', 'Results table. Scroll to see more rows or columns.');
});

document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', event => {
        const button = event.submitter;
        // Downloads keep the current page, so their controls stay available.
        if (!button || !/^Search$/.test(button.textContent.trim())) return;
        button.dataset.label = button.textContent;
    button.textContent = 'Searching...';
        form.setAttribute('aria-busy', 'true');
    });
});
window.addEventListener('pageshow', () => {
    document.querySelectorAll('button[data-label]').forEach(button => {
        button.textContent = button.dataset.label;
        button.closest('form').removeAttribute('aria-busy');
    });
});
