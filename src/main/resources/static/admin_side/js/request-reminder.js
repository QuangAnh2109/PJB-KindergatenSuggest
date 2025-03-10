$(document).ready(function () {
    let currentPage = new URLSearchParams(window.location.search).get('currentPage');
    currentPage = currentPage ? parseInt(currentPage) : 0;

    fetchData(currentPage); // Load trang đầu tiên mặc định

    // Gọi API và lấy dữ liệu
    function fetchData(page = 0) {
        currentPage = page; // Cập nhật currentPage
        const keyword = $("input[name='keyword']").val();
        $.ajax({
            url: `/manager/searchRequestReminder?currentPage=${page}&keyword=${encodeURIComponent(keyword)}`,
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                renderRequestList(data.content);
                renderPagination(data);
            },
            error: function (xhr) {
                console.error('Lỗi khi tải dữ liệu:', xhr.responseText);
            }
        });
    }

    // Submit form tìm kiếm
    $('.app-search').on('submit', function (e) {
        e.preventDefault();
        fetchData(0); // Reset về trang đầu tiên khi tìm kiếm
    });

    // Render danh sách request
    function renderRequestList(requestList) {
        const tbody = $('tbody');
        tbody.empty();

        if (requestList.length === 0) {
            tbody.append('<tr><td colspan="6">No requests found.</td></tr>');
            return;
        }

        requestList.forEach(request => {
            const row = `
                <tr >
                    <th scope="row">${request.id}</th>
                    <th>${request.fullName || 'N/A'}</th>
                    <th>${request.requestEmail || 'N/A'}</th>
                    <th>${request.requestPhone || 'N/A'}</th>
                    <th><button class="btn btn-closed">${request.requestMasterName || 'N/A'}</button></th>
                    <td>
                        <a href="/manager/request-list-detail?id=${request.id}&page=Detail&currentPage=${currentPage}" style="text-decoration: underline">
                            Go to review
                        </a>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    }

    // Render phân trang
    function renderPagination(pageData) {
        const pagination = $('.pagination');
        pagination.empty();

        const currentPage = pageData.number;
        if (pageData.totalPages > 0) {
            if(currentPage>0) {
                const prevDisabled = currentPage === 0 ? 'disabled' : '';
                pagination.append(`
            <li class="page-item ${prevDisabled}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
            </li>
        `);
            }
            for (let i = 0; i < pageData.totalPages; i++) {
                const activeClass = i === pageData.number ? 'active' : '';
                const pageItem = `
                    <li class="page-item ${activeClass}">
                        <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                    </li>
                `;
                pagination.append(pageItem);
            }
            if(currentPage!=pageData.totalPages - 1){
            const nextDisabled = currentPage === pageData.totalPages - 1 ? 'disabled' : '';
            pagination.append(`
            <li class="page-item ${nextDisabled}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
            </li>
        `);}
            // Gọi fetchData và cập nhật currentPage khi click phân trang
            $('.page-link').on('click', function (e) {
                e.preventDefault();
                const page = $(this).data('page');
                fetchData(page);
            });
        }
    }
});
