function redirectToDetail(id) {
    window.location.href = "/manager/request-list-detail?id="+id;
}

document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.querySelector('.app-search');
    const tbody = document.querySelector('tbody');
    const pagination = document.querySelector('.pagination');
    let debounceTimer;

    searchForm.addEventListener('submit', function(e) {
        e.preventDefault(); // Chặn load lại trang
        clearTimeout(debounceTimer);

        debounceTimer = setTimeout(() => {
            fetchData(0);
        }, 10); // Debounce 300ms
    });

    async function fetchData(page) {
        const keyword = document.querySelector('input[name="keyword"]').value;
        try {
            const response = await fetch(`/manager/searchRequestList?keyword=${keyword}&page=${page}&size=5`, {
                method: 'GET',
                headers: { 'Accept': 'application/json' }
            });
            if (!response.ok) throw new Error('Failed to fetch data');

            const data = await response.json();
            renderRequestList(data.content);
            renderPagination(data);
        } catch (error) {
            console.error('Error:', error);
        }
    }

    function renderRequestList(requestList) {
        const fragment = document.createDocumentFragment();
        tbody.innerHTML = ''; // Xóa nội dung cũ

        requestList.forEach(request => {
            const row = document.createElement('tr');
            row.setAttribute('onclick', `redirectToDetail(${request.id})`);
            row.innerHTML = `
                <th scope="row">${request.id}</th>
                <th>${request.fullName}</th>
                <th>${request.requestEmail}</th>
                <th>${request.requestPhone}</th>
                <td><button class="btn btn-closed">${request.requestMasterName}</button></td>
            `;
            fragment.appendChild(row);
        });

        tbody.appendChild(fragment); // Chỉ cập nhật DOM 1 lần
    }

    function renderPagination(pageData) {
        if (!pageData.totalPages) return;

        const fragment = document.createDocumentFragment();
        pagination.innerHTML = '';

        for (let i = 0; i < pageData.totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === pageData.number ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" data-page="${i}">${i + 1}</a>`;
            fragment.appendChild(li);
        }

        pagination.appendChild(fragment);

        pagination.querySelectorAll('.page-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                fetchData(e.target.dataset.page);
            });
        });
    }
});