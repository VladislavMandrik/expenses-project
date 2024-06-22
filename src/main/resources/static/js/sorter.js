document.addEventListener('DOMContentLoaded', () => {

    const getSort = ({target}) => {
        const order = (target.dataset.order = -(target.dataset.order || -1));
        const index = [...target.parentNode.cells].indexOf(target);
        const collator = new Intl.Collator(['en', 'ru'], {numeric: true});
        const tableRowComparator = (index, order) => {
            return (rowA, rowB) => {
                const cellA = rowA.children[index].innerHTML;
                const cellB = rowB.children[index].innerHTML;

                if (index === 2) {
                    const cellADateArray = cellA.split('.');
                    const cellBDateArray = cellB.split('.');

// new Date(year, monthIndex, day)
                    return order * (new Date(cellADateArray[2], cellADateArray[1] - 1, cellADateArray[0])) -
                        (new Date(cellBDateArray[2], cellBDateArray[1] - 1, cellBDateArray[0]));
                }

                return order * collator.compare(cellA, cellB)
            };
        };

        for (const tBody of target.closest('table').tBodies) tBody.append(...[...tBody.rows].sort(tableRowComparator(index, order)));

        for (const cell of target.parentNode.cells) cell.classList.toggle('sorted', cell === target);
    };

    document.querySelectorAll('.table_sort thead').forEach(tableTH => tableTH.addEventListener('click', () => getSort(event)));

});