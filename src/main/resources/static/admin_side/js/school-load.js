function loadSchoolInPage(nextPage, isAdmin){
    url = "";
    search = document.getElementById("search").value;
    if(isAdmin){
        url = "/admin";
    }else{
        url = "/school-owner";
    }
    $.ajax({
        url: url + "/school-list/searchAndPaging",
        type: "GET",
        data: {
            search: search,
            page: nextPage,
        },
        success: function (data) {
            document.getElementById("main-content-school-table").outerHTML = data;
            updateUrlParams(search, nextPage)
        }
    });
}

function updateUrlParams(search, currentPage) {
    let newUrl = window.location.pathname + "?search=" + encodeURIComponent(search) + "&page=" + currentPage;
    window.history.pushState({ path: newUrl }, "", newUrl);
}