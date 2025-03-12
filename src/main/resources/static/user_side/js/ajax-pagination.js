let currentPage = 1;

function loadPage(pageNumber) {
    currentPage = pageNumber || currentPage;
    document.getElementById("loading-spinner").style.display = "block";

    fetch(`/parent/my-request?currentPage=${currentPage}`, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const requestContainer = document.getElementById("request-container");
            requestContainer.innerHTML = "";

            document.getElementById("requestCount").textContent = data.requests.length;

            data.requests.forEach(request => {
                const requestHTML = `
                <div class="col-md-7">
                    <div class="request-card">
                        <div style="display: flex; justify-content: space-between">
                            <div class="status-time">${new Date(request.createTime).toLocaleString()}</div>
                            <div>${request.requestMasterName === "Closed" ? '<span class="status-closed">Closed</span>' : '<span class="status-open">Open</span>'}</div>
                        </div>
                        <h5>Request Number: #${request.id}</h5>
                        <p><strong>Request Information:</strong></p>
                        <p>Full Name: ${request.fullName}</p>
                        <p>Email address: ${request.requestEmail}</p>
                        <p>Phone number: ${request.requestPhone}</p>
                        <p>${request.inquires}</p>
                    </div>
                </div>
            `;
                requestContainer.innerHTML += requestHTML;
            });
            updatePaginationUI(data.currentPage, data.totalPages);
        })
        .catch(error => console.error('AJAX request failed:', error))
        .finally(() => document.getElementById("loading-spinner").style.display = "none");
}

function updatePaginationUI(currentPage, totalPages) {
    const paginationElement = document.getElementById("pagination");
    paginationElement.innerHTML = "";

    if (currentPage > 1) {
        paginationElement.innerHTML += `<li class="page-item"><a class="page-link" href="#" onclick="loadPage(${currentPage - 1})">«</a></li>`;
    }

    for (let i = 1; i <= totalPages; i++) {
        paginationElement.innerHTML += `<li class="page-item ${i === currentPage ? 'active' : ''}"><a class="page-link" href="#" onclick="loadPage(${i})">${i}</a></li>`;
    }

    if (currentPage < totalPages) {
        paginationElement.innerHTML += `<li class="page-item"><a class="page-link" href="#" onclick="loadPage(${currentPage + 1})">»</a></li>`;
    }
}

document.addEventListener("DOMContentLoaded", () => loadPage(1));
