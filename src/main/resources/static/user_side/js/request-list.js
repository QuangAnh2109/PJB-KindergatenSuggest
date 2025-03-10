function loadProducts(page) {
    $.ajax({
        url: `/parent/my-request?page=${page}&size=5&`,
        type: "GET",
        success: function (data) {
            let productList = $("#product-list");
            productList.empty();
            data.content.forEach(product => {
                productList.append(`<li>${product.name} - ${product.price}$</li>`);
            });

            updatePagination(data);
        }
    });
}

function updatePagination(data) {
    let pagination = $(".pagination");
    pagination.empty();

    if (!data.first) {
        pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadProducts(${data.number - 1})">«</a></li>`);
    }

    for (let i = 0; i < data.totalPages; i++) {
        pagination.append(`<li class="page-item ${i === data.number ? 'active' : ''}">
            <a class="page-link" href="#" onclick="loadProducts(${i})">${i + 1}</a></li>`);
    }

    if (!data.last) {
        pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="loadProducts(${data.number + 1})">»</a></li>`);
    }
}

$(document).ready(function () {
    loadProducts(0);
});
