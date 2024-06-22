document.getElementById('btn-del').onclick = function(e) {
    if (!confirm('Точно удалить?')) {
        e.preventDefault();
        return false;
    }
    alert('Удалено');
};