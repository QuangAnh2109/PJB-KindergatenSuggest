function loadSchoolInPage(nextPage){
    url = "";
    search = document.getElementById("search").value;
    $.ajax({
        url: url + "/manager/school-list",
        type: "GET",
        data: {
            search: search,
            page: nextPage,
            ajax: true
        },
        success: function (data) {
            document.getElementById("main-content-school-table").outerHTML = data;
            updateUrlParams(search, nextPage);
        },
        error: function (e) {
            $('#searchModel').modal('show');
        }
    });
}

function updateUrlParams(search, currentPage) {
    let newUrl = window.location.pathname + "?search=" + encodeURIComponent(search) + "&page=" + currentPage;
    window.history.pushState({ path: newUrl }, "", newUrl);
}